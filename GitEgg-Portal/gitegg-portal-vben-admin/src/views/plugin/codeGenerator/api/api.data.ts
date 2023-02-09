import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import {
  updateGeneratorApiStatus,
  checkGeneratorApiExist,
} from '/@/api/plugin/codeGenerator/api/api';
import { useMessage } from '/@/hooks/web/useMessage';

import { getDictCache } from '/@/utils/gitegg/dictUtils';

// 数据表格Column定义
export const columns: BasicColumn[] = [
  {
    title: '接口名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'apiName',
  },
  {
    title: '接口地址',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'apiPath',
  },
  {
    title: '接口方法',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'apiMethod',
  },
  {
    title: '状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'apiStatus',
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: Number(record.apiStatus) === 1,
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
            content: () => h('span', newStatus === 1 ? `确定要启用？` : `确定要禁用？`),
            onOk: async () => {
              await updateGeneratorApiStatus(record.id, newStatus)
                .then(() => {
                  record.apiStatus = newStatus;
                  createMessage.success(newStatus === 1 ? `已成功启用` : `已成功禁用`);
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改状态失败');
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
    title: 'label字段',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'labelField',
  },
  {
    title: 'value字段',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'valueField',
  },
  {
    title: '备注',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'comments',
  },
];

// 查询条件表单定义
export const searchFormSchema: FormSchema[] = [
  {
    field: 'apiName',
    label: '接口名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'apiMethod',
    label: '接口方法',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'apiStatus',
    label: '状态',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    colProps: { span: 6 },
  },
];

// 新增/修改表单定义
export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'apiName',
    label: '接口名称',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入接口名称',
        },
        {
          trigger: 'blur',
          message: '接口名称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'api_name',
                checkValue: value,
              };
              checkGeneratorApiExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('接口名称已存在');
                  } else {
                    resolve();
                  }
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ];
    },
    colProps: { span: 24 },
  },
  {
    field: 'apiPath',
    label: '接口地址',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'apiMethod',
    label: '接口方法',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'apiParams',
    label: '接口参数',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'apiStatus',
    label: '状态',
    defaultValue: '1',

    component: 'ApiRadioGroup',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    colProps: { span: 24 },
  },
  {
    field: 'apiObject',
    label: '接口对象',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'labelField',
    label: 'label字段',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'valueField',
    label: 'value字段',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'comments',
    label: '备注',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];
