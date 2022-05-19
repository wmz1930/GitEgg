<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="第三方ID" prop="uuid">
              <a-input
                v-model.trim="listJustAuthSocialQuery.uuid"
                placeholder="请输入第三方ID"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="用户来源" prop="source">
              <a-input
                v-model.trim="listJustAuthSocialQuery.source"
                placeholder="请输入用户来源"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="开始时间">
              <a-date-picker v-model.trim="listJustAuthSocialQuery.beginDateTime"
                             placeholder="开始时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="结束时间">
              <a-date-picker v-model.trim="listJustAuthSocialQuery.endDateTime"
                             placeholder="结束时间"
                             valueFormat="YYYY-MM-DD"
                             style="width:100%;"/>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <span class="table-page-search-submitButtons" :style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetJustAuthSocialQuery">重置</a-button>
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
    </div>
    <s-table
      ref="justAuthSocialTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="justAuthSocialPagination"
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
        ref="justAuthSocialForm"
        :model="justAuthSocialForm"
        :rules="rules"
        :label-col="justAuthSocialLabelCol"
        :wrapper-col="justAuthSocialWrapperCol">
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="第三方ID" prop="uuid">
              <a-input
                v-model.trim="justAuthSocialForm.uuid"
                placeholder="请输入第三方ID"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="用户来源" prop="source">
              <a-input
                v-model.trim="justAuthSocialForm.source"
                placeholder="请输入用户来源"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="授权令牌" prop="accessToken">
              <a-input
                v-model.trim="justAuthSocialForm.accessToken"
                placeholder="请输入授权令牌"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="令牌有效期" prop="expireIn">
              <a-input
                v-model.trim="justAuthSocialForm.expireIn"
                placeholder="请输入令牌有效期"
                :max-length="10" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="刷新令牌" prop="refreshToken">
              <a-input
                v-model.trim="justAuthSocialForm.refreshToken"
                placeholder="请输入刷新令牌"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="OpenId" prop="openId">
              <a-input
                v-model.trim="justAuthSocialForm.openId"
                placeholder="请输入OpenId"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="用户ID" prop="uid">
              <a-input
                v-model.trim="justAuthSocialForm.uid"
                placeholder="请输入用户ID"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="授权信息" prop="accessCode">
              <a-input
                v-model.trim="justAuthSocialForm.accessCode"
                placeholder="请输入授权信息"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="UnionId" prop="unionId">
              <a-input
                v-model.trim="justAuthSocialForm.unionId"
                placeholder="请输入UnionId"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="用户权限" prop="scope">
              <a-input
                v-model.trim="justAuthSocialForm.scope"
                placeholder="请输入用户权限"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="授权类型" prop="tokenType">
              <a-input
                v-model.trim="justAuthSocialForm.tokenType"
                placeholder="请输入授权类型"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="IdToken" prop="idToken">
              <a-input
                v-model.trim="justAuthSocialForm.idToken"
                placeholder="请输入IdToken"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="mac_algorithm" prop="macAlgorithm">
              <a-input
                v-model.trim="justAuthSocialForm.macAlgorithm"
                placeholder="请输入mac_algorithm"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="mac_key" prop="macKey">
              <a-input
                v-model.trim="justAuthSocialForm.macKey"
                placeholder="请输入mac_key"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="授权code" prop="code">
              <a-input
                v-model.trim="justAuthSocialForm.code"
                placeholder="请输入授权code"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="oauth_token" prop="oauthToken">
              <a-input
                v-model.trim="justAuthSocialForm.oauthToken"
                placeholder="请输入oauth_token"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="24">
            <a-form-model-item label="oauth_token_secret" prop="oauthTokenSecret">
              <a-input
                v-model.trim="justAuthSocialForm.oauthTokenSecret"
                placeholder="请输入oauth_token_secret"
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
    import { queryJustAuthSocialList, createJustAuthSocial, updateJustAuthSocial, deleteJustAuthSocial, batchDeleteJustAuthSocial, downloadJustAuthSocialList } from '@/api/system/extension/justauth/justauthSocial'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    export default {
        name: 'JustAuthSocialTable',
        components: { moment, STable },
        filters: {
        },
        data () {
            return {
                advanced: false,
                currentJustAuthSocial: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listJustAuthSocialQuery: {
                    uuid: undefined, // 第三方ID
                    source: undefined, // 用户来源
                    beginDateTime: '',
                    endDateTime: ''
                },
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                justAuthSocialForm: {
                    uuid: undefined,
                    source: undefined,
                    accessToken: undefined,
                    expireIn: undefined,
                    refreshToken: undefined,
                    openId: undefined,
                    uid: undefined,
                    accessCode: undefined,
                    unionId: undefined,
                    scope: undefined,
                    tokenType: undefined,
                    idToken: undefined,
                    macAlgorithm: undefined,
                    macKey: undefined,
                    code: undefined,
                    oauthToken: undefined,
                    oauthTokenSecret: undefined
                },
                // 表头
                columns: [
                    {
                        title: '第三方ID',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'uuid'
                    },
                    {
                        title: '用户来源',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'source'
                    },
                    {
                        title: '授权令牌',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'accessToken'
                    },
                    {
                        title: '令牌有效期',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'expireIn'
                    },
                    {
                        title: '刷新令牌',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'refreshToken'
                    },
                    {
                        title: 'OpenId',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'openId'
                    },
                    {
                        title: '用户ID',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'uid'
                    },
                    {
                        title: '授权信息',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'accessCode'
                    },
                    {
                        title: 'UnionId',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'unionId'
                    },
                    {
                        title: '用户权限',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'scope'
                    },
                    {
                        title: '授权类型',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'tokenType'
                    },
                    {
                        title: 'IdToken',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'idToken'
                    },
                    {
                        title: 'mac_algorithm',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'macAlgorithm'
                    },
                    {
                        title: 'mac_key',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'macKey'
                    },
                    {
                        title: '授权code',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'code'
                    },
                    {
                        title: 'oauth_token',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'oauthToken'
                    },
                    {
                        title: 'oauth_token_secret',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'oauthTokenSecret'
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
                    uuid: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    source: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' }
                    ],
                    accessToken: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' }
                    ],
                    expireIn: [
                    ],
                    refreshToken: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' }
                    ],
                    openId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    uid: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    accessCode: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    unionId: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    scope: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    tokenType: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    idToken: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    macAlgorithm: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    macKey: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    code: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    oauthToken: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    oauthTokenSecret: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ]
                },
                downloadLoading: false,
                justAuthSocialLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                justAuthSocialWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                justAuthSocialPagination: {
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
                    return queryJustAuthSocialList(Object.assign(parameter, that.listJustAuthSocialQuery))
                        .then(res => {
                            that.list = res.data
                            return res
                        })
                }
                that.$nextTick(() => {
                    that.handleFilter()
                })
        },
        methods: {
            resetJustAuthSocialQuery () {
                this.listJustAuthSocialQuery = {
                        uuid: undefined, // 第三方ID
                        source: undefined, // 用户来源
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetJustAuthSocialForm () {
                this.justAuthSocialForm = {
                    uuid: undefined, // 第三方ID
                    source: undefined, // 用户来源
                    accessToken: undefined, // 授权令牌
                    expireIn: undefined, // 令牌有效期
                    refreshToken: undefined, // 刷新令牌
                    openId: undefined, // OpenId
                    uid: undefined, // 用户ID
                    accessCode: undefined, // 授权信息
                    unionId: undefined, // UnionId
                    scope: undefined, // 用户权限
                    tokenType: undefined, // 授权类型
                    idToken: undefined, // IdToken
                    macAlgorithm: undefined, // mac_algorithm
                    macKey: undefined, // mac_key
                    code: undefined, // 授权code
                    oauthToken: undefined, // oauth_token
                    oauthTokenSecret: undefined // oauth_token_secret
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
                queryJustAuthSocialList(this.listJustAuthSocialQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.justAuthSocialTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.justAuthSocialTable.refresh()
            },
            handleCreate () {
                this.resetJustAuthSocialForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['justAuthSocialForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['justAuthSocialForm'].validate(valid => {
                    if (valid) {
                        createJustAuthSocial(this.justAuthSocialForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.justAuthSocialForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['justAuthSocialForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['justAuthSocialForm'].validate(valid => {
                    if (valid) {
                        updateJustAuthSocial(this.justAuthSocialForm).then(() => {
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
                        deleteJustAuthSocial(row.id).then(() => {
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
                var justAuthSocialList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: justAuthSocialList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteJustAuthSocial(that.selectedRowKeys).then(() => {
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
                downloadJustAuthSocialList(this.listJustAuthSocialQuery).then(response => {
                    handleDownloadBlod('租户第三方登录功能配置表数据列表.xlsx', response)
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
