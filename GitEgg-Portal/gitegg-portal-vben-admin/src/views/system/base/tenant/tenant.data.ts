import { BasicColumn } from '/@/components/Table';
import { FormProps, FormSchema } from '/@/components/Table';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { updateTenantStatus } from '/@/api/system/base/tenant';
import { renderStatusSwitch } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

export const tenantColumn: BasicColumn[] = [
  {
    title: '租户名称',
    align: 'center',
    dataIndex: 'tenantName',
  },
  {
    title: '域名',
    align: 'center',
    dataIndex: 'domainName',
  },
  {
    title: '联系人',
    align: 'center',
    dataIndex: 'contacts',
  },
  {
    title: '联系电话',
    align: 'center',
    dataIndex: 'contactNumber',
  },
  {
    title: '账号限额',
    align: 'center',
    dataIndex: 'accountLimit',
  },
  {
    title: '过期时间',
    align: 'center',
    dataIndex: 'expireTime',
  },
  {
    title: '授权码',
    align: 'center',
    dataIndex: 'licenseKey',
  },
  {
    title: '租户状态',
    align: 'center',
    dataIndex: 'tenantStatus',
    customRender: ({ record }) => {
      return renderStatusSwitch(record, updateTenantStatus, 'tenantStatus', '租户');
    },
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
        label: '租户名称',
        field: 'tenantName',
        component: 'Input',
        colProps: { span: 6 },
        componentProps: {
          placeholder: '请输入租户名称',
        },
      },
      {
        label: '域名',
        field: 'domainName',
        component: 'Input',
        colProps: { span: 6 },
        componentProps: {
          placeholder: '请输入域名',
        },
      },
      {
        label: '联系电话',
        field: 'contactNumber',
        component: 'Input',
        colProps: { span: 6 },
        componentProps: {
          placeholder: '请输入联系电话',
        },
      },
      {
        label: '状态',
        field: 'tenantStatus',
        component: 'ApiSelect',
        colProps: { span: 6 },
        componentProps: {
          api: getDictCache,
          params: { dictCode: 'ENABLE_OR_NOT' },
          resultField: 'dictList',
          // use name as label
          labelField: 'dictName',
          // use id as value
          valueField: 'dictCode',
          placeholder: '请选择状态',
        },
      },
      {
        label: '开始时间',
        field: 'beginDateTime',
        colProps: { span: 6 },
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD HH:mm:ss',
          style: 'width:100%;',
          placeholder: '请选择有效日期',
          showNow: false,
          showTime: {
            defaultValue: dayjs().startOf('day'),
          },
        },
      },
      {
        label: '结束时间',
        field: 'endDateTime',
        colProps: { span: 6 },
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD HH:mm:ss',
          style: 'width:100%;',
          placeholder: '请选择有效日期',
          showNow: false,
          showTime: {
            defaultValue: dayjs().endOf('day'),
          },
        },
      },
    ],
  };
}

export const tenantForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },

  {
    label: '租户名称',
    field: 'tenantName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入租户名称',
    },
    required: true,
  },
  {
    label: '域名',
    field: 'domainName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入域名',
    },
    required: true,
  },
  {
    label: '上传背景图片',
    field: 'backgroundImage',
    component: 'CommonUpload',
    componentProps: {
      uploadBtnText: '上传背景图片',
      maxNumber: 1,
      accessPrivate: false,
      uploadType: 'image',
    },
    required: true,
  },
  {
    label: '联系人',
    field: 'contacts',
    component: 'Input',
    componentProps: {
      placeholder: '联系人',
    },
    required: true,
  },
  {
    label: '联系电话',
    field: 'contactNumber',
    component: 'Input',
    componentProps: {
      placeholder: '请输入联系电话',
    },
    required: true,
  },
  {
    label: '联系地址',
    field: 'address',
    component: 'Input',
    componentProps: {
      placeholder: '请输入联系联系地址',
    },
    required: true,
  },
  {
    label: '账号限额',
    field: 'accountLimit',
    component: 'Input',
    componentProps: {
      placeholder: '输入账号限额',
    },
    required: true,
  },
  {
    label: '过期时间',
    field: 'expireTime',
    component: 'DatePicker',
    componentProps: {
      format: 'YYYY-MM-DD HH:mm:ss',
      style: 'width:100%;',
      placeholder: '请选择过期时间',
      showTime: {
        defaultValue: dayjs().endOf('day'),
      },
    },
    required: true,
  },
  {
    label: '授权码',
    field: 'licenseKey',
    component: 'Input',
    componentProps: {
      placeholder: '输入授权码',
    },
    required: true,
  },
  {
    label: '租户状态',
    field: 'tenantStatus',
    component: 'ApiRadioGroup',
    defaultValue: '1',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择状态',
    },
    required: true,
  },
  {
    label: '备注',
    field: 'comments',
    component: 'InputTextArea',
  },
];
