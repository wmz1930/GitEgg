<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="校验名称" prop="validateName">
              <a-input
                v-model.trim="listValidateQuery.validateName"
                placeholder="请输入校验名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="!advanced && 6 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetValidateQuery">重置</a-button>
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
      <a-button type="primary" icon="cloud-download" @click="handleDownload" style="margin-left: 8px;">导出</a-button>
      <a-upload name="uploadFile" :show-upload-list="false" :before-upload="beforeUpload">
        <a-button> <a-icon type="upload" /> 导入 </a-button>
      </a-upload>
      <a href="javascript:;" @click="handleDownloadTemplate" style="margin-left: 8px;">下载导入模板</a>
    </div>
    <s-table
      ref="validateTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="validatePagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="statusStatus" slot-scope="text, record">
        {{ record.status | statusDictFilter }}
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
              <a href="javascript:;" v-if="record.status!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.status!='0' && record.status!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
              </a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" @click="handleDelete(record)">删除</a>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <a-modal :title="textMap[dialogStatus]"
             :maskClosable="false"
             :visible="dialogFormVisible"
             :width="800"
             @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="validateForm"
        :model="validateForm"
        :rules="rules"
        :label-col="validateLabelCol"
        :wrapper-col="validateWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="校验名称" prop="validateName">
              <a-input
                v-model.trim="validateForm.validateName"
                placeholder="校验名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="校验规则" prop="validateRegular">
              <a-input
                v-model.trim="validateForm.validateRegular"
                placeholder="校验规则"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="状态" prop="status">
              <a-radio-group v-model="validateForm.status"
                             name="validateForm.Radiostatus">
                <a-radio :key="item.id + index" v-for="(item,index) in statusDictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
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
    import { STable } from '@/components'
    import { queryValidateList, createValidate, updateValidate, updateValidateStatus, deleteValidate, checkValidateExist, batchDeleteValidate, downloadValidateList, uploadValidate, downloadValidateTemplate } from '@/api/plugin/codeGenerator/validate/validate'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { listDict } from '@/api/system/base/dict'
    let vm = {}
    export default {
        name: 'ValidateTable',
        components: { moment, STable },
        filters: {
            // 状态数据字典展示
            statusDictFilter (dictCode) {
                return vm.statusFilterMap[dictCode]
            }
        },
        data () {
            vm = this
            // 增加或更新记录时，判断字段是否已经存在
            var validValidateName = (rule, value, callback) => {
                var keyData = {
                    id: this.validateForm.id,
                    checkField: 'validate_name',
                    checkValue: value
                }
                checkValidateExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                advanced: false,
                currentValidate: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listValidateQuery: {
                    validateName: undefined, // 校验名称
                    status: '1', // 状态
                    beginDateTime: '',
                    endDateTime: ''
                },
                statusDictList: [], // 状态数据字典列表
                statusFilterMap: {},
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                validateForm: {
                    validateName: undefined,
                    validateRegular: undefined,
                    status: '1'
                },
                // 表头
                columns: [
                    {
                        title: '序号',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'id'
                    },
                    {
                        title: '校验名称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'validateName'
                    },
                    {
                        title: '校验规则',
                        align: 'center',
                        ellipsis: true,
                        dataIndex: 'validateRegular'
                    },
                    {
                        title: '状态',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'statusStatus' },
                        dataIndex: 'status'
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'createTime'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        width: 180,
                        fixed: 'right',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                rules: {
                    // 字段校验
                    id: [
                        { min: 1, max: 19, message: '长度在 1 到 19 个字符', trigger: 'blur' }
                    ],
                    tenantId: [
                        { min: 1, max: 19, message: '长度在 1 到 19 个字符', trigger: 'blur' }
                    ],
                    validateName: [
                        { validator: validValidateName, trigger: 'blur' },
                        { required: true, message: '请输入校验名称', trigger: 'blur' },
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }
                    ],
                    validateRegular: [
                        { required: true, message: '请输入校验规则', trigger: 'blur' },
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur' }
                    ],
                    status: [
                        { required: true, message: '请输入状态', trigger: 'blur' },
                        { min: 1, max: 3, message: '长度在 1 到 3 个字符', trigger: 'blur' }
                    ],
                    createTime: [
                        { min: 1, max: 19, message: '长度在 1 到 19 个字符', trigger: 'blur' }
                    ],
                    creator: [
                        { min: 1, max: 19, message: '长度在 1 到 19 个字符', trigger: 'blur' }
                    ],
                    updateTime: [
                        { min: 1, max: 19, message: '长度在 1 到 19 个字符', trigger: 'blur' }
                    ],
                    operator: [
                        { min: 1, max: 19, message: '长度在 1 到 19 个字符', trigger: 'blur' }
                    ],
                    delFlag: [
                        { min: 1, max: 3, message: '长度在 1 到 3 个字符', trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                validateLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                validateWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                validatePagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryValidateList(Object.assign(parameter, this.listValidateQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                }
            }
        },
        watch: {

        },
        created () {
            this.querystatusDictList()
        },
        methods: {
            querystatusDictList () {
                const that = this
                that.listLoading = true
                listDict('ENABLE_OR_NOT').then(response => {
                    this.statusDictList = response.data
                    this.statusFilterMap = {}
                    this.statusDictList.forEach((item, index, arr) => {
                        this.statusFilterMap[item.dictCode] = item.dictName
                    })
                    that.listLoading = false
                })
            },
            resetValidateQuery () {
                this.listValidateQuery = {
                        validateName: undefined, // 校验名称
                        status: '1', // 状态
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetValidateForm () {
                this.validateForm = {
                    validateName: undefined, // 校验名称
                    validateRegular: undefined, // 校验规则
                    status: '1' // 状态
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
                queryValidateList(this.listValidateQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.validateTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.validateTable.refresh()
            },
            handleCreate () {
                this.resetValidateForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['validateForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['validateForm'].validate(valid => {
                    if (valid) {
                        createValidate(this.validateForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.validateForm = Object.assign({}, row) // copy obj
                this.validateForm.status = row.status + ''
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['validateForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['validateForm'].validate(valid => {
                    if (valid) {
                        updateValidate(this.validateForm).then(() => {
                            this.handleTableRefresh()
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
                        deleteValidate(row.id).then(() => {
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
                var validateList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: validateList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteValidate(that.selectedRowKeys).then(() => {
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
                updateValidateStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.status = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadValidateList(this.listValidateQuery).then(response => {
                    handleDownloadBlod('字段校验规则配置表数据列表.xlsx', response)
                    this.listLoading = false
                })
            },
            beforeUpload (file) {
                this.handleUpload(file)
                return false
            },
            handleUpload (file) {
                this.uploadedFileName = ''
                const formData = new FormData()
                formData.append('uploadFile', file)
                this.uploading = true
                uploadValidate(formData).then(() => {
                    this.uploading = false
                    this.$message.success('字段校验规则配置表数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('字段校验规则配置表数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadValidateTemplate(this.listValidateQuery).then(response => {
                    handleDownloadBlod('字段校验规则配置表批量上传模板.xlsx', response)
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
