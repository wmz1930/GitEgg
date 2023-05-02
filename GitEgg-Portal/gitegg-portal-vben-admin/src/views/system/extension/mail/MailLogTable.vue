<template>
  <div>
    <BasicTable @register="registerTable" :searchInfo="searchInfo">
      <template #tableTitle>
        <Dropdown
          v-if="checkedKeys.length > 0"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:delete-outlined',
              text: '批量删除',
              event: '1',
              popConfirm: {
                title: '是否确认删除',
                placement: 'right',
                confirm: handleBatchDelete.bind(null),
              },
            },
          ]"
          popconfirm
        >
          <slot name="more"></slot>
          <a-button v-if="!$slots.more">
            批量操作
            <icon icon="ant-design:down-outlined" />
          </a-button>
        </Dropdown>
      </template>
      <template #toolbar> </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '查看',
                icon: 'clarity:info-standard-line',
                onClick: handleEdit.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <MailLogDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import { merge } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    queryMailLogList,
    deleteMailLog,
    batchDeleteMailLog,
    downloadMailLogList,
    uploadMailLog,
    downloadMailLogTemplate,
  } from '/@/api/system/extension/mail/mailLog';

  import { useDrawer } from '/@/components/Drawer';
  import MailLogDrawer from './drawer/MailLogDrawer.vue';
  // import SmsTemplateResourceDrawer from './SmsTemplateResourceDrawer.vue';

  import { mailLogColumn, mailLogSearch } from './mail.log.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import Icon from '@/components/Icon/Icon.vue';

  import { handleDownloadBlod } from '/@/utils/file/download';
  import { useLoading } from '/@/components/Loading';

  export default defineComponent({
    name: 'SmsTemplateManagement',
    components: {
      BasicTable,
      MailLogDrawer,
      TableAction,
      Dropdown,
      Icon,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '...',
      });
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord, getForm }] = useTable({
        title: '短信模板列表',
        api: queryMailLogList,
        rowKey: 'id',
        columns: mailLogColumn,
        formConfig: {
          autoAdvancedLine: 1,
          labelWidth: 120,
          schemas: mailLogSearch,
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
          width: 150,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleCreate() {
        openDrawer(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        // handleSmsTemplateRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }
      function handleDelete(record: Recordable) {
        deleteMailLog(record.id)
          .then(() => {
            createMessage.success(`邮件记录删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('邮件记录删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteMailLog(checkedKeys.value)
          .then(() => {
            createMessage.success(`邮件记录删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('邮件记录删除失败');
          })
          .finally(() => {});
      }

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
      // function handleSmsTemplateRecord(record: Recordable) {
      //   // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
      //   record.templateStatus = `${record.templateStatus}`;
      // }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      // 获取搜索参数
      function getSearchParams() {
        const params: Recordable = merge(getForm()?.getFieldsValue(), searchInfo);
        return params;
      }

      //数据导出
      function handleExportSmsTemplateList(exportChecked: boolean) {
        openFullLoading();
        let params = getSearchParams();
        if (exportChecked) {
          params.SmsTemplateIds = checkedKeys.value as string[];
        }
        downloadMailLogList(params)
          .then((response) => {
            handleDownloadBlod('短信模板数据列表.xlsx', response);
            closeFullLoading();
          })
          .catch((err) => {
            createMessage.error('短信模板数据导出失败:' + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 数据导入
      function beforeImportSmsTemplateList(file) {
        handleUploadSmsTemplateList(file);
        return false;
      }

      function handleUploadSmsTemplateList(file) {
        const formData = new FormData();
        formData.append('uploadFile', file);
        openFullLoading();
        uploadMailLog(formData)
          .then(() => {
            createMessage.success(`短信模板数据导入成功`);
            reload();
          })
          .catch((err) => {
            createMessage.error(`短信模板数据导入失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 下载导入模板
      function handleDownloadTemplate() {
        openFullLoading();
        downloadMailLogTemplate(searchInfo)
          .then((response) => {
            handleDownloadBlod('短信模板模板批量导入模板.xlsx', response);
          })
          .catch((err) => {
            createMessage.error(`下载导入模板失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      return {
        registerTable,
        registerDrawer,
        handleCreate,
        handleEdit,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        // handleSmsTemplateRecord,
        onSelectChange,
        checkedKeys,
        searchInfo,
        handleExportSmsTemplateList,
        beforeImportSmsTemplateList,
        handleDownloadTemplate,
      };
    },
  });
</script>
