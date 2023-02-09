<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <!--资源权限树-->
    <ResourceTree helpMessage="再次点击选中的节点，可取消选中" @select="handleSelect" class="m-4" />
    <!--数据表格-->
    <BasicTable
      @register="registerTable"
      class="w-3/4 xl:w-4/5"
      :searchInfo="searchInfo"
      :rowSelection="{
        type: 'checkbox',
        selectedRowKeys: checkedKeys,
        onChange: onSelectChange,
        preserveSelectedRowKeys: true,
      }"
    >
      <template #tableTitle>
        <a-button
          type="primary"
          @click="handleCreate"
          class="mr-2"
          :disabled="
            searchInfo.resourceId === '' ||
            currentResourceType === '1' ||
            currentResourceType === '2' ||
            currentResourceType === ''
          "
        >
          <icon icon="ant-design:plus-outlined" /> 新增数据权限
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
        <a-alert type="info">
          <template #message>
            <span>新增、编辑数据权限需要选中左侧资源权限的数据请求接口</span>
          </template>
        </a-alert>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '编辑',
                icon: 'clarity:note-edit-line',
                tooltip: '编辑数据权限资料',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '角色关联',
                icon: 'clarity:note-edit-line',
                tooltip: '数据权限关联角色',
                onClick: handleDataPermission.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '删除数据权限',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                tooltip: '删除此数据权限',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <!-- 新增编辑时的弹框 -->
    <DataPermissionDrawer @register="registerDrawer" @success="handleSuccess" />
    <!-- 编辑数据权限时的弹框 -->
    <DataPermissionDataPermissionDrawer @register="dataPermissionDrawer" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { Alert } from 'ant-design-vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    getDataPermissionRoleList,
    deleteDataPermissionRole,
    batchDeleteDataPermissionRole,
  } from '/@/api/system/dataPermission/data_permission';
  import { PageWrapper } from '/@/components/Page';
  import { ResourceTree } from '/@/components/Subassembly/Tree';

  import { useDrawer } from '/@/components/Drawer';
  import DataPermissionDrawer from './role/DataPermissionDrawer.vue';
  import DataPermissionDataPermissionDrawer from './role/RoleDataPermissionDrawer.vue';

  import { columns, getSearchFormConfig } from './role/DataPermissionRole.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import { Icon } from '/@/components/Icon';

  export default defineComponent({
    name: 'DataPermissionManagement',
    components: {
      BasicTable,
      PageWrapper,
      ResourceTree,
      DataPermissionDrawer,
      DataPermissionDataPermissionDrawer,
      TableAction,
      Dropdown,
      Icon,
      AAlert: Alert,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer: openDataPermissionDrawer }] = useDrawer();
      const [dataPermissionDrawer, { openDrawer: openRoleDataPermissionDrawer }] = useDrawer();
      const searchInfo = reactive<Recordable>({});
      const currentResourceType = ref('');
      const currentResourceName = ref('');
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '数据权限列表',
        api: getDataPermissionRoleList,
        rowKey: 'id',
        columns,
        useSearchForm: true,
        formConfig: getSearchFormConfig(),
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        handleSearchInfoFn(info) {
          return info;
        },
        actionColumn: {
          width: 200,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleCreate() {
        openDataPermissionDrawer(true, {
          isUpdate: false,
          currentResourceName: currentResourceName,
          currentResourceId: searchInfo.resourceId,
        });
      }

      function handleEdit(record: Recordable) {
        // 省市区、角色、状态转换
        handleDataPermissionRecord(record);
        openDataPermissionDrawer(true, {
          record,
          isUpdate: true,
          currentResourceName: currentResourceName,
        });
      }

      function handleDataPermission(record: Recordable) {
        // 省市区、角色、状态转换
        handleDataPermissionRecord(record);
        openRoleDataPermissionDrawer(true, {
          record,
        });
      }

      function handleDelete(record: Recordable) {
        deleteDataPermissionRole(record.id)
          .then(() => {
            createMessage.success(`数据权限删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('数据权限删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteDataPermissionRole(checkedKeys.value)
          .then(() => {
            createMessage.success(`数据权限删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('数据权限删除失败');
          })
          .finally(() => {});
      }

      function handleSuccess({ isUpdate, values }) {
        createMessage.success(`操作成功`);
        if (isUpdate) {
          // 演示不刷新表格直接更新内部数据。
          // 注意：updateTableDataRecord要求表格的rowKey属性为string并且存在于每一行的record的keys中
          updateTableDataRecord(values.id, values);
        } else {
          reload();
        }
      }

      function handleSelect(resourceId = '', node) {
        if (node.selected) {
          searchInfo.resourceId = resourceId;
          const resourceNode = node?.node?.dataRef;
          currentResourceName.value = resourceNode?.resourceName;
          currentResourceType.value = resourceNode?.resourceType;
        } else {
          searchInfo.resourceId = '';
          currentResourceName.value = '';
          currentResourceType.value = '';
        }
        reload();
      }

      // 状态转换
      function handleDataPermissionRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回数据权限状态Number类型时需要进行转换
        record.status = `${record.status}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      return {
        registerTable,
        registerDrawer,
        dataPermissionDrawer,
        handleCreate,
        handleEdit,
        handleDataPermission,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handleSelect,
        onSelectChange,
        checkedKeys,
        currentResourceType,
        currentResourceName,
        searchInfo,
      };
    },
  });
</script>
