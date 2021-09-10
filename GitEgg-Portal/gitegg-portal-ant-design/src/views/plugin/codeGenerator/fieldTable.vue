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
              <a-select v-model="listQuery.fieldStatus" placeholder="状态" allow-clear show-search :filter-option="filterOption">
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
      ref="fieldTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="fieldPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.fieldStatus | statusFilter">{{ record.fieldStatus | statusNameFilter }}</a-tag>
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
              <a href="javascript:;" v-if="record.fieldStatus!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.fieldStatus!='0' && record.fieldStatus!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
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
        ref="fieldForm"
        :model="fieldForm"
        :rules="rules"
        :label-col="fieldLabelCol"
        :wrapper-col="fieldWrapperCol">
        <a-form-model-item label="主键" prop="id">
          <a-input v-model="fieldForm.id" placeholder="输入主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="代码生成主键" prop="generationId">
          <a-input v-model="fieldForm.generationId" placeholder="输入代码生成主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="关联表主键" prop="joinId">
          <a-input v-model="fieldForm.joinId" placeholder="输入关联表主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字段名称" prop="fieldName">
          <a-input v-model="fieldForm.fieldName" placeholder="输入字段名称" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字段类型" prop="fieldType">
          <a-input v-model="fieldForm.fieldType" placeholder="输入字段类型" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字段描述" prop="comment">
          <a-input v-model="fieldForm.comment" placeholder="输入字段描述" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="实体类型" prop="entityType">
          <a-input v-model="fieldForm.entityType" placeholder="输入实体类型" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="实体名称" prop="entityName">
          <a-input v-model="fieldForm.entityName" placeholder="输入实体名称" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="表单新增" prop="formAdd">
          <a-input v-model="fieldForm.formAdd" placeholder="输入表单新增" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="表单编辑" prop="formEdit">
          <a-input v-model="fieldForm.formEdit" placeholder="输入表单编辑" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="查询条件" prop="queryTerm">
          <a-input v-model="fieldForm.queryTerm" placeholder="输入查询条件" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="列表展示" prop="listShow">
          <a-input v-model="fieldForm.listShow" placeholder="输入列表展示" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="是否支持导入 1支持 0不支持" prop="importFlag">
          <a-input v-model="fieldForm.importFlag" placeholder="输入是否支持导入 1支持 0不支持" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="是否支持导出 1支持 0不支持" prop="exportFlag">
          <a-input v-model="fieldForm.exportFlag" placeholder="输入是否支持导出 1支持 0不支持" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="是否必填" prop="required">
          <a-input v-model="fieldForm.required" placeholder="输入是否必填" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="是否唯一" prop="fieldUnique">
          <a-input v-model="fieldForm.fieldUnique" placeholder="输入是否唯一" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="查询类型" prop="queryType">
          <a-input v-model="fieldForm.queryType" placeholder="输入查询类型" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="组件类型" prop="controlType">
          <a-input v-model="fieldForm.controlType" placeholder="输入组件类型" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字典编码" prop="dictCode">
          <a-input v-model="fieldForm.dictCode" placeholder="输入字典编码" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="最小值" prop="min">
          <a-input v-model="fieldForm.min" placeholder="输入最小值" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="最大值" prop="max">
          <a-input v-model="fieldForm.max" placeholder="输入最大值" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="最小长度" prop="minLength">
          <a-input v-model="fieldForm.minLength" placeholder="输入最小长度" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字段最大长度" prop="maxLength">
          <a-input v-model="fieldForm.maxLength" placeholder="输入字段最大长度" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="显示排序" prop="fieldSort">
          <a-input v-model="fieldForm.fieldSort" placeholder="输入显示排序" :maxLength="32" />
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
    import { queryFieldList, createField, updateField, updateFieldStatus, batchDeleteField, deleteField, checkFieldExist } from '@/api/plugin/codeGenerator/field/field'
    import moment from 'moment'
    export default {
        name: 'FieldTable',
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
            var validField = (rule, value, callback) => {
                var keyData = {
                    id: this.fieldForm.id,
                    checkField: 'field', // 这里改为字段名称
                    checkValue: value
                }
                checkFieldExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                advanced: false,
                currentField: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listQuery: {
                    id: '',
                    generationId: '',
                    joinId: '',
                    fieldName: '',
                    fieldType: '',
                    comment: '',
                    entityType: '',
                    entityName: '',
                    formAdd: '',
                    formEdit: '',
                    queryTerm: '',
                    listShow: '',
                    importFlag: '',
                    exportFlag: '',
                    required: '',
                    fieldUnique: '',
                    queryType: '',
                    controlType: '',
                    dictCode: '',
                    min: '',
                    max: '',
                    minLength: '',
                    maxLength: '',
                    fieldSort: '',
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
                fieldForm: {
                    id: '',
                    generationId: '',
                    joinId: '',
                    fieldName: '',
                    fieldType: '',
                    comment: '',
                    entityType: '',
                    entityName: '',
                    formAdd: '',
                    formEdit: '',
                    queryTerm: '',
                    listShow: '',
                    importFlag: '',
                    exportFlag: '',
                    required: '',
                    fieldUnique: '',
                    queryType: '',
                    controlType: '',
                    dictCode: '',
                    min: '',
                    max: '',
                    minLength: '',
                    maxLength: '',
                    fieldSort: ''
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
                        title: '关联表主键',
                        align: 'center',
                        dataIndex: 'joinId'
                    },
                    {
                        title: '字段名称',
                        align: 'center',
                        dataIndex: 'fieldName'
                    },
                    {
                        title: '字段类型',
                        align: 'center',
                        dataIndex: 'fieldType'
                    },
                    {
                        title: '字段描述',
                        align: 'center',
                        dataIndex: 'comment'
                    },
                    {
                        title: '实体类型',
                        align: 'center',
                        dataIndex: 'entityType'
                    },
                    {
                        title: '实体名称',
                        align: 'center',
                        dataIndex: 'entityName'
                    },
                    {
                        title: '表单新增',
                        align: 'center',
                        dataIndex: 'formAdd'
                    },
                    {
                        title: '表单编辑',
                        align: 'center',
                        dataIndex: 'formEdit'
                    },
                    {
                        title: '查询条件',
                        align: 'center',
                        dataIndex: 'queryTerm'
                    },
                    {
                        title: '列表展示',
                        align: 'center',
                        dataIndex: 'listShow'
                    },
                    {
                        title: '是否支持导入 1支持 0不支持',
                        align: 'center',
                        dataIndex: 'importFlag'
                    },
                    {
                        title: '是否支持导出 1支持 0不支持',
                        align: 'center',
                        dataIndex: 'exportFlag'
                    },
                    {
                        title: '是否必填',
                        align: 'center',
                        dataIndex: 'required'
                    },
                    {
                        title: '是否唯一',
                        align: 'center',
                        dataIndex: 'fieldUnique'
                    },
                    {
                        title: '查询类型',
                        align: 'center',
                        dataIndex: 'queryType'
                    },
                    {
                        title: '组件类型',
                        align: 'center',
                        dataIndex: 'controlType'
                    },
                    {
                        title: '字典编码',
                        align: 'center',
                        dataIndex: 'dictCode'
                    },
                    {
                        title: '最小值',
                        align: 'center',
                        dataIndex: 'min'
                    },
                    {
                        title: '最大值',
                        align: 'center',
                        dataIndex: 'max'
                    },
                    {
                        title: '最小长度',
                        align: 'center',
                        dataIndex: 'minLength'
                    },
                    {
                        title: '字段最大长度',
                        align: 'center',
                        dataIndex: 'maxLength'
                    },
                    {
                        title: '显示排序',
                        align: 'center',
                        dataIndex: 'fieldSort'
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
                    field: [
                        { required: true, message: '请输入field', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
                        { validator: validField, trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                fieldLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                fieldWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                fieldPagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryFieldList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                }
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['fieldTree'].filter(val)
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
                        joinId: '',
                        fieldName: '',
                        fieldType: '',
                        comment: '',
                        entityType: '',
                        entityName: '',
                        formAdd: '',
                        formEdit: '',
                        queryTerm: '',
                        listShow: '',
                        importFlag: '',
                        exportFlag: '',
                        required: '',
                        fieldUnique: '',
                        queryType: '',
                        controlType: '',
                        dictCode: '',
                        min: '',
                        max: '',
                        minLength: '',
                        maxLength: '',
                        fieldSort: '',
                        startDateTime: '',
                        endDateTime: ''
                }
            },
            resetFieldForm () {
                this.fieldForm = {
                    id: '',
                    generationId: '',
                    joinId: '',
                    fieldName: '',
                    fieldType: '',
                    comment: '',
                    entityType: '',
                    entityName: '',
                    formAdd: '',
                    formEdit: '',
                    queryTerm: '',
                    listShow: '',
                    importFlag: '',
                    exportFlag: '',
                    required: '',
                    fieldUnique: '',
                    queryType: '',
                    controlType: '',
                    dictCode: '',
                    min: '',
                    max: '',
                    minLength: '',
                    maxLength: '',
                    fieldSort: ''
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
                queryFieldList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.fieldTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.fieldTable.refresh()
            },
            handleCreate () {
                this.resetFieldForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['fieldForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['fieldForm'].validate(valid => {
                    if (valid) {
                        createField(this.fieldForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.fieldForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['fieldForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['fieldForm'].validate(valid => {
                    if (valid) {
                        updateField(this.fieldForm).then(() => {
                            for (const v of this.list) {
                                if (v.id === this.fieldForm.id) {
                                    const index = this.list.indexOf(v)
                                    this.list.splice(index, 1, this.fieldForm)
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
                        deleteField(row.id).then(() => {
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
                var fieldList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: fieldList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteField(that.selectedRowKeys).then(() => {
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
                updateFieldStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.fieldStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                    const tHeader = [
                        '主键',
                        '代码生成主键',
                        '关联表主键',
                        '字段名称',
                        '字段类型',
                        '字段描述',
                        '实体类型',
                        '实体名称',
                        '表单新增',
                        '表单编辑',
                        '查询条件',
                        '列表展示',
                        '是否支持导入 1支持 0不支持',
                        '是否支持导出 1支持 0不支持',
                        '是否必填',
                        '是否唯一',
                        '查询类型',
                        '组件类型',
                        '字典编码',
                        '最小值',
                        '最大值',
                        '最小长度',
                        '字段最大长度',
                        '显示排序'
                    ]
                    const filterVal = [
                        'id',
                        'generationId',
                        'joinId',
                        'fieldName',
                        'fieldType',
                        'comment',
                        'entityType',
                        'entityName',
                        'formAdd',
                        'formEdit',
                        'queryTerm',
                        'listShow',
                        'importFlag',
                        'exportFlag',
                        'required',
                        'fieldUnique',
                        'queryType',
                        'controlType',
                        'dictCode',
                        'min',
                        'max',
                        'minLength',
                        'maxLength',
                        'fieldSort'
                    ]
                    const data = this.formatJson(filterVal, this.list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: '字段属性配置表数据导出列表'
                    })
                    this.downloadLoading = false
                })
            },
            formatJson (filterVal, jsonData) {
                return jsonData.map(v =>
                    filterVal.map(j => {
                        if (j === 'createTime') {
                            return moment(v[j])
                        } else if (j === 'fieldStatus') {
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
