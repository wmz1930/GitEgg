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
    <OrganizationTree checkable title="机构权限列表" ref="refTree" checkStrictly />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, reactive } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { updateUserDataPermission } from '/@/api/system/user';
  import { OrganizationTree } from '/@/components/Subassembly/Tree';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    components: { BasicDrawer, OrganizationTree },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const refTree: any = ref(null);
      const rowId = ref('');
      const { createMessage } = useMessage();

      let getTitle = computed(() => '设置用户数据权限');
      // 用户默认选中的数据权限
      let userCheckOrgPermission = [];
      // 更新数据权限时提交的表单
      let dataPermissionForm = {
        userId: '',
        addDataPermissions: [],
        removeDataPermissions: [],
      };

      function getTree() {
        const tree = unref(refTree?.value?.refTree);
        if (!tree) {
          throw new Error('tree is null!');
        }
        return tree;
      }

      const [userDataPermissionDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(
        async (data) => {
          setDrawerProps({
            confirmLoading: false,
            showFooter: true,
            destroyOnClose: true,
            okButtonProps: { props: {}, on: () => handleSubmit },
          });

          getTitle = computed(() => '设置用户 [' + data.realName + '] 数据权限');

          let userForm = Object.assign({}, data?.record);
          dataPermissionForm.userId = userForm?.id;

          if (userForm.organizationIds) {
            userCheckOrgPermission = userForm.organizationIds.split(',');
            getTree().setCheckedKeys(userCheckOrgPermission);
            // 默认选中
            getTree().setExpandedKeys(userCheckOrgPermission);
          } else {
            userCheckOrgPermission = [];
            getTree().setCheckedKeys([]);
          }
        },
      );

      async function handleSubmit() {
        try {
          //获取选中的key
          const keys = reactive<never[]>(getTree().getCheckedKeys()?.checked);
          console.log(keys);
          if (keys) {
            //差集 用户原先有，现在没有，说明要删除，所以放到删除列表
            let removeDataPermissions = userCheckOrgPermission?.filter(
              (item) => keys.indexOf(item) == -1,
            );

            let addDataPermissions = keys.filter(
              (item) => userCheckOrgPermission?.indexOf(item) == -1,
            );

            if (addDataPermissions.length === 0 && removeDataPermissions.length === 0) {
              createMessage.error('数据权限没有进行任何变更');
              return;
            }
            dataPermissionForm.addDataPermissions = addDataPermissions;
            dataPermissionForm.removeDataPermissions = removeDataPermissions;

            setDrawerProps({ confirmLoading: true });
            await updateUserDataPermission(dataPermissionForm);
            closeDrawer();
            emit('success', { values: { ...dataPermissionForm, id: rowId.value } });
          } else {
            createMessage.error('数据权限没有进行任何变更');
          }
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      return {
        userDataPermissionDrawer,
        refTree,
        getTitle,
        userCheckOrgPermission,
        handleSubmit,
      };
    },
  });
</script>

<style lang="less" scoped>
  .data-permission .org-tree .vben-basic-drawer .ant-drawer-body .scrollbar__wrap {
    padding: 1px !important;
    margin-bottom: 0 !important;
  }
</style>
