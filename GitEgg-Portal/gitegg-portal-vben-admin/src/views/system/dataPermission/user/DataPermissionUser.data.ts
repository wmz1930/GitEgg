import { updateUserStatus } from '/@/api/system/user';
import { getRoleListAll } from '/@/api/system/role';
import { BasicColumn } from '/@/components/Table';
import { FormProps } from '/@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';
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
    title: '所属机构',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'organizationName',
  },
  {
    title: '数据权限机构',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'organizationNames',
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
      },
      {
        field: 'roleId',
        label: '角色',
        component: 'ApiSelect',
        componentProps: {
          api: getRoleListAll,
          labelField: 'roleName',
          valueField: 'id',
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
        },
        colProps: { span: 6 },
      },
    ],
  };
}
