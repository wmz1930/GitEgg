<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="模板编码" prop="templateCode">
              <a-input
                v-model.trim="listMailTemplateQuery.templateCode"
                placeholder="请输入模板编码"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="模板名称" prop="templateName">
              <a-input
                v-model.trim="listMailTemplateQuery.templateName"
                placeholder="请输入模板名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="模板状态" prop="templateStatus">
              <a-select v-model.trim="listMailTemplateQuery.templateStatus"
                        placeholder="模板状态"
                        show-search
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in templateStatusDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="模板类型" prop="templateType">
              <a-select v-model.trim="listMailTemplateQuery.templateType"
                        placeholder="模板类型"
                        show-search
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in templateTypeDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listMailTemplateQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listMailTemplateQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>

          <a-col :md="12" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetMailTemplateQuery">重置</a-button>
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
      ref="mailTemplateTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="mailTemplatePagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="templateStatusSlot" slot-scope="text, record">
        {{ record.templateStatus | templateStatusDictFilter }}
      </span>
      <span slot="templateTypeSlot" slot-scope="text, record">
        {{ record.templateType | templateTypeDictFilter }}
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
              <a href="javascript:;" v-if="record.templateStatus!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.templateStatus!='0' && record.templateStatus!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
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
        ref="mailTemplateForm"
        :model="mailTemplateForm"
        :rules="rules"
        :label-col="mailTemplateLabelCol"
        :wrapper-col="mailTemplateWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板编码" prop="templateCode">
              <a-input
                v-model.trim="mailTemplateForm.templateCode"
                placeholder="请输入模板编码"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板名称" prop="templateName">
              <a-input
                v-model.trim="mailTemplateForm.templateName"
                placeholder="请输入模板名称"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板签名" prop="signName">
              <a-input
                v-model.trim="mailTemplateForm.signName"
                placeholder="请输入模板签名"
                :max-length="64" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板状态" prop="templateStatus">
              <a-radio-group v-model="mailTemplateForm.templateStatus"
                             name="mailTemplateForm.RadiotemplateStatus">
                <a-radio :key="item.id + index" v-for="(item,index) in templateStatusDict.dictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板类型" prop="templateType">
              <a-select
                v-model.trim="mailTemplateForm.templateType"
                placeholder="模板类型"
                show-search
                mode="default"
                :filter-option="filterOption" >
                <a-select-option v-for="item in templateTypeDict.dictList"
                                 :key="item.dictCode"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板内容" prop="templateContent">
              <a-textarea
                v-model.trim="mailTemplateForm.templateContent"
                placeholder="请输入模板内容"
                :auto-size="{ minRows: 3, maxRows: 5 }" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-show="mailTemplateForm.templateType === '2'">
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存key" prop="cacheCodeKey">
              <a-input
                v-model.trim="mailTemplateForm.cacheCodeKey"
                placeholder="请输入缓存key"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存有效期" prop="cacheTimeOut">
              <a-input-number
                v-model.trim="mailTemplateForm.cacheTimeOut"
                placeholder="缓存有效期"
                style="width:100%;"
                :min="0"
                :max="9223372036854775807" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-show="mailTemplateForm.templateType === '2'">
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存有效期单位" prop="cacheTimeOutUnit">
              <a-input
                v-model.trim="mailTemplateForm.cacheTimeOutUnit"
                placeholder="请输入缓存有效期单位"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="发送次数限制" prop="sendTimesLimit">
              <a-input-number
                v-model.trim="mailTemplateForm.sendTimesLimit"
                placeholder="发送次数限制"
                style="width:100%;"
                :min="0"
                :max="9223372036854775807" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-show="mailTemplateForm.templateType === '2'">
          <a-col :md="24" :sm="24">
            <a-form-model-item label="限制时间间隔" prop="sendTimesLimitPeriod">
              <a-input-number
                v-model.trim="mailTemplateForm.sendTimesLimitPeriod"
                placeholder="限制时间间隔"
                style="width:100%;"
                :min="0"
                :max="9223372036854775807" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="限制时间间隔单位" prop="sendTimesLimitPeriodUnit">
              <a-input
                v-model.trim="mailTemplateForm.sendTimesLimitPeriodUnit"
                placeholder="请输入限制时间间隔单位"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="描述" prop="comments">
              <a-textarea
                v-model.trim="mailTemplateForm.comments"
                placeholder="请输入描述"
                :max-length="255" />
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
    import { queryMailTemplateList, createMailTemplate, updateMailTemplate, updateMailTemplateStatus, deleteMailTemplate, batchDeleteMailTemplate, downloadMailTemplateList, uploadMailTemplate, downloadMailTemplateTemplate } from '@/api/system/extension/mail/mailTemplate'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { batchListDict } from '@/api/system/base/dict'
    let vm = {}
    export default {
        name: 'MailTemplateTable',
        components: { moment, STable },
        filters: {
            // 模板状态数据字典展示
            templateStatusDictFilter (dictCode) {
                return vm.templateStatusDict.filterMap[dictCode]
            },
            // 模板类型数据字典展示
            templateTypeDictFilter (dictCode) {
                return vm.templateTypeDict.filterMap[dictCode]
            }
        },
        data () {
            vm = this
            return {
                advanced: false,
                currentMailTemplate: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listMailTemplateQuery: {
                    templateCode: undefined, // 模板编码
                    templateName: undefined, // 模板名称
                    templateStatus: undefined, // 模板状态
                    templateType: undefined, // 模板类型
                    beginDateTime: '',
                    endDateTime: ''
                },
                templateStatusDict: {
                    dictCode: 'ENABLE_OR_NOT',
                    dictList: [],
                    filterMap: {}
                }, // 模板状态数据字典列表
                templateTypeDict: {
                    dictCode: 'MAIL_TEMPLATE_TYPE',
                    dictList: [],
                    filterMap: {}
                }, // 模板类型数据字典列表
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                mailTemplateForm: {
                    templateCode: undefined,
                    templateName: undefined,
                    signName: undefined,
                    templateStatus: '1',
                    templateType: '1',
                    templateContent: undefined,
                    cacheCodeKey: undefined,
                    cacheTimeOut: '0',
                    cacheTimeOutUnit: undefined,
                    sendTimesLimit: '0',
                    sendTimesLimitPeriod: '0',
                    sendTimesLimitPeriodUnit: undefined,
                    comments: undefined
                },
                // 表头
                columns: [
                    {
                        title: '模板编码',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'templateCode'
                    },
                    {
                        title: '模板名称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'templateName'
                    },
                    {
                        title: '模板签名',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'signName'
                    },
                    {
                        title: '模板状态',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'templateStatusSlot' },
                        dataIndex: 'templateStatus'
                    },
                    {
                        title: '模板类型',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'templateTypeSlot' },
                        dataIndex: 'templateType'
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
                    templateCode: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入模板编码', trigger: 'blur' }
                    ],
                    templateName: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入模板名称', trigger: 'blur' }
                    ],
                    signName: [
                        { min: 1, max: 64, message: '长度在 1 到 64 个字符', trigger: 'blur', type: 'string' }
                    ],
                    templateStatus: [
                    ],
                    templateType: [
                    ],
                    templateContent: [
                        { min: 1, max: 65535, message: '长度在 1 到 65535 个字符', trigger: 'blur', type: 'string' }
                    ],
                    cacheCodeKey: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    cacheTimeOut: [
                    ],
                    cacheTimeOutUnit: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    sendTimesLimit: [
                    ],
                    sendTimesLimitPeriod: [
                    ],
                    sendTimesLimitPeriodUnit: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    comments: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ]
                },
                downloadLoading: false,
                mailTemplateLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                mailTemplateWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                mailTemplatePagination: {
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
            const dictList = [that.templateStatusDict, that.templateTypeDict]
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
                    return queryMailTemplateList(Object.assign(parameter, that.listMailTemplateQuery))
                        .then(res => {
                            that.list = res.data
                            return res
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
                await batchListDict(dictParams).then(response => {
                    result = response.data
                    that.listLoading = false
                })
                return result
            },
            resetMailTemplateQuery () {
                this.listMailTemplateQuery = {
                        templateCode: undefined, // 模板编码
                        templateName: undefined, // 模板名称
                        templateStatus: '1', // 模板状态
                        templateType: undefined, // 模板类型
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetMailTemplateForm () {
                this.mailTemplateForm = {
                    templateCode: undefined, // 模板编码
                    templateName: undefined, // 模板名称
                    signName: undefined, // 模板签名
                    templateStatus: '1', // 模板状态
                    templateType: '1', // 模板类型
                    templateContent: undefined, // 模板内容
                    cacheCodeKey: undefined, // 缓存key
                    cacheTimeOut: '0', // 缓存有效期
                    cacheTimeOutUnit: undefined, // 缓存有效期单位
                    sendTimesLimit: '0', // 发送次数限制
                    sendTimesLimitPeriod: '0', // 限制时间间隔
                    sendTimesLimitPeriodUnit: undefined, // 限制时间间隔单位
                    comments: undefined // 描述
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
                queryMailTemplateList(this.listMailTemplateQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.mailTemplateTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.mailTemplateTable.refresh()
            },
            handleCreate () {
                this.resetMailTemplateForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['mailTemplateForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['mailTemplateForm'].validate(valid => {
                    if (valid) {
                        createMailTemplate(this.mailTemplateForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.mailTemplateForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.mailTemplateForm.templateStatus = row.templateStatus + ''
                this.mailTemplateForm.templateType = row.templateType + ''
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['mailTemplateForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['mailTemplateForm'].validate(valid => {
                    if (valid) {
                        updateMailTemplate(this.mailTemplateForm).then(() => {
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
                        deleteMailTemplate(row.id).then(() => {
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
                var mailTemplateList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: mailTemplateList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteMailTemplate(that.selectedRowKeys).then(() => {
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
                updateMailTemplateStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.templateStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadMailTemplateList(this.listMailTemplateQuery).then(response => {
                    handleDownloadBlod('邮件模板数据列表.xlsx', response)
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
                uploadMailTemplate(formData).then(() => {
                    this.uploading = false
                    this.$message.success('邮件模板数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('邮件模板数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadMailTemplateTemplate(this.listMailTemplateQuery).then(response => {
                    handleDownloadBlod('邮件模板批量上传模板.xlsx', response)
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
