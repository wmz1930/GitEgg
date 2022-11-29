<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="渠道" prop="channelCode">
              <a-select v-model.trim="listSmsChannelQuery.channelCode"
                        placeholder="请选择渠道"
                        allowClear
                        show-search
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in channelCodeDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listSmsChannelQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listSmsChannelQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSmsChannelQuery">重置</a-button>
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
      <a-tag color="orange" style="float: right;margin-top: 10px;">
        请注意：默认只允许有一个渠道是启用状态，当启用多个渠道时，会随机选取一个渠道进行短信发送，请保证各个渠道正常可用
      </a-tag>
    </div>
    <s-table
      ref="smsChannelTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="smsChannelPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="channelCodeSlot" slot-scope="text, record">
        {{ record.channelCode | channelCodeDictFilter }}
      </span>
      <span slot="channelStatusSlot" slot-scope="text, record">
        <a-tag :color="record.channelStatus | statusFilter">{{ record.channelStatus | channelStatusDictFilter }}</a-tag>
      </span>
      <span slot="smsValidity" slot-scope="text, record">
        <span v-if="record.smsValidity && record.smsValidity !== ''">{{ record.smsValidity | moment }}</span>
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
        ref="smsChannelForm"
        :model="smsChannelForm"
        :rules="rules"
        :label-col="smsChannelLabelCol"
        :wrapper-col="smsChannelWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="渠道" prop="channelCode">
              <a-select v-model.trim="smsChannelForm.channelCode"
                        placeholder="请选择渠道"
                        show-search
                        mode="default"
                        :filter-option="filterOption"
                        @change="handleChannelChange" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in channelCodeDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="SecretId" prop="secretId">
              <a-input
                v-model.trim="smsChannelForm.secretId"
                placeholder="请输入SecretId"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="SecretKey" prop="secretKey">
              <a-input
                v-model.trim="smsChannelForm.secretKey"
                placeholder="请输入SecretKey"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="RegionId" prop="regionId">
              <a-input
                v-model.trim="smsChannelForm.regionId"
                placeholder="请输入RegionId"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="渠道状态" prop="channelStatus">
              <a-radio-group v-model="smsChannelForm.channelStatus"
                             name="smsChannelForm.RadiochannelStatus">
                <a-radio :key="item.id + index" v-for="(item,index) in channelStatusDict.dictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信数量" prop="smsQuantity">
              <a-input-number
                v-model.trim="smsChannelForm.smsQuantity"
                placeholder="短信数量,0为不限制"
                :min="0"
                :max="9223372036854775807"
                style="width:100%;" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="有效期" prop="smsValidity">
              <a-date-picker v-model.trim="smsChannelForm.smsValidity"
                             placeholder="有效期"
                             :showTime="{ defaultValue: moment('23:59:59', 'HH:mm:ss') }"
                             valueFormat="YYYY-MM-DD HH:mm:ss"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="描述" prop="comments">
              <a-textarea
                v-model.trim="smsChannelForm.comments"
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
  </a-card>
</template>
<script>
    import { STable } from '@/components'
    import { querySmsChannelList, createSmsChannel, updateSmsChannel, updateSmsChannelStatus, deleteSmsChannel, batchDeleteSmsChannel, downloadSmsChannelList, checkSmsChannelExist } from '@/api/system/extension/sms/smsChannel'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { batchListDict } from '@/api/system/base/dict'
    let vm = {}
    export default {
        name: 'SmsChannelTable',
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
            // 渠道状态数据字典展示
            channelStatusDictFilter (dictCode) {
                return vm.channelStatusDict.filterMap[dictCode]
            },
            // 渠道编码数据字典展示
            channelCodeDictFilter (dictCode) {
                return vm.channelCodeDict.filterMap[dictCode]
            }
        },
        data () {
            vm = this
            // 增加或更新记录时，判断字段是否已经存在
            var validSmsChannel = (rule, value, callback) => {
              var keyData = {
                id: this.smsChannelForm.id,
                checkField: 'channel_code',
                checkValue: value
              }
              checkSmsChannelExist(keyData).then(response => {
                if (!response.data) {
                  callback(new Error('记录已存在'))
                } else {
                  callback()
                }
              })
            }
            return {
                advanced: false,
                currentSmsChannel: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listSmsChannelQuery: {
                    channelCode: undefined, // 渠道编码
                    channelName: undefined, // 渠道名称
                    beginDateTime: '',
                    endDateTime: ''
                },
                channelCodeDict: {
                    dictCode: 'SMS_CHANNEL',
                    dictList: [],
                    filterMap: {}
                }, // 渠道编码数据字典列表
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
                smsChannelForm: {
                    channelCode: undefined,
                    channelName: undefined,
                    secretId: undefined,
                    secretKey: undefined,
                    regionId: undefined,
                    channelStatus: '1',
                    smsQuantity: '0',
                    smsValidity: undefined,
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
                        title: '短信数量',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'smsQuantity'
                    },
                    {
                        title: '有效期',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'smsValidity' },
                        dataIndex: 'smsValidity'
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
                        title: '描述',
                        align: 'center',
                        ellipsis: true,
                        dataIndex: 'comments'
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
                    secretId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入SecretId', trigger: 'blur' }
                    ],
                    secretKey: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入SecretKey', trigger: 'blur' }
                    ],
                    regionId: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入RegionId', trigger: 'blur' }
                    ],
                    channelStatus: [
                        { required: true, message: '请输入渠道状态', trigger: 'blur' }
                    ],
                    comments: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入描述', trigger: 'blur' },
                        { validator: validSmsChannel, trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                smsChannelLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                smsChannelWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                smsChannelPagination: {
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
            const dictList = [that.channelCodeDict, that.channelStatusDict]
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
                    return querySmsChannelList(Object.assign(parameter, that.listSmsChannelQuery))
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
            moment,
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
            handleChannelChange (value) {
              this.smsChannelForm.channelName = this.channelCodeDict.filterMap[value]
            },
            resetSmsChannelQuery () {
                this.listSmsChannelQuery = {
                        channelCode: undefined, // 渠道编码
                        channelName: undefined, // 渠道名称
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetSmsChannelForm () {
                this.smsChannelForm = {
                    channelCode: undefined, // 渠道编码
                    channelName: undefined, // 渠道名称
                    secretId: undefined, // SecretId
                    secretKey: undefined, // SecretKey
                    regionId: undefined, // RegionId
                    channelStatus: '1', // 渠道状态
                    smsQuantity: '0', // 短信数量
                    smsValidity: undefined, // 有效期
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
                querySmsChannelList(this.listSmsChannelQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.smsChannelTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.smsChannelTable.refresh()
            },
            handleCreate () {
                this.resetSmsChannelForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['smsChannelForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['smsChannelForm'].validate(valid => {
                    if (valid) {
                        createSmsChannel(this.smsChannelForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.smsChannelForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.smsChannelForm.channelStatus = row.channelStatus + ''
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['smsChannelForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['smsChannelForm'].validate(valid => {
                    if (valid) {
                        updateSmsChannel(this.smsChannelForm).then(() => {
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
                        deleteSmsChannel(row.id).then(() => {
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
                var smsChannelList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: smsChannelList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteSmsChannel(that.selectedRowKeys).then(() => {
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
                updateSmsChannelStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.channelStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadSmsChannelList(this.listSmsChannelQuery).then(response => {
                    handleDownloadBlod('短信渠道表数据列表.xlsx', response)
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
