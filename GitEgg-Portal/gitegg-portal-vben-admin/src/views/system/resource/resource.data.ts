import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Icon } from '/@/components/Icon';
import { Switch } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { updateResourceStatus, checkResourceExist } from '/@/api/system/resource';
import { getDictCache } from '/@/utils/gitegg/dictUtils';

export const columns: BasicColumn[] = [
  {
    title: '资源名称',
    dataIndex: 'resourceName',
    ellipsis: true,
    width: 200,
    align: 'left',
  },
  {
    title: '资源图标',
    dataIndex: 'resourceIcon',
    customRender: ({ record }) => {
      return h(Icon, { icon: record.resourceIcon });
    },
  },
  {
    title: '资源标识',
    dataIndex: 'resourceKey',
    width: 120,
  },
  {
    title: '资源链接',
    dataIndex: 'resourceUrl',
  },
  {
    title: '资源排序',
    dataIndex: 'resourceLevel',
  },
  {
    title: '状态',
    dataIndex: 'resourceStatus',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: Number(record.resourceStatus) === 1,
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
                  ? `确定要启用资源：${record.resourceName}？`
                  : `确定要禁用资源：${record.resourceName}？`,
              ),
            onOk: async () => {
              await updateResourceStatus(record.id, newStatus)
                .then(() => {
                  record.resourceStatus = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用资源：${record.resourceName}`
                      : `已成功禁用资源：${record.resourceName}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改资源状态失败');
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
    field: 'resourceName',
    label: '资源名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'resourceKey',
    label: '资源标识',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'resourceStatus',
    label: '资源状态',
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
    label: '上级资源',
    component: 'TreeSelect',
    componentProps: {
      fieldNames: {
        label: 'resourceName',
        key: 'id',
        value: 'id',
      },
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'resourceName',
    label: '资源名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'resourceKey',
    label: '资源标识',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入资源标识',
        },
        {
          trigger: 'blur',
          message: '资源标识已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'resourceKey',
                checkValue: value,
              };
              checkResourceExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('资源标识已存在');
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
    field: 'resourceType',
    label: '资源类型',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: '1',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'RESOURCE_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { lg: 24, md: 24 },
  },
  {
    field: 'resourceIcon',
    label: '资源图标',
    component: 'IconPicker',
  },
  {
    field: 'resourcePath',
    label: '路由地址',
    component: 'Input',
    required: true,
  },
  {
    field: 'resourceUrl',
    label: '组件路径',
    component: 'Input',
  },
  {
    field: 'resourceLevel',
    label: '资源排序',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'resourcePageName',
    label: '页面名称',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'resourceStatus',
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
    field: 'resourceCache',
    label: '是否缓存',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: 'false',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'TRUE_FALSE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
  },
  {
    field: 'resourceShow',
    label: '是否显示',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: 'false',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'TRUE_FALSE' },
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
