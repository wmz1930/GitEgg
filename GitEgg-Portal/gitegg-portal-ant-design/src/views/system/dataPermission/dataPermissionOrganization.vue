<template>
  <div style="">
    <a-alert style="text-align:center;" message="【机构权限用户】功能用于查询拥有某个机构权限的所有用户，【用户管理】页面只查询属于某个机构的所有用户" type="info"/>
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
                <a-col :md="6"
                       :sm="24">
                  <span class="table-page-search-submitButtons"
                        :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                    <a-button type="primary"
                              @click="handleFilter">查询</a-button>
                    <a-button style="margin-left: 8px"
                              @click="resetListQuery">重置</a-button>
                  </span>
                </a-col>
              </a-row>
            </a-form-model>
          </div>

          <div class="table-operator">
            <a-row :gutter="48">
              <a-col :md="12" :sm="24" >
                <a-dropdown v-if="selectedRowKeys.length > 0">
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
              </a-col>
              <a-col :md="12" :sm="24">
                <a-alert v-if="currentOrganizationName === ''" message="请选择左侧机构进行查询" type="error" />
                <a-alert v-else :message="'当前选择机构：' + currentOrganizationName" type="success"/>
              </a-col>
            </a-row>

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
              <a href="javascript:;" @click="handleDelete(record)">删除机构权限</a>
            </span>
          </s-table>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { STable, OrganizationTreeSelect } from '@/components'
import { fetchOrganizationDataList, fetchRoleList, batchDeleteOrganizationData } from '@/api/system/user'
import { fetchOrgList } from '@/api/system/organization'
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
    return {
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
      currentOrganizationId: '',
      currentOrganizationName: '',
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
        return fetchOrganizationDataList(Object.assign(parameter, this.listQuery))
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
      fetchOrganizationDataList(this.listQuery).then(response => {
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
    getRoleList () {
      this.listLoading = true
      fetchRoleList().then(response => {
        this.roleList = response.data
        this.listLoading = false
      })
    },
    handleFilter () {
      this.$refs.userTable.refresh(true)
    },
    handleTableRefresh () {
      this.$refs.userTable.refresh()
    },
    resetListQuery () {
      this.listQuery = {}
      this.listQuery.organizationId = this.currentOrganizationId
      this.leftSelectedKeys = []
    },
    resetDataPermissionForm () {
      this.dataPermissionForm = {
        userId: '',
        addDataPermissions: [],
        removeDataPermissions: []
      }
    },
    handleDelete (row) {
      var that = this
      that.selectedRowKeys = []
      that.selectedRows = []
      this.selectedRowKeys.push(row.id)
      this.selectedRows.push(row)
      this.handleBatchDelete()
    },
    handleBatchDelete () {
      if (this.listQuery.organizationId === '') {
        this.$message.error('请从左侧选择机构')
        return
      }

      var accountList = this.selectedRows.map(function (n) {
        return n.account
      })
      var that = this
      this.$confirm({
        title: '以下用户将被全部取消【' + this.currentOrganizationName + '】机构权限，是否继续?',
        content: accountList.join(','),
        onOk () {
          that.listLoading = true
          batchDeleteOrganizationData(that.selectedRowKeys).then(() => {
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
      this.currentOrganizationId = node.id
      this.currentOrganizationName = node.organizationName
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
