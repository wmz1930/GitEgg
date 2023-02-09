import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { updateRoleStatus, checkRoleExist } from '/@/api/system/role';
import { useMessage } from '/@/hooks/web/useMessage';
import { getDictCache, getDictBusinessCache } from '/@/utils/gitegg/dictUtils';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '角色名称',
    dataIndex: 'roleName',
  },
  {
    title: '角色标识',
    dataIndex: 'roleKey',
  },
  {
    title: '数据权限',
    dataIndex: 'dataPermissionType',
  },
  {
    title: '角色级别',
    dataIndex: 'roleLevel',
  },
  {
    title: '状态',
    dataIndex: 'roleStatus',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: Number(record.roleStatus) === 1,
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
                  ? `确定要启用角色：${record.roleName}？`
                  : `确定要禁用角色：${record.roleName}？`,
              ),
            onOk: async () => {
              await updateRoleStatus(record.id, newStatus)
                .then(() => {
                  record.roleStatus = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用角色：${record.roleName}`
                      : `已成功禁用角色：${record.roleName}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改角色状态失败');
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
  },
  {
    title: '备注',
    dataIndex: 'comments',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'roleName',
    label: '角色名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'roleKey',
    label: '角色标识',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'roleStatus',
    label: '状态',
    component: 'ApiSelect',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
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
    field: 'roleName',
    label: '角色名称',
    required: true,
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入角色名称',
        },
        {
          trigger: 'blur',
          message: '角色名称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'role_name',
                checkValue: value,
              };
              checkRoleExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('角色名称已存在');
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
    field: 'roleKey',
    label: '角色标识',
    required: true,
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入角色标识',
        },
        {
          trigger: 'blur',
          message: '角色标识已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'role_key',
                checkValue: value,
              };
              checkRoleExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('角色标识已存在');
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
    field: 'roleLevel',
    label: '角色级别',
    component: 'Input',
  },
  {
    field: 'dataPermissionType',
    label: '数据权限',
    component: 'ApiSelect',
    defaultValue: '3',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DATA_PERMISSION_TYPE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    required: true,
  },
  {
    label: '状态',
    field: 'roleStatus',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: '1',
    componentProps: {
      api: getDictBusinessCache,
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
  },
  {
    label: ' ',
    field: 'menu',
    slot: 'menu',
    component: 'Input',
  },
];
