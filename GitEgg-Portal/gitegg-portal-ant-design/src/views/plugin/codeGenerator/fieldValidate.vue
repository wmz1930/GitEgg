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
              <a-select v-model="listQuery.fieldValidateStatus" placeholder="状态" allow-clear show-search :filter-option="filterOption">
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
      ref="fieldValidateTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="fieldValidatePagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.fieldValidateStatus | statusFilter">{{ record.fieldValidateStatus | statusNameFilter }}</a-tag>
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
              <a href="javascript:;" v-if="record.fieldValidateStatus!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.fieldValidateStatus!='0' && record.fieldValidateStatus!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
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
        ref="fieldValidateForm"
        :model="fieldValidateForm"
        :rules="rules"
        :label-col="fieldValidateLabelCol"
        :wrapper-col="fieldValidateWrapperCol">
        <a-form-model-item label="主键" prop="id">
          <a-input v-model="fieldValidateForm.id" placeholder="输入主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字段主键" prop="fieldId">
          <a-input v-model="fieldValidateForm.fieldId" placeholder="输入字段主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字段类型" prop="validateType">
          <a-input v-model="fieldValidateForm.validateType" placeholder="输入字段类型" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="正则表达式校验规则" prop="validateRegular">
          <a-input v-model="fieldValidateForm.validateRegular" placeholder="输入正则表达式校验规则" :maxLength="32" />
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
    import { queryFieldValidateList, createFieldValidate, updateFieldValidate, updateFieldValidateStatus, batchDeleteFieldValidate, deleteFieldValidate, checkFieldValidateExist } from '@/api/plugin/codeGenerator/field/field_validate'
    import moment from 'moment'
    export default {
        name: 'FieldValidateTable',
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
            var validFieldValidate = (rule, value, callback) => {
                var keyData = {
                    id: this.fieldValidateForm.id,
                    checkField: 'fieldValidate', // 这里改为字段名称
                    checkValue: value
                }
                checkFieldValidateExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                advanced: false,
                currentFieldValidate: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listQuery: {
                    id: '',
                    fieldId: '',
                    validateType: '',
                    validateRegular: '',
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
                fieldValidateForm: {
                    id: '',
                    fieldId: '',
                    validateType: '',
                    validateRegular: ''
                },
                // 表头
                columns: [
                    {
                        title: '主键',
                        align: 'center',
                        dataIndex: 'id'
                    },
                    {
                        title: '字段主键',
                        align: 'center',
                        dataIndex: 'fieldId'
                    },
                    {
                        title: '字段类型',
                        align: 'center',
                        dataIndex: 'validateType'
                    },
                    {
                        title: '正则表达式校验规则',
                        align: 'center',
                        dataIndex: 'validateRegular'
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
                    fieldValidate: [
                        { required: true, message: '请输入fieldValidate', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
                        { validator: validFieldValidate, trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                fieldValidateLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                fieldValidateWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                fieldValidatePagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryFieldValidateList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                }
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['fieldValidateTree'].filter(val)
            // }
        },
        created () {
            this.getList()
        },
        methods: {
            resetQuery () {
                this.listQuery = {
                        id: '',
                        fieldId: '',
                        validateType: '',
                        validateRegular: '',
                        startDateTime: '',
                        endDateTime: ''
                }
            },
            resetFieldValidateForm () {
                this.fieldValidateForm = {
                    id: '',
                    fieldId: '',
                    validateType: '',
                    validateRegular: ''
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
                queryFieldValidateList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.fieldValidateTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.fieldValidateTable.refresh()
            },
            handleCreate () {
                this.resetFieldValidateForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['fieldValidateForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['fieldValidateForm'].validate(valid => {
                    if (valid) {
                        createFieldValidate(this.fieldValidateForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.fieldValidateForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['fieldValidateForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['fieldValidateForm'].validate(valid => {
                    if (valid) {
                        updateFieldValidate(this.fieldValidateForm).then(() => {
                            for (const v of this.list) {
                                if (v.id === this.fieldValidateForm.id) {
                                    const index = this.list.indexOf(v)
                                    this.list.splice(index, 1, this.fieldValidateForm)
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
                        deleteFieldValidate(row.id).then(() => {
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
                var fieldValidateList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: fieldValidateList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteFieldValidate(that.selectedRowKeys).then(() => {
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
                updateFieldValidateStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.fieldValidateStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                    const tHeader = [
                        '主键',
                        '字段主键',
                        '字段类型',
                        '正则表达式校验规则'
                    ]
                    const filterVal = [
                        'id',
                        'fieldId',
                        'validateType',
                        'validateRegular'
                    ]
                    const data = this.formatJson(filterVal, this.list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: '字段校验规则配置表数据导出列表'
                    })
                    this.downloadLoading = false
                })
            },
            formatJson (filterVal, jsonData) {
                return jsonData.map(v =>
                    filterVal.map(j => {
                        if (j === 'createTime') {
                            return moment(v[j])
                        } else if (j === 'fieldValidateStatus') {
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
