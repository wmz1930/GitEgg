<template>
  <Basic${config.formType}
    v-bind="$attrs"
    @register="register${config.formType}"
    showFooter
    :title="getTitle"
    width="30%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </Basic${config.formType}>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './${dataTsName}';
  import { Basic${config.formType}, use${config.formType}Inner } from '/@/components/${config.formType}';
  import { create${entity}, update${entity} } from '/@/api/${vueJsPath}';

  export default defineComponent({
    name: '${entity}${config.formType}',
    components: { Basic${config.formType}, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 90,
        baseColProps: { span: 24 },
        schemas: formSchema,
        showActionButtonGroup: false,
      });

      const [register${config.formType}, { set${config.formType}Props, close${config.formType} }] = use${config.formType}Inner(async (data) => {
        resetFields();
        set${config.formType}Props({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;

        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增${config.domainName!}' : '编辑${config.domainName!}'));

      async function handleSubmit() {
        try {
          const values = await validate();
          set${config.formType}Props({ confirmLoading: true });
          if (unref(isUpdate)) {
            await update${entity}(values);
          } else {
            await create${entity}(values);
          }
          close${config.formType}();
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          set${config.formType}Props({ confirmLoading: false });
        }
      }

      return {
        register${config.formType},
        registerForm,
        getTitle,
        handleSubmit,
      };
    },
  });
</script>
