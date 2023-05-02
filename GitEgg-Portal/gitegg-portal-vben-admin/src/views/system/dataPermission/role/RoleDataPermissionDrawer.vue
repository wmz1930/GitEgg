<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="userDataPermissionDrawer"
    showFooter
    :title="getTitle"
    width="30%"
    class="data-permission"
    @ok="handleSubmit"
  >
    <BasicTable
      @register="registerTable"
      :searchInfo="searchInfo"
      :rowSelection="{
        type: 'checkbox',
        selectedRowKeys: checkedKeys,
        onChange: onSelectChange,
        preserveSelectedRowKeys: true,
      }"
    />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, reactive } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { columnsRole, searchFormSchemaRole } from './DataPermissionRole.data';

  import { BasicTable, useTable } from '/@/components/Table';
  import { getRoleList } from '/@/api/system/role';
  import {
    queryRoleDataPermission,
    updateRoleDataPermission,
  } from '/@/api/system/dataPermission/data_permission';

  export default defineComponent({
    components: { BasicDrawer, BasicTable },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const checkedKeys = ref(Array<string | number>());
      const rowId = ref('');
      const { createMessage } = useMessage();

      const searchInfo = reactive<Recordable>({});
      const [registerTable, {}] = useTable({
        title: '角色列表',
        api: getRoleList,
        rowKey: 'id',
        columns: columnsRole,
        formConfig: {
          schemas: searchFormSchemaRole,
          showResetButton: false,
        },
        // isCanResizeParent: true,
        // canResize: true,
        // 自适应高度偏移， 计算结果-偏移量
        resizeHeightOffset: 80,
        useSearchForm: true,
        showTableSetting: false,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        handleSearchInfoFn(info) {
          return info;
        },
      });

      let getTitle = computed(() => '关联角色');
      // 用户默认选中的数据权限
      let oldSelectedRoleIdList = [] as any[];
      // 更新数据权限时提交的表单
      let dataPermissionRoleForm = {
        dataPermissionId: '',
        addRoles: [] as any[],
        delRoles: [] as any[],
      };

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      const [userDataPermissionDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(
        async (data) => {
          setDrawerProps({
            confirmLoading: false,
            showFooter: true,
            destroyOnClose: true,
            okButtonProps: { props: {}, on: () => handleSubmit },
          });

          getTitle = computed(() => '设置拥有数据权限[' + data.realName + ']的角色');
          rowId.value = data.record.id;

          let dataPermissionForm = Object.assign({}, data?.record);
          dataPermissionRoleForm.dataPermissionId = dataPermissionForm?.id;
          // 获取数据权限已经绑定的角色
          const dataPermissionRoleList = await queryRoleDataPermission(
            dataPermissionRoleForm.dataPermissionId,
          );
          if (dataPermissionRoleList && dataPermissionRoleList.length > 0) {
            const roleIds = dataPermissionRoleList.map((item) => {
              return item.roleId;
            });
            oldSelectedRoleIdList = roleIds;
            checkedKeys.value = roleIds;
          } else {
            oldSelectedRoleIdList = [];
            checkedKeys.value = [];
          }
        },
      );

      async function handleSubmit() {
        try {
          //获取选中的key
          const keys = checkedKeys.value;
          console.log(keys);
          if (keys) {
            //差集 用户原先有，现在没有，说明要删除，所以放到删除列表
            let delRoleIds = oldSelectedRoleIdList?.filter((item) => keys.indexOf(item) == -1);

            let addRoleIds = keys.filter((item) => oldSelectedRoleIdList?.indexOf(item) == -1);

            if (delRoleIds.length === 0 && addRoleIds.length === 0) {
              createMessage.error('没有进行任何变更');
              return;
            }

            if (addRoleIds && addRoleIds.length > 0) {
              dataPermissionRoleForm.addRoles = addRoleIds.map((item) => {
                return { dataPermissionId: dataPermissionRoleForm.dataPermissionId, roleId: item };
              });
            }

            if (delRoleIds && delRoleIds.length > 0) {
              dataPermissionRoleForm.delRoles = delRoleIds.map((item) => {
                return { dataPermissionId: dataPermissionRoleForm.dataPermissionId, roleId: item };
              });
            }

            setDrawerProps({ confirmLoading: true });
            await updateRoleDataPermission(dataPermissionRoleForm);
            closeDrawer();
            emit('success', { values: { ...dataPermissionRoleForm, id: rowId.value } });
          } else {
            createMessage.error('没有进行任何变更');
          }
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      return {
        userDataPermissionDrawer,
        getTitle,
        oldSelectedRoleIdList,
        handleSubmit,
        registerTable,
        checkedKeys,
        searchInfo,
        onSelectChange,
      };
    },
  });
</script>

<style lang="less" scoped>
  .data-permission .org-tree .vben-basic-drawer .ant-drawer-body .scrollbar__wrap {
    margin-bottom: 0 !important;
    padding: 1px !important;
  }
</style>
