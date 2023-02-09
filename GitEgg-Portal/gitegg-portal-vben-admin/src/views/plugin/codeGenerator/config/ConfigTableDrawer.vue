<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="800"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './config.table.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { createConfig, updateConfig } from '/@/api/plugin/codeGenerator/config/config';
  import { queryTableList } from '/@/api/plugin/codeGenerator/engine/engine';

  export default defineComponent({
    name: 'ConfigDrawer',
    components: { BasicDrawer, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');

      const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
        labelWidth: 90,
        baseColProps: { span: 24 },
        schemas: formSchema,
        showActionButtonGroup: false,
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;

        updateSchema([
          {
            field: 'datasourceId',
            componentProps: {
              onChange: (value: string) => {
                changeTableList(value);
              },
            },
          },
        ]);

        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          changeTableList(data.record.datasourceId);
          setFieldsValue({
            ...data.record,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增代码定制' : '编辑代码定制'));

      async function changeTableList(id) {
        const tableList = await queryTableList({ datasourceId: id });
        const tableOptions = tableList.map((item) => {
          item.labelName = `${item.tableName}[${item.tableComment}]`;
          return item;
        });
        // 切换数据源展示表
        updateSchema([
          {
            field: 'tableName',
            componentProps: {
              options: tableOptions,
              fieldNames: {
                label: 'labelName',
                title: 'labelName',
                key: 'tableName',
                value: 'tableName',
              },
              showSearch: true,
              filterOption: filterOption,
              placeholder: '请选择表名',
              onChange: (value: string, option: any) => {
                handleTableChange(value, option);
              },
            },
          },
        ]);
      }

      function handleTableChange(value: string, option: any) {
        console.log(value + option.tableComment);
      }

      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            await updateConfig(values);
          } else {
            const config = await createConfig(values);
            rowId.value = config.id;
          }
          closeDrawer();
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }

      const filterOption = (input: string, option: any) => {
        return option.labelName.toLowerCase().indexOf(input.toLowerCase()) >= 0;
      };

      return {
        registerDrawer,
        registerForm,
        getTitle,
        handleSubmit,
        changeTableList,
        filterOption,
      };
    },
  });
</script>
