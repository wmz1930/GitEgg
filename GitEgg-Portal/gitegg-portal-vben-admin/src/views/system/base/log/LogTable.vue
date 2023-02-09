<template>
  <div>
    <BasicTable @register="registerTable" :searchInfo="searchInfo">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:cloud-download" @click="jsonToExcel"
          >导出
        </a-button>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';

  import { BasicTable, useTable } from '/@/components/Table';
  import { fetchList } from '/@/api/system/base/log';

  import { columns, searchFormSchema } from './log.data';

  import { jsonToSheetXlsx } from '/@/components/Excel';

  export default defineComponent({
    name: 'SmsTemplateManagement',
    components: {
      BasicTable,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());

      const searchInfo = reactive<Recordable>({});
      const [registerTable] = useTable({
        title: '操作日志列表',
        api: fetchList,
        rowKey: 'id',
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        handleSearchInfoFn(info) {
          return info;
        },
      });
      interface query {
        current: number;
        size: number;
      }
      async function jsonToExcel() {
        const query: query = {
          current: 1,
          size: 10,
        };
        const { records: data } = await fetchList(query);
        jsonToSheetXlsx({
          data,
          header: {
            id: '序号',
            methodName: '接口名称',
            inParams: '入参',
            outParams: '出参',
            logType: '日志类型',
            operationName: '操作名称',
            operationIp: '操作IP',
            createTime: '记录时间',
            creator: '操作人',
          },
          filename: '操作日志列表.xlsx',
        });
      }

      return {
        registerTable,
        checkedKeys,
        searchInfo,
        jsonToExcel,
      };
    },
  });
</script>
