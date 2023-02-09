import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { updateJustAuthConfigStatus } from '/@/api/system/extension/justauth/justauthConfig';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict, renderStatusSwitch } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

export const justauthColumn: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '登陆开关',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'enabled',
    customRender: ({ text }) => {
      return renderDict(text, 'TRUE_FALSE', false, false);
    },
  },
  {
    title: '配置类',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'enumClass',
  },
  {
    title: 'Http超时',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'httpTimeout',
  },
  {
    title: '缓存类型',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'cacheType',
  },
  {
    title: '缓存前缀',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'cachePrefix',
  },
  {
    title: '缓存超时',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'cacheTimeout',
  },
  {
    title: '状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'status',
    customRender: ({ record }) => {
      return renderStatusSwitch(
        record,
        updateJustAuthConfigStatus,
        'status',
        '渠道' + record.enumClass,
      );
    },
  },
  {
    title: '备注',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'remark',
  },
];

export const justauthSearch: FormSchema[] = [
  {
    label: '登陆开关',
    field: 'enabled',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'TRUE_FALSE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择登陆开关',
    },
  },

  {
    label: '状态',
    field: 'status',
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
      placeholder: '请选择状态搜索',
    },
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
];

export const justauthForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },

  {
    label: '登陆开关',
    field: 'enabled',
    component: 'Switch',
    required: true,
    defaultValue: true,
    componentProps: {
      checkedChildren: '已启用',
      unCheckedChildren: '已禁用',
    },
  },
  {
    label: '配置类',
    field: 'enumClass',
    component: 'Input',
    componentProps: {
      placeholder: '请输入配置类',
    },
    required: true,
  },
  {
    label: 'Http超时',
    field: 'httpTimeout',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入Http超时时间',
    },
    required: true,
  },
  {
    label: '缓存类型',
    field: 'cacheType',
    component: 'Input',
    componentProps: {
      placeholder: '请输入缓存类型',
    },
    required: true,
  },
  {
    label: '缓存前缀',
    field: 'cachePrefix',
    component: 'Input',
    componentProps: {
      placeholder: '请输入缓存前缀',
    },
    required: true,
  },
  {
    label: '缓存超时',
    field: 'cacheTimeout',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入缓存超时时间',
    },
    required: true,
  },
  {
    label: '状态',
    field: 'status',
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
    },
    required: true,
  },

  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
