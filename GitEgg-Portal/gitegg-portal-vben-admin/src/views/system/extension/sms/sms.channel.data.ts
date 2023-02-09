import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { updateSmsChannelStatus } from '/@/api/system/extension/sms/smsChannel';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict, renderStatusSwitch, renderCheckExistRules } from '/@/utils/gitegg/formUtils';
import { checkSmsChannelExist } from '/@/api/system/extension/sms/smsChannel';
import dayjs from 'dayjs';

export const smsChannelForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '渠道',
    field: 'channelCode',
    component: 'ApiSelect',
    defaultValue: 'aliyun',
    required: true,
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'SMS_CHANNEL' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择渠道',
    },
    dynamicRules: ({ model }) => {
      return renderCheckExistRules(model, checkSmsChannelExist, 'channel_code', '短信渠道');
    },
  },
  {
    label: 'SecretId',
    field: 'secretId',
    component: 'Input',
    required: true,
  },
  {
    label: 'SecretKey',
    field: 'secretKey',
    component: 'Input',
    required: true,
  },
  {
    label: 'RegionId',
    field: 'regionId',
    component: 'Input',
    required: true,
  },

  {
    label: '渠道状态',
    field: 'channelStatus',
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
      placeholder: '请选择渠道状态',
    },
  },
  {
    label: '短信数量',
    field: 'smsQuantity',
    component: 'InputNumber',
    componentProps: {
      placeholder: '短信数量，0为不限制',
    },
  },
  {
    label: '有效日期',
    field: 'smsValidity',
    component: 'DatePicker',
    componentProps: {
      format: 'YYYY-MM-DD HH:mm:ss',
      placeholder: '请选择有效日期',
      style: 'width:100%;',
      showTime: {
        defaultValue: dayjs().endOf('day'),
      },
      showNow: false,
    },
  },
  {
    label: '描述',
    field: 'comments',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入描述',
    },
  },
];

export const smsChannelColumns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '渠道编码',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'channelCode',
  },
  {
    title: '渠道名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'channelCode',
    customRender: ({ text }) => {
      return renderDict(text, 'SMS_CHANNEL', false, false);
    },
  },
  {
    title: '短信数量',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'smsQuantity',
  },
  {
    title: '有效期',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'smsValidity',
  },
  {
    title: '渠道状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'channelStatus',
    customRender: ({ record }) => {
      return renderStatusSwitch(record, updateSmsChannelStatus, 'channelStatus', '渠道');
    },
  },
  {
    title: '描述',
    align: 'center',
    ellipsis: true,
    dataIndex: 'comments',
  },
];

export const channelSearchForm: FormSchema[] = [
  {
    label: '渠道',
    field: 'channelCode',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'SMS_CHANNEL' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择渠道',
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
