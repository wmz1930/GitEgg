import { checkUserExist, updateUserStatus } from '/@/api/system/user';
import { getRoleListAll } from '/@/api/system/role';
// import { listDictBusiness } from '/@/api/system/base/dictBusiness';
import { getDictBusinessCache } from '/@/utils/gitegg/dictUtils';
import { BasicColumn } from '/@/components/Table';
import { FormProps, FormSchema } from '/@/components/Table';
import { level } from 'province-city-china/data';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { getOrganizationList } from '/@/api/system/organization';
// import { useI18n } from '/@/hooks/web/useI18n';

import dayjs from 'dayjs';

// 数据表格定义
export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '组织机构',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'organizationName',
  },
  {
    title: '账号',
    align: 'center',
    width: 100,
    ellipsis: true,
    dataIndex: 'account',
  },
  {
    title: '姓名',
    align: 'center',
    width: 100,
    ellipsis: true,
    dataIndex: 'realName',
  },
  {
    title: '手机号',
    align: 'center',
    width: 130,
    ellipsis: true,
    dataIndex: 'mobile',
  },
  {
    title: '邮箱',
    align: 'center',
    ellipsis: true,
    width: 150,
    dataIndex: 'email',
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
                  ? `确定要启用用户：${record.account}？`
                  : `确定要禁用用户：${record.account}？`,
              ),
            onOk: async () => {
              await updateUserStatus(record.id, newStatus)
                .then(() => {
                  record.status = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用用户：${record.account}`
                      : `已成功禁用用户：${record.account}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改用户状态失败');
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
    title: '角色',
    align: 'center',
    width: 150,
    ellipsis: true,
    dataIndex: 'roleName',
  },
  {
    title: '性别',
    align: 'center',
    dataIndex: 'gender',
    width: 100,
    ellipsis: true,
  },
  {
    title: '注册日期',
    align: 'center',
    sorter: true,
    dataIndex: 'createTime',
    width: 200,
  },
];

// 搜索表单定义
export function getSearchFormConfig(): Partial<FormProps> {
  return {
    labelWidth: 100,
    autoSubmitOnEnter: true,
    autoAdvancedLine: 1,
    actionColOptions: {
      span: 24,
    },
    schemas: [
      {
        field: 'account',
        label: '账号',
        component: 'Input',
        colProps: { span: 6 },
        componentProps: {
          placeholder: '请输入账号',
        },
      },
      {
        field: 'mobile',
        label: '手机号',
        component: 'Input',
        colProps: { span: 6 },
        dynamicRules: ({}) => {
          return [
            {
              validator: (_, value) => {
                if (value && '' !== value) {
                  if (!new RegExp(/^1[3|4|5|6|7|8|9][0-9]\d{8}$/).test(value)) {
                    return Promise.reject('请输入正确的手机号码');
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          placeholder: '请输入手机号',
        },
      },
      {
        field: 'roleId',
        label: '角色',
        component: 'ApiSelect',
        componentProps: {
          api: getRoleListAll,
          labelField: 'roleName',
          valueField: 'id',
          placeholder: '请选择角色',
        },
        colProps: { span: 6 },
      },
      {
        field: 'email',
        label: '邮箱地址',
        component: 'Input',
        dynamicRules: ({}) => {
          return [
            {
              type: 'email',
              message: '请输入正确格式的电子邮箱',
            },
          ];
        },
        componentProps: {
          placeholder: '请输入邮箱',
        },
        colProps: { span: 6 },
      },
      {
        field: 'beginDateTime',
        label: '开始时间',
        component: 'DatePicker',
        componentProps: {
          showTime: {
            defaultValue: dayjs().startOf('day'),
          },
          showNow: false,
          format: 'YYYY-MM-DD HH:mm:ss',
          style: 'width:100%;',
          placeholder: '请选择开始时间',
        },
        colProps: { span: 6 },
      },
      {
        field: 'endDateTime',
        label: '结束时间',
        component: 'DatePicker',
        componentProps: {
          showTime: {
            defaultValue: dayjs().endOf('day'),
          },
          showNow: false,
          format: 'YYYY-MM-DD HH:mm:ss',
          style: 'width:100%;',
          placeholder: '请选择结束时间',
        },
        colProps: { span: 6 },
      },
    ],
  };
}

// 用户 新增  修改 查看 表单定义
export const userFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'organizationId',
    label: '所属组织',
    component: 'ApiTreeSelect',
    componentProps: {
      api: getOrganizationList,
      fieldNames: {
        label: 'organizationName',
        key: 'id',
        value: 'id',
      },
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
    required: true,
  },
  {
    field: 'account',
    label: '用户账号',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入用户名',
        },
        {
          message: '用户账号已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'account',
                checkValue: value,
              };
              checkUserExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('用户账号已存在');
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
    field: 'nickname',
    label: '昵称',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入昵称',
        },
        {
          trigger: 'blur',
          message: '昵称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'nickname',
                checkValue: value,
              };
              checkUserExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('昵称已存在');
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
    field: 'realName',
    label: '用户姓名',
    component: 'Input',
    required: true,
  },
  {
    field: 'mobile',
    label: '手机号码',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入手机号码',
        },
        {
          trigger: 'blur',
          validator: (_, value) => {
            if (value && '' !== value) {
              if (!new RegExp(/^1[3|4|5|6|7|8|9][0-9]\d{8}$/).test(value)) {
                return Promise.reject('请输入正确的手机号码');
              }
              return new Promise((resolve, reject) => {
                const keyData = {
                  id: model.id,
                  checkField: 'mobile',
                  checkValue: value,
                };
                checkUserExist(keyData)
                  .then((response) => {
                    if (!response) {
                      reject('手机号码已存在');
                    } else {
                      resolve();
                    }
                  })
                  .catch((err) => {
                    reject(err.message || '验证失败');
                  });
              });
            } else {
              return Promise.resolve();
            }
          },
        },
      ];
    },
  },
  {
    field: 'email',
    label: '电子邮箱',
    component: 'Input',
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          type: 'email',
          message: '请输入正确格式的电子邮箱',
        },
        {
          trigger: 'blur',
          message: '电子邮箱已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'email',
                checkValue: value,
              };
              checkUserExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('电子邮箱已存在');
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
    label: '角色',
    field: 'roleIds',
    component: 'ApiSelect',
    componentProps: {
      mode: 'multiple',
      api: getRoleListAll,
      resultField: 'list',
      labelField: 'roleName',
      valueField: 'id',
    },
    required: true,
  },
  {
    label: '用户地址',
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
    label: '详细地址',
    field: 'street',
    component: 'Input',
    required: true,
  },
  {
    label: '性别',
    field: 'gender',
    required: true,
    component: 'ApiRadioGroup',
    defaultValue: '2',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'GENDER' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: '2',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'USER_STATUS' },
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
];
