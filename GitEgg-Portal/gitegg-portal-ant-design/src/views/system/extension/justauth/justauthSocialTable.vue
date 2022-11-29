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
            <a-form-model-item label="第三方来源" prop="source">
              <a-input
                v-model.trim="listJustAuthSocialQuery.source"
                placeholder="请输入第三方来源"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="用户名" prop="username">
              <a-input
                v-model.trim="listJustAuthSocialQuery.username"
                placeholder="请输入用户名"
                :max-length="64"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="用户昵称" prop="nickname">
              <a-input
                v-model.trim="listJustAuthSocialQuery.nickname"
                placeholder="请输入用户昵称"
                :max-length="64"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
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
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetJustAuthSocialQuery">重置</a-button>
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
          <a-col :md="24" :sm="24">
            <a-form-model-item label="第三方ID" prop="uuid">
              <a-input
                v-model.trim="justAuthSocialForm.uuid"
                placeholder="请输入第三方ID"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="第三方来源" prop="source">
              <a-input
                v-model.trim="justAuthSocialForm.source"
                placeholder="请输入第三方来源"
                :max-length="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户名" prop="username">
              <a-input
                v-model.trim="justAuthSocialForm.username"
                placeholder="请输入用户名"
                :max-length="64" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户昵称" prop="nickname">
              <a-input
                v-model.trim="justAuthSocialForm.nickname"
                placeholder="请输入用户昵称"
                :max-length="64" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户头像" prop="avatar">
              <a-input
                v-model.trim="justAuthSocialForm.avatar"
                placeholder="请输入用户头像"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户网址" prop="blog">
              <a-input
                v-model.trim="justAuthSocialForm.blog"
                placeholder="请输入用户网址"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="所在公司" prop="company">
              <a-input
                v-model.trim="justAuthSocialForm.company"
                placeholder="请输入所在公司"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="位置" prop="location">
              <a-input
                v-model.trim="justAuthSocialForm.location"
                placeholder="请输入位置"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户邮箱" prop="email">
              <a-input
                v-model.trim="justAuthSocialForm.email"
                placeholder="请输入用户邮箱"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户备注" prop="remark">
              <a-input
                v-model.trim="justAuthSocialForm.remark"
                placeholder="请输入用户备注"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="性别" prop="gender">
              <a-input
                v-model.trim="justAuthSocialForm.gender"
                placeholder="请输入性别"
                :max-length="3" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="授权令牌" prop="accessToken">
              <a-input
                v-model.trim="justAuthSocialForm.accessToken"
                placeholder="请输入授权令牌"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="令牌有效期" prop="expireIn">
              <a-input
                v-model.trim="justAuthSocialForm.expireIn"
                placeholder="请输入令牌有效期"
                :max-length="10" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="刷新令牌" prop="refreshToken">
              <a-input
                v-model.trim="justAuthSocialForm.refreshToken"
                placeholder="请输入刷新令牌"
                :max-length="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="刷新令牌有效期" prop="accessTokenExpireIn">
              <a-input
                v-model.trim="justAuthSocialForm.accessTokenExpireIn"
                placeholder="请输入刷新令牌有效期"
                :max-length="10" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="第三方用户ID" prop="uid">
              <a-input
                v-model.trim="justAuthSocialForm.uid"
                placeholder="请输入第三方用户ID"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="第三方用户OpenId" prop="openId">
              <a-input
                v-model.trim="justAuthSocialForm.openId"
                placeholder="请输入第三方用户OpenId"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="AccessCode" prop="accessCode">
              <a-input
                v-model.trim="justAuthSocialForm.accessCode"
                placeholder="请输入AccessCode"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="第三方用户UnionId" prop="unionId">
              <a-input
                v-model.trim="justAuthSocialForm.unionId"
                placeholder="请输入第三方用户UnionId"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Google Scope" prop="scope">
              <a-input
                v-model.trim="justAuthSocialForm.scope"
                placeholder="请输入Google Scope"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Google TokenType" prop="tokenType">
              <a-input
                v-model.trim="justAuthSocialForm.tokenType"
                placeholder="请输入Google TokenType"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Google IdToken" prop="idToken">
              <a-input
                v-model.trim="justAuthSocialForm.idToken"
                placeholder="请输入Google IdToken"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="小米MacAlgorithm" prop="macAlgorithm">
              <a-input
                v-model.trim="justAuthSocialForm.macAlgorithm"
                placeholder="请输入小米MacAlgorithm"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="小米Mac_Key" prop="macKey">
              <a-input
                v-model.trim="justAuthSocialForm.macKey"
                placeholder="请输入小米Mac_Key"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="企业微信code" prop="code">
              <a-input
                v-model.trim="justAuthSocialForm.code"
                placeholder="请输入企业微信code"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Twitter OauthToken" prop="oauthToken">
              <a-input
                v-model.trim="justAuthSocialForm.oauthToken"
                placeholder="请输入Twitter OauthToken"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Twitter OauthTokenSecret" prop="oauthTokenSecret">
              <a-input
                v-model.trim="justAuthSocialForm.oauthTokenSecret"
                placeholder="请输入Twitter OauthTokenSecret"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Twitter UserId" prop="userId">
              <a-input
                v-model.trim="justAuthSocialForm.userId"
                placeholder="请输入Twitter UserId"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Twitter ScreenName" prop="screenName">
              <a-input
                v-model.trim="justAuthSocialForm.screenName"
                placeholder="请输入Twitter ScreenName"
                :max-length="255" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Twitter OauthCallbackConfirmed" prop="oauthCallbackConfirmed">
              <a-input
                v-model.trim="justAuthSocialForm.oauthCallbackConfirmed"
                placeholder="请输入Twitter OauthCallbackConfirmed"
                :max-length="1" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="原始用户信息" prop="rawUserInfo">
              <a-input
                v-model.trim="justAuthSocialForm.rawUserInfo"
                placeholder="请输入原始用户信息"
                :max-length="65535" />
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
                    source: undefined, // 第三方来源
                    username: undefined, // 用户名
                    nickname: undefined, // 用户昵称
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
                    username: undefined,
                    nickname: undefined,
                    avatar: undefined,
                    blog: undefined,
                    company: undefined,
                    location: undefined,
                    email: undefined,
                    remark: undefined,
                    gender: '0',
                    accessToken: undefined,
                    expireIn: undefined,
                    refreshToken: undefined,
                    accessTokenExpireIn: undefined,
                    uid: undefined,
                    openId: undefined,
                    accessCode: undefined,
                    unionId: undefined,
                    scope: undefined,
                    tokenType: undefined,
                    idToken: undefined,
                    macAlgorithm: undefined,
                    macKey: undefined,
                    code: undefined,
                    oauthToken: undefined,
                    oauthTokenSecret: undefined,
                    userId: undefined,
                    screenName: undefined,
                    oauthCallbackConfirmed: undefined,
                    rawUserInfo: undefined
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
                        title: '第三方来源',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'source'
                    },
                    {
                        title: '用户名',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'username'
                    },
                    {
                        title: '用户昵称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'nickname'
                    },
                    {
                        title: '用户头像',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'avatar'
                    },
                    {
                        title: '用户网址',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'blog'
                    },
                    {
                        title: '所在公司',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'company'
                    },
                    {
                        title: '位置',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'location'
                    },
                    {
                        title: '用户邮箱',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'email'
                    },
                    {
                        title: '用户备注',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'remark'
                    },
                    {
                        title: '性别',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'gender'
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
                    uuid: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入第三方ID', trigger: 'blur' }
                    ],
                    source: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入第三方来源', trigger: 'blur' }
                    ],
                    username: [
                        { min: 1, max: 64, message: '长度在 1 到 64 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    nickname: [
                        { min: 1, max: 64, message: '长度在 1 到 64 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入用户昵称', trigger: 'blur' }
                    ],
                    avatar: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入用户头像', trigger: 'blur' }
                    ],
                    blog: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入用户网址', trigger: 'blur' }
                    ],
                    company: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入所在公司', trigger: 'blur' }
                    ],
                    location: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入位置', trigger: 'blur' }
                    ],
                    email: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入用户邮箱', trigger: 'blur' }
                    ],
                    remark: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入用户备注', trigger: 'blur' }
                    ],
                    gender: [
                    ],
                    accessToken: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入授权令牌', trigger: 'blur' }
                    ],
                    expireIn: [
                        { required: true, message: '请输入令牌有效期', trigger: 'blur' }
                    ],
                    refreshToken: [
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入刷新令牌', trigger: 'blur' }
                    ],
                    accessTokenExpireIn: [
                        { required: true, message: '请输入刷新令牌有效期', trigger: 'blur' }
                    ],
                    uid: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入第三方用户ID', trigger: 'blur' }
                    ],
                    openId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入第三方用户OpenId', trigger: 'blur' }
                    ],
                    accessCode: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入AccessCode', trigger: 'blur' }
                    ],
                    unionId: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入第三方用户UnionId', trigger: 'blur' }
                    ],
                    scope: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Google Scope', trigger: 'blur' }
                    ],
                    tokenType: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Google TokenType', trigger: 'blur' }
                    ],
                    idToken: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Google IdToken', trigger: 'blur' }
                    ],
                    macAlgorithm: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入小米MacAlgorithm', trigger: 'blur' }
                    ],
                    macKey: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入小米Mac_Key', trigger: 'blur' }
                    ],
                    code: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入企业微信code', trigger: 'blur' }
                    ],
                    oauthToken: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Twitter OauthToken', trigger: 'blur' }
                    ],
                    oauthTokenSecret: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Twitter OauthTokenSecret', trigger: 'blur' }
                    ],
                    userId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Twitter UserId', trigger: 'blur' }
                    ],
                    screenName: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入Twitter ScreenName', trigger: 'blur' }
                    ],
                    oauthCallbackConfirmed: [
                        { required: true, message: '请输入Twitter OauthCallbackConfirmed', trigger: 'blur' }
                    ],
                    rawUserInfo: [
                        { min: 1, max: 65535, message: '长度在 1 到 65535 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入原始用户信息', trigger: 'blur' }
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
                            that.list = res.data.records
                            return res.data
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
                        source: undefined, // 第三方来源
                        username: undefined, // 用户名
                        nickname: undefined, // 用户昵称
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetJustAuthSocialForm () {
                this.justAuthSocialForm = {
                    uuid: undefined, // 第三方ID
                    source: undefined, // 第三方来源
                    username: undefined, // 用户名
                    nickname: undefined, // 用户昵称
                    avatar: undefined, // 用户头像
                    blog: undefined, // 用户网址
                    company: undefined, // 所在公司
                    location: undefined, // 位置
                    email: undefined, // 用户邮箱
                    remark: undefined, // 用户备注
                    gender: '0', // 性别
                    accessToken: undefined, // 授权令牌
                    expireIn: undefined, // 令牌有效期
                    refreshToken: undefined, // 刷新令牌
                    accessTokenExpireIn: undefined, // 刷新令牌有效期
                    uid: undefined, // 第三方用户ID
                    openId: undefined, // 第三方用户OpenId
                    accessCode: undefined, // AccessCode
                    unionId: undefined, // 第三方用户UnionId
                    scope: undefined, // Google Scope
                    tokenType: undefined, // Google TokenType
                    idToken: undefined, // Google IdToken
                    macAlgorithm: undefined, // 小米MacAlgorithm
                    macKey: undefined, // 小米Mac_Key
                    code: undefined, // 企业微信code
                    oauthToken: undefined, // Twitter OauthToken
                    oauthTokenSecret: undefined, // Twitter OauthTokenSecret
                    userId: undefined, // Twitter UserId
                    screenName: undefined, // Twitter ScreenName
                    oauthCallbackConfirmed: undefined, // Twitter OauthCallbackConfirmed
                    rawUserInfo: undefined // 原始用户信息
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
