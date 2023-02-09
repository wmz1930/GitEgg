import { updateGeneratorDictStatus } from '/@/api/plugin/codeGenerator/dict/dict';
import { BasicColumn } from '/@/components/Table';
import { FormProps, FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { getDictCache } from '/@/utils/gitegg/dictUtils';

// 数据表格定义
export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '字典名称',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'dictName',
  },
  {
    title: '字典编码',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'dictCode',
  },
  {
    title: '字典排序',
    align: 'center',
    width: 100,
    ellipsis: true,
    dataIndex: 'dictOrder',
  },
  {
    title: '字典状态',
    dataIndex: 'dictStatus',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: Number(record.dictStatus) === 1,
        checkedChildren: '已启用',
        unCheckedChildren: '已禁用',
        loading: record.pendingStatus,
        onChange(checked: boolean) {
          record.pendingStatus = true;
          const newStatus = checked ? 1 : 0;
          const { createMessage, createConfirm } = useMessage();
          // const { t } = useI18n();
          createConfirm({
            iconType: 'warning',
            title: () => h('span', '操作提示'),
            content: () =>
              h(
                'span',
                newStatus === 1
                  ? `确定要启用基础配置字典：${record.dictName}？`
                  : `确定要禁用基础配置字典：${record.dictName}？`,
              ),
            onOk: async () => {
              await updateGeneratorDictStatus(record.id, newStatus)
                .then(() => {
                  record.dictStatus = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用基础配置字典：${record.dictName}`
                      : `已成功禁用基础配置字典：${record.dictName}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改基础配置字典状态失败');
                })
                .finally(() => {
                  record.pendingStatus = false;
                });
            },
            onCancel: async () => {
              record.pendingStatus = false;
            },
          });
        },
      });
    },
  },
  {
    title: '备注信息',
    align: 'center',
    width: 100,
    ellipsis: true,
    dataIndex: 'comments',
  },
];

// 搜索表单定义
export function getSearchFormConfig(): Partial<FormProps> {
  return {
    labelWidth: 50,
    baseColProps: { md: 8, sm: 24 },
    autoSubmitOnEnter: true,
    autoAdvancedLine: 1,
    actionColOptions: {
      md: 8,
      sm: 24,
    },
    schemas: [
      {
        field: 'dictName',
        label: '名称',
        component: 'Input',
      },
      {
        field: 'dictCode',
        label: '编码',
        component: 'Input',
      },
    ],
  };
}

// 系统字典 新增  修改 查看 表单定义
export const dictFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'parentId',
    label: '父级ID',
    show: false,
    component: 'Input',
  },
  {
    field: 'parentDictName',
    label: '字典类型',
    component: 'Input',
    show: ({ model, field }) => {
      return model[field] && model[field] !== '' ? true : false;
    },
    render: ({ model, field }) => {
      return h(
        'span',
        {
          style: {
            fontSize: '16px',
            fontWeight: '600',
          },
        },
        model[field] && model[field] !== '' ? model[field] : '',
      );
    },
  },
  {
    field: 'dictName',
    label: '字典名称',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入字典名称',
    },
  },
  {
    field: 'dictCode',
    label: '字典编码',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入字典编码',
    },
  },
  {
    field: 'dictOrder',
    label: '字典排序',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入字典排序',
    },
  },
  {
    label: '字典状态',
    field: 'dictStatus',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: '1',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
  },
  {
    label: '备注',
    field: 'comments',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入备注信息',
    },
  },
];
