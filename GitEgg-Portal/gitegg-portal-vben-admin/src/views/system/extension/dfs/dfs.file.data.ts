import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict } from '/@/utils/gitegg/formUtils';

export const dfsFileColumns: BasicColumn[] = [
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
    title: '文件地址',
    align: 'center',
    width: 300,
    dataIndex: 'accessUrl',
  },
  {
    title: '原文件名',
    align: 'center',
    width: 300,
    dataIndex: 'originalName',
  },
  {
    title: '存储文件名',
    align: 'center',
    width: 300,
    dataIndex: 'fileName',
  },
  {
    title: '文件类型',
    align: 'center',
    width: 100,
    dataIndex: 'fileExtension',
  },
  {
    title: '文件大小',
    align: 'center',
    width: 100,
    dataIndex: 'fileSize',
  },
  {
    title: '文件状态',
    align: 'center',
    width: 100,
    dataIndex: 'fileStatus',
    customRender: ({ text }) => {
      return renderDict(text, 'SUCCESS_AND_FAILURE', false, true, 'TAG_COLOR');
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
    title: '上传时间',
    align: 'center',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '备注',
    align: 'center',
    width: 100,
    dataIndex: 'comments',
  },
];

export const dfsFileSearchForm: FormSchema[] = [
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
    field: 'originalName',
    label: '原文件名',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入原文件名',
    },
  },
  {
    field: 'fileName',
    label: '存储文件名',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入存储文件名',
    },
  },
  {
    label: '状态',
    field: 'fileStatus',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'SUCCESS_AND_FAILURE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择状态',
    },
  },
];

export const dfsFileForm: FormSchema[] = [
  {
    field: 'id',
    label: '主键',
    component: 'InputNumber',
    show: false,
    componentProps: {
      placeholder: '请输入主键',
    },
  },

  {
    label: '分布式存储配置id',
    field: 'dfsId',
    component: 'InputNumber',
    componentProps: {
      placeholder: '输入分布式存储配置id',
    },
    required: true,
  },

  {
    label: '文件访问地址',
    field: 'accessUrl',
    component: 'Input',
    componentProps: {
      placeholder: '请输入文件访问地址',
    },
    required: true,
  },
  {
    label: '输入原文件名',
    field: 'originalName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入原文件名',
    },
    required: true,
  },
  {
    label: '存储文件名',
    field: 'fileName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入存储文件名',
    },
    required: true,
  },
  {
    label: '输入文件类型',
    field: 'fileExtension',
    component: 'Input',
    componentProps: {
      placeholder: '请输入文件类型',
    },
    required: true,
  },
  {
    label: '文件大小',
    field: 'fileSize',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入文件大小',
    },
    required: true,
  },
  {
    label: '状态',
    field: 'dfsType',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'SUCCESS_AND_FAILURE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择状态',
    },
  },

  {
    label: '文件大小',
    field: 'comments',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入备注',
    },
    required: true,
  },
];
