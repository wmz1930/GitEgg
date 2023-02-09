<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <!--组织结构树-->
    <OrganizationTree
      helpMessage="再次点击选中的节点，可取消选中"
      @select="handleSelect"
      class="m-4"
    />
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
        <Dropdown
          v-if="checkedKeys.length > 0"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:delete-outlined',
              text: '批量取消',
              event: '1',
              popConfirm: {
                title: '是否确认取消',
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
            <span>查询机构下拥有该机构数据权限的用户列表</span>
          </template>
        </a-alert>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'gender'"> {{ record.gender }} </template>
        <template v-else-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '取消数据权限',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                disabled: record.organizationId === record.organizationIds,
                tooltip: '取消此用户的组织机构数据权限',
                popConfirm: {
                  title:
                    '用户' +
                    record.account +
                    '将被取消【' +
                    record.organizationNames +
                    '】机构数据权限，是否继续?',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { Alert } from 'ant-design-vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { getOrganizationDataList, batchDeleteOrganizationData } from '/@/api/system/user';
  import { PageWrapper } from '/@/components/Page';
  import { OrganizationTree } from '/@/components/Subassembly/Tree';

  import { columns, getSearchFormConfig } from './user/DataPermissionUser.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import { Icon } from '/@/components/Icon';

  // 这里演示打开新的tab页时需要
  // import { useGo } from '/@/hooks/web/usePage';

  export default defineComponent({
    name: 'UserManagement',
    components: {
      BasicTable,
      PageWrapper,
      OrganizationTree,
      TableAction,
      Dropdown,
      Icon,
      AAlert: Alert,
    },
    setup() {
      // 这里演示打开新的tab页时需要
      // const go = useGo();
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '账号列表',
        api: getOrganizationDataList,
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
          width: 180,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleDelete(record: Recordable) {
        batchDeleteOrganizationData([record.id])
          .then(() => {
            createMessage.success(`取消数据权限成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('取消数据权限失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteOrganizationData(checkedKeys.value)
          .then(() => {
            createMessage.success(`取消数据权限成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('取消数据权限失败');
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

      function handleSelect(organizationId = '') {
        searchInfo.organizationId = organizationId;
        reload();
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      return {
        registerTable,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handleSelect,
        onSelectChange,
        checkedKeys,
        searchInfo,
      };
    },
  });
</script>
