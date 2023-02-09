import { BasicColumn } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '字段名称',
    align: 'center',
    width: '100px',
    ellipsis: true,
    dataIndex: 'fieldName',
  },
  {
    title: '字段类型',
    align: 'center',
    width: '100px',
    ellipsis: true,
    dataIndex: 'fieldType',
  },
  {
    title: '字段描述',
    align: 'center',
    width: '100px',
    dataIndex: 'comment',
  },
  {
    title: '字段类型',
    align: 'center',
    width: '130px',
    ellipsis: true,
    dataIndex: 'entityType',
  },
  {
    title: '字段名称',
    align: 'center',
    width: '130px',
    ellipsis: true,
    dataIndex: 'entityName',
  },
];
