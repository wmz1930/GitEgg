import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { level } from 'province-city-china/data';
import { Switch } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { updateOrganizationStatus, checkOrganizationExist } from '/@/api/system/organization';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict } from '/@/utils/gitegg/formUtils';

export const columns: BasicColumn[] = [
  {
    title: '组织名称',
    dataIndex: 'organizationName',
    ellipsis: true,
    width: 200,
    align: 'left',
  },
  {
    title: '组织标识',
    dataIndex: 'organizationKey',
    width: 120,
  },
  {
    title: '组织类型',
    dataIndex: 'organizationType',
    customRender: ({ text }) => {
      return renderDict(text, 'ORGANIZATION_TYPE', false, false);
    },
  },
  {
    title: '组织排序',
    dataIndex: 'organizationLevel',
  },
  {
    title: '状态',
    dataIndex: 'organizationStatus',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: Number(record.organizationStatus) === 1,
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
                  ? `确定要启用组织机构：${record.organizationName}？`
                  : `确定要禁用组织机构：${record.organizationName}？`,
              ),
            onOk: async () => {
              await updateOrganizationStatus(record.id, newStatus)
                .then(() => {
                  record.organizationStatus = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用组织机构：${record.organizationName}`
                      : `已成功禁用组织机构：${record.organizationName}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改组织机构状态失败');
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
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'organizationName',
    label: '组织名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'organizationKey',
    label: '组织标识',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'organizationStatus',
    label: '组织状态',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 6 },
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
    field: 'parentId',
    label: '上级组织',
    component: 'TreeSelect',
    componentProps: {
      fieldNames: {
        label: 'organizationName',
        key: 'id',
        value: 'id',
      },
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'organizationName',
    label: '组织名称',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入组织名称',
        },
        {
          trigger: 'blur',
          message: '组织名称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'organization_name',
                checkValue: value,
              };
              checkOrganizationExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('组织名称已存在');
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
    field: 'organizationKey',
    label: '组织标识',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入组织标识',
        },
        {
          trigger: 'blur',
          message: '组织标识已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'organization_key',
                checkValue: value,
              };
              checkOrganizationExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('组织标识已存在');
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
    field: 'organizationType',
    label: '组织类型',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: '1',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ORGANIZATION_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { lg: 24, md: 24 },
  },
  {
    field: 'organizationIcon',
    label: '组织图标',
    component: 'IconPicker',
  },
  {
    field: 'organizationLevel',
    label: '组织排序',
    component: 'InputNumber',
    required: true,
  },
  {
    label: '组织地区',
    field: 'areas',
    component: 'Cascader',
    required: true,
    componentProps: {
      options: level,
      fieldNames: {
        label: 'name',
        value: 'code',
        children: 'children',
      },
    },
  },
  {
    field: 'street',
    label: '详细地址',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'organizationStatus',
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
    field: 'comments',
    label: '备注信息',
    component: 'InputTextArea',
  },
];
