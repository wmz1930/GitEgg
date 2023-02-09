<template>
  <BasicModal
    v-bind="$attrs"
    @register="userModal"
    :title="getTitle"
    @ok="closeViewModal"
    width="800"
  >
    <BasicForm @register="userForm" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { userFormSchema } from './user.data';

  export default defineComponent({
    name: 'UserModal',
    components: { BasicModal, BasicForm },
    setup(_, {}) {
      const rowId = ref('');
      const [userForm, { setFieldsValue, resetFields }] = useForm({
        labelWidth: 100,
        baseColProps: { lg: 24, md: 24 },
        schemas: userFormSchema,
        showActionButtonGroup: false,
        disabled: true,
        actionColOptions: {
          span: 24,
        },
      });

      const [userModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ confirmLoading: false });
        rowId.value = data.record.id;
        setFieldsValue({
          ...data.record,
        });
      });

      const getTitle = '用户详情';

      async function closeViewModal() {
        closeModal();
      }

      return { userModal, userForm, getTitle, closeViewModal };
    },
  });
</script>
