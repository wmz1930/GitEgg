import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {
  updateMailChannelStatus,
  checkMailChannelExist,
} from '/@/api/system/extension/mail/mailChannel';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderStatusSwitch, renderCheckExistRules } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

export const mailColumn: BasicColumn[] = [
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
    dataIndex: 'channelName',
  },
  {
    title: 'SMTP地址',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'host',
  },
  {
    title: 'SMTP端口',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'port',
  },
  {
    title: '渠道状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'channelStatus',
    customRender: ({ record }) => {
      return renderStatusSwitch(
        record,
        updateMailChannelStatus,
        'channelStatus',
        '渠道' + record.channelName,
      );
    },
  },
];

export const mailChannelSearch: FormSchema[] = [
  {
    field: 'channelCode',
    label: '渠道编码',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入渠道编码',
    },
  },
  {
    field: 'channelName',
    label: '渠道名称',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入渠道名称',
    },
  },
  {
    field: 'username',
    label: '账户名',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入账户名',
    },
  },
  {
    label: '渠道状态',
    field: 'dictStatus',
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

export const mailChannelForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '渠道编码',
    field: 'channelCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入渠道编码',
    },
    required: true,
    dynamicRules: ({ model }) => {
      return renderCheckExistRules(model, checkMailChannelExist, 'channel_code', '渠道编码');
    },
  },
  {
    label: '渠道名称',
    field: 'channelName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入渠道名称',
    },
    required: true,
    dynamicRules: ({ model }) => {
      return renderCheckExistRules(model, checkMailChannelExist, 'channel_name', '渠道名称');
    },
  },
  {
    label: 'SMTP地址',
    field: 'host',
    component: 'Input',
    componentProps: {
      placeholder: '请输入SMTP地址',
    },
    required: true,
  },
  {
    label: 'SMTP端口',
    field: 'port',
    component: 'Input',
    componentProps: {
      placeholder: '请输入SMTP端口',
    },
    required: true,
  },
  {
    label: '账户名',
    field: 'username',
    component: 'Input',
    componentProps: {
      placeholder: '请输入账户名',
    },
    required: true,
  },
  {
    label: '密码',
    field: 'password',
    component: 'Input',
    componentProps: {
      placeholder: '请输入密码',
    },
    required: true,
  },
  {
    label: '协议',
    field: 'protocol',
    component: 'Input',
    componentProps: {
      placeholder: '请输入协议',
    },
  },
  {
    label: '默认编码',
    field: 'defaultEncoding',
    component: 'Input',
    componentProps: {
      placeholder: '请输入默认编码',
    },
  },
  {
    label: '会话JNDI',
    field: 'jndiName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入会话JNDI',
    },
  },
  {
    label: 'JavaMail',
    field: 'properties',
    component: 'InputTextArea',
    helpMessage: '遵循Properties格式配置，从mail.开始配置。',
    componentProps: {
      placeholder: '请输入JavaMail配置：mail.smtp.auth: true',
    },
  },
  {
    label: '渠道状态',
    field: 'channelStatus',
    component: 'ApiRadioGroup',
    defaultValue: '1',
    required: true,
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
  },
];

export const modalForm: FormSchema[] = [
  {
    label: 'channelCode',
    field: 'channelCode',
    component: 'Input',
    show: false,
  },
  {
    label: 'name',
    field: 'attachmentName',
    component: 'Input',
    defaultValue: '0',
    show: false,
  },
  {
    label: 'size',
    field: 'attachmentSize',
    component: 'Input',
    defaultValue: '0',
    show: false,
  },
  {
    label: '收件人',
    field: 'mailTo',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入收件人邮箱地址',
    },
  },
  {
    label: '主题',
    field: 'mailSubject',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入邮件主题',
    },
  },
  {
    label: '内容',
    field: 'mailContent',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入邮件',
    },
    required: true,
  },
];
