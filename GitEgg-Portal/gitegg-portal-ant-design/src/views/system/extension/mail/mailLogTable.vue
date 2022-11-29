<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="邮件主题" prop="mailSubject">
              <a-input
                v-model.trim="listMailLogQuery.mailSubject"
                placeholder="请输入邮件主题"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="发送人" prop="mailFrom">
              <a-input
                v-model.trim="listMailLogQuery.mailFrom"
                placeholder="请输入发送人"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="收件人" prop="mailTo">
              <a-input
                v-model.trim="listMailLogQuery.mailTo"
                placeholder="请输入收件人"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="邮件内容" prop="mailContent">
              <a-input
                v-model.trim="listMailLogQuery.mailContent"
                placeholder="请输入邮件内容"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listMailLogQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listMailLogQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <span class="table-page-search-submitButtons" :style=" { float: 'right', overflow: 'hidden' } ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetMailLogQuery">重置</a-button>
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
      ref="mailLogTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="mailLogPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
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
              <a href="javascript:;" @click="handleDelete(record)">删除</a>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <a-drawer :title="textMap[dialogStatus]"
              placement="right"
              :visible="dialogFormVisible"
              :width="700"
              :closable="false"
              :destroyOnClose="true"
              :maskClosable="false"
              @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="mailLogForm"
        :model="mailLogForm"
        :rules="rules"
        :label-col="mailLogLabelCol"
        :wrapper-col="mailLogWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="邮件渠道" prop="channelId">
              <a-input
                v-model.trim="mailLogForm.channelId"
                placeholder="请输入邮件渠道"
                :max-length="19" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="邮件模板" prop="templateId">
              <a-input
                v-model.trim="mailLogForm.templateId"
                placeholder="请输入邮件模板"
                :max-length="19" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="邮件主题" prop="mailSubject">
              <a-input
                v-model.trim="mailLogForm.mailSubject"
                placeholder="请输入邮件主题"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="发送人" prop="mailFrom">
              <a-input
                v-model.trim="mailLogForm.mailFrom"
                placeholder="请输入发送人"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="收件人" prop="mailTo">
              <a-textarea
                v-model.trim="mailLogForm.mailTo"
                placeholder="请输入收件人"
                :auto-size="{ minRows: 3, maxRows: 5 }" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="抄送" prop="mailCc">
              <a-input
                v-model.trim="mailLogForm.mailCc"
                placeholder="请输入抄送"
                :max-length="65535" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="密抄送" prop="mailBcc">
              <a-input
                v-model.trim="mailLogForm.mailBcc"
                placeholder="请输入密抄送"
                :max-length="65535" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="邮件内容" prop="mailContent">
              <a-textarea
                v-model.trim="mailLogForm.mailContent"
                placeholder="请输入邮件内容"
                :auto-size="{ minRows: 3, maxRows: 5 }" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="附件名称" prop="attachmentName">
              <a-input
                v-model.trim="mailLogForm.attachmentName"
                placeholder="请输入附件名称"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="附件大小" prop="attachmentSize">
              <a-input
                v-model.trim="mailLogForm.attachmentSize"
                placeholder="请输入附件大小"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="发送时间" prop="sendTime">
              <a-input
                v-model.trim="mailLogForm.sendTime"
                placeholder="请输入发送时间"
                :max-length="19" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="发送结果码" prop="sendResultCode">
              <a-input
                v-model.trim="mailLogForm.sendResultCode"
                placeholder="请输入发送结果码"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="发送结果消息" prop="sendResultMsg">
              <a-input
                v-model.trim="mailLogForm.sendResultMsg"
                placeholder="请输入发送结果消息"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
      <div class="footer-button">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</a-button>
        <a-button v-else type="primary" @click="updateData">修改</a-button>
      </div>
    </a-drawer>
  </a-card>
</template>
<script>
    import { STable } from '@/components'
    import { queryMailLogList, createMailLog, updateMailLog, deleteMailLog, batchDeleteMailLog, downloadMailLogList, uploadMailLog, downloadMailLogTemplate } from '@/api/system/extension/mail/mailLog'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    export default {
        name: 'MailLogTable',
        components: { moment, STable },
        filters: {
        },
        data () {
            return {
                advanced: false,
                currentMailLog: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listMailLogQuery: {
                    mailSubject: undefined, // 邮件主题
                    mailFrom: undefined, // 发送人
                    mailTo: undefined, // 收件人
                    mailContent: undefined, // 邮件内容
                    beginDateTime: '',
                    endDateTime: ''
                },
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                mailLogForm: {
                    channelId: undefined,
                    templateId: undefined,
                    mailSubject: undefined,
                    mailFrom: undefined,
                    mailTo: undefined,
                    mailCc: undefined,
                    mailBcc: undefined,
                    mailContent: undefined,
                    attachmentName: '0',
                    attachmentSize: '0',
                    sendTime: undefined,
                    sendResultCode: '1',
                    sendResultMsg: undefined
                },
                // 表头
                columns: [
                    {
                        title: '邮件渠道',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'channelId'
                    },
                    {
                        title: '邮件主题',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'mailSubject'
                    },
                    {
                        title: '发送人',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'mailFrom'
                    },
                    {
                        title: '收件人',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'mailTo'
                    },
                    {
                        title: '附件大小',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'attachmentSize'
                    },
                    {
                        title: '发送时间',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'sendTime'
                    },
                    {
                        title: '发送结果码',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'sendResultCode'
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
                    channelId: [
                        { required: true, message: '请输入邮件渠道', trigger: 'blur' }
                    ],
                    templateId: [
                    ],
                    mailSubject: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入邮件主题', trigger: 'blur' }
                    ],
                    mailFrom: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入发送人', trigger: 'blur' }
                    ],
                    mailTo: [
                        { min: 1, max: 65535, message: '长度在 1 到 65535 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入收件人', trigger: 'blur' }
                    ],
                    mailCc: [
                        { min: 1, max: 65535, message: '长度在 1 到 65535 个字符', trigger: 'blur', type: 'string' }
                    ],
                    mailBcc: [
                        { min: 1, max: 65535, message: '长度在 1 到 65535 个字符', trigger: 'blur', type: 'string' }
                    ],
                    mailContent: [
                        { min: 1, max: 65535, message: '长度在 1 到 65535 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入邮件内容', trigger: 'blur' }
                    ],
                    attachmentName: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入附件名称', trigger: 'blur' }
                    ],
                    attachmentSize: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    sendTime: [
                    ],
                    sendResultCode: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    sendResultMsg: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' }
                    ]
                },
                downloadLoading: false,
                mailLogLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                mailLogWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                mailLogPagination: {
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

        },
        created () {
             const that = this
                that.loadData = function (parameter) {
                    return queryMailLogList(Object.assign(parameter, that.listMailLogQuery))
                        .then(res => {
                            that.list = res.data.records
                            return res.data
                        })
                }
                that.$nextTick(() => {
                    that.handleFilter()
                })
        },
        methods: {
            resetMailLogQuery () {
                this.listMailLogQuery = {
                        mailSubject: undefined, // 邮件主题
                        mailFrom: undefined, // 发送人
                        mailTo: undefined, // 收件人
                        mailContent: undefined, // 邮件内容
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetMailLogForm () {
                this.mailLogForm = {
                    channelId: undefined, // 邮件渠道
                    templateId: undefined, // 邮件模板
                    mailSubject: undefined, // 邮件主题
                    mailFrom: undefined, // 发送人
                    mailTo: undefined, // 收件人
                    mailCc: undefined, // 抄送
                    mailBcc: undefined, // 密抄送
                    mailContent: undefined, // 邮件内容
                    attachmentName: '0', // 附件名称
                    attachmentSize: '0', // 附件大小
                    sendTime: undefined, // 发送时间
                    sendResultCode: '1', // 发送结果码
                    sendResultMsg: undefined // 发送结果消息
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
                queryMailLogList(this.listMailLogQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.mailLogTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.mailLogTable.refresh()
            },
            handleCreate () {
                this.resetMailLogForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['mailLogForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['mailLogForm'].validate(valid => {
                    if (valid) {
                        createMailLog(this.mailLogForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.mailLogForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['mailLogForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['mailLogForm'].validate(valid => {
                    if (valid) {
                        updateMailLog(this.mailLogForm).then(() => {
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
                        deleteMailLog(row.id).then(() => {
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
                var mailLogList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: mailLogList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteMailLog(that.selectedRowKeys).then(() => {
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
            handleDownload () {
                this.downloadLoading = true
                downloadMailLogList(this.listMailLogQuery).then(response => {
                    handleDownloadBlod('邮件记录数据列表.xlsx', response)
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
                uploadMailLog(formData).then(() => {
                    this.uploading = false
                    this.$message.success('邮件记录数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('邮件记录数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadMailLogTemplate(this.listMailLogQuery).then(response => {
                    handleDownloadBlod('邮件记录批量上传模板.xlsx', response)
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
<style lang="less" scoped>
  .footer-button {
    position: absolute;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }

  .footer-button button {
    margin-left: 10px;
  }
</style>
