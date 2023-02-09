<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="roleResourceDrawer"
    showFooter
    :title="getTitle"
    width="30%"
    class="role-resource"
    @ok="handleSubmit"
  >
    <ResourceTree checkable title="资源权限列表" ref="refTree" checkStrictly />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, reactive } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { queryRoleResource, updateRoleResources } from '/@/api/system/role';
  import { ResourceTree } from '/@/components/Subassembly/Tree';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    components: { BasicDrawer, ResourceTree },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const refTree: any = ref(null);
      const rowId = ref('');
      const { createMessage } = useMessage();

      let getTitle = computed(() => '设置角色资源权限');
      // 角色默认选中的资源权限
      let roleCheckResource = [];
      // 更新资源权限时提交的表单
      let roleResourceForm = {
        roleId: '',
        addResources: [] as any[],
        delResources: [] as any[],
      };

      function getTree() {
        const tree = unref(refTree?.value?.refTree);
        if (!tree) {
          throw new Error('tree is null!');
        }
        return tree;
      }

      const [roleResourceDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        setDrawerProps({
          confirmLoading: false,
          showFooter: true,
          destroyOnClose: true,
          okButtonProps: { props: {}, on: () => handleSubmit },
        });

        getTitle = computed(() => '设置角色 [' + data.roleName + '] 资源权限');

        let userForm = Object.assign({}, data?.record);
        roleResourceForm.roleId = userForm?.id;

        // 获取角色的资源权限
        const roleResourceData = await queryRoleResource(roleResourceForm.roleId);
        if (roleResourceData && roleResourceData.length > 0) {
          let resourceIds = roleResourceData.map((item) => {
            return item.resourceId;
          });
          roleCheckResource = resourceIds;
          getTree().setCheckedKeys(resourceIds);
          // 默认选中
          getTree().setExpandedKeys(resourceIds);
        } else {
          roleCheckResource = [];
          getTree().setCheckedKeys([]);
        }
      });

      async function handleSubmit() {
        try {
          //获取选中的key
          const keys = reactive<never[]>(getTree().getCheckedKeys()?.checked);
          console.log(keys);
          if (keys) {
            //差集 角色原先有，现在没有，说明要删除，所以放到删除列表
            let delResources = roleCheckResource?.filter((item) => keys.indexOf(item) == -1);

            let addResources = keys.filter((item) => roleCheckResource?.indexOf(item) == -1);

            if (addResources.length === 0 && delResources.length === 0) {
              createMessage.error('角色权限没有进行任何变更');
              return;
            }

            roleResourceForm.addResources = addResources.map((item) => {
              return { roleId: roleResourceForm.roleId, resourceId: item };
            });

            roleResourceForm.delResources = delResources.map((item) => {
              return { roleId: roleResourceForm.roleId, resourceId: item };
            });

            setDrawerProps({ confirmLoading: true });
            await updateRoleResources(roleResourceForm);
            closeDrawer();
            emit('success', { values: { ...roleResourceForm, id: rowId.value } });
          } else {
            createMessage.error('角色权限没有进行任何变更');
          }
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      return {
        roleResourceDrawer,
        refTree,
        getTitle,
        roleCheckResource,
        handleSubmit,
      };
    },
  });
</script>

<style lang="less" scoped>
  .role-resource .resource-tree .vben-basic-drawer .ant-drawer-body .scrollbar__wrap {
    padding: 1px !important;
    margin-bottom: 0 !important;
  }
</style>
