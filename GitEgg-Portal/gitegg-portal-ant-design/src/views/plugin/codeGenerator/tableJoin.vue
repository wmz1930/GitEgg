<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="序号">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="序号"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="状态">
              <a-select v-model="listQuery.tableJoinStatus" placeholder="状态" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in statusOption" :key="item.key" :value="item.key">
                  {{ item.label }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :md="6" :sm="24">
              <a-form-model-item label="开始时间">
                <a-date-picker v-model.trim="listQuery.startDateTime" placeholder="开始时间" valueFormat="YYYY-MM-DD" style="width:100%;"/>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="结束时间">
                <a-date-picker v-model.trim="listQuery.endDateTime" placeholder="结束时间" valueFormat="YYYY-MM-DD" style="width:100%;"/>
              </a-form-model-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24">
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
      <a-button type="primary" icon="cloud-download" @click="handleDownload" style="margin-left: 8px">导出</a-button>
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
      ref="tableJoinTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
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
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多 <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;" v-if="record.tableJoinStatus!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.tableJoinStatus!='0' && record.tableJoinStatus!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
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
        ref="tableJoinForm"
        :model="tableJoinForm"
        :rules="rules"
        :label-col="tableJoinLabelCol"
        :wrapper-col="tableJoinWrapperCol">
        <a-form-model-item label="主键" prop="id">
          <a-input v-model="tableJoinForm.id" placeholder="输入主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="代码生成主键" prop="generationId">
          <a-input v-model="tableJoinForm.generationId" placeholder="输入代码生成主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="数据源和主表一致" prop="datasourceId">
          <a-input v-model="tableJoinForm.datasourceId" placeholder="输入数据源和主表一致" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="表名" prop="joinTableName">
          <a-input v-model="tableJoinForm.joinTableName" placeholder="输入表名" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="表别名" prop="joinTableAlias">
          <a-input v-model="tableJoinForm.joinTableAlias" placeholder="输入表别名" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="表前缀" prop="joinTablePrefix">
          <a-input v-model="tableJoinForm.joinTablePrefix" placeholder="输入表前缀" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="left左连接 right右连接 inner等值连接 union联合查询" prop="joinTableType">
          <a-input v-model="tableJoinForm.joinTableType" placeholder="输入left左连接 right右连接 inner等值连接 union联合查询" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="自定义查询字段" prop="joinTableSelect">
          <a-input v-model="tableJoinForm.joinTableSelect" placeholder="输入自定义查询字段" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="自定义on条件" prop="joinTableOn">
          <a-input v-model="tableJoinForm.joinTableOn" placeholder="输入自定义on条件" :maxLength="32" />
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
    import { queryTableJoinList, createTableJoin, updateTableJoin, updateTableJoinStatus, batchDeleteTableJoin, deleteTableJoin, checkTableJoinExist } from '@/api/plugin/codeGenerator/join/table_join'
    import moment from 'moment'
    export default {
        name: 'TableJoinTable',
        components: { moment, STable },
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
                advanced: false,
                currentTableJoin: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listQuery: {
                    id: '',
                    generationId: '',
                    datasourceId: '',
                    joinTableName: '',
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    joinTableType: '',
                    joinTableSelect: '',
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
                    joinTableName: '',
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    joinTableType: '',
                    joinTableSelect: '',
                    joinTableOn: ''
                },
                // 表头
                columns: [
                    {
                        title: '主键',
                        align: 'center',
                        dataIndex: 'id'
                    },
                    {
                        title: '代码生成主键',
                        align: 'center',
                        dataIndex: 'generationId'
                    },
                    {
                        title: '数据源和主表一致',
                        align: 'center',
                        dataIndex: 'datasourceId'
                    },
                    {
                        title: '表名',
                        align: 'center',
                        dataIndex: 'joinTableName'
                    },
                    {
                        title: '表别名',
                        align: 'center',
                        dataIndex: 'joinTableAlias'
                    },
                    {
                        title: '表前缀',
                        align: 'center',
                        dataIndex: 'joinTablePrefix'
                    },
                    {
                        title: 'left左连接 right右连接 inner等值连接 union联合查询',
                        align: 'center',
                        dataIndex: 'joinTableType'
                    },
                    {
                        title: '自定义查询字段',
                        align: 'center',
                        dataIndex: 'joinTableSelect'
                    },
                    {
                        title: '自定义on条件',
                        align: 'center',
                        dataIndex: 'joinTableOn'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        width: '180px',
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
                    return queryTableJoinList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                }
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['tableJoinTree'].filter(val)
            // }
        },
        created () {
            this.getList()
        },
        methods: {
            resetQuery () {
                this.listQuery = {
                        id: '',
                        generationId: '',
                        datasourceId: '',
                        joinTableName: '',
                        joinTableAlias: '',
                        joinTablePrefix: '',
                        joinTableType: '',
                        joinTableSelect: '',
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
                    joinTableName: '',
                    joinTableAlias: '',
                    joinTablePrefix: '',
                    joinTableType: '',
                    joinTableSelect: '',
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
                this.listLoading = true
                queryTableJoinList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
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
                        createTableJoin(this.tableJoinForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.tableJoinForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['tableJoinForm'].clearValidate()
                })
            },
            updateData () {
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
            handleBatchDelete () {
                // 这里可以设置需要提示的信息，可以将id换为其他内容
                var tableJoinList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: tableJoinList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteTableJoin(that.selectedRowKeys).then(() => {
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
                updateTableJoinStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.tableJoinStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                    const tHeader = [
                        '主键',
                        '代码生成主键',
                        '数据源和主表一致',
                        '表名',
                        '表别名',
                        '表前缀',
                        'left左连接 right右连接 inner等值连接 union联合查询',
                        '自定义查询字段',
                        '自定义on条件'
                    ]
                    const filterVal = [
                        'id',
                        'generationId',
                        'datasourceId',
                        'joinTableName',
                        'joinTableAlias',
                        'joinTablePrefix',
                        'joinTableType',
                        'joinTableSelect',
                        'joinTableOn'
                    ]
                    const data = this.formatJson(filterVal, this.list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: '多表查询时的联合表配置数据导出列表'
                    })
                    this.downloadLoading = false
                })
            },
            formatJson (filterVal, jsonData) {
                return jsonData.map(v =>
                    filterVal.map(j => {
                        if (j === 'createTime') {
                            return moment(v[j])
                        } else if (j === 'tableJoinStatus') {
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
