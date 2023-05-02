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
        <a-button type="primary" @click="handleCreate" class="mr-2">
          <icon icon="ant-design:plus-outlined" /> 新增角色
        </a-button>
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
        <Upload accept=".xls,.xlsx" :showUploadList="false" :beforeUpload="beforeImportRoleList">
          <a-button type="primary"> <icon icon="ant-design:upload-outlined" /> 数据导入 </a-button>
        </Upload>
        <Dropdown
          class="mr-2"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:check-circle-outlined',
              text: '导出选中',
              event: '1',
              disabled: checkedKeys.length === 0,
              onClick: handleExportRoleList.bind(null, true),
            },
            {
              icon: 'ant-design:check-circle-twotone',
              text: '导出全部',
              event: '1',
              onClick: handleExportRoleList.bind(null, false),
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
        <a-button type="link" @click="handleDownloadTemplate">下载模板</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '编辑',
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '权限',
                color: 'error',
                icon: 'ant-design:lock-twotone',
                onClick: handleResource.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '删除角色',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除角色',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <RoleDrawer @register="registerDrawer" @success="handleSuccess" />
    <RoleResourceDrawer @register="registerResourceDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import { Upload } from 'ant-design-vue';
  import { merge } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    getRoleList,
    deleteRole,
    batchDeleteRole,
    exportRoleList,
    importRoleList,
    downloadRoleTemplate,
  } from '/@/api/system/role';

  import { useDrawer } from '/@/components/Drawer';
  import RoleDrawer from './RoleDrawer.vue';
  import RoleResourceDrawer from './RoleResourceDrawer.vue';

  import { columns, searchFormSchema } from './role.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import Icon from '@/components/Icon/Icon.vue';

  import { handleDownloadBlod } from '/@/utils/file/download';
  import { useLoading } from '/@/components/Loading';

  export default defineComponent({
    name: 'RoleManagement',
    components: { BasicTable, RoleDrawer, RoleResourceDrawer, TableAction, Dropdown, Icon, Upload },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerResourceDrawer, { openDrawer: openResourceDrawer }] = useDrawer();
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '...',
      });
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord, getForm }] = useTable({
        title: '角色列表',
        api: getRoleList,
        rowKey: 'id',
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
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
          width: 180,
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
        handleRoleRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleResource(record: Recordable) {
        openResourceDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        deleteRole(record.id)
          .then(() => {
            createMessage.success(`角色删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('角色删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteRole(checkedKeys.value)
          .then(() => {
            createMessage.success(`角色删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('角色删除失败');
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
      function handleRoleRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.roleStatus = `${record.roleStatus}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      function getSearchParams() {
        const params: Recordable = merge(getForm()?.getFieldsValue(), searchInfo);
        return params;
      }

      //数据导出
      function handleExportRoleList(exportChecked: boolean) {
        openFullLoading();
        let params = getSearchParams();
        if (exportChecked) {
          params.roleIds = checkedKeys.value as string[];
        }
        exportRoleList(params)
          .then((response) => {
            handleDownloadBlod('角色数据列表.xlsx', response);
            closeFullLoading();
          })
          .catch((err) => {
            createMessage.error('角色数据导出失败:' + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 数据导入
      function beforeImportRoleList(file) {
        handleUploadRoleList(file);
        return false;
      }

      function handleUploadRoleList(file) {
        const formData = new FormData();
        formData.append('uploadFile', file);
        openFullLoading();
        importRoleList(formData)
          .then(() => {
            createMessage.success(`角色数据导入成功`);
            reload();
          })
          .catch((err) => {
            createMessage.error(`角色数据导入失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 下载导入模板
      function handleDownloadTemplate() {
        openFullLoading();
        downloadRoleTemplate(searchInfo)
          .then((response) => {
            handleDownloadBlod('角色模板批量导入模板.xlsx', response);
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
        registerResourceDrawer,
        handleCreate,
        handleEdit,
        handleResource,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handleRoleRecord,
        onSelectChange,
        checkedKeys,
        searchInfo,
        handleExportRoleList,
        beforeImportRoleList,
        handleDownloadTemplate,
      };
    },
  });
</script>
