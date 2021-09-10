<template>
  <a-card :bordered="false" class="content">
    <router-view/>
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="数据源">
              <select-asyn optKey="id"
                           optValue="id"
                           optLable="datasourceName"
                           optPlaceholder="请选择数据源"
                           queryKey="datasourceName"
                           :queryDataList="queryDatasourceListFunction"
                           @holderBack="clickSetListQuery"
              ></select-asyn>
            </a-form-model-item>
          </a-col>

          <a-col :md="6" :sm="24">
            <a-form-model-item label="模块名称">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="请输入模块名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="服务名称">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="请输入服务名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="表名">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="请输入表名"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :md="6" :sm="24">
              <a-form-model-item label="展示类型">
                <a-select v-model="listQuery.formType" placeholder="请选择展示类型" allow-clear show-search :filter-option="filterOption">
                  <a-select-option v-for="item in formTypeDict.dictList" :key="item.id" :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="表单类型">
                <a-select v-model="listQuery.tableType" placeholder="请选择表单类型" allow-clear show-search :filter-option="filterOption">
                  <a-select-option v-for="item in tableTypeDict.dictList" :key="item.id" :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="联表类型">
                <a-select v-model="listQuery.tableShowType" placeholder="请选择联表类型" allow-clear show-search :filter-option="filterOption">
                  <a-select-option v-for="item in tableShowTypeDict.dictList" :key="item.id" :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24" style="float: right;text-align: right;">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form-model>
    </div>

    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleCreate">新建</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="handleBatchDelete"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>
    <s-table
      ref="configTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="configPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.configStatus | statusFilter">{{ record.configStatus | statusNameFilter }}</a-tag>
      </span>
      <span slot="createTime" slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handleUpdate(record)">编辑</a>
        <a-divider type="vertical" />
        <router-link :to="'/plugin/code/generator/config/edit/'+record.id">
          规则配置
        </router-link>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多 <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;" v-if="record.configStatus!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.configStatus!='0' && record.configStatus!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
              </a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" @click="handleDelete(record)">删除</a>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <a-modal :title="textMap[dialogStatus]" :maskClosable="false" :visible="dialogFormVisible" :width="800" @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="configForm"
        :model="configForm"
        :rules="rules"
        :label-col="configLabelCol"
        :wrapper-col="configWrapperCol">
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="数据源" prop="datasourceId">
              <select-asyn optKey="id"
                           optValue="id"
                           optLable="datasourceName"
                           optPlaceholder="请选择数据源"
                           queryKey="datasourceName"
                           :queryDataList="queryDatasourceListFunction"
                           @holderBack="clickSetForm"
              ></select-asyn>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="模块名称" prop="moduleName">
              <a-input v-model="configForm.moduleName" placeholder="输入模块名称，例：系统管理" :maxLength="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="模块代码" prop="moduleCode">
              <a-input v-model="configForm.moduleCode" placeholder="输入模块代码，例：system" :maxLength="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="服务名称" prop="serviceName">
              <a-input v-model="configForm.serviceName" placeholder="例：gitegg-service-system" :maxLength="64" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="表名" prop="tableName">
              <a-input v-model="configForm.tableName" placeholder="输入表名，例：t_sys_user" :maxLength="54" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="表别名" prop="tableAlias">
              <a-input v-model="configForm.tableAlias" placeholder="输入表别名，例：user" :maxLength="54" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="表前缀" prop="tablePrefix">
              <a-input v-model="configForm.tablePrefix" placeholder="输入表前缀，例：t_sys_" :maxLength="64" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="父级包名" prop="parentPackage">
              <a-input v-model="configForm.parentPackage" placeholder="例：com.gitegg.system" :maxLength="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="展示类型" prop="formType">
              <a-select v-model="configForm.formType" placeholder="请选择展示类型" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in formTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="联表类型" prop="tableType">
              <a-select v-model="configForm.tableType" placeholder="请选择表单类型" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in tableTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="展示类型" prop="tableShowType">
              <a-select v-model="configForm.tableShowType" placeholder="请选择联表类型" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in tableShowTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="字段排列" prop="formItemCol">
              <a-input v-model="configForm.formItemCol" placeholder="输入表单字段排列 1一列一行  2 两列一行" :maxLength="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="左树类型" prop="leftTreeType">
              <a-input v-model="configForm.leftTreeType" placeholder="输入左树类型 organization机构树 resource资源权限树 " :maxLength="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="前端代码路径" prop="frontCodePath">
              <a-input v-model="configForm.frontCodePath" placeholder="输入前端代码路径" :maxLength="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="后端代码路径" prop="serviceCodePath">
              <a-input v-model="configForm.serviceCodePath" placeholder="输入后端代码路径" :maxLength="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="支持导入" prop="importFlag">
              <a-input v-model="configForm.importFlag" placeholder="输入是否支持导入 1支持 0不支持" :maxLength="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="支持导出" prop="exportFlag">
              <a-input v-model="configForm.exportFlag" placeholder="输入是否支持导出 1支持 0不支持" :maxLength="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
          </a-col>
        </a-row>
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
    import { STable, SelectAsyn } from '@/components'
    import { queryConfigList, createConfig, updateConfig, updateConfigStatus, batchDeleteConfig, deleteConfig, checkConfigExist } from '@/api/plugin/codeGenerator/config/config'
    import { queryDatasourceList } from '@/api/plugin/codeGenerator/datasource/datasource'
    import moment from 'moment'
    import { listDict } from '@/api/system/base/dict'
    const vm = {}
    export default {
        name: 'ConfigTable',
        components: { moment, STable, SelectAsyn },
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
            },
            formTypeFilter (formType) {
              return vm.formTypeDict.filterMap[formType]
            },
            tableTypeFilter (tableType) {
              return vm.tableTypeDict.filterMap[tableType]
            },
            tableShowTypeFilter (tableShowType) {
              return vm.tableShowTypeDict.filterMap[tableShowType]
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
                advanced: false,
                currentConfig: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                formTypeDict: {
                  dictCode: 'FORM_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                tableTypeDict: {
                  dictCode: 'TABLE_DATA_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                tableShowTypeDict: {
                  dictCode: 'TABLE_DATA_SHOW_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                listQuery: {
                    datasourceId: '',
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    formType: undefined,
                    tableType: undefined,
                    tableShowType: undefined,
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
                    importFlag: '',
                    exportFlag: ''
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
                        width: '230px',
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
                },
                // 获取数据源搜索的查询接口
                queryDatasourceListFunction: queryDatasourceList
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['configTree'].filter(val)
            // }
        },
        created () {
            const that = this
            this.getDataDictList(this.formTypeDict).then(function (result) {
              that.formTypeDict = result
            })
            this.getDataDictList(this.tableTypeDict).then(function (result) {
              that.tableTypeDict = result
            })
            this.getDataDictList(this.tableShowTypeDict).then(function (result) {
              that.tableShowTypeDict = result
            })
            this.getList()
        },
        methods: {
           async getDataDictList (dictParams) {
              const that = this
              that.listLoading = true
              await listDict(dictParams.dictCode).then(response => {
                dictParams.dictList = response.data
                dictParams.filterMap = {}
                dictParams.dictList.forEach((item, index, arr) => {
                  dictParams.filterMap[item.id] = item.dictName
                })
                that.listLoading = false
              })
              return dictParams
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
                        formType: undefined,
                        tableType: undefined,
                        tableShowType: undefined,
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
                    formType: undefined,
                    tableType: undefined,
                    tableShowType: undefined,
                    formItemCol: '',
                    leftTreeType: '',
                    frontCodePath: '',
                    serviceCodePath: '',
                    importFlag: '',
                    exportFlag: ''
                }
            },
            // 选中 option 调用
            clickSetListQuery (val) {
              this.listQuery.datasourceId = val
            },
            clickSetForm (val) {
              this.configForm.datasourceId = val
            },
            onSelectChange (selectedRowKeys, selectedRows) {
                this.selectedRowKeys = selectedRowKeys
                this.selectedRows = selectedRows
            },
            toggleAdvanced () {
                this.advanced = !this.advanced
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
