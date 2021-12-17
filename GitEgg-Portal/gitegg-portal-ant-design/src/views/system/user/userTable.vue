<template>
  <div style="">
    <a-row :gutter="10">
      <a-col :span="5">
        <a-card :bordered="false">
          <organization-tree-select :onSelect="organizationSelect"
                                    :selectedKeys="leftSelectedKeys"></organization-tree-select>
        </a-card>
      </a-col>
      <a-col :span="19">
        <a-card :bordered="false"
                class="content">
          <div class="table-page-search-wrapper">
            <a-form-model layout="inline">
              <a-row :gutter="48">
                <a-col :md="6"
                       :sm="24">
                  <a-form-model-item label="姓名">
                    <a-input v-model.trim="listQuery.realName"
                             placeholder="姓名"
                             :max-length="32"
                             @keyup.enter.native="handleFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="6"
                       :sm="24">
                  <a-form-model-item label="手机号码">
                    <a-input v-model.trim="listQuery.mobile"
                             placeholder="手机号码"
                             :max-length="11"
                             @keyup.enter.native="handleFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="6"
                       :sm="24">
                  <a-form-model-item label="用户角色">
                    <a-select v-model="listQuery.roleId"
                              placeholder="用户角色"
                              allow-clear
                              show-search
                              :filter-option="filterOption">
                      <a-select-option v-for="item in roleList"
                                       :key="item.roleKey"
                                       :value="item.id">
                        {{ item.roleName }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </a-col>
                <template v-if="advanced">
                  <a-col :md="6"
                         :sm="24">
                    <a-form-model-item label="邮箱地址">
                      <a-input v-model.trim="listQuery.email"
                               placeholder="邮箱地址"
                               :max-length="100"
                               @keyup.enter.native="handleFilter" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :md="6"
                         :sm="24">
                    <a-form-model-item label="状态">
                      <a-select v-model="listQuery.status"
                                placeholder="状态"
                                allow-clear
                                show-search
                                :filter-option="filterOption">
                        <a-select-option v-for="item in statusOption"
                                         :key="item.key"
                                         :value="item.key">
                          {{ item.label }}
                        </a-select-option>
                      </a-select>
                    </a-form-model-item>
                  </a-col>
                  <a-col :md="6"
                         :sm="24">
                    <a-form-model-item label="开始时间">
                      <a-date-picker v-model.trim="listQuery.startDate"
                                     placeholder="开始时间"
                                     valueFormat="YYYY-MM-DD"
                                     style="width:100%;" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :md="6"
                         :sm="24">
                    <a-form-model-item label="结束时间">
                      <a-date-picker v-model.trim="listQuery.endDate"
                                     placeholder="结束时间"
                                     valueFormat="YYYY-MM-DD"
                                     style="width:100%;" />
                    </a-form-model-item>
                  </a-col>
                </template>
                <a-col :md="!advanced && 6 || 24"
                       :sm="24">
                  <span class="table-page-search-submitButtons"
                        :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                    <a-button type="primary"
                              @click="handleFilter"
                              v-hasAnyPerms="['system:user:list']">查询</a-button>
                    <a-button style="margin-left: 8px"
                              @click="resetListQuery">重置</a-button>
                    <a @click="toggleAdvanced"
                       style="margin-left: 8px">
                      {{ advanced ? '收起' : '展开' }}
                      <a-icon :type="advanced ? 'up' : 'down'" />
                    </a>
                  </span>
                </a-col>
              </a-row>
            </a-form-model>
          </div>

          <div class="table-operator">
            <a-button type="primary"
                      icon="plus"
                      @click="handleCreate"
                      v-hasAnyPerms="['system:user:create']">新建</a-button>
            <a-button type="primary"
                      icon="cloud-download"
                      @click="handleDownload"
                      style="margin-left: 8px">导出</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0" v-hasAnyPerms="['system:user:delete', 'system:user:batch:delete']">
              <a-menu slot="overlay">
                <a-menu-item key="1"
                             @click="handleBatchDelete">
                  <a-icon type="delete" />删除
                </a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                批量操作
                <a-icon type="down" />
              </a-button>
            </a-dropdown>
          </div>

          <s-table ref="userTable"
                   size="default"
                   bordered
                   :rowKey="row=>row.id"
                   :columns="columns"
                   :data="loadData"
                   showPagination="auto"
                   :scroll="{x:true}"
                   :pagination="userPagination"
                   :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }">
            <span slot="gender"
                  slot-scope="text, record">
              <span>{{ record.gender | genderNameFilter }}</span>
            </span>
            <span slot="status"
                  slot-scope="text, record">
              <a-tag :color="record.status | statusFilter">{{ record.status | statusNameFilter }}</a-tag>
            </span>
            <span slot="createTime"
                  slot-scope="text, record">
              <span>{{ record.createTime | moment }}</span>
            </span>
            <span slot="action"
                  slot-scope="text, record">
              <a @click="handleUpdate(record)" v-hasAnyPerms="['system:user:update']">编辑</a>
              <a-divider type="vertical" />
              <a href="javascript:;"
                 @click="handleDataPermission(record)"
                 v-hasAnyPerms="['system:user:update:organization:data:permission']">机构权限</a>
              <a-divider type="vertical" />
              <a-dropdown>
                <a class="ant-dropdown-link">
                  更多
                  <a-icon type="down" />
                </a>
                <a-menu slot="overlay">
                  <a-menu-item>
                    <a href="javascript:;"
                       @click="handleResetUserPassword(record)"
                       v-hasAnyPerms="['system:user:password:reset']">重置密码</a>
                  </a-menu-item>
                  <a-menu-item v-hasAnyPerms="['system:user:status']">
                    <a href="javascript:;"
                       v-if="record.status!=1"
                       size="mini"
                       type="success"
                       @click="handleModifyStatus(record,1)">启用
                    </a>
                    <a href="javascript:;"
                       v-if="record.status!=0 && record.status!=2"
                       size="mini"
                       @click="handleModifyStatus(record,0)">禁用
                    </a>
                  </a-menu-item>
                  <a-menu-item v-hasAnyPerms="['system:user:delete', 'system:user:batch:delete']">
                    <a href="javascript:;"
                       @click="handleDelete(record)">删除</a>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
          </s-table>

          <a-drawer :title="textMap[dialogStatus]"
                    :maskClosable="false"
                    :visible="dialogFormVisible"
                    placement="right"
                    :width="700"
                    @cancel="() => dialogFormVisible = false">
            <a-form-model ref="userForm"
                          :model="userForm"
                          :rules="rules"
                          :label-col="userLabelCol"
                          :wrapper-col="userWrapperCol">
              <a-form-model-item label="组织机构"
                                 prop="orgList">
                <a-cascader :options="orgList"
                            v-model="selectedOrgOptions"
                            :field-names="propsOrg"
                            :show-search="{ filter }"
                            :display-render="displayRender"
                            expand-trigger="hover"
                            placeholder="组织机构" />
              </a-form-model-item>
              <a-form-model-item label="用户账号"
                                 prop="account">
                <a-input v-model="userForm.account"
                         placeholder="输入用户账号"
                         :max-length="32" />
              </a-form-model-item>
              <a-form-model-item label="用户昵称"
                                 prop="nickname">
                <a-input v-model="userForm.nickname"
                         placeholder="输入用户昵称"
                         :max-length="32" />
              </a-form-model-item>
              <a-form-model-item label="用户姓名"
                                 prop="realName">
                <a-input v-model="userForm.realName"
                         placeholder="输入用户姓名"
                         :max-length="32" />
              </a-form-model-item>
              <a-form-model-item label="手机号码"
                                 prop="mobile">
                <a-input v-model="userForm.mobile"
                         placeholder="输入用户手机号码"
                         :max-length="11" />
              </a-form-model-item>
              <a-form-model-item label="电子邮箱"
                                 prop="email">
                <a-input v-model="userForm.email"
                         placeholder="输入用户电子邮箱"
                         :max-length="32" />
              </a-form-model-item>
              <a-form-model-item label="用户角色"
                                 prop="roleId">
                <a-select v-model="userForm.roleIds"
                          placeholder="选择用户角色"
                          allow-clear
                          show-search
                          :filter-option="filterOption"
                          mode="multiple">
                  <a-select-option v-for="item in roleList"
                                   :key="item.roleKey"
                                   :value="item.id">
                    {{ item.roleName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="用户地址"
                                 prop="areas">
                <a-cascader v-model="userForm.areas"
                            :options="provinceOptions"
                            placeholder="输选择用户地址"
                            style="width:100%;" />
              </a-form-model-item>
              <a-form-model-item label="详细地址"
                                 prop="street">
                <a-input v-model="userForm.street"
                         placeholder="详细地址"
                         :max-length="120" />
              </a-form-model-item>
              <a-form-model-item label="性别"
                                 prop="gender">
                <a-radio-group v-model="userForm.gender"
                               name="gender">
                  <a-radio :value="'1'">男性</a-radio>
                  <a-radio :value="'0'">女性</a-radio>
                  <a-radio :value="'2'">保密</a-radio>
                </a-radio-group>
              </a-form-model-item>
              <a-form-model-item label="状态"
                                 prop="status">
                <a-radio-group v-model="userForm.status"
                               name="status">
                  <a-radio :value="1">启用</a-radio>
                  <a-radio :value="0">禁用</a-radio>
                  <a-radio :value="2">未激活</a-radio>
                </a-radio-group>
              </a-form-model-item>
              <a-form-model-item label="备注">
                <a-input v-model="userForm.comments"
                         :autoSize="{ minRows: 2, maxRows: 4}"
                         type="textarea"
                         placeholder="请输入备注信息" />
              </a-form-model-item>
            </a-form-model>
            <div class="footer-button">
              <a-button @click="dialogFormVisible = false">取消</a-button>
              <a-button v-if="dialogStatus=='create'" type="primary" @click="createData" v-hasAnyPerms="['system:user:create']">确定</a-button>
              <a-button v-else type="primary" @click="updateData" v-hasAnyPerms="['system:user:update']">修改</a-button>
            </div>
          </a-drawer>

          <a-drawer :title="drawerTitle"
                    placement="right"
                    :closable="false"
                    :destroyOnClose="true"
                    :maskClosable="false"
                    :width="600"
                    :visible="dialogDataPermissionVisible"
                    @close="dialogDataPermissionVisible = false">
            <a-card class="box-card">
              <organization-tree-select :treeId="'orgPermissionTree'"
                                        :checkable="checkable"
                                        :onCheck="computeOrgPermission"
                                        :checkStrictly="checkStrictly"
                                        :expandedKeys="userCheckOrgPermission"
                                        :checkedKeys="userCheckOrgPermission"></organization-tree-select>
            </a-card>
            <div class="footer-button">
              <a-button @click="dialogDataPermissionVisible = false">取消</a-button>
              <a-button type="primary"
                        @click="updateDataPermission"
                        v-hasAnyPerms="['system:user:update:organization:data:permission']">确定</a-button>
            </div>
          </a-drawer>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { STable, OrganizationTreeSelect } from '@/components'
import { fetchList, createUser, resetUserPassword, deleteUser, batchDeleteUser, updateUser, updateUserStatus, fetchRoleList, updateUserDataPermission, checkUserExist } from '@/api/system/user'
import { fetchOrgList } from '@/api/system/organization'
import moment from 'moment'
import Data from '@/api/pcaa'
export default {
  name: 'UserTable',
  components: {
    STable,
    OrganizationTreeSelect
  },
  filters: {
    statusFilter (status) {
      const statusMap = {
        1: 'green',
        2: '',
        0: 'pink'
      }
      return statusMap[status]
    },
    statusNameFilter (status) {
      const statusNameMap = {
        1: '启用',
        2: '未激活',
        0: '禁用'
      }
      return statusNameMap[status]
    },
    genderNameFilter (sex) {
      const sexNameMap = {
        '1': '男',
        '2': '保密',
        '0': '女'
      }
      return sexNameMap[sex]
    }
  },
  data () {
    var validAccount = (rule, value, callback) => {
      var keyData = {
        id: this.userForm.id,
        checkField: 'account',
        checkValue: value
      }
      checkUserExist(keyData).then(response => {
        if (!response.data) {
          callback(new Error('用户账号已存在'))
        } else {
          callback()
        }
      })
    }
    var validnickname = (rule, value, callback) => {
      if (value) {
        var keyData = {
          id: this.userForm.id,
          checkField: 'nickname',
          checkValue: value
        }
        checkUserExist(keyData).then(response => {
          if (!response.data) {
            callback(new Error('用户昵称已存在'))
          } else {
            callback()
          }
        })
      }
    }
    var validmobile = (rule, value, callback) => {
      var keyData = {
        id: this.userForm.id,
        checkField: 'mobile',
        checkValue: value
      }
      checkUserExist(keyData).then(response => {
        if (!response.data) {
          callback(new Error('手机号已存在'))
        } else {
          callback()
        }
      })
    }
    var validemail = (rule, value, callback) => {
      var keyData = {
        id: this.userForm.id,
        checkField: 'email',
        checkValue: value
      }
      checkUserExist(keyData).then(response => {
        if (!response.data) {
          callback(new Error('电子邮箱已存在'))
        } else {
          callback()
        }
      })
    }
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      userLabelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      userWrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      form: null,
      advanced: false,
      tableKey: 0,
      roleList: null,
      provinceOptions: null,
      treeProps: {
        children: 'children', title: 'organizationName', key: 'id'
      },
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        realName: '',
        mobile: '',
        email: '',
        roleIds: [],
        organizationId: '',
        status: undefined
      },
      statusOption: [
        { label: '启用', key: 1 },
        { label: '禁用', key: 0 },
        { label: '未激活', key: 2 }
      ],
      leftSelectedKeys: [],
      dialogFormVisible: false,
      dialogDataPermissionVisible: false,
      drawerTitle: '设置用户机构权限',
      checkStrictly: true,
      checkable: true,
      dialogStatus: '',
      textMap: {
        update: '编辑用户信息',
        create: '添加用户'
      },
      userForm: {
        id: '',
        account: '',
        nickname: '',
        realName: '',
        mobile: '',
        email: '',
        roleIds: [],
        organizationId: '',
        gender: '1',
        status: 1,
        areas: [],
        street: '',
        comments: ''
      },
      dataPermissionForm: {
        userId: '',
        addDataPermissions: [],
        removeDataPermissions: []
      },
      // 表头
      columns: [
        {
          title: '序号',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '组织机构',
          align: 'center',
          ellipsis: true,
          width: 200,
          dataIndex: 'organizationName'
        },
        {
          title: '账号',
          align: 'center',
          width: 100,
          ellipsis: true,
          dataIndex: 'account'
        },
        {
          title: '姓名',
          align: 'center',
          width: 100,
          ellipsis: true,
          dataIndex: 'realName'
        },
        {
          title: '手机号',
          align: 'center',
          width: 130,
          ellipsis: true,
          dataIndex: 'mobile'
        },
        {
          title: '邮箱',
          align: 'center',
          ellipsis: true,
          width: 150,
          dataIndex: 'email'
        },
        {
          title: '角色',
          align: 'center',
          width: 150,
          ellipsis: true,
          dataIndex: 'roleName'
        },
        {
          title: '性别',
          align: 'center',
          dataIndex: 'gender',
          width: 100,
          ellipsis: true,
          scopedSlots: { customRender: 'gender' }
        },
        {
          title: '注册日期',
          align: 'center',
          dataIndex: 'createTime',
          width: 200,
          scopedSlots: { customRender: 'createTime' }
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          width: 100,
          scopedSlots: { customRender: 'status' }
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          width: '200px',
          fixed: 'right',
          scopedSlots: { customRender: 'action' }
        }
      ],
      rules: {
        account: [
          { required: true, message: '请输入用户账号', trigger: 'blur' },
          { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' },
          { validator: validAccount, trigger: 'blur' }
        ],
        nickname: [
          { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
          { validator: validnickname, trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入用户姓名', trigger: 'blur' },
          { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' }
        ],
        mobile: [
          {
            pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/,
            required: true,
            message: '请输入正确的手机号',
            trigger: 'blur'
          },
          {
            min: 11,
            max: 11,
            message: '长度在 11 到 11 个字符',
            trigger: 'blur'
          },
          { validator: validmobile, trigger: 'blur' }
        ],
        email: [
          {
            type: 'email',
            required: true,
            message: '请输入正确的邮箱',
            trigger: 'blur'
          },
          { min: 5, max: 32, message: '长度在 5 到 32 个字符', trigger: 'blur' },
          { validator: validemail, trigger: 'blur' }
        ],
        roleIds: [
          { required: true, message: '请选择用户角色', trigger: 'change' }
        ],
        organizationId: [
          { required: true, message: '请选择组织机构', trigger: 'change' }
        ],
        gender: [
          { required: true, message: '请选择用户性别', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择用户状态', trigger: 'change' }
        ],
        comments: [
          { required: true, message: '请填写备注信息', trigger: 'blur' }
        ]
      },
      downloadLoading: false,
      selectedOrgOptions: [],
      propsOrg: {
        children: 'children',
        value: 'id',
        label: 'organizationName'
      },
      orgList: [],
      orgTreeList: [],
      orgSearchTreeList: [],
      userCheckOrgPermission: [],
      addOrgPermission: [],
      removeOrgPermission: [],
      selectedRowKeys: [],
      selectedRows: [],
      userPagination: {
        defaultPageSize: 10,
        showQuickJumper: true,
        defaultCurrent: 1,
        showTotal: (total, range) => `共 ${total} 条`
      },
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return fetchList(Object.assign(parameter, this.listQuery))
          .then(res => {
            this.list = res.data
            return res
          })
      }
    }
  },
  created () {
    this.getOrgList()
    this.getRoleList()
    this.getAreaList()
  },
  methods: {
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    displayRender ({ labels }) {
      return labels[labels.length - 1]
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    getList () {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.count
        this.listLoading = false
      })
    },
    getOrgList () {
      this.listLoading = true
      fetchOrgList({ parentId: 0 }).then(response => {
        this.orgList = response.data
        if (this.orgList) {
          var orgListStr = JSON.stringify(this.orgList)
          this.orgTreeList = JSON.parse(orgListStr.replace(/"isLeaf":1/g, '"isLeaf":true').replace(/"isLeaf":0/g, '"isLeaf":false'))
        }
        this.listLoading = false
      })
    },
    selectOrgListByLastId (orgList, lastId) {
      // 递归查询机构父机构，用于展示已选中的机构
      var orgStr = ''
      if (orgList) {
        for (var org of orgList) {
          // a-tree的isLeaf必须为boolean类型，这里需要转换一下
          if (org.isLeaf === 1) {
            org.isLeaf = true
          } else {
            org.isLeaf = false
          }
          if (lastId === org.id) {
            return lastId
          } else if (org.children) {
            var childOrg = this.selectOrgListByLastId(org.children, lastId)
            if (childOrg) {
              orgStr = org.id + ',' + childOrg
              return orgStr
            }
          }
        }
      }
      return orgStr
    },
    computeOrgPermission (item, e) {
      var node = e.node
      var checked = e.checked
      if (checked) {
        // 如果原先不存在，则添加到新增列表
        if (this.userCheckOrgPermission.indexOf(node.dataRef.id) === -1) {
          this.addOrgPermission.push(node.dataRef.id)
        }
        // 如果在删除列表中，则从删除列表中删除
        var removeIndex = this.removeOrgPermission.indexOf(node.dataRef.id)
        if (removeIndex > -1) {
          this.removeOrgPermission.splice(removeIndex, 1)
        }
      } else {
        // 如果原先存在，则添加到删除列表
        if (this.userCheckOrgPermission.indexOf(node.dataRef.id) > -1) {
          this.removeOrgPermission.push(node.dataRef.id)
        }
        // 如果在新增列表中，则从新增列表中删除
        var addIndex = this.addOrgPermission.indexOf(node.dataRef.id)
        if (addIndex > -1) {
          this.addOrgPermission.splice(addIndex, 1)
        }
      }
    },
    getRoleList () {
      this.listLoading = true
      fetchRoleList().then(response => {
        this.roleList = response.data
        this.listLoading = false
      })
    },
    getAreaList () {
      var options = []
      for (var key in Data['86']) {
        var citys = []
        for (var keyc in Data[key]) {
          var areas = []
          for (var keya in Data[keyc]) {
            var area = { value: keya, label: Data[keyc][keya] }
            areas.push(area)
          }
          var city = { value: keyc, label: Data[key][keyc], children: areas }
          citys.push(city)
        }
        var province = { value: key, label: Data['86'][key], children: citys }
        options.push(province)
      }
      this.provinceOptions = options
    },
    handleFilter () {
      this.$refs.userTable.refresh(true)
    },
    handleTableRefresh () {
      this.$refs.userTable.refresh()
    },
    resetListQuery () {
      this.listQuery = {}
      this.leftSelectedKeys = []
    },
    resetUserForm () {
      this.userForm = {
        id: '',
        account: '',
        nickname: '',
        realName: '',
        mobile: '',
        email: '',
        roleIds: [],
        organizationId: '',
        gender: '1',
        status: 1,
        areas: [],
        street: '',
        comments: ''
      }
      if (this.listQuery.organizationId && this.listQuery.organizationId !== '') {
        var orgStr = this.selectOrgListByLastId(this.orgList, this.listQuery.organizationId) + ''
        this.selectedOrgOptions = orgStr.split(',')
      } else {
        this.selectedOrgOptions = []
      }
    },
    resetDataPermissionForm () {
      this.dataPermissionForm = {
        userId: '',
        addDataPermissions: [],
        removeDataPermissions: []
      }
    },
    handleCreate () {
      this.resetUserForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['userForm'].clearValidate()
      })
    },
    createData () {
      if (this.selectedOrgOptions.length > 0) {
        this.userForm.organizationId = this.selectedOrgOptions[this.selectedOrgOptions.length - 1]
      } else {
        this.userForm.organizationId = ''
      }
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          createUser(this.userForm).then(() => {
            this.dialogFormVisible = false
            this.handleFilter()
            this.$message.success('创建成功')
          })
        }
      })
    },
    handleUpdate (row) {
      this.resetUserForm()
      this.userForm = Object.assign({}, row) // copy obj
      this.userForm.password = undefined
      if (!this.userForm.areas || this.userForm.areas.length === 0) {
        this.userForm.areas = [
          this.userForm.province,
          this.userForm.city,
          this.userForm.area
        ]
      }

      if (this.userForm.organizationId) {
        var orgStr = this.selectOrgListByLastId(this.orgList, this.userForm.organizationId) + ''
        this.selectedOrgOptions = orgStr.split(',')
      }

      if (!(this.userForm.roleIds instanceof Array)) {
        var roleIds = this.userForm.roleIds.split(',')
        var arrRoleIds = []
        for (var roleId of roleIds) {
          arrRoleIds.push(roleId)
        }
        this.userForm.roleIds = arrRoleIds
      }

      this.userForm.status = this.userForm.status
      this.userForm.gender = this.userForm.gender
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['userForm'].clearValidate()
      })
    },
    updateData () {
      if (this.selectedOrgOptions.length > 0) {
        this.userForm.organizationId = this.selectedOrgOptions[this.selectedOrgOptions.length - 1]
      } else {
        this.userForm.organizationId = ''
      }
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          updateUser(this.userForm).then(() => {
            this.handleTableRefresh()
            this.dialogFormVisible = false
            this.$message.success('更新成功')
          })
        }
      })
    },
    handleDataPermission (row) {
      this.drawerTitle = '设置用户 [' + row.realName + '] 机构权限'
      this.dialogStatus = 'update'
      this.dialogDataPermissionVisible = true
      this.resetDataPermissionForm()
      this.userForm = Object.assign({}, row)

      this.addOrgPermission = []
      this.removeOrgPermission = []
      if (this.userForm.dataPermission) {
        this.userCheckOrgPermission = this.userForm.dataPermission.split(',')
      } else {
        this.userCheckOrgPermission = []
      }
      this.dataPermissionForm.userId = this.userForm.id
    },
    updateDataPermission () {
      if (this.addOrgPermission.length === 0 && this.removeOrgPermission.length === 0) {
        this.$message.error('权限没有进行任何变更')
        return
      }
      this.dataPermissionForm.addDataPermissions = this.addOrgPermission
      this.dataPermissionForm.removeDataPermissions = this.removeOrgPermission
      updateUserDataPermission(this.dataPermissionForm).then(() => {
        this.handleFilter()
        this.dialogDataPermissionVisible = false
        this.$message.success('更新成功')
      })
    },
    handleResetUserPassword (row) {
      var that = this
      this.$confirm({
        title: '该用户密码将被重置：' + row.account + ', 是否继续?',
        content: '',
        onOk () {
          that.listLoading = true
          resetUserPassword(row.id).then(() => {
            that.listLoading = false
            that.$message.success('重置成功!')
          })
        },
        onCancel () {
          that.$message.info('已取消重置')
        }
      })
    },
    handleDelete (row) {
      var that = this
      this.$confirm({
        title: '此操作将永久删除该用户：' + row.account + ', 是否继续?',
        content: '',
        onOk () {
          that.listLoading = true
          deleteUser(row.id).then(() => {
            that.listLoading = false
            that.$message.success('删除成功!')
            this.handleTableRefresh()
          })
        },
        onCancel () {
          that.$message.info('已取消删除')
        }
      })
    },
    handleBatchDelete () {
      var accountList = this.selectedRows.map(function (n) {
        return n.account
      })
      var that = this
      this.$confirm({
        title: '以下用户将被全部删除，是否继续?',
        content: accountList.join(','),
        onOk () {
          that.listLoading = true
          batchDeleteUser(that.selectedRowKeys).then(() => {
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
      updateUserStatus(row.id, status).then(() => {
        this.listLoading = false
        row.status = status
        this.$message.success('状态修改成功')
      })
    },
    handleDownload () {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [
          '序号',
          '账号',
          '姓名',
          '手机号',
          '邮箱',
          '角色',
          '性别',
          '注册时间',
          '状态'
        ]
        const filterVal = [
          'id',
          'account',
          'realName',
          'mobile',
          'email',
          'roleName',
          'gender',
          'createTime',
          'status'
        ]
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: '用户列表'
        })
        this.downloadLoading = false
      })
    },
    formatJson (filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === 'createTime') {
            return moment(v[j]).format('YYYY-MM-DD HH:mm:ss')
          } else if (j === 'gender') {
            return this.$options.filters['genderNameFilter'](v[j])
          } else if (j === 'status') {
            return this.$options.filters['statusNameFilter'](v[j])
          } else {
            return v[j]
          }
        })
      )
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    filter (inputValue, path) {
      return path.some(option => option.organizationName.toLowerCase().indexOf(inputValue.toLowerCase()) > -1)
    },
    organizationSelect (node) {
      this.listQuery.organizationId = node.id
      this.handleFilter()
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
