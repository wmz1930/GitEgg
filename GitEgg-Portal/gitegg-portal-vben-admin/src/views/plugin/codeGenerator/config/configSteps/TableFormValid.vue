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
        :columns="FormValidColumns"
        :dataSource="fieldData.fieldDTOList"
        :pagination="false"
        :resizeHeightOffset="70"
      >
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.key === 'maxLength'">
            <a-input v-model:value="record.maxLength" placeholder="最大长度" />
          </template>
          <template v-else-if="column.key === 'minLength'">
            <a-input v-model:value="record.minLength" placeholder="最小长度" />
          </template>
          <template v-else-if="column.key === 'required'">
            <a-checkbox v-model:checked="record.required">必填</a-checkbox>
          </template>
          <template v-else-if="column.key === 'fieldUnique'">
            <a-checkbox v-model:checked="record.fieldUnique">唯一</a-checkbox>
          </template>
          <template v-else-if="column.key === 'validateId'">
            <a-select
              placeholder="请选择校验规则"
              style="width: 100%"
              :default-value="text"
              show-search
              :filter-option="filterOption"
              v-model:value="record.validateId"
            >
              <a-select-option
                v-for="item in validateDictList"
                :key="item.id"
                :title="item.validateName"
                :value="item.id"
              >
                {{ item.validateName }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.key === 'validateRegular'">
            <a-input v-model:value="record.validateRegular" placeholder="自定义校验规则" />
          </template>
          <template v-else-if="column.key === 'max'">
            <a-input v-model:value="record.max" placeholder="最大值" />
          </template>
          <template v-else-if="column.key === 'min'">
            <a-input v-model:value="record.min" placeholder="最小值" />
          </template>
        </template>
      </BasicTable>
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, onMounted } from 'vue';
  import { Tabs, Input, Select, Checkbox } from 'ant-design-vue';
  import { BasicTable } from '/@/components/Table';
  import { FormValidColumns } from './table-form/table.form.data';
  import { getValidateListAll } from '/@/api/plugin/codeGenerator/validate/validate';
  export default defineComponent({
    components: {
      [Tabs.name]: Tabs,
      [Tabs.TabPane.name]: Tabs.TabPane,
      [Input.name]: Input,
      [Input.Group.name]: Input.Group,
      [Select.name]: Select,
      ASelectOption: Select.Option,
      [Checkbox.name]: Checkbox,
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
      const validateDictList = ref<any[]>([]);
      const activeKey = ref('0');

      // 挂载后查询配置
      onMounted(() => {
        getValidateList();
      });

      async function getValidateList() {
        validateDictList.value = await getValidateListAll({});
      }

      function dictCodeDisabled(type: string) {
        if (
          type &&
          (type === 'SELECT' ||
            type === 'RADIO' ||
            type === 'CHECKBOX' ||
            type === 'SELECT_MULTI' ||
            type === 'SWITCH')
        ) {
          return false;
        } else {
          return true;
        }
      }

      const filterOption = (input: string, option: any) => {
        return option.title.toLowerCase().indexOf(input.toLowerCase()) >= 0;
      };

      return {
        FormValidColumns,
        activeKey,
        fieldDataList,
        getValidateList,
        validateDictList,
        filterOption,
        dictCodeDisabled,
      };
    },
  });
</script>
