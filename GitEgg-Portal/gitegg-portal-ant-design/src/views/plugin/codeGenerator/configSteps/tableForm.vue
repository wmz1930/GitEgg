<template>
  <a-card :bordered="false" class="step-content">
    <a-tabs :default-active-key="0">
      <a-tab-pane v-for="fieldData in fieldDataList" :key="fieldData.joinId" :tab="'【' + fieldData.joinTableName + '】 字段配置'">
        <a-table
          :rowKey="row=>row.id + row.fieldName"
          :columns="columnsField"
          :data-source="fieldData.fieldDTOList"
          :pagination="false"
          bordered>
          <template slot="formAddRender" slot-scope="text, record" >
            <a-checkbox :checked="text" v-model="record.formAdd">
              新增
            </a-checkbox>
          </template>
          <template slot="formEditRender" slot-scope="text, record" >
            <a-checkbox :checked="text" v-model="record.formEdit">
              编辑
            </a-checkbox>
          </template>
          <template slot="controlTypeRender" slot-scope="text, record" >
            <a-select :value="text"
                      placeholder="请选择组件类型"
                      show-search
                      style="width:100%;"
                      :filter-option="filterOption"
                      v-model="record.controlType">
              <a-select-option v-for="item in controlTypeDict.dictList" :key="item.id" :value="item.dictCode">
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template slot="dictCodeRender" slot-scope="text, record" >
            <a-select :value="text"
                      placeholder="请选择字典编码"
                      show-search
                      style="width:100%;"
                      :default-value="text"
                      :filter-option="filterOption"
                      :disabled="dictCodeDisabled(record.controlType)"
                      v-model="record.dictCode">
              <a-select-option v-for="item in baseDictList" :key="item.id" :value="item.dictCode">
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
    import { queryDictBusinessList } from '@/api/system/base/dictBusiness'
    import { batchListGeneratorDict } from '@/api/plugin/codeGenerator/dict/dict'
    export default {
        name: 'FormTable',
        components: { },
        filters: {
        },
        props: {
            configForm: {
                type: Object,
                default: undefined
            },
            fields: {
                type: Array,
                default: undefined
            }
        },
        data () {
            return {
                listLoading: true,
                entityTypeDict: {
                  dictCode: 'ENTITY_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                controlTypeDict: {
                  dictCode: 'CONTROL_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                queryTypeDict: {
                  dictCode: 'QUERY_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                baseDictList: [],
                fieldDataList: [],
                listQuery: {
                    id: '',
                    generationId: '',
                    joinId: ''
                },
                // 表头
                columnsField: [
                   {
                        title: '序号',
                        dataIndex: 'index',
                        key: 'index',
                        align: 'center',
                        width: 70,
                        customRender: (text, record, index) => `${index + 1}`
                    },
                    {
                        title: '字段描述',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'comment'
                    },
                    {
                        title: '实体类型',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'entityType'
                    },
                    {
                        title: '实体名称',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'entityName'
                    },
                    {
                        title: '表单新增',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'formAdd',
                        scopedSlots: { customRender: 'formAddRender' }
                    },
                    {
                        title: '表单编辑',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'formEdit',
                        scopedSlots: { customRender: 'formEditRender' }
                    },
                    {
                        title: '组件类型',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'controlType',
                        scopedSlots: { customRender: 'controlTypeRender' }
                    },
                    {
                        title: '字典编码',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'dictCode',
                        scopedSlots: { customRender: 'dictCodeRender' }
                    }
                ],
                rules: {
                    // 字段校验，这里自己选择使用哪些校验
                    field: [
                        { required: true, message: '请输入field', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' }
                    ]
                }
            }
        },
        watch: {
            configForm (val) {

            },
            fields (val) {
                this.fieldDataList = val
            }
        },
        created () {
            const that = this
            this.getDictList()
            const dictList = [this.entityTypeDict, this.controlTypeDict, this.queryTypeDict]
            const dictCodeList = dictList.map(function (n) {
              return n.dictCode
            })
            this.getBatchDataDictList(dictCodeList).then(function (result) {
                dictList.forEach(function (dict) {
                  dict.dictList = result[dict.dictCode]
                  dict.filterMap = {}
                  dict.dictList.forEach((item, index, arr) => {
                    dict.filterMap[item.dictCode] = item.dictName
                  })
                })
                that.fieldDataList = that.fields
            })
        },
        methods: {
            async getBatchDataDictList (dictParams) {
              const that = this
              let result = {}
              that.listLoading = true
              await batchListGeneratorDict(dictParams).then(response => {
                result = response.data
                that.listLoading = false
              })
              return result
            },
            getDictList () {
                this.listLoading = true
                queryDictBusinessList({ parentId: 0 }).then(response => {
                    this.baseDictList = response.data
                    this.listLoading = false
                })
            },
            dictCodeDisabled (type) {
              if (type && (type === 'SELECT' || type === 'RADIO' || type === 'CHECKBOX' ||
               type === 'SELECT_MULTI' || type === 'SWITCH')) {
                  return false
              } else {
                  return true
              }
            },
            filterOption (input, option) {
              return (
                      option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
              )
            }
        }
    }
</script>
<style scoped>
.step-content /deep/ .ant-card-body{
    padding: 0;
}
</style>
