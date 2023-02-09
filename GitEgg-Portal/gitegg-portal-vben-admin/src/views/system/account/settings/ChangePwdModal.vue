<template>
  <BasicModal
    v-bind="$attrs"
    @register="passwordModal"
    showFooter
    :title="getTitle"
    width="30%"
    @ok="handleSubmit"
  >
    <div>
      <BasicForm @register="passwordForm" />
    </div>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, h } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { passwordFormSchema } from './data';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { updatePwd } from '/@/api/system/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStoreWithOut } from '/@/store/modules/user';

  export default defineComponent({
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, {}) {
      const getTitle = '修改密码';
      const { createConfirm } = useMessage();
      const userStore = useUserStoreWithOut();
      const [passwordForm, { setFieldsValue, resetFields, validate }] = useForm({
        layout: 'horizontal',
        labelWidth: 100,
        baseColProps: { lg: 24, md: 24 },
        schemas: passwordFormSchema,
        showActionButtonGroup: false,
      });

      const [passwordModal, { setModalProps, closeModal }] = useModalInner(async () => {
        resetFields();
        setModalProps({
          confirmLoading: false,
          showFooter: true,
          destroyOnClose: true,
          okButtonProps: { props: {}, on: () => handleSubmit },
        });
      });

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          await updatePwd(values);
          createConfirm({
            iconType: 'success',
            title: () => h('span', '密码修改成功'),
            content: () => h('span', '用户密码修改成功,是否退出并重新登录？'),
            onOk: async () => {
              await userStore.logout(true);
            },
          });
          closeModal();
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return { passwordModal, getTitle, passwordForm, handleSubmit };
    },
  });
</script>
