<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #tableTitle>
        <a-button type="primary" @click="handleCreate" class="mr-2">
          <icon icon="ant-design:plus-outlined" /> 新增组织
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '新增',
                icon: 'ant-design:plus-outlined',
                onClick: handleCreateChildren.bind(null, record),
              },
              {
                label: '编辑',
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '删除组织机构',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除组织机构',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <OrganizationDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';

  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { getOrganizationList, deleteOrganization } from '/@/api/system/organization';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { useDrawer } from '/@/components/Drawer';
  import OrganizationDrawer from './OrganizationDrawer.vue';

  import { columns, searchFormSchema } from './organization.data';

  export default defineComponent({
    name: 'OrganizationManagement',
    components: { BasicTable, OrganizationDrawer, TableAction },
    setup() {
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerTable, { reload }] = useTable({
        title: '组织机构',
        api: getOrganizationList,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        isTreeTable: true,
        pagination: false,
        striped: false,
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        canResize: false,
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

      function handleCreateChildren(record: Recordable) {
        openDrawer(true, {
          record,
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        handleOrganizationRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        deleteOrganization(record.id)
          .then(() => {
            createMessage.success(`组织机构删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('组织机构删除失败');
          })
          .finally(() => {});
      }

      function handleSuccess() {
        createMessage.success(`操作成功`);
        reload();
      }

      // 状态转换
      function handleOrganizationRecord(record: Recordable) {
        // 转换省市区
        if (!record.areas || record.areas.length === 0) {
          record.areas = [record.province, record.city, record.area];
        }
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.organizationStatus = `${record.organizationStatus}`;
      }

      function onFetchSuccess() {
        // 演示默认展开所有表项
        // nextTick(expandAll);
      }

      return {
        registerTable,
        registerDrawer,
        handleCreate,
        handleCreateChildren,
        handleEdit,
        handleOrganizationRecord,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
      };
    },
  });
</script>
