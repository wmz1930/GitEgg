import { BasicColumn } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '字段描述',
    align: 'center',
    width: '130px',
    dataIndex: 'comment',
  },
  {
    title: '实体类型',
    align: 'center',
    width: '130px',
    dataIndex: 'entityType',
  },
  {
    title: '实体名称',
    align: 'center',
    width: '130px',
    dataIndex: 'entityName',
  },
  {
    title: '查询条件',
    align: 'center',
    width: '130px',
    dataIndex: 'queryTerm',
  },
  {
    title: '查询类型',
    align: 'center',
    width: '130px',
    dataIndex: 'queryType',
  },
  {
    title: '列表展示',
    align: 'center',
    width: '130px',
    dataIndex: 'listShow',
  },
];

export const ImportColumns: BasicColumn[] = [
  {
    title: '支持导入',
    align: 'center',
    width: '130px',
    dataIndex: 'importFlag',
  },
];

export const ExportColumns: BasicColumn[] = [
  {
    title: '支持导出',
    align: 'center',
    width: '130px',
    dataIndex: 'exportFlag',
  },
];
