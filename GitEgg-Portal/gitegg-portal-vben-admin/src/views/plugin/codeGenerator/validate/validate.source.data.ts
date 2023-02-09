import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {
  checkValidateExist,
  updateValidateStatus,
} from '/@/api/plugin/codeGenerator/validate/validate';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';

import { getDictCache } from '/@/utils/gitegg/dictUtils';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '校验名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'validateName',
  },
  {
    title: '校验规则',
    align: 'center',
    ellipsis: true,
    dataIndex: 'validateRegular',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: Number(record.status) === 1,
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
                  ? `确定要启用校验规则：${record.status}？`
                  : `确定要禁用校验规则：${record.status}？`,
              ),
            onOk: async () => {
              await updateValidateStatus(record.id, newStatus)
                .then(() => {
                  record.status = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用校验规则：${record.status}`
                      : `已成功禁用校验规则：${record.status}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改校验规则状态失败');
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
    title: '创建日期',
    align: 'center',
    dataIndex: 'createTime',
    width: 200,
  },
  {
    title: '备注',
    dataIndex: 'comments',
    width: 200,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'validateName',
    label: '校验名称',
    component: 'Input',
    componentProps: {
      placeholder: '请输入校验规则名称',
    },
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择状态',
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'validateName',
    label: '校验名称',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入校验规则名称',
    },
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入校验规则名称',
        },
        {
          trigger: 'blur',
          message: '校验规则名称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'validate_name',
                checkValue: value,
              };
              checkValidateExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('校验规则名称已存在');
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
  },
  {
    field: 'validateRegular',
    label: '校验规则',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入校验规则',
    },
  },
  {
    field: 'status',
    label: '状态',
    component: 'ApiRadioGroup',
    defaultValue: '1',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择状态',
    },
    required: true,
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
