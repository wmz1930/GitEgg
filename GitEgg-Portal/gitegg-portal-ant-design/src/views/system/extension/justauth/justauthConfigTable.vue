<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="登录开关" prop="enabled">
              <a-select v-model.trim="listJustAuthConfigQuery.enabled"
                        placeholder="登录开关"
                        allow-clear
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in enabledDictList"
                                 :label="item.dictName"
                                 :value="item.dictCode === 'checkedChildren' ? 'true' : 'false' ">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="状态" prop="status">
              <a-select v-model.trim="listJustAuthConfigQuery.status"
                        placeholder="状态"
                        allow-clear
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in statusDictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listJustAuthConfigQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listJustAuthConfigQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetJustAuthConfigQuery">重置</a-button>
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
      <!--
      <a-upload name="uploadFile" :show-upload-list="false" :before-upload="beforeUpload">
        <a-button> <a-icon type="upload" /> 导入 </a-button>
      </a-upload>
      <a href="javascript:;" @click="handleDownloadTemplate" style="margin-left: 8px;">下载导入模板</a>-->
      <a-tag color="orange">
        此页面需在系统管理员指导下进行配置
      </a-tag>
    </div>
    <s-table
      ref="justAuthConfigTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="justAuthConfigPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >

      <span slot="enabledSlot" slot-scope="text, record">
        {{ record.enabled | enabledDictFilter }}
      </span>
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

    <a-drawer :title="textMap[dialogStatus]"
              placement="right"
              :visible="dialogFormVisible"
              :width="700"
              :closable="false"
              :destroyOnClose="true"
              :maskClosable="false"
              @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="justAuthConfigForm"
        :model="justAuthConfigForm"
        :rules="rules"
        :label-col="justAuthConfigLabelCol"
        :wrapper-col="justAuthConfigWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="登录开关" prop="enabled">
              <a-switch v-model="justAuthConfigForm.enabled"
                        name="justAuthConfigForm.Switchenabled"
                        checked-children="开"
                        un-checked-children="关"
                        default-checked>
                <span :key="item.id + index" v-for="(item,index) in enabledDictList" :slot="item.dictCode" >{{ item.dictName }}</span>
              </a-switch>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="配置类" prop="enumClass">
              <a-input
                v-model.trim="justAuthConfigForm.enumClass"
                placeholder="请输入配置类"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Http超时" prop="httpTimeout">
              <a-input-number
                style="width: 100%;"
                v-model.trim="justAuthConfigForm.httpTimeout"
                placeholder="请输入Http超时时间"
                :min="0"
                :max="2147483647"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存类型" prop="cacheType">
              <a-input
                v-model.trim="justAuthConfigForm.cacheType"
                placeholder="请输入缓存类型"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存前缀" prop="cachePrefix">
              <a-input
                v-model.trim="justAuthConfigForm.cachePrefix"
                placeholder="请输入缓存前缀"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="缓存超时" prop="cacheTimeout">
              <a-input-number
                style="width: 100%;"
                v-model.trim="justAuthConfigForm.cacheTimeout"
                placeholder="请输入缓存超时时间，单位： 分钟"
                :min="0"
                :max="2147483647"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="状态" prop="status">
              <a-radio-group v-model="justAuthConfigForm.status"
                             name="justAuthConfigForm.Radiostatus">
                <a-radio :key="item.id + index" v-for="(item,index) in statusDictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="备注" prop="remark">
              <a-textarea
                v-model.trim="justAuthConfigForm.remark"
                placeholder="请输入备注"
                :auto-size="{ minRows: 3, maxRows: 5 }"
                @keyup.enter.native="handleFilter" />
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
    import { queryJustAuthConfigList, createJustAuthConfig, updateJustAuthConfig, updateJustAuthConfigStatus, deleteJustAuthConfig, batchDeleteJustAuthConfig, downloadJustAuthConfigList, uploadJustAuthConfig, downloadJustAuthConfigTemplate } from '@/api/system/extension/justauth/justauthConfig'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { listDictBusiness } from '@/api/system/base/dictBusiness'
    let vm = {}
    export default {
        name: 'JustAuthConfigTable',
        components: { moment, STable },
        filters: {
            // 登录开关数据字典展示
            enabledDictFilter (dictCode) {
                if (dictCode === true) {
                  return vm.enabledFilterMap ? vm.enabledFilterMap['checkedChildren'] : true
                } else {
                  return vm.enabledFilterMap ? vm.enabledFilterMap['unCheckedChildren'] : false
                }
            },
            // 状态数据字典展示
            statusDictFilter (dictCode) {
                return vm.statusFilterMap[dictCode]
            }
        },
        data () {
            vm = this
            return {
                advanced: false,
                currentJustAuthConfig: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listJustAuthConfigQuery: {
                    enabled: undefined, // 登录开关
                    status: '1', // 状态
                    beginDateTime: '',
                    endDateTime: ''
                },
                enabledDictList: [], // 登录开关数据字典列表
                statusDictList: [], // 状态数据字典列表
                statusFilterMap: {},
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                justAuthConfigForm: {
                    enabled: true,
                    enumClass: undefined,
                    httpTimeout: undefined,
                    cacheType: undefined,
                    cachePrefix: undefined,
                    cacheTimeout: undefined,
                    status: '1',
                    remark: undefined
                },
                // 表头
                columns: [
                    {
                        title: '登录开关',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'enabledSlot' },
                        dataIndex: 'enabled'
                    },
                    {
                        title: '配置类',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'enumClass'
                    },
                    {
                        title: 'Http超时',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'httpTimeout'
                    },
                    {
                        title: '缓存类型',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'cacheType'
                    },
                    {
                        title: '缓存前缀',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'cachePrefix'
                    },
                    {
                        title: '缓存超时',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'cacheTimeout'
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
                        title: '备注',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'remark'
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
                    enabled: [
                        { required: true, message: '请输入登录开关', trigger: 'blur' }
                    ],
                    enumClass: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    httpTimeout: [
                    ],
                    cacheType: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    cachePrefix: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    cacheTimeout: [
                    ],
                    status: [
                        { required: true, message: '请输入状态', trigger: 'blur' }
                    ],
                    remark: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ]
                },
                downloadLoading: false,
                justAuthConfigLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                justAuthConfigWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                justAuthConfigPagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryJustAuthConfigList(Object.assign(parameter, this.listJustAuthConfigQuery))
                        .then(res => {
                            this.list = res.data.records
                            return res.data
                        })
                }
            }
        },
        watch: {

        },
        created () {
            this.queryEnabledDictList()
            this.queryStatusDictList()
        },
        methods: {
            queryEnabledDictList () {
                const that = this
                that.listLoading = true
                listDictBusiness('ON_OFF').then(response => {
                    this.enabledDictList = response.data
                    this.enabledFilterMap = {}
                    this.enabledDictList.forEach((item, index, arr) => {
                        this.enabledFilterMap[item.dictCode] = item.dictName
                    })
                    that.listLoading = false
                })
            },
            queryStatusDictList () {
                const that = this
                that.listLoading = true
                listDictBusiness('ENABLE_OR_NOT').then(response => {
                    this.statusDictList = response.data
                    this.statusFilterMap = {}
                    this.statusDictList.forEach((item, index, arr) => {
                        this.statusFilterMap[item.dictCode] = item.dictName
                    })
                    that.listLoading = false
                    this.getList()
                })
            },
            resetJustAuthConfigQuery () {
                this.listJustAuthConfigQuery = {
                        enabled: undefined, // 登录开关
                        status: '1', // 状态
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetJustAuthConfigForm () {
                this.justAuthConfigForm = {
                    enabled: true, // 登录开关
                    enumClass: undefined, // 配置类
                    httpTimeout: undefined, // Http超时
                    cacheType: undefined, // 缓存类型
                    cachePrefix: undefined, // 缓存前缀
                    cacheTimeout: undefined, // 缓存超时
                    status: '1', // 状态
                    remark: undefined // 备注
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
                queryJustAuthConfigList(this.listJustAuthConfigQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.justAuthConfigTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.justAuthConfigTable.refresh()
            },
            handleCreate () {
                this.resetJustAuthConfigForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['justAuthConfigForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['justAuthConfigForm'].validate(valid => {
                    if (valid) {
                        createJustAuthConfig(this.justAuthConfigForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.justAuthConfigForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.justAuthConfigForm.status = row.status + ''
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['justAuthConfigForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['justAuthConfigForm'].validate(valid => {
                    if (valid) {
                        updateJustAuthConfig(this.justAuthConfigForm).then(() => {
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
                        deleteJustAuthConfig(row.id).then(() => {
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
                var justAuthConfigList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: justAuthConfigList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteJustAuthConfig(that.selectedRowKeys).then(() => {
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
                updateJustAuthConfigStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.status = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadJustAuthConfigList(this.listJustAuthConfigQuery).then(response => {
                    handleDownloadBlod('租户第三方登录功能配置表数据列表.xlsx', response)
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
                uploadJustAuthConfig(formData).then(() => {
                    this.uploading = false
                    this.$message.success('租户第三方登录功能配置表数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('租户第三方登录功能配置表数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadJustAuthConfigTemplate(this.listJustAuthConfigQuery).then(response => {
                    handleDownloadBlod('租户第三方登录功能配置表批量上传模板.xlsx', response)
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
