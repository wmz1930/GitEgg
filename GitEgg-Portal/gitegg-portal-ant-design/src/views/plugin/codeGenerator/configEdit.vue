<template>
  <a-card :bordered="false" class="content">
    <a-steps size="small" :current="current">
      <a-step v-for="item in steps" :key="item.title" :title="item.title" >
        <a-icon slot="icon" :type="item.icon" />
      </a-step>
    </a-steps>
    <div class="steps-content">
      <table-join :configForm="configForm" v-show="steps[current] && steps[current].content === 'union-table' && (configForm.tableType === 'join_query' || configForm.tableType === 'main_sub')" ref="tableJoin"></table-join>
      <table-field :configForm="configForm" :fields="fieldList" v-show="steps[current] && steps[current].content === 'filed-config'" ref="tableConfig"></table-field>
      <table-form :configForm="configForm" :fields="fieldList" v-show="steps[current] && steps[current].content === 'form-config'" ref="tableForm"></table-form>
      <table-form-valid :configForm="configForm" :fields="fieldList" v-show="steps[current] && steps[current].content === 'form-valid'" ref="tableFormValid"></table-form-valid>
      <table-list :configForm="configForm" :fields="fieldList" v-show="steps[current] && steps[current].content === 'list-config'" ref="tableList"></table-list>
    </div>
    <div class="back-action">
      <a-button style="margin-left: 12px" @click="backToList">
        返回列表
      </a-button>
    </div>
    <div class="steps-action">

      <a-button :disabled="current <= 0" style="margin-right: 18px" @click="prev">
        上一步
      </a-button>

      <a-button v-if="current > 0 && current < steps.length - 1" style="margin-right: 18px" type="primary" ghost @click="createFieldList">
        保存
      </a-button>

      <a-button
        v-if="current == steps.length - 1"
        type="primary"
        @click="completeSteps"
      >
        配置完成
      </a-button>

      <a-button v-if="current < steps.length - 1" type="primary" @click="next">
        下一步
      </a-button>
    </div>
  </a-card>
</template>

<script>
    import { STable } from '@/components'
    import { queryConfigList, createConfig, updateConfig, updateConfigStatus, batchDeleteConfig, deleteConfig, checkConfigExist, queryConfig } from '@/api/plugin/codeGenerator/config/config'
    import { queryFieldListAll, editField } from '@/api/plugin/codeGenerator/field/field'
    import TableJoin from './configSteps/TableJoin'
    import TableField from './configSteps/TableField'
    import TableForm from './configSteps/TableForm'
    import TableFormValid from './configSteps/TableFormValid'
    import TableList from './configSteps/TableList'

    import moment from 'moment'
    export default {
        name: 'ConfigTable',
        components: { moment, STable, TableJoin, TableField, TableForm, TableFormValid, TableList },
        filters: {
            statusFilter (status) {
                const statusMap = {
                    '1': 'green',
                    '2': '',
                    '0': 'pink'
                }
                return statusMap[status]
            },
            statusNameFilter (status) {
                const statusNameMap = {
                    '1': '启用',
                    '0': '禁用'
                }
                return statusNameMap[status]
            }
        },
        data () {
            // 增加或更新记录时，判断字段是否已经存在
            var validConfig = (rule, value, callback) => {
                var keyData = {
                    id: this.configForm.id,
                    checkField: 'config', // 这里改为字段名称
                    checkValue: value
                }
                checkConfigExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                current: 0,
                steps: [],
                stepsMulti: [
                    {
                        title: '联表配置',
                        content: 'union-table',
                        icon: 'appstore'
                    }
                ],
                stepsSingle: [
                    {
                        title: '字段配置',
                        content: 'filed-config',
                        icon: 'apartment'
                    },
                    {
                        title: '表单配置',
                        content: 'form-config',
                        icon: 'file-text'
                    },
                    {
                        title: '表单校验',
                        content: 'form-valid',
                        icon: 'carry-out'
                    },
                    {
                        title: '列表配置',
                        content: 'list-config',
                        icon: 'table'
                    }
                ],
                advanced: false,
                currentConfig: '',
                filterText: '',
                tableKey: 0,
                list: null,
                fieldList: [],
                total: 0,
                listLoading: true,
                listQuery: {
                    datasourceId: '',
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    formType: '',
                    tableType: '',
                    tableShowType: '',
                    formItemCol: '',
                    leftTreeType: '',
                    frontCodePath: '',
                    serviceCodePath: '',
                    importFlag: '',
                    exportFlag: '',
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
                configForm: {
                    id: '',
                    datasourceId: '',
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    formType: '',
                    tableType: '',
                    tableShowType: '',
                    formItemCol: '',
                    leftTreeType: '',
                    frontCodePath: '',
                    serviceCodePath: '',
                    importFlag: false,
                    exportFlag: false
                },
                // 表头
                columns: [
                    {
                        title: '数据源',
                        align: 'center',
                        dataIndex: 'datasourceId'
                    },
                    {
                        title: '模块名称',
                        align: 'center',
                        dataIndex: 'moduleName'
                    },
                    {
                        title: '模块代码',
                        align: 'center',
                        dataIndex: 'moduleCode'
                    },
                    {
                        title: '服务名称',
                        align: 'center',
                        dataIndex: 'serviceName'
                    },
                    {
                        title: '表名',
                        align: 'center',
                        dataIndex: 'tableName'
                    },

                    {
                        title: '表单类型',
                        align: 'center',
                        dataIndex: 'formType'
                    },
                    {
                        title: '联表类型',
                        align: 'center',
                        dataIndex: 'tableType'
                    },
                    {
                        title: '展示类型',
                        align: 'center',
                        dataIndex: 'tableShowType'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        width: '200px',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                rules: {
                    // 字段校验，这里自己选择使用哪些校验
                    config: [
                        { required: true, message: '请输入config', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
                        { validator: validConfig, trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                configLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                configWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                configPagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryConfigList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                }
            }
        },
        watch: {
            '$route' (to, from) {
                if (to.name === 'ConfigEdit' && to.params.id && to.params.id !== '') {
                    this.fieldList = []
                    this.current = 0
                    this.getConfig()
                }
            }
        },
        created () {
            this.getConfig()
        },
        methods: {
            backToList () {
                this.$router.push({ path: '/plugin/code/generator/config/table' })
            },
            next () {
                this.current++
                // 字段配置
                if (this.steps[this.current].content === 'filed-config') {
                    this.getFieldList()
                }
                // 表单配置
                if (this.steps[this.current].content === 'form-config') {

                }
                // 表单校验
                if (this.steps[this.current].content === 'form-valid') {

                }
                // 列表配置
                if (this.steps[this.current].content === 'list-config') {

                }
            },
            prev () {
                this.current--
            },
            getConfig () {
                this.$loading.show()
                this.steps = []
                const id = this.$route.params && this.$route.params.id
                this.listQuery.id = id
                queryConfig(this.listQuery).then(response => {
                    this.configForm = response.data
                    this.$loading.hide()
                    if (this.configForm.tableType === 'join_query' || this.configForm.tableType === 'main_sub') {
                        this.steps = this.steps.concat(this.stepsMulti)
                    } else {
                        this.getFieldList()
                    }
                    this.steps = this.steps.concat(this.stepsSingle)
                })
            },
            getFieldList () {
                this.$loading.show()
                const id = this.$route.params && this.$route.params.id
                queryFieldListAll({ generationId: id }).then(response => {
                    this.fieldList = response.data
                    this.$loading.hide()
                })
            },
            createFieldList () {
               this.$loading.show()
               let fileds = []
               this.fieldList.forEach(function (fieldData) {
                  fileds = fileds.concat(fieldData.fieldDTOList)
               })
               editField(fileds).then(() => {
                   this.dialogFormVisible = false
                   this.$loading.hide()
                   this.getFieldList()
                   this.$message.success('字段设置保存成功')
               })
            },
            completeSteps () {
                this.createFieldList()
            },
            resetQuery () {
                this.listQuery = {
                        id: '',
                        datasourceId: '',
                        moduleName: '',
                        moduleCode: '',
                        serviceName: '',
                        tableName: '',
                        tableAlias: '',
                        tablePrefix: '',
                        parentPackage: '',
                        formType: '',
                        tableType: '',
                        tableShowType: '',
                        formItemCol: '',
                        leftTreeType: '',
                        frontCodePath: '',
                        serviceCodePath: '',
                        importFlag: '',
                        exportFlag: '',
                        startDateTime: '',
                        endDateTime: ''
                }
            },
            resetConfigForm () {
                this.configForm = {
                    id: '',
                    datasourceId: '',
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    formType: '',
                    tableType: '',
                    tableShowType: '',
                    formItemCol: '',
                    leftTreeType: '',
                    frontCodePath: '',
                    serviceCodePath: '',
                    importFlag: false,
                    exportFlag: false
                }
            },
            onSelectChange (selectedRowKeys, selectedRows) {
                this.selectedRowKeys = selectedRowKeys
                this.selectedRows = selectedRows
            },
            toggleAdvanced () {
                this.advanced = !this.advanced
            },
            getConfigDetail () {
              this.listLoading = true
              const id = this.$route.params && this.$route.params.id
              queryConfigList(id).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            getList () {
                this.listLoading = true
                queryConfigList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.configTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.configTable.refresh()
            },
            handleCreate () {
                this.resetConfigForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['configForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['configForm'].validate(valid => {
                    if (valid) {
                        createConfig(this.configForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.configForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['configForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['configForm'].validate(valid => {
                    if (valid) {
                        updateConfig(this.configForm).then(() => {
                            for (const v of this.list) {
                                if (v.id === this.configForm.id) {
                                    const index = this.list.indexOf(v)
                                    this.list.splice(index, 1, this.configForm)
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
                        deleteConfig(row.id).then(() => {
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
            handleBatchDelete () {
                // 这里可以设置需要提示的信息，可以将id换为其他内容
                var configList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: configList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteConfig(that.selectedRowKeys).then(() => {
                            that.listLoading = false
                            that.$message.success('删除成功!')
                            that.selectedRowKeys = []
                            that.selectedRows = []
                            that.handleTableRefresh()
                        })
                    },
                    onCancel () {
                        that.$message.info('已取消删除')
                    }
                })
            },
            handleModifyStatus (row, status) {
                this.listLoading = true
                updateConfigStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.configStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                    const tHeader = [
                        '主键',
                        '数据源',
                        '模块名称',
                        '模块代码',
                        '服务名称',
                        '表名',
                        '表别名',
                        '表前缀',
                        '父级包名',
                        '表单类型 model弹出框  drawer抽屉  tab新页面',
                        '表类型 single单表  multi多表',
                        '展示类型 table数据表格 tree_table 树表格 3 left_tree_table左树右表  tree数据树',
                        '表单字段排列 1一列一行  2 两列一行',
                        '左树类型 organization机构树 resource资源权限树 ',
                        '前端代码路径',
                        '后端代码路径',
                        '是否支持导入 1支持 0不支持',
                        '是否支持导出 1支持 0不支持'
                    ]
                    const filterVal = [
                        'id',
                        'datasourceId',
                        'moduleName',
                        'moduleCode',
                        'serviceName',
                        'tableName',
                        'tableAlias',
                        'tablePrefix',
                        'parentPackage',
                        'formType',
                        'tableType',
                        'tableShowType',
                        'formItemCol',
                        'leftTreeType',
                        'frontCodePath',
                        'serviceCodePath',
                        'importFlag',
                        'exportFlag'
                    ]
                    const data = this.formatJson(filterVal, this.list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: '代码生成配置表数据导出列表'
                    })
                    this.downloadLoading = false
                })
            },
            formatJson (filterVal, jsonData) {
                return jsonData.map(v =>
                    filterVal.map(j => {
                        if (j === 'createTime') {
                            return moment(v[j])
                        } else if (j === 'configStatus') {
                            return this.$options.filters['statusNameFilter'](v[j])
                        } else {
                            return v[j]
                        }
                    })
                )
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
.steps-content {
  margin-top: 16px;
  /* border: 1px dashed #e9e9e9; */
  border-radius: 6px;
  background-color: #fafafa;
  min-height: 200px;
}

.steps-action {
  margin-top: 24px;
  float: right;
}

.back-action {
  margin-top: 24px;
  float: left;
}
</style>
