import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import dayjs from 'dayjs';

// -----------------邮件记录----------------------
export const mailLogColumn: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '邮件渠道',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'channelName',
  },
  {
    title: '邮件主题',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'mailSubject',
  },
  {
    title: '发送人',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'mailFrom',
  },
  {
    title: '收件人',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'mailTo',
  },
  {
    title: '附件大小',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'attachmentSize',
  },
  {
    title: '发送时间',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sendTime',
  },
  {
    title: '发送结果码',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sendResultCode',
  },
];

export const mailLogSearch: FormSchema[] = [
  {
    field: 'mailSubject',
    label: '邮件主题',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入邮件主题',
    },
  },
  {
    field: 'mailFrom',
    label: '发送人',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入发送人',
    },
  },
  {
    label: '收件人',
    field: 'mailTo',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入发送人',
    },
  },
  {
    label: '邮件内容',
    field: 'mailContent',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入邮件内容',
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

export const mailLogForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '邮件渠道',
    field: 'channelName',
    component: 'Input',
    componentProps: {
      placeholder: '邮件渠道',
      disabled: true,
    },
  },
  {
    label: '邮件主题',
    field: 'mailSubject',
    component: 'Input',
    componentProps: {
      placeholder: '邮件主题',
      disabled: true,
    },
  },
  {
    label: '发送人',
    field: 'mailFrom',
    component: 'Input',
    componentProps: {
      placeholder: '发送人',
      disabled: true,
    },
  },
  {
    label: '收件人',
    field: 'mailTo',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '收件人邮箱',
      disabled: true,
    },
  },
  {
    label: '抄送',
    field: 'mailCc',
    component: 'Input',
    componentProps: {
      placeholder: '抄送',
      disabled: true,
    },
  },
  {
    label: '密抄送',
    field: 'mailBcc',
    component: 'Input',
    componentProps: {
      placeholder: '密抄送内容',
      disabled: true,
    },
  },
  {
    label: '邮件内容',
    field: 'mailContent',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '邮件内容',
      disabled: true,
    },
  },
  {
    label: '附件名称',
    field: 'attachmentName',
    component: 'Input',
    componentProps: {
      placeholder: '附件名称',
      disabled: true,
    },
  },
  {
    label: '附件大小',
    field: 'attachmentSize',
    component: 'Input',
    componentProps: {
      placeholder: '附件大小',
      disabled: true,
    },
  },
  {
    label: '发送时间',
    field: 'sendTime',
    component: 'Input',
    componentProps: {
      placeholder: '发送时间',
      disabled: true,
    },
  },
  {
    label: '发送结果码',
    field: 'sendResultCode',
    component: 'Input',
    componentProps: {
      placeholder: '发送结果码',
      disabled: true,
    },
  },
  {
    label: '发送结果消息',
    field: 'sendResultMsg',
    component: 'Input',
    componentProps: {
      placeholder: '发送结果消息',
      disabled: true,
    },
  },
];
