import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getDictBusinessCache } from '/@/utils/gitegg/dictUtils';
import { updateJustAuthSourceStatus } from '/@/api/system/extension/justauth/justauthSource';
import { renderDict, renderStatusSwitch } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

// ---------------------第三方列表--------------------------------
export const JustauthSourceColumn: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sourceName',
    customRender: ({ text }) => {
      return renderDict(text, 'JUST_SOURCE_NAME', true, false);
    },
  },
  {
    title: '登录类型',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'sourceType',
    customRender: ({ text }) => {
      return renderDict(text, 'JUST_SOURCE_TYPE', true, false);
    },
  },
  {
    title: '客户端id',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'clientId',
  },
  {
    title: '客户端Secret',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'clientSecret',
  },
  {
    title: '回调地址',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'redirectUri',
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
        updateJustAuthSourceStatus,
        'status',
        '第三方列表配置：' + record.sourceName,
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

export const JustauthSourceSearch: FormSchema[] = [
  {
    label: '名称',
    field: 'sourceName',
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
      placeholder: '请选择第三方登录名称',
    },
  },

  {
    label: '登陆类型',
    field: 'sourceType',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'JUST_SOURCE_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择登陆类型',
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

export const JustauthSourceForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '登陆类型',
    field: 'sourceType',
    component: 'ApiSelect',
    defaultValue: 'default',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'JUST_SOURCE_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择登陆类型',
    },
    required: true,
  },
  {
    label: '名称',
    field: 'sourceName',
    component: 'ApiSelect',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'JUST_SOURCE_NAME' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择第三方登陆名称',
    },
    required: true,
  },
  {
    label: '客户端ID',
    field: 'clientId',
    component: 'Input',
    componentProps: {
      placeholder: '请输入客户端ID',
    },
    required: true,
  },
  {
    label: '客户端Secret',
    field: 'clientSecret',
    component: 'Input',
    componentProps: {
      placeholder: '请输入客户端Secret',
    },
    required: true,
  },
  {
    label: '回调地址',
    field: 'redirectUri',
    component: 'Input',
    componentProps: {
      placeholder: '请输入回调地址',
    },
    required: true,
  },
  {
    label: '自定义Class',
    field: 'requestClass',
    component: 'Input',
    componentProps: {
      placeholder: '请输入自定义Class',
    },
    ifShow: ({ values }) => {
      if (values.sourceType === 'custom') {
        return true;
      }
      return false;
    },
  },
  {
    label: '支付宝公钥',
    field: 'alipayPublicKey',
    component: 'Input',
    componentProps: {
      placeholder: '请输入支付宝公钥',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'ALIPAY') {
        return true;
      }
      return false;
    },
  },
  {
    label: 'unionid',
    field: 'unionId',
    component: 'Switch',
    required: true,
    componentProps: {
      checkedChildren: '开',
      unCheckedChildren: '关',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'QQ') {
        return true;
      }
      return false;
    },
  },
  {
    label: 'Stack Overflow Key',
    field: 'stackOverflowKey',
    component: 'Input',
    componentProps: {
      placeholder: '请输入Stack Overflow Key',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'STACK_OVERFLOW') {
        return true;
      }
      return false;
    },
  },
  {
    label: '应用ID',
    field: 'agentId',
    component: 'Input',
    componentProps: {
      placeholder: '请输入企业微信网页应用ID',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'WECHAT_ENTERPRISE_WEB') {
        return true;
      }
      return false;
    },
  },
  {
    label: '企业微信用户类型',
    field: 'userType',
    component: 'ApiSelect',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'JUST_USER_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择企业微信用户类型',
    },
    ifShow: ({ values }) => {
      if (
        values.sourceName === 'WECHAT_ENTERPRISE' ||
        values.sourceName === 'WECHAT_ENTERPRISE_QRCODE_THIRD' ||
        values.sourceName === 'WECHAT_ENTERPRISE_WEB'
      ) {
        return true;
      }
      return false;
    },
  },
  {
    label: 'DomainPrefix',
    field: 'domainPrefix',
    component: 'Input',
    componentProps: {
      placeholder: '请输入DomainPrefix',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'OKTA' || values.sourceName === 'CODING') {
        return true;
      }
      return false;
    },
  },

  {
    label: 'Okta ID',
    field: 'authServerId',
    component: 'Input',
    componentProps: {
      placeholder: '请输入Okta授权服务器的 ID',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'OKTA') {
        return true;
      }
      return false;
    },
  },

  {
    label: '自定义授权scope',
    field: 'scopes',
    component: 'Input',
    componentProps: {
      placeholder: '请输入自定义授权scope',
    },
    ifShow: ({ values }) => {
      if (values.sourceType === 'custom') {
        return true;
      }
      return false;
    },
  },

  {
    label: '设备ID',
    field: 'deviceId',
    component: 'Input',
    componentProps: {
      placeholder: '请输入设备ID',
    },
  },

  {
    label: '客户端包名',
    field: 'packId',
    component: 'Input',
    componentProps: {
      placeholder: '请输入客户端包名',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'XMLY') {
        return true;
      }
      return false;
    },
  },
  {
    label: '客户端操作系统类型',
    field: 'clientOsType',
    component: 'Input',
    componentProps: {
      placeholder: '请输入客户端操作系统类型',
    },
    ifShow: ({ values }) => {
      if (values.sourceName === 'XMLY') {
        return true;
      }
      return false;
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'ApiRadioGroup',
    defaultValue: '1',
    componentProps: {
      api: getDictBusinessCache,
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
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入备注',
    },
  },
  {
    label: '开启PKCE模式',
    field: 'pkce',
    component: 'Switch',
    componentProps: {
      checkedChildren: '开',
      unCheckedChildren: '关',
    },
  },
  {
    label: '忽略校验codestate',
    field: 'ignoreCheckState',
    component: 'Switch',
    componentProps: {
      checkedChildren: '开',
      unCheckedChildren: '关',
    },
  },
  {
    label: '忽略校验RedirectUri',
    field: 'ignoreCheckRedirectUri',
    component: 'Switch',
    componentProps: {
      checkedChildren: '开',
      unCheckedChildren: '关',
    },
  },
  {
    label: 'Http代理类型',
    field: 'proxyType',
    component: 'ApiSelect',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'HTTP_PROXY_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择Http代理类型',
    },
  },
  {
    label: 'Http代理Host',
    field: 'proxyHostName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入Http代理Host',
    },
  },
  {
    label: 'Http代理Port',
    field: 'proxyPort',
    component: 'Input',
    componentProps: {
      placeholder: '请输入Http代理Port',
    },
  },
];
