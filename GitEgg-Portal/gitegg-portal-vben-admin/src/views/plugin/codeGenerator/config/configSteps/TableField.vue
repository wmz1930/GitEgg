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
          <template v-if="column.key === 'comment'">
            <a-input v-model:value="record.comment" />
          </template>
          <template v-else-if="column.key === 'entityType'">
            <a-select
              placeholder="请选择展示类型"
              show-search
              style="width: 100%"
              :default-value="text"
              v-model:value="record.entityType"
            >
              <a-select-option
                v-for="item in entityTypeDictList"
                :key="item.id"
                :value="item.dictName"
              >
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.key === 'entityName'">
            <a-input v-model:value="record.entityName" />
          </template>
        </template>
      </BasicTable>
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, onMounted } from 'vue';
  import { Tabs, Input, Select } from 'ant-design-vue';
  import { BasicTable } from '/@/components/Table';
  import { columns } from './table-field/table.field.data';
  import { listGeneratorDict } from '/@/api/plugin/codeGenerator/dict/dict';
  export default defineComponent({
    components: {
      [Tabs.name]: Tabs,
      [Tabs.TabPane.name]: Tabs.TabPane,
      [Input.name]: Input,
      [Input.Group.name]: Input.Group,
      [Select.name]: Select,
      ASelectOption: Select.Option,
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
      const entityTypeDictList = ref<any[]>([]);
      const activeKey = ref('0');

      // 挂载后查询配置
      onMounted(() => {
        getEntityType();
      });

      async function getEntityType() {
        entityTypeDictList.value = await listGeneratorDict('ENTITY_TYPE');
      }

      return {
        columns: columns,
        activeKey,
        fieldDataList,
        getEntityType,
        entityTypeDictList,
      };
    },
  });
</script>
