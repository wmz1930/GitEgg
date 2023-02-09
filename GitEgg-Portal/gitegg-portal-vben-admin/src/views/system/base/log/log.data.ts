import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getDictCache } from '/@/utils/gitegg/dictUtils';
import { renderDict } from '/@/utils/gitegg/formUtils';
import dayjs from 'dayjs';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    dataIndex: 'id',
  },
  {
    title: '接口名称',
    align: 'center',
    dataIndex: 'methodName',
  },
  {
    title: '入参',
    align: 'center',
    ellipsis: true,
    dataIndex: 'inParams',
  },
  {
    title: '出参',
    align: 'center',
    ellipsis: true,
    dataIndex: 'outParams',
  },
  {
    title: '日志类型',
    align: 'center',
    dataIndex: 'logType',
    customRender: ({ text }) => {
      return renderDict(text, 'OPERATION_LOG_TYPE', false, false);
    },
  },
  {
    title: '操作名称',
    align: 'center',
    dataIndex: 'operationName',
  },
  {
    title: '操作的IP',
    align: 'center',
    dataIndex: 'operationIp',
  },
  {
    title: '记录时间',
    align: 'center',
    dataIndex: 'createTime',
  },
  {
    title: '操作人',
    align: 'center',
    dataIndex: 'creator',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'methodName',
    label: '接口名称',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入渠道编码',
    },
  },
  {
    field: 'creator',
    label: '操作人',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入渠道名称',
    },
  },
  {
    label: '日志类型',
    field: 'logType',
    component: 'ApiSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getDictCache,
      params: { dictCode: 'OPERATION_LOG_TYPE' },
      resultField: 'dictList',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择日志类型',
    },
  },
  {
    label: '开始时间',
    field: 'beginDateTime',
    colProps: { span: 6 },
    component: 'DatePicker',
    componentProps: {
      format: 'YYYY-MM-DD HH:mm:ss',
      style: 'width:100%;',
      placeholder: '请选择有效日期',
      showNow: false,
      showTime: {
        defaultValue: dayjs().startOf('day'),
      },
    },
  },
  {
    label: '结束时间',
    field: 'endDateTime',
    colProps: { span: 6 },
    component: 'DatePicker',
    componentProps: {
      format: 'YYYY-MM-DD HH:mm:ss',
      style: 'width:100%;',
      placeholder: '请选择有效日期',
      showNow: false,
      showTime: {
        defaultValue: dayjs().endOf('day'),
      },
    },
  },
];
