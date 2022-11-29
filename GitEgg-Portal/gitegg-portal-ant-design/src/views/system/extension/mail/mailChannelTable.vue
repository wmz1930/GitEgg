<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="渠道编码" prop="channelCode">
              <a-input
                v-model.trim="listMailChannelQuery.channelCode"
                placeholder="请输入渠道编码"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="渠道名称" prop="channelName">
              <a-input
                v-model.trim="listMailChannelQuery.channelName"
                placeholder="请输入渠道名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="账户名" prop="username">
              <a-input
                v-model.trim="listMailChannelQuery.username"
                placeholder="请输入账户名"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="渠道状态" prop="channelStatus">
              <a-select v-model.trim="listMailChannelQuery.channelStatus"
                        placeholder="渠道状态"
                        show-search
                        mode="default"
                        allow-clear
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in channelStatusDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listMailChannelQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listMailChannelQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetMailChannelQuery">重置</a-button>
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

      <a-button type="dashed" icon="mail" @click="handleSendMailTest" style="float: right;" >测试发送系统邮件配置</a-button>
    </div>
    <s-table
      ref="mailChannelTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="mailChannelPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="channelStatusSlot" slot-scope="text, record">
        {{ record.channelStatus | channelStatusDictFilter }}
      </span>
      <span slot="createTime" slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handleUpdate(record)">编辑</a>
        <a-divider type="vertical" />
        <a @click="handleSendMailTest(record)">测试</a>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多 <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;" v-if="record.channelStatus!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.channelStatus!='0' && record.channelStatus!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
              </a>
            </a-menu-item>
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
        ref="mailChannelForm"
        :model="mailChannelForm"
        :rules="rules"
        :label-col="mailChannelLabelCol"
        :wrapper-col="mailChannelWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="渠道编码" prop="channelCode">
              <a-input
                v-model.trim="mailChannelForm.channelCode"
                placeholder="请输入渠道编码"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="渠道名称" prop="channelName">
              <a-input
                v-model.trim="mailChannelForm.channelName"
                placeholder="请输入渠道名称"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="SMTP地址" prop="host">
              <a-input
                v-model.trim="mailChannelForm.host"
                placeholder="请输入SMTP地址"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="SMTP端口" prop="port">
              <a-input
                v-model.trim="mailChannelForm.port"
                placeholder="请输入SMTP端口"
                :max-length="10" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="账户名" prop="username">
              <a-input
                v-model.trim="mailChannelForm.username"
                placeholder="请输入账户名"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="密码" prop="password">
              <a-input
                v-model.trim="mailChannelForm.password"
                placeholder="请输入密码"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="协议" prop="protocol">
              <a-input
                v-model.trim="mailChannelForm.protocol"
                placeholder="请输入协议"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="默认编码" prop="defaultEncoding">
              <a-input
                v-model.trim="mailChannelForm.defaultEncoding"
                placeholder="请输入默认编码"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="会话JNDI" prop="jndiName">
              <a-input
                v-model.trim="mailChannelForm.jndiName"
                placeholder="请输入会话JNDI"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="JavaMail配置" prop="properties">
              <a-alert message="遵循Properties格式配置，从mail.开始配置。" type="info" close-text="关闭提示" />
              <a-textarea
                v-model.trim="mailChannelForm.properties"
                placeholder="请输入JavaMail配置：mail.smtp.auth: true"
                :auto-size="{ minRows: 3, maxRows: 5 }" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="渠道状态" prop="channelStatus">
              <a-radio-group v-model="mailChannelForm.channelStatus"
                             name="mailChannelForm.RadiochannelStatus">
                <a-radio :key="item.id + index" v-for="(item,index) in channelStatusDict.dictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="描述" prop="comments">
              <a-textarea
                v-model.trim="mailChannelForm.comments"
                placeholder="请输入描述"
                :auto-size="{ minRows: 3, maxRows: 5 }" />
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
    <!--邮件发送测试-->
    <a-modal title="邮件发送测试" :maskClosable="false" :visible="dialogTestSendMailVisible" :width="800" @cancel="() => dialogTestSendMailVisible = false">
      <a-form-model
        ref="sendMailForm"
        :model="sendMailForm"
        :rules="sendMailRules"
        :label-col="mailTestLabelCol"
        :wrapper-col="mailTestWrapperCol">
        <a-form-model-item has-feedback label="收件人" prop="mailTo">
          <a-input v-model="sendMailForm.mailTo" type="text" autocomplete="off" placeholder="请输入收件人邮箱地址" />
        </a-form-model-item>
        <a-form-model-item has-feedback label="主题" prop="mailSubject">
          <a-input v-model="sendMailForm.mailSubject" type="text" autocomplete="off" placeholder="请输入邮件主题" />
        </a-form-model-item>
        <a-form-model-item has-feedback label="内容" prop="mailContent">
          <a-textarea v-model="sendMailForm.mailContent" type="text" autocomplete="off" placeholder="请输入邮件内容" />
        </a-form-model-item>
      </a-form-model>
      <div slot="footer" class="dialog-footer">
        <a-button @click="dialogTestSendMailVisible = false">取消</a-button>
        <a-button style="float: right;" type="primary" @click="testSendSimple" :loading="sendLoading"> <a-icon type="mail" /> 立即发送 </a-button>
      </div>
    </a-modal>
  </a-card>
</template>
<script>
    import { STable } from '@/components'
    import { queryMailChannelList, createMailChannel, updateMailChannel, updateMailChannelStatus, deleteMailChannel, batchDeleteMailChannel, downloadMailChannelList, uploadMailChannel, downloadMailChannelTemplate } from '@/api/system/extension/mail/mailChannel'
    import { testSendSimpleMail } from '@/api/system/extension/mail/sendMailTest'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { batchListDictBusiness } from '@/api/system/base/dictBusiness'
    let vm = {}
    export default {
        name: 'MailChannelTable',
        components: { moment, STable },
        filters: {
            // 渠道状态数据字典展示
            channelStatusDictFilter (dictCode) {
                return vm.channelStatusDict.filterMap[dictCode]
            }
        },
        data () {
            vm = this
            return {
                advanced: false,
                currentMailChannel: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                sendLoading: false,
                listMailChannelQuery: {
                    channelCode: undefined, // 渠道编码
                    channelName: undefined, // 渠道名称
                    username: undefined, // 账户名
                    channelStatus: undefined, // 渠道状态
                    beginDateTime: '',
                    endDateTime: ''
                },
                channelStatusDict: {
                    dictCode: 'ENABLE_OR_NOT',
                    dictList: [],
                    filterMap: {}
                }, // 渠道状态数据字典列表
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                mailChannelForm: {
                    channelCode: undefined,
                    channelName: undefined,
                    host: undefined,
                    port: undefined,
                    username: undefined,
                    password: undefined,
                    protocol: 'smtp',
                    defaultEncoding: undefined,
                    jndiName: undefined,
                    properties: undefined,
                    channelStatus: '0',
                    comments: undefined
                },
                // 表头
                columns: [
                    {
                        title: '渠道编码',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'channelCode'
                    },
                    {
                        title: '渠道名称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'channelName'
                    },
                    {
                        title: 'SMTP地址',
                        align: 'center',
                        ellipsis: true,
                        dataIndex: 'host'
                    },
                    {
                        title: 'SMTP端口',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'port'
                    },
                    {
                        title: '渠道状态',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'channelStatusSlot' },
                        dataIndex: 'channelStatus'
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
                    channelCode: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入渠道编码', trigger: 'blur' }
                    ],
                    channelName: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入渠道名称', trigger: 'blur' }
                    ],
                    host: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入SMTP地址', trigger: 'blur' }
                    ],
                    port: [
                        { required: true, message: '请输入SMTP端口', trigger: 'blur' }
                    ],
                    username: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入账户名', trigger: 'blur' }
                    ],
                    password: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                    protocol: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    defaultEncoding: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    jndiName: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    properties: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' }
                    ],
                    channelStatus: [
                        { required: true, message: '请输入渠道状态', trigger: 'blur' }
                    ],
                    comments: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ]
                },
                dialogTestSendMailVisible: false,
                sendMailForm: {
                    channelCode: undefined,
                    templateId: undefined,
                    mailSubject: 'GitEgg系统邮件发送测试',
                    mailFrom: undefined,
                    mailTo: undefined,
                    mailCc: undefined,
                    mailBcc: undefined,
                    mailContent: 'GitEgg系统邮件发送测试内容',
                    attachmentName: '0',
                    attachmentSize: '0'
                },
                sendMailRules: {
                    mailTo: [
                        { type: 'email', required: true, message: '请输入正确的邮箱地址', trigger: 'blur' },
                        { required: true, message: '请输入收件人', trigger: 'blur' }
                    ],
                    mailSubject: [
                        { required: true, message: '请输入主题', trigger: 'blur' }
                    ],
                    mailContent: [
                        { required: true, message: '请输入邮件内容', trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                mailChannelLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                mailChannelWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                mailTestLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                mailTestWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                mailChannelPagination: {
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
            const dictList = [that.channelStatusDict]
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
                that.loadData = function (parameter) {
                    return queryMailChannelList(Object.assign(parameter, that.listMailChannelQuery))
                        .then(res => {
                            that.list = res.data.records
                            return res.data
                        })
                }
                that.$nextTick(() => {
                    that.handleFilter()
                })
            })
        },
        methods: {
            async getBatchDataDictList (dictParams) {
                const that = this
                let result = {}
                that.listLoading = true
                await batchListDictBusiness(dictParams).then(response => {
                    result = response.data
                    that.listLoading = false
                })
                return result
            },
            resetMailChannelQuery () {
                this.listMailChannelQuery = {
                        channelCode: undefined, // 渠道编码
                        channelName: undefined, // 渠道名称
                        username: undefined, // 账户名
                        channelStatus: undefined, // 渠道状态
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetMailChannelForm () {
                this.mailChannelForm = {
                    channelCode: undefined, // 渠道编码
                    channelName: undefined, // 渠道名称
                    host: undefined, // SMTP地址
                    port: undefined, // SMTP端口
                    username: undefined, // 账户名
                    password: undefined, // 密码
                    protocol: 'smtp', // 协议
                    defaultEncoding: undefined, // 默认编码
                    jndiName: undefined, // 会话JNDI
                    properties: undefined, // JavaMail配置
                    channelStatus: '0', // 渠道状态
                    comments: undefined // 描述
                }
            },
            resetSendMailForm () {
              this.sendMailForm = {
                    channelCode: undefined,
                    templateId: undefined,
                    mailSubject: 'GitEgg系统邮件发送测试',
                    mailFrom: undefined,
                    mailTo: undefined,
                    mailCc: undefined,
                    mailBcc: undefined,
                    mailContent: 'GitEgg系统邮件发送测试内容',
                    attachmentName: '0',
                    attachmentSize: '0'
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
                queryMailChannelList(this.listMailChannelQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
           handleSendMailTest (row) {
                this.resetSendMailForm()
                this.sendLoading = false
                if (row && row.channelCode) {
                    this.sendMailForm.channelCode = row.channelCode
                }
                this.dialogTestSendMailVisible = true
            },
            sendMailTestChange () {
                this.resetSendMailForm()
            },
            handleFilter () {
                this.$refs.mailChannelTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.mailChannelTable.refresh()
            },
            handleCreate () {
                this.resetMailChannelForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['mailChannelForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['mailChannelForm'].validate(valid => {
                    if (valid) {
                        createMailChannel(this.mailChannelForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.mailChannelForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.mailChannelForm.channelStatus = row.channelStatus + ''
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['mailChannelForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['mailChannelForm'].validate(valid => {
                    if (valid) {
                        updateMailChannel(this.mailChannelForm).then(() => {
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
                        deleteMailChannel(row.id).then(() => {
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
                var mailChannelList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: mailChannelList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteMailChannel(that.selectedRowKeys).then(() => {
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
                updateMailChannelStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.channelStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadMailChannelList(this.listMailChannelQuery).then(response => {
                    handleDownloadBlod('短信渠道表数据列表.xlsx', response)
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
                uploadMailChannel(formData).then(() => {
                    this.uploading = false
                    this.$message.success('短信渠道表数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('短信渠道表数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadMailChannelTemplate(this.listMailChannelQuery).then(response => {
                    handleDownloadBlod('短信渠道表批量上传模板.xlsx', response)
                    this.listLoading = false
                })
            },
            filterOption (input, option) {
              return (
                      option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
              )
            },
            testSendSimple () {
                this.$refs['sendMailForm'].validate(valid => {
                    if (valid) {
                        this.sendLoading = true
                        testSendSimpleMail(this.sendMailForm).then(() => {
                            this.sendLoading = false
                            this.dialogTestSendMailVisible = false
                            this.$message.success('发送成功')
                        })
                    }
                })
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
