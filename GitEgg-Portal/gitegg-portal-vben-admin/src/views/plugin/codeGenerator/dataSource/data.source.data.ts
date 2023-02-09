import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { checkDataSourceExist } from '/@/api/plugin/codeGenerator/dataSource/data_source';
import { getDictCache } from '/@/utils/gitegg/dictUtils';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '数据源名称',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: 'datasourceName',
  },
  {
    title: '连接地址',
    align: 'center',
    ellipsis: true,
    dataIndex: 'url',
  },
  {
    title: '用户名',
    align: 'center',
    width: 150,
    ellipsis: true,
    dataIndex: 'username',
  },
  {
    title: '密码',
    align: 'center',
    width: 150,
    ellipsis: true,
    dataIndex: 'password',
  },
  {
    title: '数据库类型',
    align: 'center',
    width: 150,
    ellipsis: true,
    dataIndex: 'dbType',
  },
  {
    title: '创建日期',
    align: 'center',
    dataIndex: 'createTime',
    width: 200,
  },
  {
    title: '备注',
    dataIndex: 'comments',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'datasourceName',
    label: '数据源名称',
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据源名称',
    },
    colProps: { span: 8 },
  },
  {
    field: 'dbType',
    label: '数据库类型',
    component: 'ApiSelect',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DB_TYPE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择数据库类型',
    },
    colProps: { span: 8 },
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
    field: 'datasourceName',
    label: '数据源名称',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据源名称',
    },
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          message: '请输入数据源名称',
        },
        {
          trigger: 'blur',
          message: '数据源名称已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: 'datasource_name',
                checkValue: value,
              };
              checkDataSourceExist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('数据源名称已存在');
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
  },
  {
    field: 'url',
    label: '连接地址',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder:
        'jdbc:mysql://127.0.0.1/gitegg?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=Asia/Shanghai',
    },
  },
  {
    field: 'username',
    label: '用户名',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据库用户名',
    },
  },
  {
    field: 'password',
    label: '密码',
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据库密码',
    },
  },
  {
    field: 'dbType',
    label: '数据库类型',
    component: 'ApiSelect',
    defaultValue: 'mysql',
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'DB_TYPE' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
      placeholder: '请选择数据库类型',
    },
    required: true,
  },
  {
    field: 'driver',
    label: '数据库驱动',
    component: 'Input',
    componentProps: {
      placeholder: '请输入数据库驱动',
    },
  },
  {
    label: '备注',
    field: 'comments',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入备注信息',
    },
  },
];
