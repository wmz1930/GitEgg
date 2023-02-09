import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {
  updateMailTemplateStatus,
  checkMailTemplateExist,
} from '/@/api/system/extension/mail/mailTemplate';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderStatusSwitch, renderCheckExistRules } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

// -------------------邮件模板--------------------------------
export const mailTemplateColumn: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '模板编码',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'templateCode',
  },
  {
    title: '模板名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'templateName',
  },
  {
    title: '模板签名',
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
    dataIndex: '0',
    customRender: ({ record }) => {
      return renderStatusSwitch(
        record,
        updateMailTemplateStatus,
        'templateStatus',
        '邮件模板：' + record.templateName,
      );
    },
  },
  {
    title: '模板类型',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'comments',
  },
];

export const mailTemplateSearch: FormSchema[] = [
  {
    field: 'templateCode',
    label: '模板编码',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入渠道编码',
    },
  },
  {
    field: 'templateName',
    label: '模板名称',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入模板名称',
    },
  },

  {
    label: '模板状态',
    field: 'templateStatus',
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
    },
  },
  {
    label: '模板类型',
    field: 'templateType',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'MAIL_TEMPLATE_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
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

export const mailTemplateForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '模板编码',
    field: 'templateCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入模板编码',
    },
    dynamicRules: ({ model }) => {
      return renderCheckExistRules(model, checkMailTemplateExist, 'template_code', '模板编码');
    },
  },
  {
    label: '模板名称',
    field: 'templateName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入模板名称',
    },
    required: true,
  },
  {
    label: '模板签名',
    field: 'signName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入模板签名',
    },
    required: true,
  },
  {
    label: '模板状态',
    field: 'templateStatus',
    component: 'ApiRadioGroup',
    required: true,
    defaultValue: '0',
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
    label: '模板类型',
    field: 'templateType',
    component: 'ApiSelect',
    defaultValue: '1',
    required: true,
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'MAIL_TEMPLATE_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择模板类型',
    },
  },
  {
    label: '模板内容',
    field: 'templateContent',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入模板内容',
    },
    required: true,
  },
  {
    label: '缓存key',
    field: 'cacheCodeKey',
    component: 'Input',
    componentProps: {
      placeholder: '请输入缓存key',
    },
    show: ({ model }) => {
      return model['templateType'] === '2';
    },
  },
  {
    label: '缓存有效期',
    field: 'cacheTimeOut',
    component: 'InputNumber',
    defaultValue: 0,
    componentProps: {
      placeholder: '请输入协议',
    },
    show: ({ model }) => {
      return model['templateType'] === '2';
    },
  },
  {
    label: '缓存有效期单位',
    field: 'cacheTimeOutUnit',
    component: 'Input',
    defaultValue: 'min',
    componentProps: {
      placeholder: '请输入缓存有效期',
    },
    show: ({ model }) => {
      return model['templateType'] === '2';
    },
  },
  {
    label: '发送次数限制',
    field: 'sendTimesLimit',
    component: 'InputNumber',
    defaultValue: 5,
    componentProps: {
      placeholder: '请配置发送次数限制',
    },
    show: ({ model }) => {
      return model['templateType'] === '2';
    },
  },
  {
    label: '限制时间间隔',
    field: 'sendTimesLimitPeriod',
    component: 'InputNumber',
    defaultValue: 5,
    componentProps: {
      placeholder: '请配置限制时间间隔',
    },
    show: ({ model }) => {
      return model['templateType'] === '2';
    },
  },
  {
    label: '限制间隔单位',
    field: 'sendTimesLimitPeriodUnit',
    component: 'Input',
    defaultValue: 'min',
    componentProps: {
      placeholder: '请配置限制时间间隔单位',
    },
    show: ({ model }) => {
      return model['templateType'] === '2';
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
