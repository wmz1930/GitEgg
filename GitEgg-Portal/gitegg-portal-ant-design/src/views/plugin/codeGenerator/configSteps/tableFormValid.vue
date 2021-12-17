<template>
  <a-card :bordered="false" class="step-content">
    <a-tabs :default-active-key="0">
      <a-tab-pane v-for="fieldData in fieldDataList" :key="fieldData.joinId" :tab="'【' + fieldData.joinTableName + '】 字段配置'">
        <a-table
          :rowKey="row=>row.id + row.fieldName"
          :columns="columnsField"
          :data-source="fieldData.fieldDTOList"
          :scroll="{x:1500}"
          :pagination="false"
          bordered>
          <template slot="maxRender" slot-scope="text, record" >
            <a-input
              :value="text"
              placeholder="最大值"
              v-model="record.max"
            />
          </template>
          <template slot="minRender" slot-scope="text, record" >
            <a-input
              :value="text"
              placeholder="最小值"
              v-model="record.min"
            />
          </template>
          <template slot="minLengthRender" slot-scope="text, record" >
            <a-input
              :value="text"
              v-model="record.minLength"
            />
          </template>
          <template slot="maxLengthRender" slot-scope="text, record" >
            <a-input
              :value="text"
              v-model="record.maxLength"
            />
          </template>
          <template slot="requiredRender" slot-scope="text, record" >
            <a-checkbox :checked="text" v-model="record.required">
              必填
            </a-checkbox>
          </template>
          <template slot="fieldUniqueRender" slot-scope="text, record" >
            <a-checkbox :checked="text" v-model="record.fieldUnique">
              唯一
            </a-checkbox>
          </template>
          <template slot="validateTypeRender" slot-scope="text, record" >
            <a-select :value="text"
                      placeholder="请选择校验规则"
                      show-search
                      style="width:100%;"
                      :filter-option="filterOption"
                      v-model="record.validateId">
              <a-select-option v-for="item in validateList" :key="item.id" :value="item.id">
                {{ item.validateName }}
              </a-select-option>
            </a-select>
          </template>
          <template slot="validateRegularRender" slot-scope="text, record" >
            <a-input :value="text" placeholder="自定义校验规则" v-model="record.validateRegular"/>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
    import { batchListGeneratorDict } from '@/api/plugin/codeGenerator/dict/dict'
    import { queryValidateListAll } from '@/api/plugin/codeGenerator/validate/validate'
    export default {
        name: 'FormValidTable',
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
                validateList: [],
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
                        width: '100px',
                        ellipsis: true,
                        dataIndex: 'comment'
                    },
                    {
                        title: '实体类型',
                        align: 'center',
                        width: '100px',
                        ellipsis: true,
                        dataIndex: 'entityType'
                    },
                    {
                        title: '实体名称',
                        align: 'center',
                        width: '100px',
                        ellipsis: true,
                        dataIndex: 'entityName'
                    },
                    {
                        title: '最小长度',
                        align: 'center',
                        width: '80px',
                        ellipsis: true,
                        dataIndex: 'minLength',
                        scopedSlots: { customRender: 'minLengthRender' }
                    },
                    {
                        title: '最大长度',
                        align: 'center',
                        width: '80px',
                        ellipsis: true,
                        dataIndex: 'maxLength',
                        scopedSlots: { customRender: 'maxLengthRender' }
                    },
                    {
                        title: '是否必填',
                        align: 'center',
                        width: '80px',
                        ellipsis: true,
                        dataIndex: 'required',
                        scopedSlots: { customRender: 'requiredRender' }
                    },
                    {
                        title: '是否唯一',
                        align: 'center',
                        width: '80px',
                        ellipsis: true,
                        dataIndex: 'fieldUnique',
                        scopedSlots: { customRender: 'fieldUniqueRender' }
                    },
                    {
                        title: '校验类型',
                        align: 'center',
                        width: '200px',
                        ellipsis: true,
                        dataIndex: 'validateType',
                        scopedSlots: { customRender: 'validateTypeRender' }
                    },
                    {
                        title: '正则表达式',
                        align: 'center',
                        width: '130px',
                        ellipsis: true,
                        dataIndex: 'validateRegular',
                        scopedSlots: { customRender: 'validateRegularRender' }
                    },
                    {
                        title: '最大值',
                        align: 'center',
                        width: '80px',
                        ellipsis: true,
                        dataIndex: 'max',
                        scopedSlots: { customRender: 'maxRender' }
                    },
                    {
                        title: '最小值',
                        align: 'center',
                        width: '80px',
                        ellipsis: true,
                        dataIndex: 'min',
                        scopedSlots: { customRender: 'minRender' }
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
            this.getValidateList()
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
            getValidateList () {
                this.listLoading = true
                queryValidateListAll().then(response => {
                    this.validateList = response.data
                    this.listLoading = false
                })
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
