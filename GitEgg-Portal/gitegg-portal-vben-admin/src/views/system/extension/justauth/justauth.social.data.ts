import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getDictBusinessCache } from '/@/utils/gitegg/dictUtils';
import { renderDict } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

//  ---------------------第三方用户-----------------------------------
export const JustauthSocialColumn: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '第三方ID',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'uuid',
  },
  {
    title: '第三方来源',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'source',
    customRender: ({ text }) => {
      return renderDict(text, 'JUST_SOURCE_NAME', true, false);
    },
  },
  {
    title: '用户名',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'username',
  },
  {
    title: '用户昵称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'nickname',
  },
  {
    title: '用户头像',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'avatar',
  },
  {
    title: '用户网址',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'blog',
  },
  {
    title: '所在公司',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'company',
  },
  {
    title: '位置',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'location',
  },
  {
    title: '用户邮箱',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'email',
  },
  {
    title: '用户备注',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'remark',
  },
  {
    title: '性别',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'gender',
  },
  {
    title: '创建时间',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'createTime',
  },
];

export const JustauthSocialSearch: FormSchema[] = [
  {
    label: '第三方ID',
    field: 'uuid',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入第三方ID',
    },
  },
  {
    label: '第三方来源',
    field: 'source',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'JUST_SOURCE_NAME' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择第三方来源',
    },
  },
  {
    label: '用户名',
    field: 'username',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入用户名',
    },
  },
  {
    label: '用户昵称',
    field: 'nickname',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入用户昵称',
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

export const JustauthSocialForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '第三方ID',
    field: 'uuid',
    component: 'Input',
    componentProps: {
      placeholder: '第三方ID',
      disabled: true,
    },
  },
  {
    label: '第三方来源',
    field: 'source',
    component: 'Input',
    componentProps: {
      placeholder: '第三方来源',
      disabled: true,
    },
  },
  {
    label: '用户名',
    field: 'username',
    component: 'Input',
    componentProps: {
      placeholder: '用户名',
      disabled: true,
    },
  },
  {
    label: '用户昵称',
    field: 'nickname',
    component: 'Input',
    componentProps: {
      placeholder: '用户昵称',
      disabled: true,
    },
  },
  {
    label: '用户头像地址',
    field: 'avatar',
    component: 'Input',
    componentProps: {
      placeholder: '用户头像地址',
      disabled: true,
    },
  },
  {
    label: '用户网址',
    field: 'blog',
    component: 'Input',
    componentProps: {
      placeholder: '用户网址',
      disabled: true,
    },
  },
  {
    label: '所在公司',
    field: 'company',
    component: 'Input',
    componentProps: {
      placeholder: '所在公司',
      disabled: true,
    },
  },
  {
    label: '位置',
    field: 'location',
    component: 'Input',
    componentProps: {
      placeholder: '位置',
      disabled: true,
    },
  },
  {
    label: '用户邮箱',
    field: 'email',
    component: 'Input',
    componentProps: {
      placeholder: '用户邮箱',
      disabled: true,
    },
  },
  {
    label: '用户备注',
    field: 'remark',
    component: 'Input',
    componentProps: {
      placeholder: '用户备注',
      disabled: true,
    },
  },
  {
    label: '性别',
    field: 'gender',
    component: 'ApiSelect',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'GENDER' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择登陆类型',
      disabled: true,
    },
  },
  {
    label: '授权令牌',
    field: 'accessToken',
    component: 'Input',
    componentProps: {
      placeholder: '授权令牌',
      disabled: true,
    },
  },
  {
    label: '令牌有效期',
    field: 'expireIn',
    component: 'Input',
    componentProps: {
      placeholder: '令牌有效期',
      disabled: true,
    },
  },
  {
    label: '刷新令牌',
    field: 'refreshToken',
    component: 'Input',
    componentProps: {
      placeholder: '刷新令牌',
      disabled: true,
    },
  },
  {
    label: '刷新令牌有效期',
    field: 'accessTokenExpireIn',
    component: 'Input',
    componentProps: {
      placeholder: '刷新令牌有效期',
      disabled: true,
    },
  },
  {
    label: '第三方用户ID',
    field: 'uid',
    component: 'Input',
    componentProps: {
      placeholder: '第三方用户ID',
      disabled: true,
    },
  },
  {
    label: '第三方用户OpenId',
    field: 'openId',
    component: 'Input',
    componentProps: {
      placeholder: '第三方用户OpenId',
      disabled: true,
    },
  },
  {
    label: 'AccessCode',
    field: 'accessCode',
    component: 'Input',
    componentProps: {
      placeholder: 'AccessCode',
      disabled: true,
    },
  },
  {
    label: '第三方用户UnionId',
    field: 'unionId',
    component: 'Input',
    componentProps: {
      placeholder: '第三方用户UnionId',
      disabled: true,
    },
  },
  {
    label: 'Google Scope',
    field: 'scope',
    component: 'Input',
    componentProps: {
      placeholder: 'Google Scope',
      disabled: true,
    },
  },
  {
    label: 'Google TokenType',
    field: 'tokenType',
    component: 'Input',
    componentProps: {
      placeholder: 'Google TokenType',
      disabled: true,
    },
  },
  {
    label: 'Google IdToken',
    field: 'idToken',
    component: 'Input',
    componentProps: {
      placeholder: 'Google IdToken',
      disabled: true,
    },
  },
  {
    label: '小米MacAlgorithm',
    field: 'macAlgorithm',
    component: 'Input',
    componentProps: {
      placeholder: '小米MacAlgorithm',
      disabled: true,
    },
  },

  {
    label: '小米Mac_Key',
    field: 'macKey',
    component: 'Input',
    componentProps: {
      placeholder: '小米Mac_Key',
      disabled: true,
    },
  },
  {
    label: '企业微信code',
    field: 'code',
    component: 'Input',
    componentProps: {
      placeholder: '企业微信code',
      disabled: true,
    },
  },
  {
    label: 'Twitter OauthToken',
    field: 'oauthToken',
    component: 'Input',
    componentProps: {
      placeholder: 'Twitter OauthToken',
      disabled: true,
    },
  },
  {
    label: 'Twitter OauthTokenSecret',
    field: 'oauthTokenSecret',
    component: 'Input',
    componentProps: {
      placeholder: 'Twitter OauthTokenSecret',
      disabled: true,
    },
  },
  {
    label: 'Twitter UserId',
    field: 'userId',
    component: 'Input',
    componentProps: {
      placeholder: 'Twitter UserId',
      disabled: true,
    },
  },
  {
    label: 'Twitter OauthCallbackConfirmed',
    field: 'oauthCallbackConfirmed',
    component: 'Input',
    componentProps: {
      placeholder: 'Twitter OauthCallbackConfirmed',
      disabled: true,
    },
  },
  {
    label: '原始用户信息',
    field: 'rawUserInfo',
    component: 'Input',
    componentProps: {
      placeholder: '原始用户信息',
      disabled: true,
    },
  },
];
