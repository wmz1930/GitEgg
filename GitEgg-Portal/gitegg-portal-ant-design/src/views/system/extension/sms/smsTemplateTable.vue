<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="短信渠道" prop="channelId">
              <a-select v-model.trim="listSmsTemplateQuery.channelId"
                        placeholder="短信渠道"
                        show-search
                        allowClear
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in smsChannelList"
                                 :label="item.channelName"
                                 :value="item.id">
                  {{ item.channelName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="短信编码" prop="smsCode">
              <a-input
                v-model.trim="listSmsTemplateQuery.smsCode"
                placeholder="请输入短信编码"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="短信名称" prop="smsName">
              <a-input
                v-model.trim="listSmsTemplateQuery.smsName"
                placeholder="请输入短信名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="模板ID" prop="templateId">
              <a-input
                v-model.trim="listSmsTemplateQuery.templateId"
                placeholder="请输入模板ID"
                :max-length="64"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listSmsTemplateQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listSmsTemplateQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSmsTemplateQuery">重置</a-button>
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
      ref="smsTemplateTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="smsTemplatePagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="channelIdSlot" slot-scope="text, record">
        {{ record.channelId | channelFilter }}
      </span>
      <span slot="templateStatusSlot" slot-scope="text, record">
        {{ record.templateStatus | templateStatusDictFilter }}
      </span>
      <span slot="templateTypeSlot" slot-scope="text, record">
        {{ record.templateType | templateTypeDictFilter }}
      </span>
      <span slot="cacheTimeOutUnitSlot" slot-scope="text, record">
        {{ record.cacheTimeOutUnit | timeUnitDictFilter }}
      </span>
      <span slot="sendTimesLimitPeriodUnitSlot" slot-scope="text, record">
        {{ record.sendTimesLimitPeriodUnit | timeUnitDictFilter }}
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
        ref="smsTemplateForm"
        :model="smsTemplateForm"
        :rules="rules"
        :label-col="smsTemplateLabelCol"
        :wrapper-col="smsTemplateWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信渠道" prop="channelId">
              <a-select
                v-model.trim="smsTemplateForm.channelId"
                placeholder="短信渠道"
                show-search
                mode="default"
                :filter-option="filterOption" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in smsChannelList"
                                 :label="item.channelName"
                                 :value="item.id">
                  {{ item.channelName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信编码" prop="smsCode">
              <a-input
                v-model.trim="smsTemplateForm.smsCode"
                placeholder="请输入自定义短信编码"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信名称" prop="smsName">
              <a-input
                v-model.trim="smsTemplateForm.smsName"
                placeholder="请输入短信名称"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="模板ID" prop="templateId">
              <a-input
                v-model.trim="smsTemplateForm.templateId"
                placeholder="请输入短信服务商的模板ID"
                :max-length="64" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信签名" prop="signName">
              <a-input
                v-model.trim="smsTemplateForm.signName"
                placeholder="请输入短信签名"
                :max-length="64" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信状态" prop="templateStatus">
              <a-radio-group v-model="smsTemplateForm.templateStatus"
                             name="smsTemplateForm.RadiotemplateStatus">
                <a-radio :key="item.id + index" v-for="(item,index) in templateStatusDict.dictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="短信类型" prop="templateType">
              <a-select
                v-model.trim="smsTemplateForm.templateType"
                placeholder="短信类型"
                show-search
                mode="default"
                :filter-option="filterOption" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in templateTypeDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-show="smsTemplateForm.templateType === '1'">
          <!--因多个渠道同时启用时会随机选择一个渠道,此时直接取默认配置的key前缀+短信编码即可-->
          <!-- <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存key" prop="cacheCodeKey">
              <a-input
                v-model.trim="smsTemplateForm.cacheCodeKey"
                placeholder="如果是验证码，这里缓存key必填"
                :max-length="255" />
            </a-form-model-item>
          </a-col> -->
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存有效期" prop="cacheTimeOut">
              <a-input-number
                v-model.trim="smsTemplateForm.cacheTimeOut"
                placeholder="缓存有效期"
                style="width:100%;"
                :min="0"
                :max="9223372036854775807" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" v-show="smsTemplateForm.templateType === '1'">
            <a-form-model-item label="有效期单位" prop="cacheTimeOutUnit">
              <a-select
                v-model.trim="smsTemplateForm.cacheTimeOutUnit"
                placeholder="有效期单位"
                show-search
                mode="default"
                :filter-option="filterOption" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in timeUnitDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="发送次数限制" prop="sendTimesLimit">
              <a-input-number
                v-model.trim="smsTemplateForm.sendTimesLimit"
                placeholder="发送次数限制"
                style="width:100%;"
                :min="0"
                :max="9223372036854775807" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="限制时间间隔" prop="sendTimesLimitPeriod">
              <a-input-number
                v-model.trim="smsTemplateForm.sendTimesLimitPeriod"
                placeholder="限制时间间隔"
                style="width:100%;"
                :min="0"
                :max="9223372036854775807" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="时间间隔单位" prop="sendTimesLimitPeriodUnit">
              <a-select
                v-model.trim="smsTemplateForm.sendTimesLimitPeriodUnit"
                placeholder="请选择时间间隔单位"
                show-search
                mode="default"
                :filter-option="filterOption" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in timeUnitDict.dictList"
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
            <a-form-model-item label="描述" prop="comments">
              <a-textarea
                v-model.trim="smsTemplateForm.comments"
                placeholder="请输入描述"
                :max-length="255"
                :auto-size="{ minRows: 3, maxRows: 5 }"/>
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
    import { querySmsTemplateList, createSmsTemplate, updateSmsTemplate, updateSmsTemplateStatus, deleteSmsTemplate, checkSmsTemplateExist, batchDeleteSmsTemplate, downloadSmsTemplateList, uploadSmsTemplate, downloadSmsTemplateTemplate } from '@/api/system/extension/sms/smsTemplate'
    import { querySmsChannelListAll } from '@/api/system/extension/sms/smsChannel'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { batchListDict } from '@/api/system/base/dict'
    let vm = {}
    export default {
        name: 'SmsTemplateTable',
        components: { moment, STable },
        filters: {
            // 短信渠道数据字典展示
            channelFilter (channelId) {
                return vm.smsChannelMap[channelId]
            },
            // 短信类型数据字典展示
            templateTypeDictFilter (dictCode) {
                return vm.templateTypeDict.filterMap[dictCode]
            },
            // 短信状态数据字典展示
            templateStatusDictFilter (dictCode) {
                return vm.templateStatusDict.filterMap[dictCode]
            },
            // 时间单位
            timeUnitDictFilter (dictCode) {
                return vm.timeUnitDict.filterMap[dictCode]
            }
        },
        data () {
            vm = this
            // 增加或更新记录时，判断字段是否已经存在
            var validSmsCode = (rule, value, callback) => {
                var keyData = {
                    id: this.smsTemplateForm.id,
                    checkField: 'sms_code',
                    checkValue: value
                }
                checkSmsTemplateExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            var validSmsName = (rule, value, callback) => {
                var keyData = {
                    id: this.smsTemplateForm.id,
                    checkField: 'sms_name',
                    checkValue: value
                }
                checkSmsTemplateExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                advanced: false,
                currentSmsTemplate: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listSmsTemplateQuery: {
                    channelId: undefined, // 短信渠道
                    smsCode: undefined, // 短信编码
                    smsName: undefined, // 短信名称
                    templateId: undefined, // 模板ID
                    beginDateTime: '',
                    endDateTime: ''
                },
                smsChannelList: [],
                templateTypeDict: {
                    dictCode: 'SMS_TEMPLATE_TYPE',
                    dictList: [],
                    filterMap: {}
                }, // 短信类型数据字典列表
                templateStatusDict: {
                    dictCode: 'ENABLE_OR_NOT',
                    dictList: [],
                    filterMap: {}
                }, // 短信状态数据字典列表
                timeUnitDict: {
                    dictCode: 'TIME_UNIT',
                    dictList: [],
                    filterMap: {}
                }, // 时间单位
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                smsTemplateForm: {
                    channelId: undefined,
                    smsCode: undefined,
                    smsName: undefined,
                    templateId: undefined,
                    signName: undefined,
                    templateStatus: '1',
                    templateType: '1',
                    cacheCodeKey: undefined,
                    cacheTimeOut: '0',
                    cacheTimeOutUnit: 'MINUTES',
                    sendTimesLimit: '0',
                    sendTimesLimitPeriod: '0',
                    sendTimesLimitPeriodUnit: 'MINUTES',
                    comments: undefined
                },
                // 表头
                columns: [
                    {
                        title: '短信渠道',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'channelIdSlot' },
                        dataIndex: 'channelId'
                    },
                    {
                        title: '短信编码',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'smsCode'
                    },
                    {
                        title: '短信名称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'smsName'
                    },
                    {
                        title: '模板ID',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'templateId'
                    },
                    {
                        title: '短信签名',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'signName'
                    },
                    {
                        title: '短信状态',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'templateStatusSlot' },
                        dataIndex: 'templateStatus'
                    },
                    {
                        title: '短信类型',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'templateTypeSlot' },
                        dataIndex: 'templateType'
                    },
                    // {
                    //     title: '缓存key',
                    //     align: 'center',
                    //     width: 200,
                    //     ellipsis: true,
                    //     dataIndex: 'cacheCodeKey'
                    // },
                    {
                        title: '缓存有效期',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'cacheTimeOut'
                    },
                    {
                        title: '有效期单位',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'cacheTimeOutUnitSlot' },
                        dataIndex: 'cacheTimeOutUnit'
                    },
                    {
                        title: '发送次数限制',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'sendTimesLimit'
                    },
                    {
                        title: '限制时间间隔',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'sendTimesLimitPeriod'
                    },
                    {
                        title: '时间间隔单位',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'sendTimesLimitPeriodUnitSlot' },
                        dataIndex: 'sendTimesLimitPeriodUnit'
                    },
                    {
                        title: '描述',
                        align: 'center',
                        width: 200,
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
                    channelId: [
                        { required: true, message: '请输入短信渠道', trigger: 'blur' }
                    ],
                    smsCode: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { validator: validSmsCode, trigger: 'blur' },
                        { required: true, message: '请输入短信编码', trigger: 'blur' }
                    ],
                    smsName: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { validator: validSmsName, trigger: 'blur' },
                        { required: true, message: '请输入短信名称', trigger: 'blur' }
                    ],
                    templateId: [
                        { min: 1, max: 64, message: '长度在 1 到 64 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入模板ID', trigger: 'blur' }
                    ],
                    signName: [
                        { min: 1, max: 64, message: '长度在 1 到 64 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入短信签名', trigger: 'blur' }
                    ],
                    templateStatus: [
                        { required: true, message: '请输入短信状态', trigger: 'blur' }
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
                smsTemplateLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                smsTemplateWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                smsTemplatePagination: {
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
            that.getSmsChannelList()
            const dictList = [that.templateTypeDict, that.templateStatusDict, that.timeUnitDict]
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
                    return querySmsTemplateList(Object.assign(parameter, that.listSmsTemplateQuery))
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
                await batchListDict(dictParams).then(response => {
                    result = response.data
                    that.listLoading = false
                })
                return result
            },
            async getSmsChannelList () {
                this.listLoading = true
                await querySmsChannelListAll({}).then(response => {
                    this.smsChannelList = response.data
                    this.smsChannelMap = {}
                    this.smsChannelList.forEach((item, index, arr) => {
                        this.smsChannelMap[item.id] = item.channelName
                    })
                    this.listLoading = false
                })
            },
            resetSmsTemplateQuery () {
                this.listSmsTemplateQuery = {
                        channelId: undefined, // 短信渠道
                        smsCode: undefined, // 短信编码
                        smsName: undefined, // 短信名称
                        templateId: undefined, // 模板ID
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetSmsTemplateForm () {
                this.smsTemplateForm = {
                    channelId: undefined, // 短信渠道
                    smsCode: undefined, // 短信编码
                    smsName: undefined, // 短信名称
                    templateId: undefined, // 模板ID
                    signName: undefined, // 短信签名
                    templateStatus: '1', // 短信状态
                    templateType: '1', // 短信类型
                    cacheCodeKey: undefined, // 缓存key
                    cacheTimeOut: '0', // 缓存有效期
                    cacheTimeOutUnit: 'MINUTES', // 有效期单位
                    sendTimesLimit: '0', // 发送次数限制
                    sendTimesLimitPeriod: '0', // 限制时间间隔
                    sendTimesLimitPeriodUnit: 'MINUTES', // 时间间隔单位
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
                querySmsTemplateList(this.listSmsTemplateQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.smsTemplateTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.smsTemplateTable.refresh()
            },
            handleCreate () {
                this.resetSmsTemplateForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['smsTemplateForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['smsTemplateForm'].validate(valid => {
                    if (valid) {
                        createSmsTemplate(this.smsTemplateForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.smsTemplateForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.smsTemplateForm.templateStatus = row.templateStatus + ''
                this.smsTemplateForm.templateType = row.templateType + ''
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['smsTemplateForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['smsTemplateForm'].validate(valid => {
                    if (valid) {
                        updateSmsTemplate(this.smsTemplateForm).then(() => {
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
                        deleteSmsTemplate(row.id).then(() => {
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
                var smsTemplateList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: smsTemplateList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteSmsTemplate(that.selectedRowKeys).then(() => {
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
                updateSmsTemplateStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.templateStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadSmsTemplateList(this.listSmsTemplateQuery).then(response => {
                    handleDownloadBlod('短信配置表数据列表.xlsx', response)
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
                uploadSmsTemplate(formData).then(() => {
                    this.uploading = false
                    this.$message.success('短信配置表数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('短信配置表数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadSmsTemplateTemplate(this.listSmsTemplateQuery).then(response => {
                    handleDownloadBlod('短信配置表批量上传模板.xlsx', response)
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
