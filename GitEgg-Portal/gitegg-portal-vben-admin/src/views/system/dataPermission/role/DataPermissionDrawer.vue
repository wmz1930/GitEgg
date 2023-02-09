<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="userDrawer"
    showFooter
    :title="getTitle"
    width="30%"
    @ok="handleSubmit"
  >
    <div>
      <BasicForm @register="userForm" />
    </div>
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { userFormSchema } from './DataPermissionRole.data';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import {
    createDataPermissionRole,
    updateDataPermissionRole,
  } from '/@/api/system/dataPermission/data_permission';
  import { h } from 'vue';

  export default defineComponent({
    components: { BasicDrawer, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const currentResourceId = ref('');
      const currentResourceName = ref('');

      const getTitle = computed(() => (!unref(isUpdate) ? '新增数据权限' : '编辑数据权限'));

      const [userForm, { setFieldsValue, resetFields, validate, updateSchema }] = useForm({
        layout: 'horizontal',
        labelWidth: 120,
        baseColProps: { lg: 24, md: 24 },
        schemas: userFormSchema,
        showActionButtonGroup: false,
      });

      const [userDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({
          confirmLoading: false,
          showFooter: true,
          destroyOnClose: true,
          okButtonProps: { props: {}, on: () => handleSubmit },
        });

        isUpdate.value = !!data?.isUpdate;
        currentResourceName.value = data?.currentResourceName;
        currentResourceId.value = data?.currentResourceId;

        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
        }

        // 可以初始化一些数据字典数据， 也可以在form中使用自定义组件
        updateSchema([
          {
            field: 'resourceName',
            label: '资源权限名称',
            component: 'Input',
            render: ({ model, field }) => {
              return h(
                'span',
                model[field] && model[field] !== '' ? model[field] : currentResourceName.value,
              );
            },
          },
        ]);
      });

      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            await updateDataPermissionRole(values);
          } else {
            values.resourceId = currentResourceId.value;
            await createDataPermissionRole(values);
          }
          closeDrawer();
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      return { userDrawer, getTitle, userForm, handleSubmit };
    },
  });
</script>
