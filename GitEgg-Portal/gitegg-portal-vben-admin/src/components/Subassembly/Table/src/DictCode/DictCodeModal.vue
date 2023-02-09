<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    showFooter
    :title="getTitle"
    width="800"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { dictFormSchema } from './dict.code';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { createGeneratorDict, updateGeneratorDict } from '/@/api/plugin/codeGenerator/dict/dict';

  export default defineComponent({
    name: 'DictModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const parentDictName = ref('');

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 90,
        baseColProps: { span: 24 },
        schemas: dictFormSchema,
        showActionButtonGroup: false,
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        parentDictName.value = data?.parentDict?.dictName;
        if (unref(isUpdate)) {
          rowId.value = data?.record?.id;
          data.record.parentDictName = parentDictName?.value;
          setFieldsValue({
            ...data.record,
          });
        } else if (data?.parentDict) {
          setFieldsValue({
            parentId: data?.parentDict?.id,
            parentDictName: parentDictName?.value,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增基础配置字典' : '编辑基础配置字典'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            await updateGeneratorDict(values);
          } else {
            await createGeneratorDict(values);
          }
          closeModal();
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return {
        registerModal,
        registerForm,
        getTitle,
        handleSubmit,
      };
    },
  });
</script>
