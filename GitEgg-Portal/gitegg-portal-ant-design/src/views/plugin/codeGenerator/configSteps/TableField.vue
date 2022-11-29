<template>
  <a-card :bordered="false" class="step-content">
    <a-tabs :default-active-key="0">
      <a-tab-pane v-for="fieldData in fieldDataList" :key="fieldData.joinId" :tab="'表【' + fieldData.joinTableName + '】 字段配置'">
        <a-table
          :rowKey="row=>row.id + row.fieldName"
          :columns="columnsField"
          :data-source="fieldData.fieldDTOList"
          :scroll="{x:1500}"
          :pagination="false"
          bordered>
          <template slot="requiredRender" slot-scope="text, record" >
            <a-checkbox :value="text" v-model="record.required">
              必填
            </a-checkbox>
          </template>
          <template slot="fieldUniqueRender" slot-scope="text, record" >
            <a-checkbox :value="text" v-model="record.fieldUnique">
              唯一
            </a-checkbox>
          </template>
          <template slot="commentRender" slot-scope="text, record" >
            <a-input
              :value="text"
              v-model="record.comment"
            />
          </template>
          <template slot="entityTypeRender" slot-scope="text, record" >
            <a-select :value="text"
                      placeholder="请选择展示类型"
                      show-search
                      style="width:100%;"
                      :default-value="text"
                      :filter-option="filterOption"
                      v-model="record.entityType">
              <a-select-option v-for="item in entityTypeDict.dictList" :key="item.id" :value="item.dictName">
                {{ item.dictName }}
              </a-select-option>
            </a-select>
          </template>
          <template slot="entityNameRender" slot-scope="text, record" >
            <a-input
              :value="text"
              v-model="record.entityName"
            />
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
    import { batchListGeneratorDict } from '@/api/plugin/codeGenerator/dict/dict'
    let vm = {}
    export default {
        name: 'FieldTable',
        filters: {
            entityTypeFilter (formType) {
              return vm.entityTypeDict.filterMap[formType]
            }
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
            vm = this
            return {
                listLoading: true,
                entityTypeDict: {
                  dictCode: 'ENTITY_TYPE',
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
                        title: '字段名称',
                        align: 'center',
                        width: '100px',
                        ellipsis: true,
                        dataIndex: 'fieldName'
                    },
                    {
                        title: '字段类型',
                        align: 'center',
                        width: '100px',
                        ellipsis: true,
                        dataIndex: 'fieldType'
                    },
                    {
                        title: '字段描述',
                        align: 'center',
                        width: '100px',
                        ellipsis: true,
                        dataIndex: 'comment',
                        scopedSlots: { customRender: 'commentRender' }
                    },
                    {
                        title: '字段类型',
                        align: 'center',
                        width: '130px',
                        ellipsis: true,
                        dataIndex: 'entityType',
                        scopedSlots: { customRender: 'entityTypeRender' }
                    },
                    {
                        title: '字段名称',
                        align: 'center',
                        width: '130px',
                        ellipsis: true,
                        dataIndex: 'entityName',
                        scopedSlots: { customRender: 'entityNameRender' }
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
            const dictList = [this.entityTypeDict]
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
