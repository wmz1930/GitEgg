<template>
  <a-tabs v-model:activeKey="activeKey" v-bind="$attrs">
    <a-tab-pane
      v-for="fieldData in fieldDataList"
      :key="fieldData.joinId"
      :tab="'表【' + fieldData.joinTableName + '】 字段配置'"
    >
      <BasicTable
        v-bind="$attrs"
        :rowKey="(record) => record.id + record.fieldName"
        :columns="columns"
        :dataSource="fieldData.fieldDTOList"
        :pagination="false"
        :resizeHeightOffset="70"
      >
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.key === 'queryTerm'">
            <a-switch
              checked-children="是"
              un-checked-children="否"
              v-model:checked="record.queryTerm"
              @change="onChange(record)"
            />
          </template>
          <template v-else-if="column.key === 'queryType'">
            <a-select
              placeholder="请选择查询类型"
              style="width: 100%"
              :default-value="text"
              :disabled="!record.queryTerm"
              show-search
              :filter-option="filterOption"
              v-model:value="record.queryType"
            >
              <a-select-option
                v-for="item in queryTypeDictList"
                :key="item.id"
                :title="item.dictName"
                :value="item.dictCode"
              >
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.key === 'listShow'">
            <a-checkbox v-model:checked="record.listShow">展示</a-checkbox>
          </template>
          <template v-else-if="column.key === 'importFlag'">
            <a-checkbox v-model:checked="record.importFlag">导入</a-checkbox>
          </template>
          <template v-else-if="column.key === 'exportFlag'">
            <a-checkbox v-model:checked="record.exportFlag">导出</a-checkbox>
          </template>
        </template>
      </BasicTable>
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, onMounted } from 'vue';
  import { Tabs, Input, Select, Checkbox, Switch } from 'ant-design-vue';
  import { BasicTable } from '/@/components/Table';
  import { columns, ImportColumns, ExportColumns } from './table-list/table.list.data';
  import { listGeneratorDict } from '/@/api/plugin/codeGenerator/dict/dict';
  export default defineComponent({
    components: {
      [Tabs.name]: Tabs,
      [Tabs.TabPane.name]: Tabs.TabPane,
      [Input.name]: Input,
      [Input.Group.name]: Input.Group,
      [Select.name]: Select,
      ASelectOption: Select.Option,
      [Checkbox.name]: Checkbox,
      [Switch.name]: Switch,
      BasicTable,
    },
    props: {
      config: {
        type: Object,
        default: () => {},
      },
      fields: {
        type: Array,
        default: () => [],
      },
    },
    setup(props) {
      const fieldDataList = reactive<any[]>(props.fields);
      const queryTypeDictList = ref<any[]>([]);
      const activeKey = ref('0');

      // 挂载后查询配置
      onMounted(() => {
        getValidateList();
      });

      async function getValidateList() {
        queryTypeDictList.value = await listGeneratorDict('QUERY_TYPE');
      }

      function onChange(item) {
        if (
          item.queryTerm &&
          (item.queryType === null || item.queryType === undefined || item.queryType === '')
        ) {
          item.queryType = 'EQUAL';
        }
        if (!item.queryTerm) {
          item.queryType = null;
        }
      }

      // 根据配置确定是否展示导出导出选项
      function processImportAndExportColumns() {
        let columnsField = columns;
        if (props.config.importFlag) {
          columnsField = columnsField.concat(ImportColumns);
        }
        if (props.config.exportFlag) {
          columnsField = columnsField.concat(ExportColumns);
        }
        return columnsField;
      }

      const filterOption = (input: string, option: any) => {
        return option.title.toLowerCase().indexOf(input.toLowerCase()) >= 0;
      };

      return {
        columns: processImportAndExportColumns(),
        activeKey,
        fieldDataList,
        getValidateList,
        queryTypeDictList,
        filterOption,
        onChange,
      };
    },
  });
</script>
