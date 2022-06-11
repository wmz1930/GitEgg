<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="名称" prop="sourceName">
              <a-select v-model.trim="listJustAuthSourceQuery.sourceName"
                        placeholder="名称"
                        show-search
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in sourceNameDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="登录类型" prop="sourceType">
              <a-select v-model.trim="listJustAuthSourceQuery.sourceType"
                        placeholder="登录类型"
                        show-search
                        mode="default"
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in sourceTypeDict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :md="6" :sm="24">
              <a-form-model-item label="开始时间">
                <a-date-picker v-model.trim="listJustAuthSourceQuery.beginDateTime"
                               placeholder="开始时间"
                               valueFormat="YYYY-MM-DD"
                               style="width:100%;"/>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="结束时间">
                <a-date-picker v-model.trim="listJustAuthSourceQuery.endDateTime"
                               placeholder="结束时间"
                               valueFormat="YYYY-MM-DD"
                               style="width:100%;"/>
              </a-form-model-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetJustAuthSourceQuery">重置</a-button>
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
      <a-upload name="uploadFile" :show-upload-list="false" :before-upload="beforeUpload">
        <a-button> <a-icon type="upload" /> 导入 </a-button>
      </a-upload>
      <a href="javascript:;" @click="handleDownloadTemplate" style="margin-left: 8px;">下载导入模板</a>
    </div>
    <s-table
      ref="justAuthSourceTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="justAuthSourcePagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="sourceNameSlot" slot-scope="text, record">
        {{ record.sourceName | sourceNameDictFilter }}
      </span>
      <span slot="sourceTypeSlot" slot-scope="text, record">
        {{ record.sourceType | sourceTypeDictFilter }}
      </span>
      <span slot="unionIdSlot" slot-scope="text, record">
        {{ record.unionId | unionIdDictFilter }}
      </span>
      <span slot="userTypeSlot" slot-scope="text, record">
        {{ record.userType | userTypeDictFilter }}
      </span>
      <span slot="ignoreCheckStateSlot" slot-scope="text, record">
        {{ record.ignoreCheckState | ignoreCheckStateDictFilter }}
      </span>
      <span slot="pkceSlot" slot-scope="text, record">
        {{ record.pkce | pkceDictFilter }}
      </span>
      <span slot="proxyPortSlot" slot-scope="text, record">
        {{ record.proxyPort | proxyPortDictFilter }}
      </span>
      <span slot="statusSlot" slot-scope="text, record">
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
        ref="justAuthSourceForm"
        :model="justAuthSourceForm"
        :rules="rules"
        :label-col="justAuthSourceLabelCol"
        :wrapper-col="justAuthSourceWrapperCol">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="登录类型" prop="sourceType">
              <a-select
                v-model.trim="justAuthSourceForm.sourceType"
                placeholder="登录类型"
                show-search
                mode="default"
                :filter-option="filterOption"
                @keyup.enter.native="handleFilter" >
                <a-select-option v-for="item in sourceTypeDict.dictList"
                                 :key="item.dictCode"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="名称" prop="sourceName">
              <a-select
                v-model.trim="justAuthSourceForm.sourceName"
                placeholder="名称"
                show-search
                mode="default"
                :filter-option="filterOption"
                @keyup.enter.native="handleFilter" >
                <a-select-option v-for="item in sourceNameDict.dictList"
                                 :key="item.dictCode"
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
            <a-form-model-item label="客户端id" prop="clientId">
              <a-input
                v-model.trim="justAuthSourceForm.clientId"
                placeholder="请输入客户端id"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="客户端Secret" prop="clientSecret">
              <a-input
                v-model.trim="justAuthSourceForm.clientSecret"
                placeholder="请输入客户端Secret"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="回调地址" prop="redirectUri">
              <a-input
                v-model.trim="justAuthSourceForm.redirectUri"
                placeholder="请输入回调地址"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceType === 'custom'">
            <a-form-model-item label="自定义Class" prop="requestClass">
              <a-input
                v-model.trim="justAuthSourceForm.requestClass"
                placeholder="请输入自定义Class"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'ALIPAY'">
            <a-form-model-item label="支付宝公钥" prop="alipayPublicKey">
              <a-input
                v-model.trim="justAuthSourceForm.alipayPublicKey"
                placeholder="请输入支付宝公钥"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'QQ'">
            <a-form-model-item label="unionid" prop="unionId">
              <a-switch v-model="justAuthSourceForm.unionId"
                        name="justAuthSourceForm.SwitchunionId"
                        checked-children="开"
                        un-checked-children="关"
                        default-checked>
                <span :key="item.id + index" v-for="(item,index) in sourceNameDict.dictList" :slot="item.dictCode" >{{ item.dictName }}</span>
              </a-switch>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'STACK_OVERFLOW'">
            <a-form-model-item label="Stack Overflow Key" prop="stackOverflowKey">
              <a-input
                v-model.trim="justAuthSourceForm.stackOverflowKey"
                placeholder="请输入Stack Overflow Key"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'WECHAT_ENTERPRISE_WEB'">
            <a-form-model-item label="应用ID" prop="agentId">
              <a-input
                v-model.trim="justAuthSourceForm.agentId"
                placeholder="请输入企业微信网页应用ID"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="用户类型" prop="userType" v-show="justAuthSourceForm.sourceName === 'WECHAT_ENTERPRISE' || listJustAuthSourceQuery.sourceName === 'WECHAT_ENTERPRISE_QRCODE_THIRD' || listJustAuthSourceQuery.sourceName === 'WECHAT_ENTERPRISE_WEB' ">
              <a-select
                v-model.trim="justAuthSourceForm.userType"
                placeholder="企业微信用户类型"
                show-search
                mode="default"
                :filter-option="filterOption"
                @keyup.enter.native="handleFilter" >
                <a-select-option v-for="item in userTypeDict.dictList"
                                 :key="item.dictCode"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'OKTA' || listJustAuthSourceQuery.sourceName === 'CODING'">
            <a-form-model-item label="DomainPrefix" prop="domainPrefix">
              <a-input
                v-model.trim="justAuthSourceForm.domainPrefix"
                placeholder="请输入DomainPrefix"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'OKTA'">
            <a-form-model-item label="Okta ID" prop="authServerId">
              <a-input
                v-model.trim="justAuthSourceForm.authServerId"
                placeholder="请输入Okta授权服务器的 ID"
                :max-length="255"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceType === 'custom'">
            <a-form-model-item label="自定义授权scope" prop="scopes">
              <a-input
                v-model.trim="justAuthSourceForm.scopes"
                placeholder="请输入自定义授权scope"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="设备ID" prop="deviceId">
              <a-input
                v-model.trim="justAuthSourceForm.deviceId"
                placeholder="请输入设备ID"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'XMLY'">
            <a-form-model-item label="客户端操作系统类型" prop="clientOsType">
              <a-input
                v-model.trim="justAuthSourceForm.clientOsType"
                placeholder="请输入客户端操作系统类型"
                :max-length="10"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" v-show="justAuthSourceForm.sourceName === 'XMLY'">
            <a-form-model-item label="客户端包名" prop="packId">
              <a-input
                v-model.trim="justAuthSourceForm.packId"
                placeholder="请输入客户端包名"
                :max-length="100"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="状态" prop="status">
              <a-radio-group v-model="justAuthSourceForm.status"
                             name="justAuthSourceForm.Radiostatus">
                <a-radio :key="item.id + index" v-for="(item,index) in statusDict.dictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="备注" prop="remark">
              <a-textarea
                v-model.trim="justAuthSourceForm.remark"
                placeholder="请输入备注"
                :auto-size="{ minRows: 3, maxRows: 5 }"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="开启PKCE模式" prop="pkce">
              <a-switch v-model="justAuthSourceForm.pkce"
                        name="justAuthSourceForm.Switchpkce"
                        checked-children="开"
                        un-checked-children="关"
                        default-checked>
                <span :key="item.id + index" v-for="(item,index) in pkceDict.dictList" :slot="item.dictCode" >{{ item.dictName }}</span>
              </a-switch>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="忽略校验codestate" prop="ignoreCheckState">
              <a-switch v-model="justAuthSourceForm.ignoreCheckState"
                        name="justAuthSourceForm.SwitchignoreCheckState"
                        checked-children="开"
                        un-checked-children="关"
                        default-checked>
                <span :key="item.id + index" v-for="(item,index) in ignoreCheckStateDict.dictList" :slot="item.dictCode" >{{ item.dictName }}</span>
              </a-switch>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="忽略校验RedirectUri" prop="ignoreCheckRedirectUri">
              <a-switch v-model="justAuthSourceForm.ignoreCheckRedirectUri"
                        name="justAuthSourceForm.ignoreCheckRedirectUri"
                        checked-children="开"
                        un-checked-children="关"
                        default-checked>
                <span :key="item.id + index" v-for="(item,index) in ignoreCheckStateDict.dictList" :slot="item.dictCode" >{{ item.dictName }}</span>
              </a-switch>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Http代理类型" prop="proxyType">
              <a-select
                v-model.trim="justAuthSourceForm.proxyType"
                placeholder="请选择Http代理类型"
                show-search
                mode="default"
                :filter-option="filterOption"
                @keyup.enter.native="handleFilter" >
                <a-select-option v-for="item in proxyTypeDict.dictList"
                                 :key="item.dictCode"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Http代理Host" prop="proxyHostName">
              <a-input
                v-model.trim="justAuthSourceForm.proxyHostName"
                placeholder="请输入Http代理Host"
                :max-length="100" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-model-item label="Http代理Port" prop="proxyPort">
              <a-input
                v-model.trim="justAuthSourceForm.proxyPort"
                placeholder="请输入Http代理Port"
                :max-length="10" />
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
    import { queryJustAuthSourceList, createJustAuthSource, updateJustAuthSource, updateJustAuthSourceStatus, deleteJustAuthSource, batchDeleteJustAuthSource, downloadJustAuthSourceList, uploadJustAuthSource, downloadJustAuthSourceTemplate } from '@/api/system/extension/justauth/justauthSource'
    import moment from 'moment'
    import { handleDownloadBlod } from '@/utils/util'
    import { batchListDictBusiness } from '@/api/system/base/dictBusiness'

    let vm = {}
    export default {
        name: 'JustAuthSourceTable',
        components: { moment, STable },
        filters: {
            // 名称数据字典展示
            sourceNameDictFilter (dictCode) {
                return vm.sourceNameDict.filterMap[dictCode]
            },
            // 登录类型数据字典展示
            sourceTypeDictFilter (dictCode) {
                return vm.sourceTypeDict.filterMap[dictCode]
            },
            // unionid数据字典展示
            unionIdDictFilter (dictCode) {
                return vm.unionIdDict.filterMap[dictCode]
            },
            // 企业微信用户类型数据字典展示
            userTypeDictFilter (dictCode) {
                return vm.userTypeDict.filterMap[dictCode]
            },
            // 忽略校验code state数据字典展示
            ignoreCheckStateDictFilter (dictCode) {
                return vm.ignoreCheckStateDict.filterMap[dictCode]
            },
            // 开启PKC模式数据字典展示
            pkceDictFilter (dictCode) {
                return vm.pkceDict.filterMap[dictCode]
            },
            // Http代理Port数据字典展示
            proxyPortDictFilter (dictCode) {
                return vm.proxyTypeDict.filterMap[dictCode]
            },
            // 状态数据字典展示
            statusDictFilter (dictCode) {
                return vm.statusDict.filterMap[dictCode]
            }
        },
        data () {
            vm = this
            return {
                advanced: false,
                currentJustAuthSource: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listJustAuthSourceQuery: {
                    sourceName: undefined, // 名称
                    sourceType: undefined, // 登录类型
                    beginDateTime: '',
                    endDateTime: ''
                },
                sourceNameDict: {
                  dictCode: 'JUST_SOURCE_NAME',
                  dictList: [],
                  filterMap: {}
                },
                sourceTypeDict: {
                  dictCode: 'JUST_SOURCE_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                unionIdDict: {
                  dictCode: 'ON_OFF',
                  dictList: [],
                  filterMap: {}
                },
                userTypeDict: {
                  dictCode: 'JUST_USER_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                ignoreCheckStateDict: {
                  dictCode: 'ON_OFF',
                  dictList: [],
                  filterMap: {}
                },
                pkceDict: {
                  dictCode: 'ON_OFF',
                  dictList: [],
                  filterMap: {}
                },
                proxyTypeDict: {
                  dictCode: 'HTTP_PROXY_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                statusDict: {
                  dictCode: 'ENABLE_OR_NOT',
                  dictList: [],
                  filterMap: {}
                },
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                justAuthSourceForm: {
                    sourceName: undefined,
                    sourceType: 'default',
                    requestClass: undefined,
                    clientId: undefined,
                    clientSecret: undefined,
                    redirectUri: undefined,
                    alipayPublicKey: undefined,
                    unionId: undefined,
                    stackOverflowKey: undefined,
                    agentId: undefined,
                    userType: undefined,
                    domainPrefix: undefined,
                    ignoreCheckState: false,
                    scopes: undefined,
                    deviceId: undefined,
                    clientOsType: undefined,
                    packId: undefined,
                    pkce: true,
                    authServerId: undefined,
                    ignoreCheckRedirectUri: false,
                    proxyType: undefined,
                    proxyHostName: undefined,
                    proxyPort: undefined,
                    status: '1',
                    remark: undefined
                },
                // 表头
                columns: [
                    {
                        title: '名称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'sourceNameSlot' },
                        dataIndex: 'sourceName'
                    },
                    {
                        title: '登录类型',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        scopedSlots: { customRender: 'sourceTypeSlot' },
                        dataIndex: 'sourceType'
                    },
                    {
                        title: '客户端id',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'clientId'
                    },
                    {
                        title: '客户端Secret',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'clientSecret'
                    },
                    {
                        title: '回调地址',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'redirectUri'
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
                    sourceName: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入名称', trigger: 'blur' }
                    ],
                    sourceType: [
                        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入登录类型', trigger: 'blur' }
                    ],
                    requestClass: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    clientId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入客户端id', trigger: 'blur' }
                    ],
                    clientSecret: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入客户端Secret', trigger: 'blur' }
                    ],
                    redirectUri: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' },
                        { required: true, message: '请输入回调地址', trigger: 'blur' }
                    ],
                    alipayPublicKey: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    unionId: [
                    ],
                    stackOverflowKey: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    agentId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    userType: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    domainPrefix: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    ignoreCheckState: [
                    ],
                    scopes: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    deviceId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    clientOsType: [
                    ],
                    packId: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    pkce: [
                    ],
                    authServerId: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ],
                    ignoreCheckRedirectUri: [
                    ],
                    proxyType: [
                        { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur', type: 'string' }
                    ],
                    proxyHostName: [
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur', type: 'string' }
                    ],
                    proxyPort: [
                    ],
                    status: [
                    ],
                    remark: [
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur', type: 'string' }
                    ]
                },
                downloadLoading: false,
                justAuthSourceLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                justAuthSourceWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                justAuthSourcePagination: {
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
            const dictList = [this.sourceNameDict, this.sourceTypeDict, this.unionIdDict, this.userTypeDict, this.ignoreCheckStateDict, this.pkceDict, this.proxyTypeDict, this.statusDict]
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
                    return queryJustAuthSourceList(Object.assign(parameter, that.listJustAuthSourceQuery))
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
              await batchListDictBusiness(dictParams).then(response => {
                result = response.data
                that.listLoading = false
              })
              return result
            },
            resetJustAuthSourceQuery () {
                this.listJustAuthSourceQuery = {
                        sourceName: undefined, // 名称
                        sourceType: undefined, // 登录类型
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            resetJustAuthSourceForm () {
                this.justAuthSourceForm = {
                    sourceName: undefined, // 名称
                    sourceType: 'default', // 登录类型
                    requestClass: undefined, // 自定义Class
                    clientId: undefined, // 客户端id
                    clientSecret: undefined, // 客户端Secret
                    redirectUri: undefined, // 回调地址
                    alipayPublicKey: undefined, // 支付宝公钥
                    unionId: false, // unionid
                    stackOverflowKey: undefined, // Stack Overflow Key
                    agentId: undefined, // 企业微信网页应用ID
                    userType: undefined, // 企业微信用户类型
                    domainPrefix: undefined, // DomainPrefix
                    ignoreCheckState: false, // 忽略校验code state
                    scopes: undefined, // 自定义授权scope
                    deviceId: undefined, // 设备ID
                    clientOsType: undefined, // 客户端操作系统类型
                    packId: undefined, // 客户端包名
                    pkce: true, // 开启PKC模式
                    authServerId: undefined, // Okta授权服务器的 ID
                    ignoreCheckRedirectUri: false, // 忽略校验RedirectUri
                    proxyType: undefined, // Http代理类型
                    proxyHostName: undefined, // Http代理Host
                    proxyPort: undefined, // Http代理Port
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
                queryJustAuthSourceList(this.listJustAuthSourceQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.justAuthSourceTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.justAuthSourceTable.refresh()
            },
            handleCreate () {
                this.resetJustAuthSourceForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['justAuthSourceForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['justAuthSourceForm'].validate(valid => {
                    if (valid) {
                        createJustAuthSource(this.justAuthSourceForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.justAuthSourceForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.justAuthSourceForm.status = row.status + ''
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['justAuthSourceForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['justAuthSourceForm'].validate(valid => {
                    if (valid) {
                        updateJustAuthSource(this.justAuthSourceForm).then(() => {
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
                        deleteJustAuthSource(row.id).then(() => {
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
                var justAuthSourceList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: justAuthSourceList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteJustAuthSource(that.selectedRowKeys).then(() => {
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
                updateJustAuthSourceStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.status = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadJustAuthSourceList(this.listJustAuthSourceQuery).then(response => {
                    handleDownloadBlod('租户第三方登录信息配置表数据列表.xlsx', response)
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
                uploadJustAuthSource(formData).then(() => {
                    this.uploading = false
                    this.$message.success('租户第三方登录信息配置表数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('租户第三方登录信息配置表数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadJustAuthSourceTemplate(this.listJustAuthSourceQuery).then(response => {
                    handleDownloadBlod('租户第三方登录信息配置表批量上传模板.xlsx', response)
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
