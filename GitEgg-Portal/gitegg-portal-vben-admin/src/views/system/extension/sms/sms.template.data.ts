import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {
  updateSmsTemplateStatus,
  checkSmsTemplateExist,
} from '/@/api/system/extension/sms/sms_template';
import { querySmsChannelListAll } from '/@/api/system/extension/sms/smsChannel';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict, renderStatusSwitch, renderCheckExistRules } from '/@/utils/gitegg/formUtils';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '短信渠道',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'channelId',
    customRender: ({ text }) => {
      return renderDict(text, 'SMS_CHANNEL', false, false);
    },
  },
  {
    title: '短信编码',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'smsCode',
  },
  {
    title: '短信名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'smsName',
  },
  {
    title: '模板ID',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'templateId',
  },
  {
    title: '短信签名',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'signName',
  },
  {
    title: '模板状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'templateStatus',
    customRender: ({ record }) => {
      return renderStatusSwitch(record, updateSmsTemplateStatus, 'templateStatus', '短信模板');
    },
  },
  {
    title: '短信类型',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'templateType',
  },

  {
    title: '缓存有效期',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'cacheTimeOut',
  },
  {
    title: '有效期单位',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'cacheTimeOutUnit',
  },
  {
    title: '发送次数限制',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sendTimesLimit',
  },
  {
    title: '限制时间间隔',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sendTimesLimitPeriod',
  },
  {
    title: '时间间隔单位',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sendTimesLimitPeriodUnit',
  },
  {
    title: '描述',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'comments',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'channelId',
    label: '短信渠道',
    colProps: { span: 6 },
    component: 'ApiSelect',
    componentProps: {
      api: querySmsChannelListAll,
      params: {},
      resultField: 'list',
      // use name as label
      labelField: 'channelName',
      // use id as value
      valueField: 'id',
      placeholder: '请选择渠道',
    },
  },
  {
    field: 'smsCode',
    label: '短信编码',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'smsName',
    label: '短信名称',
    component: 'Input',
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
    field: 'channelId',
    label: '短信渠道',
    component: 'ApiSelect',
    required: true,
    componentProps: {
      api: querySmsChannelListAll,
      params: {},
      resultField: 'list',
      // use name as label
      labelField: 'channelName',
      // use id as value
      valueField: 'id',
      placeholder: '请选择渠道',
    },
  },
  {
    field: 'smsCode',
    label: '短信编码',
    component: 'Input',
    required: true,
  },
  {
    field: 'smsName',
    label: '短信名称',
    required: true,
    component: 'Input',
    dynamicRules: ({ model }) => {
      return renderCheckExistRules(model, checkSmsTemplateExist, 'sms_name', '短信名称');
    },
  },
  {
    field: 'templateId',
    label: '模板ID',
    component: 'Input',
    required: true,
  },
  {
    field: 'signName',
    label: '短信签名',
    component: 'Input',
    required: true,
  },
  {
    label: '短信状态',
    field: 'templateStatus',
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
    field: 'templateType',
    label: '短信类型',
    component: 'ApiSelect',
    defaultValue: '1',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'SMS_TEMPLATE_TYPE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    required: true,
  },
  {
    label: '缓存有效期',
    field: 'cacheTimeOut',
    defaultValue: 0,
    component: 'InputNumber',
  },
  {
    label: '有效期单位',
    field: 'cacheTimeOutUnit',
    component: 'ApiSelect',
    defaultValue: 'MINUTES',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'TIME_UNIT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
  },
  {
    label: '发送次数限制',
    field: 'sendTimesLimit',
    defaultValue: 0,
    component: 'InputNumber',
  },
  {
    label: '限制时间间隔',
    field: 'sendTimesLimitPeriod',
    defaultValue: 0,
    component: 'InputNumber',
  },
  {
    label: '有效期单位',
    field: 'sendTimesLimitPeriodUnit',
    component: 'ApiSelect',
    defaultValue: 'MINUTES',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'TIME_UNIT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
  },
  {
    label: '备注',
    field: 'comments',
    component: 'InputTextArea',
  },
];
