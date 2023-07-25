import { BasicColumn, FormSchema } from '/@/components/Table';

import { renderDict, renderStatusSwitch } from '/@/utils/gitegg/formUtils';
import { updateMiniappStatus, checkMiniappExist } from '/@/api/system/extension/wx/miniapp';
import { getDictBusinessCache } from '/@/utils/gitegg/dictUtils';
// import dayjs from 'dayjs';

// 数据表格Column定义
export const columns: BasicColumn[] = [
  {
    title: '小程序名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'miniappName',
  },
  {
    title: 'appid',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'appid',
  },
  {
    title: 'secret',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'secret',
  },
  {
    title: '消息格式',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'msgDataFormat',
    customRender: ({ text }) => {
      return renderDict(text, 'MINIAPP_MSG_FORMAT', false, false);
    },
  },
  {
    title: '状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'status',
    customRender: ({ record }) => {
      return renderStatusSwitch(record, updateMiniappStatus, 'status', '状态');
    },
  },
  {
    title: '创建时间',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'createTime',
  },
];

// 查询条件表单定义
export const searchFormSchema: FormSchema[] = [
  {
    field: 'miniappName',
    label: '小程序名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'appid',
    label: 'appid',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'secret',
    label: 'secret',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'ApiSelect',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    colProps: { span: 6 },
  },
];

// 新增/修改表单定义
export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'miniappName',
    label: '小程序名称',
    component: 'Input',
    componentProps: {
      placeholder: '请输入微信小程序名称',
    },
    dynamicRules: ({ model }) => {
      return [
        { required: true, message: '小程序名称不能为空！' },
        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' },
        {
          trigger: 'blur',
          message: '小程序名称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'miniapp_name',
                checkValue: value,
              };
              checkMiniappExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('小程序名称已存在');
                  } else {
                    resolve();
                  }
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ];
    },
    colProps: { span: 24 },
  },
  {
    field: 'appid',
    label: 'appid',
    component: 'Input',
    componentProps: {
      placeholder: '请输入微信小程序appid',
    },
    dynamicRules: ({ model }) => {
      return [
        { required: true, message: 'appid不能为空！' },
        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' },
        {
          trigger: 'blur',
          message: 'appid已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'appid',
                checkValue: value,
              };
              checkMiniappExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('appid已存在');
                  } else {
                    resolve();
                  }
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ];
    },
    colProps: { span: 24 },
  },
  {
    field: 'secret',
    label: 'secret',
    componentProps: {
      placeholder: '请输入微信小程序secret',
    },
    component: 'Input',
    rules: [
      { required: true, message: 'secret不能为空！' },
      { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' },
    ],
    colProps: { span: 24 },
  },
  {
    field: 'token',
    label: 'token',
    component: 'Input',
    componentProps: {
      placeholder: '请输入微信小程序token【非必填】',
    },
    rules: [{ min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'aesKey',
    label: 'aesKey',
    component: 'Input',
    componentProps: {
      placeholder: '请输入微信小程序aesKey【非必填】',
    },
    rules: [{ min: 0, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'msgDataFormat',
    label: '消息格式',
    defaultValue: 'JSON',
    component: 'ApiSelect',
    componentProps: {
      placeholder: '请输选择微信小程序消息格式',
      api: getDictBusinessCache,
      params: { dictCode: 'MINIAPP_MSG_FORMAT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      allowClear: false,
    },
    rules: [{ min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'storageType',
    label: '缓存类型',
    defaultValue: 'RedisTemplate',
    component: 'ApiSelect',
    show: false,
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'MINIAPP_CACHE_TYPE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    rules: [{ min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'keyPrefix',
    label: '缓存前缀',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'redisHost',
    label: 'Redis地址',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'redisPort',
    label: 'Redis端口',
    component: 'Input',
    show: false,
    rules: [
      { min: 0, max: 2147483647, message: '数值大小在 0 到 2147483647 之间', trigger: 'blur' },
      { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' },
    ],
    colProps: { span: 24 },
  },
  {
    field: 'httpClientType',
    label: 'http类型',
    defaultValue: 'OkHttp',
    component: 'ApiSelect',
    show: false,
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'MINIAPP_HTTP_TYPE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    rules: [{ min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'httpProxyHost',
    label: '代理地址',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'httpProxyPort',
    label: '代理端口',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'httpProxyUsername',
    label: '代理账号',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'httpProxyPassword',
    label: '代理密码',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'status',
    label: '状态',
    defaultValue: '1',
    component: 'ApiRadioGroup',
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: 'ENABLE_OR_NOT' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
    },
    rules: [
      { required: true, message: '状态不能为空！' },
      { min: 0, max: 2147483647, message: '数值大小在 0 到 2147483647 之间', trigger: 'blur' },
      { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' },
    ],
    colProps: { span: 24 },
  },
  {
    field: 'md5',
    label: 'MD5',
    component: 'Input',
    show: false,
    rules: [{ min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
  {
    field: 'comments',
    label: '描述',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入微信小程序描述信息',
    },
    rules: [{ min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur' }],
    colProps: { span: 24 },
  },
];
