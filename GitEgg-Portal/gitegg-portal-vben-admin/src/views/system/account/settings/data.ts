import { FormSchema } from '/@/components/Form/index';
import { level } from 'province-city-china/data';

export interface ListItem {
  key: string;
  title: string;
  description: string;
  extra?: string;
  avatar?: string;
  color?: string;
}

// tab的list
export const settingList = [
  {
    key: '1',
    name: '基本设置',
    component: 'BaseSetting',
  },
  {
    key: '2',
    name: '安全设置',
    component: 'SecureSetting',
  },
  {
    key: '3',
    name: '账号绑定',
    component: 'AccountBind',
  },
  {
    key: '4',
    name: '新消息通知',
    component: 'MsgNotify',
  },
];

// 基础设置 form
export const baseSetschemas: FormSchema[] = [
  {
    field: 'id',
    component: 'Input',
    label: 'id',
    show: false,
  },
  {
    field: 'nickname',
    component: 'Input',
    label: '昵称',
    colProps: { span: 18 },
  },
  {
    field: 'mobile',
    component: 'Input',
    label: '联系电话',
    colProps: { span: 18 },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'email',
    component: 'Input',
    label: '邮箱',
    colProps: { span: 18 },
    componentProps: {
      disabled: true,
    },
  },
  {
    label: '所在地区',
    field: 'areas',
    component: 'Cascader',
    colProps: { span: 18 },
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
    field: 'comments',
    component: 'InputTextArea',
    label: '个人简介',
    colProps: { span: 18 },
  },
];

// 账号绑定 list
export const accountBindList: ListItem[] = [
  {
    key: '1',
    title: '绑定淘宝',
    description: '当前未绑定淘宝账号',
    extra: '绑定',
    avatar: 'ri:taobao-fill',
    color: '#ff4000',
  },
  {
    key: '2',
    title: '绑定支付宝',
    description: '当前未绑定支付宝账号',
    extra: '绑定',
    avatar: 'fa-brands:alipay',
    color: '#2eabff',
  },
  {
    key: '3',
    title: '绑定钉钉',
    description: '当前未绑定钉钉账号',
    extra: '绑定',
    avatar: 'ri:dingding-fill',
    color: '#2eabff',
  },
];

// 新消息通知 list
export const msgNotifyList: ListItem[] = [
  {
    key: '1',
    title: '账户密码',
    description: '其他用户的消息将以站内信的形式通知',
  },
  {
    key: '2',
    title: '系统消息',
    description: '系统消息将以站内信的形式通知',
  },
  {
    key: '3',
    title: '待办任务',
    description: '待办任务将以站内信的形式通知',
  },
];

export const passwordFormSchema: FormSchema[] = [
  {
    field: 'oldPwd',
    label: '旧密码',
    component: 'InputPassword',
    componentProps: {
      placeholder: '请输入旧密码',
    },
    rules: [
      {
        required: true,
        message: '请输入旧密码',
      },
      { min: 6, max: 32, message: '旧密码长度在 6 到 32 个字符', trigger: 'blur' },
    ],
  },
  {
    label: '新密码',
    field: 'newPwd',
    component: 'InputPassword',
    componentProps: {
      placeholder: '请输入新密码',
    },
    rules: [
      {
        required: true,
        message: '请输入旧密码',
      },
      { min: 6, max: 32, message: '新密码长度在 6 到 32 个字符', trigger: 'blur' },
    ],
  },
  {
    label: '新密码',
    field: 'newPwdRe',
    component: 'InputPassword',
    componentProps: {
      placeholder: '请再次输入新密码',
    },
    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (!value) {
              return Promise.reject('密码不能为空');
            }
            if (value !== values.newPwd) {
              return Promise.reject('两次输入的密码不一致!');
            }
            return Promise.resolve();
          },
        },
      ];
    },
  },
];
