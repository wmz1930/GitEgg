import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { listGeneratorDict } from '/@/api/plugin/codeGenerator/dict/dict';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    dataIndex: 'tableSort',
  },
  {
    title: '表名',
    align: 'center',
    width: '250px',
    ellipsis: true,
    dataIndex: 'joinTableName',
  },
  {
    title: '别名',
    align: 'center',
    ellipsis: true,
    width: '180px',
    dataIndex: 'joinTableAlias',
  },
  {
    title: '表前缀',
    align: 'center',
    dataIndex: 'joinTablePrefix',
  },
  {
    title: '联表类型',
    align: 'center',
    dataIndex: 'joinTableType',
  },
  {
    title: '自定义查询字段',
    align: 'center',
    ellipsis: true,
    width: '250px',
    dataIndex: 'joinTableSelect',
  },
  {
    title: '自定义on条件',
    align: 'center',
    ellipsis: true,
    width: '250px',
    dataIndex: 'joinTableOn',
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
    field: 'joinTableName',
    label: '表名',
    component: 'Select',
    required: true,
  },
  {
    field: 'joinTableAlias',
    label: '别名',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入表别名',
    },
  },
  {
    field: 'moduleName',
    label: '模块名称',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '主表子表，子表的模块名称',
    },
  },
  {
    field: 'moduleCode',
    label: '模块代码',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '主表子表，子表的模块代码',
    },
  },
  {
    field: 'controllerPath',
    label: 'Controller路径',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '主表子表，子表的Controller请求路径',
    },
  },
  {
    field: 'joinTablePrefix',
    label: '表前缀',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入表前缀',
    },
  },
  {
    field: 'tableSort',
    label: '排序',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '组成SQL语句时的顺序',
    },
  },
  {
    field: 'joinTableType',
    label: '联表方式',
    component: 'ApiSelect',
    componentProps: {
      api: listGeneratorDict,
      params: 'UNION_TYPE',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择联表方式',
    },
    required: true,
  },
  {
    field: 'joinTableSelect',
    label: '查询字段',
    component: 'Select',
    required: true,
    componentProps: {
      mode: 'multiple',
      placeholder: '请先选择表',
    },
  },
  {
    field: 'associationId',
    label: '关联主键',
    component: 'Select',
    required: true,
    componentProps: {
      placeholder: '请先选择表',
    },
  },
  {
    label: '自定义on条件',
    field: 'joinTableOn',
    component: 'InputTextArea',
  },
  {
    field: 'datasourceId',
    label: '数据源主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'generationId',
    label: '代码生成主键',
    show: false,
    component: 'Input',
  },
];
