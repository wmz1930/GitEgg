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
  import { userFormSchema } from './user.data';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { createUser, updateUser } from '/@/api/system/user';

  export default defineComponent({
    components: { BasicDrawer, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');

      const getTitle = computed(() => (!unref(isUpdate) ? '新增用户' : '编辑用户'));

      const [userForm, { setFieldsValue, resetFields, validate }] = useForm({
        layout: 'horizontal',
        labelWidth: 100,
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

        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
        }

        // 可以初始化一些数据字典数据， 也可以在form中使用自定义组件
        // const treeData = await getOrganizationList({});
        // updateSchema([
        //   {
        //     field: 'organizationId',
        //     componentProps: { treeData },
        //   },
        // ]);
      });

      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            await updateUser(values);
          } else {
            await createUser(values);
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
