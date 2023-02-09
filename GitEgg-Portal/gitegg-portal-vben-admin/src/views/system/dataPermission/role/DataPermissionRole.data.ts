import { updateDataPermissionRoleStatus } from '/@/api/system/dataPermission/data_permission';
import { BasicColumn } from '/@/components/Table';
import { FormProps, FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Switch, Tag } from 'ant-design-vue';
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
    title: '资源接口名称',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'resourceName',
  },
  {
    title: '数据权限名称',
    align: 'center',
    ellipsis: true,
    width: 200,
    dataIndex: 'dataName',
  },
  {
    title: '权限类型',
    align: 'center',
    width: 100,
    ellipsis: true,
    dataIndex: 'dataPermissionType',
  },
  {
    title: 'Mapper方法名称',
    align: 'center',
    width: 100,
    ellipsis: true,
    dataIndex: 'dataMapperFunction',
  },
  {
    title: '数据主表',
    align: 'center',
    width: 130,
    ellipsis: true,
    dataIndex: 'dataTableName',
  },
  {
    title: '数据权限表',
    align: 'center',
    ellipsis: true,
    width: 150,
    dataIndex: 'innerTableName',
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
                  ? `确定要启用数据权限：${record.dataName}？`
                  : `确定要禁用数据权限：${record.dataName}？`,
              ),
            onOk: async () => {
              await updateDataPermissionRoleStatus(record.id, newStatus)
                .then(() => {
                  record.status = newStatus;
                  createMessage.success(
                    newStatus === 1
                      ? `已成功启用数据权限：${record.dataName}`
                      : `已成功禁用数据权限：${record.dataName}`,
                  );
                })
                .catch(() => {
                  record.pendingStatus = false;
                  createMessage.error('修改数据权限状态失败');
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
        field: 'dataName',
        label: '名称',
        component: 'Input',
        colProps: { span: 6 },
      },
      {
        field: 'dataMapperFunction',
        label: 'Mapper接口',
        component: 'Input',
        colProps: { span: 6 },
      },
      {
        field: 'dataPermissionType',
        label: '类型',
        component: 'ApiSelect',
        componentProps: {
          api: getDictCache,
          params: { dictCode: 'DATA_PERMISSION_TYPE' },
          resultField: 'dictList',
          // use name as label
          labelField: 'dictName',
          // use id as value
          valueField: 'dictCode',
          getPopupContainer: () => document.body,
        },
        colProps: { span: 6 },
      },
      {
        field: 'dataTableName',
        label: '数据主表',
        component: 'Input',
        colProps: { span: 6 },
      },
    ],
  };
}

// 数据权限 新增  修改 查看 表单定义
export const userFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'resourceName',
    label: '功能权限名称',
    component: 'Input',
    // render: ({ model, field }) => {
    //   return h('span', model[field]);
    // },
  },
  {
    field: 'dataName',
    label: '数据权限名称',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入数据权限名称',
    },
  },
  {
    field: 'dataMapperFunction',
    label: 'Mapper路径',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入数据权限对应的mapper方法全路径',
    },
  },
  {
    field: 'dataPermissionType',
    label: '数据权限类型',
    component: 'ApiSelect',
    defaultValue: '3',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DATA_PERMISSION_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      getPopupContainer: () => document.body,
    },
    required: true,
  },
  {
    field: 'dataTableName',
    label: '权限主表',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入需要做数据权限的主表',
    },
  },
  {
    field: 'dataTableAlias',
    label: '权限主表别名',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入需要做数据权限的主表别名',
    },
  },
  {
    field: 'innerTableName',
    label: '数据权限表',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入数据权限表,默认t_sys_organization',
    },
  },
  {
    field: 'innerTableAlias',
    label: '数据权限表别名',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入数据权限表的别名,默认organization',
    },
  },
  {
    field: 'dataColumnExclude',
    label: '需排除的字段',
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据权限需要排除的字段',
    },
  },
  {
    field: 'dataColumnInclude',
    label: '仅保留的字段',
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据权限需要保留的字段',
    },
  },
  {
    field: 'customExpression',
    label: '自定义表达式',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入自定义表达式，有SQL注入风险，请谨慎使用',
    },
  },
  {
    label: '状态',
    field: 'status',
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

export const columnsRole: BasicColumn[] = [
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
    title: '状态',
    dataIndex: 'roleStatus',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      const color = Number(record.roleStatus) === 1 ? 'green' : 'red';
      return h(Tag, { color }, () => (Number(record.roleStatus) === 1 ? '已启用' : '已禁用'));
    },
  },
];

export const searchFormSchemaRole: FormSchema[] = [
  {
    field: 'roleName',
    label: '角色名称',
    component: 'Input',
    componentProps: {
      placeholder: '请输入角色名称',
    },
    colProps: { span: 16 },
  },
];
