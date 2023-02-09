<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="40%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './table.join.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { createTableJoin, updateTableJoin } from '/@/api/plugin/codeGenerator/join/table_join';
  import { queryTableList, queryTableFieldList } from '/@/api/plugin/codeGenerator/engine/engine';

  // import { getMenuList } from '/@/api/demo/system';

  export default defineComponent({
    name: 'TableJoinDrawer',
    components: { BasicDrawer, BasicForm },
    props: {
      config: {
        type: Object,
        default: () => {},
      },
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      // const treeData = ref<TreeItem[]>([]);

      const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
        labelWidth: 120,
        baseColProps: { span: 24 },
        schemas: formSchema,
        showActionButtonGroup: false,
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          handleTableChange(data.record.joinTableName);
          if (data.record.joinTableSelect && !Array.isArray(data.record.joinTableSelect)) {
            data.record.joinTableSelect = data.record.joinTableSelect.split(',');
          }
          setFieldsValue({
            datasourceId: props.config.datasourceId,
            generationId: props.config.id,
            ...data.record,
          });
        } else {
          setFieldsValue({
            datasourceId: props.config.datasourceId,
            generationId: props.config.id,
          });
        }

        const tableList = await queryTableList({ datasourceId: props.config.datasourceId });
        const tableOptions = tableList.map((item) => {
          item.labelName = `${item.tableName}[${item.tableComment}]`;
          return item;
        });
        // 数据字典赋值
        updateSchema([
          {
            field: 'joinTableName',
            componentProps: {
              options: tableOptions,
              fieldNames: {
                label: 'labelName',
                key: 'tableName',
                value: 'tableName',
              },
              placeholder: '请选择表名',
              onChange: (value: string) => {
                handleTableChange(value);
              },
            },
          },
        ]);
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增联表' : '编辑联表'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          if (values.joinTableSelect && Array.isArray(values.joinTableSelect)) {
            values.joinTableSelect = values.joinTableSelect.join(',');
          }
          if (unref(isUpdate)) {
            await updateTableJoin(values);
          } else {
            await createTableJoin(values);
          }
          closeDrawer();
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }

      // 当table change时，字段需要更新change
      async function handleTableChange(value) {
        const filedQuery = { tableName: value, datasourceId: props.config.datasourceId };
        const tableFieldList = await queryTableFieldList(filedQuery);
        const tableFieldOptions = tableFieldList[0]?.fields.map((item) => {
          item.labelName = `${item.name}[${item.comment}]`;
          return item;
        });
        // 使用updateSchema数组，只能更新一个，框架应该是有bug，所以此处单独更新
        updateSchema({
          field: 'joinTableSelect',
          componentProps: {
            options: tableFieldOptions,
            fieldNames: {
              label: 'labelName',
              key: 'name',
              value: 'name',
            },
            placeholder: '请选择查询字段',
          },
        });
        updateSchema({
          field: 'associationId',
          componentProps: {
            options: tableFieldOptions,
            fieldNames: {
              label: 'labelName',
              key: 'name',
              value: 'name',
            },
            placeholder: '请选择主键字段',
          },
        });
      }

      return {
        registerDrawer,
        registerForm,
        getTitle,
        handleSubmit,
        handleTableChange,
      };
    },
  });
</script>
