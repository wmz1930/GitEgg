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
        <template #headerCell="{ column }">
          <template v-if="column.key === 'dictCode'">
            <span>
              字典编码
              <a-tag
                color="blue"
                style="cursor: pointer"
                @click="showAddDataModal({ openType: 'dict', titleText: '新增数据字典' })"
              >
                <template #icon>
                  <icon icon="clarity:add-line" />
                  新增
                </template>
              </a-tag>
            </span>
          </template>
          <template v-else-if="column.key === 'apiId'">
            数据接口
            <a-tag
              color="blue"
              style="cursor: pointer"
              @click="showAddDataModal({ openType: 'api', titleText: '新增API接口' })"
            >
              <template #icon>
                <icon icon="clarity:add-line" />
                新增
              </template>
            </a-tag>
          </template>
          <template v-else>
            <HeaderCell :column="column" />
          </template>
        </template>
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.key === 'formAdd'">
            <a-checkbox v-model:checked="record.formAdd">新增</a-checkbox>
          </template>
          <template v-if="column.key === 'formEdit'">
            <a-checkbox v-model:checked="record.formEdit">编辑</a-checkbox>
          </template>
          <template v-else-if="column.key === 'controlType'">
            <a-select
              placeholder="请选择组件类型"
              show-search
              :filter-option="filterOption"
              style="width: 100%"
              :default-value="text"
              v-model:value="record.controlType"
            >
              <a-select-option
                v-for="item in controlTypeDictList"
                :key="item.id"
                :title="item.dictName"
                :value="item.dictCode"
              >
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.key === 'dictCode'">
            <a-select
              placeholder="请选择字典编码"
              show-search
              :filter-option="filterOption"
              style="width: 100%"
              :default-value="text"
              :disabled="dictCodeDisabled(record.controlType)"
              @change="changeDictCode(record)"
              v-model:value="record.dictCode"
            >
              <a-select-option
                v-for="item in dictCodeDictList"
                :key="item.id"
                :title="item.dictName"
                :value="item.dictCode"
              >
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.key === 'apiId'">
            <a-select
              placeholder="请选择数据接口"
              show-search
              :filter-option="filterOption"
              style="width: 100%"
              :default-value="text"
              :disabled="apiDisabled(record.dictCode)"
              v-model:value="record.apiId"
            >
              <a-select-option
                v-for="item in apiList"
                :key="item.id"
                :title="item.apiName"
                :value="item.id"
              >
                {{ item.apiName }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.key === 'defaultValue'">
            <!-- 当为字典类型,不为接口时. -->
            <template v-if="!dictCodeDisabled(record.controlType) && apiDisabled(record.dictCode)">
              <a-select
                placeholder="请选择默认值"
                show-search
                :filter-option="filterOption"
                style="width: 100%"
                v-model:value="record.defaultValue"
              >
                <a-select-option
                  v-for="item in defaultValueDictMap.get(record.dictCode)"
                  :key="item.id"
                  :title="item.dictName"
                  :value="item.dictCode"
                >
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </template>
            <template v-else>
              <a-input v-model:value="record.defaultValue" placeholder="可输入默认值" />
            </template>
          </template>
        </template>
      </BasicTable>
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, onMounted } from 'vue';
  import { Tabs, Input, Select, Tag, Checkbox } from 'ant-design-vue';
  import Icon from '@/components/Icon/Icon.vue';
  import HeaderCell from '/@/components/Table/src/components/HeaderCell.vue';
  import { BasicTable } from '/@/components/Table';
  import { columns } from './table-form/table.form.data';
  import { listGeneratorDict } from '/@/api/plugin/codeGenerator/dict/dict';
  import {
    queryDictBusinessList,
    listDictBusiness,
    batchListDictBusiness,
  } from '/@/api/system/base/dictBusiness';
  import { getGeneratorApiListAll } from '/@/api/plugin/codeGenerator/api/api';
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
      HeaderCell,
      Icon,
      [Tag.name]: Tag,
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
    emits: ['add-data-modal'],
    setup(props, { emit }) {
      const fieldDataList = reactive<any[]>(props.fields);
      const controlTypeDictList = ref<any[]>([]);
      const dictCodeDictList = ref<any[]>([]);
      const defaultValueDictMap = ref(new Map<any, any>());
      const apiList = ref<any[]>([]);
      const activeKey = ref('0');

      // 挂载后查询配置
      onMounted(async () => {
        // 批量初始化数据字典值
        const dictCodeList: string[] = [];
        fieldDataList.forEach((val) => {
          val.fieldDTOList.forEach((val) => {
            if (!dictCodeDisabled(val.controlType) && val.dictCode && val.dictCode !== '') {
              dictCodeList.push(val.dictCode);
            }
          });
        });
        if (dictCodeList && dictCodeList.length > 0) {
          const dictResultMap = await batchListDictBusiness(dictCodeList);
          if (dictResultMap) {
            dictCodeList.forEach(function (dictCode) {
              if (dictResultMap[dictCode]) {
                defaultValueDictMap.value.set(dictCode, dictResultMap[dictCode]);
              }
            });
          }
        }

        getControlType();
        getDictList();
        getApiList();
      });

      async function getControlType() {
        controlTypeDictList.value = await listGeneratorDict('CONTROL_TYPE');
      }

      async function getDefaultValueDict(dictCode: string) {
        const defaultValueDictList = await listDictBusiness(dictCode);
        defaultValueDictMap.value.set(dictCode, defaultValueDictList);
      }

      async function changeDictCode(record: Recordable) {
        record.defaultValue = undefined;
        getDefaultValueDict(record.dictCode);
      }

      async function getDictList() {
        dictCodeDictList.value = await queryDictBusinessList({ parentId: 0, dictStatus: 1 });
      }

      async function getApiList() {
        apiList.value = await getGeneratorApiListAll({});
      }

      function dictCodeDisabled(type: string) {
        if (
          type &&
          (type === 'SELECT' ||
            type === 'RADIO' ||
            type === 'CHECKBOX' ||
            type === 'SELECT_MULTI' ||
            type === 'SWITCH' ||
            type === 'API_TREE_SELECT' ||
            type === 'API_TREE' ||
            type === 'CASCADER')
        ) {
          return false;
        } else {
          return true;
        }
      }

      function apiDisabled(type: string) {
        if (type && type === 'API_DICT') {
          return false;
        } else {
          return true;
        }
      }

      function showAddDataModal(record: Recordable) {
        emit('add-data-modal', record);
      }

      const filterOption = (input: string, option: any) => {
        return option.title.toLowerCase().indexOf(input.toLowerCase()) >= 0;
      };

      return {
        columns: columns,
        activeKey,
        fieldDataList,
        getControlType,
        getDictList,
        getApiList,
        controlTypeDictList,
        dictCodeDictList,
        defaultValueDictMap,
        getDefaultValueDict,
        changeDictCode,
        apiList,
        filterOption,
        dictCodeDisabled,
        apiDisabled,
        showAddDataModal,
      };
    },
  });
</script>
