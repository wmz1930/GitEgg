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
          <template slot="queryTermRender" slot-scope="text, record" >
            <a-switch :value="text" checked-children="是" un-checked-children="否" default-checked v-model="record.queryTerm"/>
          </template>
          <template slot="queryTypeRender" slot-scope="text, record" >
            <a-select :value="text"
                      placeholder="请选择查询类型"
                      show-search
                      style="width:100%;"
                      :default-value="text"
                      :disabled="!record.queryTerm"
                      :filter-option="filterOption"
                      v-model="record.queryType">
              <a-select-option v-for="item in queryTypeDict.dictList" :key="item.id" :value="item.dictCode">
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template slot="listShowRender" slot-scope="text, record" >
            <a-checkbox :value="text" v-model="record.listShow">
              展示
            </a-checkbox>
          </template>
          <template slot="importFlagRender" slot-scope="text, record" >
            <a-checkbox :value="text" v-model="record.importFlag">
              导入
            </a-checkbox>
          </template>
          <template slot="exportFlagRender" slot-scope="text, record" >
            <a-checkbox :value="text" v-model="record.exportFlag">
              导出
            </a-checkbox>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
    import { batchListGeneratorDict } from '@/api/plugin/codeGenerator/dict/dict'
    export default {
        name: 'FieldTable',
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
                fieldDataList: [],
                listQuery: {
                    id: '',
                    generationId: '',
                    joinId: ''
                },
                // 表头
                columnsField: [],
                // 表头
                baseColumnsField: [
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
                        title: '查询条件',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'queryTerm',
                        scopedSlots: { customRender: 'queryTermRender' }
                    },
                    {
                        title: '查询类型',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'queryType',
                        scopedSlots: { customRender: 'queryTypeRender' }
                    },
                    {
                        title: '列表展示',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'listShow',
                        scopedSlots: { customRender: 'listShowRender' }
                    }
                ],
                // 表头
                importColumnField: {
                        title: '支持导入',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'importFlag',
                        scopedSlots: { customRender: 'importFlagRender' }
                },
                // 表头
                exportColumnField: {
                        title: '支持导出',
                        align: 'center',
                        width: '130px',
                        dataIndex: 'exportFlag',
                        scopedSlots: { customRender: 'exportFlagRender' }
                },
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
              this.processImportAndExport()
            },
            fields (val) {
                this.fieldDataList = val
            }
        },
        created () {
            const that = this
            const dictList = [this.entityTypeDict, this.controlTypeDict, this.queryTypeDict]
            const dictCodeList = dictList.map(function (n) {
              return n.dictCode
            })
            this.processImportAndExport()
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
            processImportAndExport () {
                this.columnsField = this.baseColumnsField
                if (this.configForm.importFlag) {
                    this.columnsField = this.columnsField.concat(this.importColumnField)
                }
                if (this.configForm.exportFlag) {
                    this.columnsField = this.columnsField.concat(this.exportColumnField)
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
