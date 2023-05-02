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
      <template #toolbar>
        <Dropdown
          class="mr-2"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:check-circle-outlined',
              text: '导出选中',
              event: '1',
              disabled: checkedKeys.length === 0,
              onClick: handleExportJustauthSocialList.bind(null, true),
            },
            {
              icon: 'ant-design:check-circle-twotone',
              text: '导出全部',
              event: '1',
              onClick: handleExportJustauthSocialList.bind(null, false),
            },
          ]"
          popconfirm
        >
          <slot name="more"></slot>
          <a-button v-if="!$slots.more">
            数据导出
            <icon icon="ant-design:down-outlined" />
          </a-button>
        </Dropdown>
      </template>
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
    <JustauthSocialDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  // import { Tag } from 'ant-design-vue';
  import { merge } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    queryJustAuthSocialList,
    deleteJustAuthSocial,
    batchDeleteJustAuthSocial,
    downloadJustAuthSocialList,
  } from '/@/api/system/extension/justauth/justauthSocial';

  import { useDrawer } from '/@/components/Drawer';
  import JustauthSocialDrawer from './drawer/JustauthSocialDrawer.vue';
  // import JustauthSocialResourceDrawer from './JustauthSocialResourceDrawer.vue';

  import { JustauthSocialColumn, JustauthSocialSearch } from './justauth.social.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import Icon from '@/components/Icon/Icon.vue';

  import { handleDownloadBlod } from '/@/utils/file/download';
  import { useLoading } from '/@/components/Loading';

  export default defineComponent({
    name: 'JustauthSocialManagement',
    components: {
      BasicTable,
      JustauthSocialDrawer,
      TableAction,
      Dropdown,
      Icon,
      // Upload,
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
        title: '第三方用户列表',
        api: queryJustAuthSocialList,
        rowKey: 'id',
        columns: JustauthSocialColumn,
        formConfig: {
          autoAdvancedLine: 1,
          labelWidth: 120,
          schemas: JustauthSocialSearch,
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
        handleJustauthSocialRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        deleteJustAuthSocial(record.id)
          .then(() => {
            createMessage.success(`第三方用户删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('第三方用户删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteJustAuthSocial(checkedKeys.value)
          .then(() => {
            createMessage.success(`第三方用户删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('第三方用户删除失败');
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
      function handleJustauthSocialRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.gender = `${record.gender}`;
      }

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
      function handleExportJustauthSocialList(exportChecked: boolean) {
        openFullLoading();
        let params = getSearchParams();
        if (exportChecked) {
          params.JustauthSocialIds = checkedKeys.value as string[];
        }
        downloadJustAuthSocialList(params)
          .then((response) => {
            handleDownloadBlod('第三方用户数据列表.xlsx', response);
            closeFullLoading();
          })
          .catch((err) => {
            createMessage.error('第三方用户数据导出失败:' + err);
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
        handleJustauthSocialRecord,
        onSelectChange,
        checkedKeys,
        searchInfo,
        handleExportJustauthSocialList,
      };
    },
  });
</script>
