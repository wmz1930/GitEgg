<template>
  <a-card :bordered="false" class="step-content">
    <div class="table-operator">
      <a-row>
        <a-col :md="6" :sm="24">
          <a-button type="primary" icon="plus" @click="handleCreate">新增关联表</a-button>
        </a-col>
        <a-col :md="18" :sm="24">
          <a-descriptions size="small">
            <a-descriptions-item label="主表表名">
              {{ configForm.tableName }}
            </a-descriptions-item>
            <a-descriptions-item label="主表别名">
              {{ configForm.tableAlias }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

    </div>

    <s-table
      ref="tableJoinTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :scroll="{x:1500}"
      :pagination="tableJoinPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.tableJoinStatus | statusFilter">{{ record.tableJoinStatus | statusNameFilter }}</a-tag>
      </span>
      <span slot="createTime" slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handleUpdate(record)">编辑</a>
        <a-divider type="vertical" />
        <a href="javascript:;" @click="handleDelete(record)">删除</a>
      </span>
    </s-table>

    <a-modal :title="textMap[dialogStatus]" :maskClosable="false" :visible="dialogFormVisible" :width="800" @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="tableJoinForm"
        :model="tableJoinForm"
        :rules="rules"
        :label-col="tableJoinLabelCol"
        :wrapper-col="tableJoinWrapperCol">
        <a-form-model-item label="表名" prop="joinTableName">
          <a-select v-model="tableJoinForm.joinTableName"
                    placeholder="请选择表"
                    allow-clear
                    show-search
                    :filter-option="filterOption"
                    @change="handleTableChange">
            <a-select-option v-for="item in tableList" :key="item.tableName" :value="item.tableName">
              {{ item.tableName }} : [{{ item.tableComment }}]
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="别名" prop="joinTableAlias">
          <a-input v-model="tableJoinForm.joinTableAlias" placeholder="组成SQL语句时表的别名" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="模块名称" prop="moduleName">
          <a-input v-model="tableJoinForm.moduleName" placeholder="主表字表，字表的模块名称" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="模块代码" prop="moduleCode">
          <a-input v-model="tableJoinForm.moduleCode" placeholder="主表字表，字表的模块代码" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="Controller请求路径" prop="controllerPath">
          <a-input v-model="tableJoinForm.controllerPath" placeholder="主表字表，字表的Controller请求路径" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="表前缀" prop="joinTablePrefix">
          <a-input v-model="tableJoinForm.joinTablePrefix" placeholder="输入表前缀" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="排序" prop="tableSort">
          <a-input v-model="tableJoinForm.tableSort" placeholder="组成SQL语句时的顺序" :maxLength="5" />
        </a-form-model-item>
        <a-form-model-item label="联表方式" prop="joinTableType">
          <a-select v-model="tableJoinForm.joinTableType" placeholder="请选择连接类型" allow-clear show-search :filter-option="filterOption">
            <a-select-option v-for="item in unionTypeDictList" :key="item.dictCode" :value="item.dictCode">
              {{ item.dictName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="查询字段" prop="joinTableSelect">
          <a-select mode="multiple"
                    v-model="tableJoinForm.joinTableSelect"
                    placeholder="请选择自定义查询字段"
                    allow-clear
                    show-search
                    :filter-option="filterOption">
            <a-select-option v-for="item in tableFieldList" :key="item.name" :value="item.name">
              {{ item.name }} : [{{ item.comment }}]
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="关联主键" prop="associationId">
          <a-select v-model="tableJoinForm.associationId"
                    placeholder="请选择关联主键"
                    allow-clear
                    show-search
                    :filter-option="filterOption">
            <a-select-option v-for="item in tableFieldList" :key="item.name" :value="item.name">
              {{ item.name }} : [{{ item.comment }}]
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="自定义on条件" prop="joinTableOn">
          <a-input v-model="tableJoinForm.joinTableOn" placeholder="例：on a.id = b.id and b.del_flag = 0" :maxLength="255" />
        </a-form-model-item>
      </a-form-model>
      <div slot="footer" class="dialog-footer">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</a-button>
        <a-button v-else type="primary" @click="updateData">修改</a-button>
      </div>
    </a-modal>
  </a-card>
</template>

<script>
    import { STable } from '@/components'
    import { queryTableList, queryTableFieldList } from '@/api/plugin/codeGenerator/engine/engine'
    import { queryTableJoinList, createTableJoin, updateTableJoin, deleteTableJoin, checkTableJoinExist } from '@/api/plugin/codeGenerator/join/table_join'
    import { listGeneratorDict } from '@/api/plugin/codeGenerator/dict/dict'
    import moment from 'moment'
    let vm = {}
    export default {
        name: 'TableJoinTable',
        components: { moment, STable },
        filters: {
            // 联表类型数据字典展示
            unionTypeDictFilter (dictCode) {
                return vm.unionTypeDictFilterMap[dictCode]
            }
        },
        props: {
            configForm: {
                type: Object,
                default: undefined
            }
        },
        data () {
            vm = this
            // 增加或更新记录时，判断字段是否已经存在
            var validTableJoin = (rule, value, callback) => {
                var keyData = {
                    id: this.tableJoinForm.id,
                    checkField: 'tableJoin', // 这里改为字段名称
                    checkValue: value
                }
                checkTableJoinExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                configQuery: { id: undefined },
                tableQuery: { datasourceId: undefined },
                filedQuery: { datasourceId: undefined, tableName: undefined },
                tableList: [],
                tableFieldList: [],
                unionTypeDictList: [],
                unionTypeDictFilterMap: {},
                listQuery: {
                    id: '',
                    generationId: '',
                    datasourceId: '',
                    moduleName: undefined,
                    moduleCode: undefined,
                    controllerPath: undefined,
                    joinTableName: undefined,
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    joinTableType: undefined,
                    joinTableSelect: undefined,
                    associationId: undefined,
                    joinTableOn: '',
                    startDateTime: '',
                    endDateTime: ''
                },
                statusOption: [{ label: '启用', key: '1' }, { label: '禁用', key: '0' }],
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                tableJoinForm: {
                    id: '',
                    generationId: '',
                    datasourceId: '',
                    moduleName: undefined,
                    moduleCode: undefined,
                    controllerPath: undefined,
                    joinTableName: undefined,
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    tableSort: '',
                    joinTableType: undefined,
                    joinTableSelect: undefined,
                    associationId: undefined,
                    joinTableOn: ''
                },
                // 表头
                columns: [
                    {
                        title: '序号',
                        align: 'center',
                        dataIndex: 'tableSort'
                    },
                    {
                        title: '表名',
                        align: 'center',
                        width: '250px',
                        ellipsis: true,
                        dataIndex: 'joinTableName'
                    },
                    {
                        title: '别名',
                        align: 'center',
                        ellipsis: true,
                        width: '180px',
                        dataIndex: 'joinTableAlias'
                    },
                    {
                        title: '表前缀',
                        align: 'center',
                        dataIndex: 'joinTablePrefix'
                    },
                    {
                        title: '联表类型',
                        align: 'center',
                        dataIndex: 'joinTableType'
                    },
                    {
                        title: '自定义查询字段',
                        align: 'center',
                        ellipsis: true,
                        width: '250px',
                        dataIndex: 'joinTableSelect'
                    },
                    {
                        title: '自定义on条件',
                        align: 'center',
                        ellipsis: true,
                        width: '250px',
                        dataIndex: 'joinTableOn'
                    },
                    {
                        title: '操作',
                        align: 'center',
                        dataIndex: 'action',
                        width: '180px',
                        fixed: 'right',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                rules: {
                    // 字段校验，这里自己选择使用哪些校验
                    tableJoin: [
                        { required: true, message: '请输入tableJoin', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
                        { validator: validTableJoin, trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                tableJoinLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                tableJoinWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                tableJoinPagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return function () {}
                }
            }
        },
        watch: {
            configForm (val) {
              this.getTableList()
              this.queryDbTypeDictList()
            }
        },
        created () {
            // this.getTableList()
            // this.queryDbTypeDictList()
        },
        methods: {
            getTableList () {
                if (this.configForm.datasourceId && this.configForm.datasourceId !== '') {
                    this.listLoading = true
                    this.tableQuery.datasourceId = this.configForm.datasourceId
                    queryTableList(this.tableQuery).then(response => {
                        this.tableList = response.data
                        this.listLoading = false
                    })
                }
            },
            async getTableFieldList () {
                this.listLoading = true
                await queryTableFieldList(this.filedQuery).then(response => {
                    this.tableFieldList = response.data[0].fields
                    this.listLoading = false
                })
            },
            queryDbTypeDictList () {
                const that = this
                if (that.configForm.id && that.configForm.id !== '') {
                    that.listLoading = true
                    listGeneratorDict('UNION_TYPE').then(response => {
                        this.unionTypeDictList = response.data
                        this.unionTypeDictList.forEach((item, index, arr) => {
                            this.unionTypeDictFilterMap[item.dictCode] = item.dictName
                        })
                        that.listLoading = false
                        that.getList()
                    })
                }
            },
            handleTableChange (value) {
                this.tableJoinForm.joinTableSelect = undefined
                this.filedQuery.tableName = value
                this.filedQuery.datasourceId = this.configForm.datasourceId
                this.getTableFieldList()
            },
            resetQuery () {
                this.listQuery = {
                    id: '',
                    generationId: '',
                    datasourceId: '',
                    moduleName: undefined,
                    moduleCode: undefined,
                    controllerPath: undefined,
                    joinTableName: undefined,
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    tableSort: '',
                    joinTableType: undefined,
                    joinTableSelect: undefined,
                    associationId: undefined,
                    joinTableOn: '',
                    startDateTime: '',
                    endDateTime: ''
                }
            },
            resetTableJoinForm () {
                this.tableJoinForm = {
                    id: '',
                    generationId: '',
                    datasourceId: '',
                    moduleName: undefined,
                    moduleCode: undefined,
                    controllerPath: undefined,
                    joinTableName: undefined,
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    tableSort: '',
                    joinTableType: undefined,
                    joinTableSelect: undefined,
                    associationId: undefined,
                    joinTableOn: ''
                }
            },
            onSelectChange (selectedRowKeys, selectedRows) {
                this.selectedRowKeys = selectedRowKeys
                this.selectedRows = selectedRows
            },
            toggleAdvanced () {
                this.advanced = !this.advanced
            },
            getList () {
                const that = this
                that.listQuery.generationId = that.configForm.id
                  that.loadData = function (parameter) {
                  return queryTableJoinList(Object.assign(parameter, that.listQuery)).then(response => {
                    that.list = response.data.records
                    return response.data
                  })
                }
                that.$nextTick(() => {
                  that.handleFilter()
                })
            },
            handleFilter () {
                this.$refs.tableJoinTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.tableJoinTable.refresh()
            },
            handleCreate () {
                this.resetTableJoinForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['tableJoinForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['tableJoinForm'].validate(valid => {
                    if (valid) {
                        this.tableJoinForm.generationId = this.configForm.id
                        this.tableJoinForm.joinTableSelect = this.tableJoinForm.joinTableSelect.join(',')
                        createTableJoin(this.tableJoinForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                const that = this
                this.tableJoinForm = Object.assign({}, row) // copy obj

                // 获取表的所有字段
                this.tableJoinForm.joinTableSelect = undefined
                this.filedQuery.tableName = row.joinTableName
                this.filedQuery.datasourceId = this.configForm.datasourceId
                this.listLoading = true
                queryTableFieldList(this.filedQuery).then(response => {
                    that.tableFieldList = response.data[0].fields
                    that.tableJoinForm.joinTableSelect = row.joinTableSelect.split(',')
                    that.listLoading = false
                })

                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['tableJoinForm'].clearValidate()
                })
            },
            updateData () {
                this.tableJoinForm.joinTableSelect = this.tableJoinForm.joinTableSelect.join(',')
                this.$refs['tableJoinForm'].validate(valid => {
                    if (valid) {
                        updateTableJoin(this.tableJoinForm).then(() => {
                            for (const v of this.list) {
                                if (v.id === this.tableJoinForm.id) {
                                    const index = this.list.indexOf(v)
                                    this.list.splice(index, 1, this.tableJoinForm)
                                    break
                                }
                            }
                            this.dialogFormVisible = false
                            this.$message.success('更新成功')
                        })
                    }
                })
            },
            handleDelete (row) {
                var that = this
                this.$confirm({
                    title: '此操作将永久删除该记录，是否继续?',
                    content: '',
                    onOk () {
                        that.listLoading = true
                        deleteTableJoin(row.id).then(() => {
                            that.listLoading = false
                            that.$message.success('删除成功!')
                            that.handleTableRefresh()
                        })
                    },
                    onCancel () {
                        that.$message.info('已取消删除')
                    }
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
