import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { updateDfsStatus } from '/@/api/system/extension/dfs/dfs';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict, renderStatusSwitch } from '/@/utils/gitegg/formUtils';

export const dfsColumns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '存储分类',
    align: 'center',
    dataIndex: 'dfsType',
    width: 180,
    customRender: ({ text }) => {
      return renderDict(text, 'DFS_TYPE', false, false);
    },
  },
  {
    title: '存储编号',
    align: 'center',
    width: 180,
    dataIndex: 'dfsCode',
  },
  {
    title: '空间名称',
    align: 'center',
    width: 180,
    dataIndex: 'bucket',
  },
  {
    title: '默认存储',
    align: 'center',
    dataIndex: 'dfsDefault',
    width: 100,
    customRender: ({ text }) => {
      return renderDict(text, 'YES_NO', false, true, 'TAG_COLOR');
    },
  },
  {
    title: '访问控制',
    align: 'center',
    dataIndex: 'accessControl',
    width: 100,
    customRender: ({ text }) => {
      return renderDict(text, 'ACCESS_CONTROL', false, true, 'TAG_COLOR');
    },
  },
  {
    title: '状态',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'dfsStatus',
    customRender: ({ record }) => {
      return renderStatusSwitch(record, updateDfsStatus, 'dfsStatus', '状态');
    },
  },
  {
    title: '访问地址',
    align: 'center',
    width: 300,
    dataIndex: 'accessUrlPrefix',
  },
  {
    title: '上传地址',
    align: 'center',
    width: 300,
    dataIndex: 'uploadUrl',
  },
];

export const dfsSearchForm: FormSchema[] = [
  {
    label: '存储类型',
    field: 'dfsType',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DFS_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择存储类型',
    },
  },
  {
    field: 'dfsCode',
    label: '存储编号',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入存储编号',
    },
  },
  {
    label: '状态',
    field: 'dfsStatus',
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
      placeholder: '请选择状态',
    },
  },
];

export const dfsForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    label: '存储分类',
    field: 'dfsType',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DFS_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择存储类型',
    },
    required: true,
  },
  {
    label: '存储编号',
    field: 'dfsCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入存储编号',
    },
    required: true,
  },
  {
    label: '访问地址前缀',
    field: 'accessUrlPrefix',
    component: 'Input',
    componentProps: {
      placeholder: '请输入分布式文件访问地址前缀',
    },
    required: true,
  },
  {
    label: '文件上传地址',
    field: 'uploadUrl',
    component: 'Input',
    componentProps: {
      placeholder: '请输入文件上传地址',
    },
    required: true,
  },
  {
    label: '空间名称',
    field: 'bucket',
    component: 'Input',
    componentProps: {
      placeholder: '请输入空间名称',
    },
    required: true,
  },
  {
    label: '应用ID',
    field: 'appId',
    component: 'Input',
    componentProps: {
      placeholder: '请输入应用ID',
    },
    required: true,
  },
  {
    label: '区域',
    field: 'region',
    component: 'Input',
    componentProps: {
      placeholder: '请输入区域',
    },
    required: true,
  },
  {
    label: 'AccessKey',
    field: 'accessKey',
    component: 'Input',
    componentProps: {
      placeholder: '请输入AccessKey',
    },
    required: true,
  },
  {
    label: 'SecretKey',
    field: 'secretKey',
    component: 'Input',
    componentProps: {
      placeholder: '请输入SecretKey',
    },
    required: true,
  },
  {
    label: '是否默认存储',
    field: 'dfsDefault',
    defaultValue: '0',
    component: 'ApiRadioGroup',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'YES_NO' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '默认存储',
    },
  },
  {
    label: '状态',
    field: 'dfsStatus',
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
    label: '访问控制',
    field: 'accessControl',
    component: 'ApiRadioGroup',
    defaultValue: '0',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'ACCESS_CONTROL' },
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
    field: 'comments',
    component: 'InputTextArea',
  },
];

export const dfsUploadTestForm: FormSchema[] = [
  {
    label: '存储编号',
    field: 'dfsCode',
    show: false,
    component: 'Input',
  },
  {
    label: '存储分类',
    field: 'dfsType',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DFS_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择存储类型',
    },
    required: true,
  },
  {
    label: '测试文件',
    field: 'uploadFile',
    component: 'Input',
    slot: 'upload',
    componentProps: {
      placeholder: '请选择测试文件',
    },
  },
];
