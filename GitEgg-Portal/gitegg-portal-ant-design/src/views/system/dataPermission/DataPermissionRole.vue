<template>
  <div style="">
    <a-row :gutter="10">
      <a-col :span="5">
        <a-card :bordered="false">
          <resource-tree-select :onSelect="resourceSelect"
                                :selectedKeys="leftSelectedKeys">
          </resource-tree-select>
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
                  <a-form-model-item label="数据权限名称">
                    <a-input v-model.trim="listQuery.dataName"
                             placeholder="数据权限名称"
                             :max-length="100"
                             @keyup.enter.native="handleFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="6"
                       :sm="24">
                  <a-form-model-item label="Mapper接口名称">
                    <a-input v-model.trim="listQuery.dataMapperFunction"
                             placeholder="Mapper接口名称"
                             :max-length="200"
                             @keyup.enter.native="handleFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="6"
                       :sm="24">
                  <a-form-model-item label="数据权限类型">
                    <a-select v-model="listQuery.dataPermissionType"
                              placeholder="数据权限类型"
                              allow-clear
                              show-search
                              :filter-option="filterOption">
                      <a-select-option v-for="item in dataPermissionTypeList"
                                       :key="item.dictCode"
                                       :label="item.dictName"
                                       :value="item.dictCode">
                        {{ item.dictName }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </a-col>
                <template v-if="advanced">
                  <a-col :md="6"
                         :sm="24">
                    <a-form-model-item label="数据主表">
                      <a-input v-model.trim="listQuery.dataTableName"
                               placeholder="数据主表"
                               :max-length="100"
                               @keyup.enter.native="handleFilter" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :md="6"
                         :sm="24">
                    <a-form-model-item label="数据权限状态">
                      <a-select v-model="listQuery.status"
                                placeholder="数据权限状态"
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
                </template>
                <a-col :md="!advanced && 6 || 24"
                       :sm="24">
                  <span class="table-page-search-submitButtons"
                        :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                    <a-button type="primary"
                              @click="handleFilter">查询</a-button>
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
                      :disabled="currentResourceId=== ''
                        || currentResourceType === '1' || currentResourceType === '2'">新建</a-button>
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
          </div>

          <s-table ref="dataPermissionRoleTable"
                   size="default"
                   bordered
                   :rowKey="row=>row.id"
                   :columns="columns"
                   :data="loadData"
                   showPagination="auto"
                   :scroll="{x:true}"
                   :pagination="dataPermissionRolePagination"
                   :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }">
            <span slot="permissionType"
                  slot-scope="text, record">
              <span>{{ record.dataPermissionType | dataPermissionTypeFilter }}</span>
            </span>
            <span slot="status"
                  slot-scope="text, record">
              <a-tag :color="record.status | statusFilter">{{ record.status | statusNameFilter }}</a-tag>
            </span>
            <span slot="action"
                  slot-scope="text, record">
              <a @click="handleUpdate(record)">编辑</a>
              <a-divider type="vertical" />
              <a href="javascript:;"
                 @click="queryDataPermissionRole(record)">角色关联</a>
              <a-divider type="vertical" />
              <a-dropdown>
                <a class="ant-dropdown-link">
                  更多
                  <a-icon type="down" />
                </a>
                <a-menu slot="overlay">
                  <a-menu-item>
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
                  <a-menu-item>
                    <a href="javascript:;"
                       @click="handleDelete(record)">删除</a>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
          </s-table>

          <a-drawer :title="textMap[dialogStatus]"
                    placement="right"
                    :closable="false"
                    :destroyOnClose="true"
                    :maskClosable="false"
                    :visible="dialogFormVisible"
                    :width="700"
                    @close="dialogFormVisible = false">
            <!-- <a-card class="box-card"> -->
            <a-form-model
              ref="dataPermissionRoleForm"
              :model="dataPermissionRoleForm"
              :rules="rules"
              :label-col="dataPermissionRoleLabelCol"
              :wrapper-col="dataPermissionRoleWrapperCol">
              <a-form-model-item label="功能权限名称" prop="resourceId">
                <span>{{ currentResourceName || dataPermissionRoleForm.resourceName }}</span>
              </a-form-model-item>
              <a-form-model-item label="数据权限名称" prop="dataName">
                <a-input v-model="dataPermissionRoleForm.dataName" placeholder="输入数据权限名称" :maxLength="100" />
              </a-form-model-item>
              <a-form-model-item label="Mapper全路径" prop="dataMapperFunction">
                <a-input v-model="dataPermissionRoleForm.dataMapperFunction" placeholder="输入数据权限对应的mapper方法全路径" :maxLength="200" />
              </a-form-model-item>
              <a-form-model-item label="数据权限类型" prop="dataPermissionType">
                <a-select v-model="dataPermissionRoleForm.dataPermissionType"
                          placeholder="数据权限类型"
                          allow-clear
                          show-search
                          :filter-option="filterOption">
                  <a-select-option v-for="item in dataPermissionTypeList"
                                   :key="item.dictCode"
                                   :label="item.dictName"
                                   :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="权限主表" prop="dataTableName">
                <a-input v-model="dataPermissionRoleForm.dataTableName" placeholder="输入需要做数据权限主表" :maxLength="100" />
              </a-form-model-item>
              <a-form-model-item label="权限主表别名" prop="dataTableAlias">
                <a-input v-model="dataPermissionRoleForm.dataTableAlias" placeholder="输入需要做数据权限表的别名" :maxLength="32" />
              </a-form-model-item>
              <!-- <a-divider orientation="left">
                数据权限表配置
              </a-divider> -->
              <a-form-model-item label="数据权限表" prop="innerTableName">
                <a-input v-model="dataPermissionRoleForm.innerTableName" placeholder="输入数据权限表,默认t_sys_organization" :maxLength="100" />
              </a-form-model-item>
              <a-form-model-item label="数据权限表别名" prop="innerTableAlias">
                <a-input v-model="dataPermissionRoleForm.innerTableAlias" placeholder="输入数据权限表的别名,默认organization" :maxLength="32" />
              </a-form-model-item>
              <!-- <a-divider orientation="left">
                数据权限字段配置
              </a-divider> -->
              <a-form-model-item label="需排除的字段" prop="dataColumnExclude">
                <a-input v-model="dataPermissionRoleForm.dataColumnExclude" placeholder="输入数据权限需要排除的字段"/>
              </a-form-model-item>
              <a-form-model-item label="仅保留的字段" prop="dataColumnInclude">
                <a-input v-model="dataPermissionRoleForm.dataColumnInclude" placeholder="输入数据权限需要保留的字段"/>
              </a-form-model-item>
              <a-form-model-item label="自定义表达式">
                <a-input v-model="dataPermissionRoleForm.customExpression" :autoSize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="请输入自定义表达式" />
              </a-form-model-item>
              <a-form-model-item label="状态" prop="status">
                <a-radio-group v-model="dataPermissionRoleForm.status" name="status">
                  <a-radio :value="1">启用</a-radio>
                  <a-radio :value="0">禁用</a-radio>
                </a-radio-group>
              </a-form-model-item>
              <a-form-model-item label="备注">
                <a-input v-model="dataPermissionRoleForm.comments" :autoSize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="请输入备注信息" />
              </a-form-model-item>
            </a-form-model>
            <!-- </a-card> -->
            <div class="footer-button">
              <a-button @click="dialogFormVisible = false">取消</a-button>
              <a-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</a-button>
              <a-button v-else type="primary" @click="updateData">修改</a-button>
            </div>
          </a-drawer>

          <a-drawer :title="drawerTitle"
                    placement="right"
                    :closable="false"
                    :destroyOnClose="true"
                    :maskClosable="false"
                    :width="700"
                    :visible="dialogDataPermissionVisible"
                    @close="dialogDataPermissionVisible = false">
            <a-card class="box-card">
              <s-table ref="roleTable"
                       size="default"
                       bordered
                       showPagination="auto"
                       :rowKey="row=>row.id"
                       :columns="roleColumns"
                       :data="loadRoleData"
                       :pagination="rolePagination"
                       :rowSelection="rowSelection">
                <span slot="status"
                      slot-scope="text, record">
                  <a-tag :color="record.roleStatus | statusFilter">{{ record.roleStatus | statusNameFilter }}</a-tag>
                </span>
              </s-table>

            </a-card>
            <div class="footer-button">
              <a-button @click="dialogDataPermissionVisible = false">取消</a-button>
              <a-button type="primary" @click="updateRoleDataPermission">确定</a-button>
            </div>
          </a-drawer>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { STable, ResourceTreeSelect } from '@/components'
import { queryDataPermissionRoleList, createDataPermissionRole, deleteDataPermissionRole, batchDeleteDataPermissionRole, updateDataPermissionRole, updateDataPermissionRoleStatus, checkDataPermissionRoleExist, queryRoleDataPermission, updateRoleDataPermission } from '@/api/system/dataPermission/data_permission'
import { fetchResourceList } from '@/api/system/resource'
import { fetchList } from '@/api/system/role'
import { listDict } from '@/api/system/base/dict'
let vm = {}
export default {
  name: 'DataPermissionRoleTable',
  components: {
    STable,
    ResourceTreeSelect
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
        0: '禁用'
      }
      return statusNameMap[status]
    },
    dataPermissionTypeFilter (dataType) {
      return vm.dataPermissionTypeFilterMap[dataType]
    }
  },
  data () {
    var validDataMapperFunction = (rule, value, callback) => {
      if (value) {
        var keyData = {
          id: this.dataPermissionRoleForm.id,
          checkField: 'data_mapper_function',
          checkValue: value
        }
        checkDataPermissionRoleExist(keyData).then(response => {
          if (!response.data) {
            callback(new Error('Mapper方法已存在'))
          } else {
            callback()
          }
        })
      }
    }
    vm = this
    return {
      dataPermissionRoleLabelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      dataPermissionRoleWrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      form: null,
      advanced: false,
      tableKey: 0,
      roleList: [],
      provinceOptions: null,
      treeProps: {
        children: 'children', title: 'resourceName', key: 'id'
      },
      list: null,
      total: 0,
      listLoading: true,
      dataPermissionTypeList: [],
      dataPermissionTypeFilterMap: {},
      listQuery: {
        resourceId: '',
        dataName: '',
        dataMapperFunction: '',
        dataTableName: '',
        dataPermissionType: '',
        status: undefined
      },
      statusOption: [
        { label: '启用', key: 1 },
        { label: '禁用', key: 0 }
      ],
      leftSelectedKeys: [],
      dialogFormVisible: false,
      dialogDataPermissionVisible: false,
      drawerTitle: '设置拥有数据权限的角色',
      checkStrictly: true,
      checkable: true,
      dialogStatus: '',
      textMap: {
        update: '编辑数据权限规则',
        create: '添加数据权限规则'
      },
      currentResourceId: '',
      currentResourceName: '',
      currentResourceType: '',
      dataPermissionRoleForm: {
        id: '',
        resourceId: '',
        dataName: '',
        dataMapperFunction: '',
        dataTableName: '',
        dataTableAlias: '',
        dataColumnExclude: '',
        dataColumnInclude: '',
        innerTableName: '',
        innerTableAlias: '',
        dataPermissionType: '',
        customExpression: '',
        status: 1,
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
          title: '资源接口名称',
          align: 'center',
          ellipsis: true,
          width: 200,
          dataIndex: 'resourceName'
        },
        {
          title: '数据权限名称',
          align: 'center',
          width: 130,
          ellipsis: true,
          dataIndex: 'dataName'
        },
        {
          title: '权限类型',
          align: 'center',
          dataIndex: 'dataPermissionType',
          width: 100,
          ellipsis: true,
          scopedSlots: { customRender: 'permissionType' }
        },
        {
           title: 'Mapper方法名称',
           width: 130,
           ellipsis: true,
           align: 'center',
           dataIndex: 'dataMapperFunction'
        },
        {
          title: '数据主表',
          align: 'center',
          width: 130,
          ellipsis: true,
          dataIndex: 'dataTableName'
        },
        {
          title: '数据权限表',
          align: 'center',
          ellipsis: true,
          width: 150,
          dataIndex: 'innerTableName'
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
        dataName: [
          { required: true, message: '请输入数据权限名称', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        dataMapperFunction: [
          { required: true, message: '请输入数据权限对应的mapper方法全路径', trigger: 'blur' },
          { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' },
          { validator: validDataMapperFunction, trigger: 'blur' }
        ],
        dataTableName: [
          { required: true, message: '请输入数据主表', trigger: 'blur' }
        ],
        dataTableAlias: [
          { required: true, message: '请输入数据主表的别名', trigger: 'blur' }
        ],
        innerTableName: [
          { required: true, message: '请输入数据权限表', trigger: 'blur' }
        ],
        innerTableAlias: [
          { required: true, message: '请输入数据权限表的别名', trigger: 'change' }
        ],
        dataPermissionType: [
          { required: true, message: '请选择数据权限类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择数据权限状态', trigger: 'change' }
        ]
      },
      downloadLoading: false,
      selectedOrgOptions: [],
      propsOrg: {
        children: 'children',
        value: 'id',
        label: 'resourceName'
      },
      resourceList: [],
      resourceTreeList: [],
      selectedRowKeys: [],
      selectedRows: [],
      dataPermissionRolePagination: {
        defaultPageSize: 10,
        showQuickJumper: true,
        defaultCurrent: 1,
        showTotal: (total, range) => `共 ${total} 条`
      },
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return queryDataPermissionRoleList(Object.assign(parameter, this.listQuery))
          .then(res => {
            this.list = res.data.records
            return res.data
          })
      },
      listRoleQuery: {
        id: ''
      },
      // 角色表头
      roleColumns: [
        {
          title: '序号',
          align: 'center',
          dataIndex: 'id'
        },
        {
          title: '角色名称',
          align: 'center',
          dataIndex: 'roleName'
        },
        {
          title: '角色状态',
          align: 'center',
          dataIndex: 'roleStatus',
          scopedSlots: { customRender: 'status' }
        }
      ],
      currentDataPermissionId: '',
      roleDataPermissionList: [],
      selectedRoleRowKeys: [],
      selectedRoleRows: [],
      oldSelectedRoleList: [],
      oldSelectedRoleIdList: [],
      dataPermissionRoleData: {
        dataPermissionId: '',
        addRoles: [],
        delRoles: []
      },
      rolePagination: {
        defaultPageSize: 10,
        // showQuickJumper: true,
        showLessItems: true,
        defaultCurrent: 1,
        showTotal: (total, range) => `共 ${total} 条`
      },
      // 加载数据方法 必须为 Promise 对象
      loadRoleData: parameter => {
        return fetchList(Object.assign(parameter, this.listRoleQuery))
          .then(res => {
            this.roleDataPermissionList = res.data.records
            return res.data
          })
      },
      rowSelection: {
        type: 'checkbox',
        onSelect: (record, selected, selectedRows, nativeEvent) => {
          const roleIdList = vm.selectedRoleRowKeys
          const selectedRowKeys = vm.rowSelection.selectedRowKeys
          if (!selected) {
          // 这里是取消勾选，删除对应的数组项
            selectedRowKeys.map((x, item) => {
              if (x === record.id) {
                selectedRowKeys.splice(item, 1)
              }
            })
            roleIdList.map((x, item) => {
              if (x === record.id) {
                roleIdList.splice(item, 1)
              }
            })
          }
          if (selected) {
            // 这里是点击勾选，添加roleId字段到selectedRowKeys数组里
            if (vm.selectedRoleRowKeys.indexOf(record.id) === -1) {
                vm.selectedRoleRowKeys.push(record.id)
            }
            if (vm.rowSelection.selectedRowKeys.indexOf(record.id) === -1) {
                vm.rowSelection.selectedRowKeys.push(record.id)
            }
          }
        },
        // 全选
        onSelectAll: (selected, selectedRows, changeRows) => {
          console.log(selected, selectedRows, changeRows)
          if (selected) {
            changeRows.map((x) => {
              if (vm.selectedRoleRowKeys.indexOf(x.id) === -1) {
                  vm.selectedRoleRowKeys.push(x.id)
              }
              if (vm.rowSelection.selectedRowKeys.indexOf(x.id) === -1) {
                  vm.rowSelection.selectedRowKeys.push(x.id)
              }
            })
          }
          if (!selected) {
            const roleIdList = vm.selectedRoleRowKeys
            const selectedRowKeys = vm.rowSelection.selectedRowKeys
            changeRows.map((item, k) => {
              roleIdList.map((x, i) => {
                if (x === item.id) {
                  roleIdList.splice(i, 1)
                }
              })
              selectedRowKeys.map((x, i) => {
                if (x === item.id) {
                  selectedRowKeys.splice(i, 1)
                }
              })
            })
            vm.selectedRoleRowKeys = roleIdList
            vm.rowSelection.selectedRowKeys = selectedRowKeys
          }
        },
        selectedRowKeys: [],
        getCheckboxProps: (record) => {
          if (vm.selectedRoleRowKeys.indexOf(record.id) > -1) {
              vm.rowSelection.selectedRowKeys.push(record.id)
          }
          return {
            props: {
              defaultChecked: vm.selectedRoleRowKeys.indexOf(record.id) > -1
            }
          }
        }
      }
    }
  },
  created () {
    this.queryDataPermissionTypeList()
    this.getResourceList()
  },
  methods: {
    queryDataPermissionTypeList () {
      const that = this
      that.listLoading = true
      listDict('DATA_PERMISSION_TYPE').then(response => {
        that.dataPermissionTypeList = response.data
        that.dataPermissionTypeFilterMap = {}
        that.dataPermissionTypeList.forEach((item, index, arr) => {
          that.dataPermissionTypeFilterMap[item.dictCode] = item.dictName
        })
        that.listLoading = false
        })
    },
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
      queryDataPermissionRoleList(this.listQuery).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    getResourceList () {
      this.listLoading = true
      fetchResourceList({ parentId: 0 }).then(response => {
        this.resourceList = response.data
        if (this.resourceList) {
          var resourceListStr = JSON.stringify(this.resourceList)
          this.resourceTreeList = JSON.parse(resourceListStr.replace(/"isLeaf":1/g, '"isLeaf":true').replace(/"isLeaf":0/g, '"isLeaf":false'))
        }
        this.listLoading = false
      })
    },
    handleFilter () {
      this.$refs.dataPermissionRoleTable.refresh(true)
    },
    handleTableRefresh () {
      this.$refs.dataPermissionRoleTable.refresh()
    },
    resetListQuery () {
      this.listQuery = {}
      this.leftSelectedKeys = []
      this.listQuery.resourceId = this.currentResourceId
    },
    resetDataPermissionRoleForm () {
      this.dataPermissionRoleForm = {
        id: '',
        resourceId: '',
        dataName: '',
        dataMapperFunction: '',
        dataTableName: '',
        dataTableAlias: '',
        dataColumnExclude: '',
        dataColumnInclude: '',
        innerTableName: '',
        innerTableAlias: '',
        dataPermissionType: '',
        customExpression: '',
        status: 1,
        comments: ''
      }
    },
    handleCreate () {
      this.resetDataPermissionRoleForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataPermissionRoleForm'].clearValidate()
      })
    },
    createData () {
      this.dataPermissionRoleForm.resourceId = this.currentResourceId
      this.$refs['dataPermissionRoleForm'].validate(valid => {
        if (valid) {
          createDataPermissionRole(this.dataPermissionRoleForm).then(() => {
            this.dialogFormVisible = false
            this.resetListQuery()
            this.handleFilter()
            this.$message.success('创建成功')
          })
        }
      })
    },
    handleUpdate (row) {
      this.resetDataPermissionRoleForm()
      this.dataPermissionRoleForm = Object.assign({}, row) // copy obj
      this.dataPermissionRoleForm.dataPermissionType = this.dataPermissionRoleForm.dataPermissionType + ''
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataPermissionRoleForm'].clearValidate()
      })
    },
    updateData () {
      this.$refs['dataPermissionRoleForm'].validate(valid => {
        if (valid) {
          updateDataPermissionRole(this.dataPermissionRoleForm).then(() => {
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
        title: '此操作将永久删除该数据权限规则：' + row.dataName + ', 是否继续?',
        content: '',
        onOk () {
          that.listLoading = true
          deleteDataPermissionRole(row.id).then(() => {
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
      var accountList = this.selectedRows.map(function (n) {
        return n.account
      })
      var that = this
      this.$confirm({
        title: '以下数据权限规则将被全部删除，是否继续?',
        content: accountList.join(','),
        onOk () {
          that.listLoading = true
          batchDeleteDataPermissionRole(that.selectedRowKeys).then(() => {
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
      updateDataPermissionRoleStatus(row.id, status).then(() => {
        this.listLoading = false
        row.status = status
        this.$message.success('状态修改成功')
      })
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    filter (inputValue, path) {
      return path.some(option => option.resourceName.toLowerCase().indexOf(inputValue.toLowerCase()) > -1)
    },
    resourceSelect (node) {
      this.currentResourceName = node.resourceName
      this.currentResourceType = node.resourceType
      this.listQuery.resourceId = node.id
      this.currentResourceId = node.id
      this.handleFilter()
    },
    queryDataPermissionRole (row) {
      this.drawerTitle = '查看角色和 [' + row.dataName + '] 数据权限的关联'
      this.dialogStatus = 'update'
      this.currentDataPermissionId = row.id
      this.getRoleDataPermissionList()
      this.dialogDataPermissionVisible = true
    },
    getRoleDataPermissionList () {
         const that = this
         that.listLoading = true
         that.oldSelectedRoleList = []
         that.selectedRoleRowKeys = []
         that.oldSelectedRoleIdList = []
         that.rowSelection.selectedRowKeys = []
         queryRoleDataPermission(that.currentDataPermissionId).then(response => {
          if (response.data) {
              that.oldSelectedRoleList = response.data
              that.oldSelectedRoleList.forEach(function (item) {
              that.oldSelectedRoleIdList.push(item.roleId)
              that.selectedRoleRowKeys.push(item.roleId)
            })
          }
          that.listLoading = false
       })
    },
    updateRoleDataPermission () {
      var that = this
      that.listLoading = true
      var ids = that.selectedRoleRowKeys
      var addRoleIds = ids.filter(function (v) { return that.oldSelectedRoleIdList.indexOf(v) === -1 })
      var delRoleIds = that.oldSelectedRoleIdList.filter(function (v) { return ids.indexOf(v) === -1 })
      if (addRoleIds && addRoleIds.length > 0) {
        for (var k = 0; k < addRoleIds.length; k++) {
          that.dataPermissionRoleData.addRoles[k] = { dataPermissionId: that.currentDataPermissionId, roleId: addRoleIds[k] }
        }
      }
      if (delRoleIds && delRoleIds.length > 0) {
        for (var q = 0; q < delRoleIds.length; q++) {
          that.dataPermissionRoleData.delRoles[q] = { dataPermissionId: that.currentDataPermissionId, roleId: delRoleIds[q] }
        }
      }
      that.dataPermissionRoleData.dataPermissionId = that.currentDataPermissionId
      updateRoleDataPermission(that.dataPermissionRoleData).then(response => {
        that.dialogDataPermissionVisible = false
        that.listLoading = false
        that.$message.success('数据权限修改成功')
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
