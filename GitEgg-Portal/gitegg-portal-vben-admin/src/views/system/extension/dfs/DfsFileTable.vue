<template>
  <div>
    <BasicTable
      @register="registerTable"
      :searchInfo="searchInfo"
      :rowSelection="{
        type: 'checkbox',
        selectedRowKeys: checkedKeys,
        onChange: onSelectChange,
        preserveSelectedRowKeys: true,
      }"
    >
      <template #tableTitle> </template>
      <template #toolbar> </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '下载',
                onClick: downLoadFile.bind(null, record),
              },
              {
                label: '查看',
                onClick: getFileUrl.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { queryDfsFileList } from '/@/api/system/extension/dfs/dfs_file';
  import { dfsGetFileUrl, dfsDownloadFileUrl } from '/@/api/system/extension/dfs/dfs_upload';
  import { dfsFileColumns, dfsFileSearchForm } from './dfs.file.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLoading } from '/@/components/Loading';

  export default defineComponent({
    name: 'DfsFileManagement',
    components: {
      BasicTable,
      TableAction,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [openFullLoading, closeFullLoading] = useLoading({
        // tip: '...',
      });
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '上传记录配置',
        api: queryDfsFileList,
        rowKey: 'id',
        columns: dfsFileColumns,
        formConfig: {
          labelWidth: 120,
          schemas: dfsFileSearchForm,
          showAdvancedButton: false,
          actionColOptions: {
            span: 24,
          },
        },
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        handleSearchInfoFn(info) {
          return info;
        },
        actionColumn: {
          width: 120,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleSuccess({ isUpdate, values }) {
        createMessage.success(`操作成功`);
        if (isUpdate) {
          // 演示不刷新表格直接更新内部数据。
          // 注意：updateTableDataRecord要求表格的rowKey属性为string并且存在于每一行的record的keys中
          const result = updateTableDataRecord(values.id, values);
          console.log(result);
        } else {
          reload();
        }
      }

      // 状态转换
      function handleSmsTemplateRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.gender = `${record.gender}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      interface fileDownloadData {
        dfsCode: string | number;
        fileName: string | number;
        responseType?: string;
      }

      function getFileUrl(row) {
        const fileDownload: fileDownloadData = {
          dfsCode: row.dfsCode,
          fileName: row.fileName,
        };
        openFullLoading();
        dfsGetFileUrl(fileDownload).then((response) => {
          window.open(response);
          closeFullLoading();
        });
      }

      function downLoadFile(row) {
        const downLoadFile: fileDownloadData = {
          dfsCode: row.dfsCode,
          fileName: row.fileName,
          responseType: 'blob',
        };
        openFullLoading();
        dfsDownloadFileUrl(downLoadFile).then((response) => {
          const blob = new Blob([response.data]);
          const fileName = row.originalName;
          const elink = document.createElement('a');
          elink.download = fileName;
          elink.style.display = 'none';
          elink.href = URL.createObjectURL(blob);
          document.body.appendChild(elink);
          elink.click();
          URL.revokeObjectURL(elink.href);
          document.body.removeChild(elink);
          closeFullLoading();
        });
      }

      return {
        registerTable,
        handleSuccess,
        handleSmsTemplateRecord,
        onSelectChange,
        checkedKeys,
        searchInfo,
        getFileUrl,
        downLoadFile,
      };
    },
  });
</script>
