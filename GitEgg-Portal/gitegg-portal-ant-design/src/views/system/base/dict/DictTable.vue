<template>
  <div class="content">
    <a-row :gutter="10">
      <a-col :span="!childShow && 24 || 12">
        <a-card title="字典类型"
                :bordered="false"
                class="content">
          <div class="table-page-search-wrapper">
            <a-form-model layout="inline">
              <a-row :gutter="48">
                <a-col :md="8"
                       :sm="24">
                  <a-form-model-item label="字典名称">
                    <a-input v-model.trim="parentTreeQuery.dictName"
                             placeholder="请输入字典名称"
                             :max-length="32"
                             @keyup.enter.native="handleParentFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="8"
                       :sm="24">
                  <a-form-model-item label="字典值">
                    <a-input v-model.trim="parentTreeQuery.dictCode"
                             placeholder="请输入字典值"
                             :max-length="64"
                             @keyup.enter.native="handleParentFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="!advanced && 8 || 24"
                       :sm="24">
                  <span class="table-page-search-submitButtons"
                        :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                    <a-button type="primary"
                              @click="handleParentFilter">查询</a-button>
                    <a-button style="margin-left: 8px"
                              @click="resetParentQuery">重置</a-button>
                  </span>
                </a-col>
              </a-row>
            </a-form-model>
          </div>
          <div class="table-operator">
            <a-button type="primary"
                      icon="plus"
                      @click="handleCreate(true)">新建</a-button>
            <!-- <a-button type="primary" icon="cloud-download" @click="handleDownload" style="margin-left: 8px">导出</a-button> -->
            <a-dropdown v-if="selectedParentRowKeys.length > 0">
              <a-menu slot="overlay">
                <a-menu-item key="1"
                             @click="handleBatchDelete(true)">
                  <a-icon type="delete" />删除
                </a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                批量操作
                <a-icon type="down" />
              </a-button>
            </a-dropdown>
            <a-tag color="orange">
              点击记录可以查看对应的字典值列表
            </a-tag>
          </div>
          <s-table ref="parentDictTable"
                   size="default"
                   bordered
                   :rowKey="row=>row.id"
                   childrenColumnName="false"
                   :columns="parentColumns"
                   :data="loadParentData"
                   showPagination="auto"
                   :scroll="{x:true}"
                   :pagination="parentPagination"
                   :customRow="queryChildren"
                   :rowSelection="{ selectedParentRowKeys: this.selectedParentRowKeys, onChange: this.onParentSelectChange }">
            <span slot="dictStatus"
                  slot-scope="text, record">
              <a-tag :color="record.dictStatus | statusFilter">{{ record.dictStatus | statusNameFilter }}</a-tag>
            </span>
            <span slot="action"
                  slot-scope="text, record">
              <!-- <a @click="handleCreate(record)">新增</a>
              <a-divider type="vertical" /> -->
              <a @click="handleUpdate(record, true)">编辑</a>
              <a-divider type="vertical" />
              <a-dropdown>
                <a class="ant-dropdown-link">
                  更多
                  <a-icon type="down" />
                </a>
                <a-menu slot="overlay">
                  <a-menu-item>
                    <a href="javascript:;"
                       v-if="record.dictStatus!=1"
                       size="mini"
                       type="success"
                       @click="handleModifyStatus(record,1)">启用
                    </a>
                    <a href="javascript:;"
                       v-if="record.dictStatus!=0"
                       size="mini"
                       @click="handleModifyStatus(record,0)">禁用
                    </a>
                  </a-menu-item>
                  <a-menu-item>
                    <a @click="handleDelete(record, true)">删除</a>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
          </s-table>
        </a-card>
      </a-col>
      <a-col :span="12"
             v-if="childShow">
        <a-card :title="dictTitle + '字典值列表'"
                :bordered="false"
                class="content">
          <div class="table-page-search-wrapper">
            <a-form-model layout="inline">
              <a-row :gutter="48">
                <a-col :md="8"
                       :sm="24">
                  <a-form-model-item label="字典名称">
                    <a-input v-model.trim="treeQuery.dictName"
                             placeholder="请输入字典名称"
                             :max-length="32"
                             @keyup.enter.native="handleFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="8"
                       :sm="24">
                  <a-form-model-item label="字典值">
                    <a-input v-model.trim="treeQuery.dictCode"
                             placeholder="请输入字典值"
                             :max-length="64"
                             @keyup.enter.native="handleFilter" />
                  </a-form-model-item>
                </a-col>
                <a-col :md="!advanced && 8 || 24"
                       :sm="24">
                  <span class="table-page-search-submitButtons"
                        :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                    <a-button type="primary"
                              @click="handleFilter">查询</a-button>
                    <a-button style="margin-left: 8px"
                              @click="resetQuery">重置</a-button>
                  </span>
                </a-col>
              </a-row>
            </a-form-model>
          </div>
          <div class="table-operator">
            <a-button type="primary"
                      icon="plus"
                      @click="handleCreate(false)">新建</a-button>
            <!-- <a-button type="primary" icon="cloud-download" @click="handleDownload" style="margin-left: 8px">导出</a-button> -->
            <a-dropdown v-if="selectedRowKeys.length > 0">
              <a-menu slot="overlay">
                <a-menu-item key="1"
                             @click="handleBatchDelete(false)">
                  <a-icon type="delete" />删除
                </a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                批量操作
                <a-icon type="down" />
              </a-button>
            </a-dropdown>
          </div>
          <s-table ref="dictTable"
                   size="default"
                   bordered
                   :rowKey="row=>row.id"
                   childrenColumnName="false"
                   :columns="parentColumns"
                   :data="loadData"
                   showPagination="auto"
                   :scroll="{x:true}"
                   :pagination="parentPagination"
                   :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }">
            <span slot="dictStatus"
                  slot-scope="text, record">
              <a-tag :color="record.dictStatus | statusFilter">{{ record.dictStatus | statusNameFilter }}</a-tag>
            </span>
            <span slot="action"
                  slot-scope="text, record">
              <!-- <a @click="handleCreate(record)">新增</a>
              <a-divider type="vertical" /> -->
              <a @click="handleUpdate(record, false)">编辑</a>
              <a-divider type="vertical" />
              <a-dropdown>
                <a class="ant-dropdown-link">
                  更多
                  <a-icon type="down" />
                </a>
                <a-menu slot="overlay">
                  <a-menu-item>
                    <a href="javascript:;"
                       v-if="record.dictStatus!=1"
                       size="mini"
                       type="success"
                       @click="handleModifyStatus(record,1)">启用
                    </a>
                    <a href="javascript:;"
                       v-if="record.dictStatus!=0"
                       size="mini"
                       @click="handleModifyStatus(record,0)">禁用
                    </a>
                  </a-menu-item>
                  <a-menu-item>
                    <a @click="handleDelete(record, false)">删除</a>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
          </s-table>
        </a-card>
      </a-col>
    </a-row>

    <a-modal :title="textMap[dialogStatus]"
             v-model="dialogFormVisible"
             :width="800"
             :maskClosable="false"
             :destroyOnClose="true"
             @cancel="() => dialogFormVisible = false">
      <a-form-model ref="dictForm"
                    :model="dictForm"
                    :rules="rules"
                    label-width="100px"
                    :label-col="dictLabelCol"
                    :wrapper-col="dictWrapperCol">
        <a-form-model-item label="字典名称"
                           prop="dictName">
          <a-input v-model.trim="dictForm.dictName"
                   placeholder="输入字典名称"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字典值"
                           prop="dictCode">
          <a-input v-model.trim="dictForm.dictCode"
                   placeholder="输入字典类型/字典值"
                   :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="字典排序"
                           prop="dictOrder">
          <a-input v-model.number="dictForm.dictOrder"
                   placeholder="输入字典排序"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="字典状态"
                           prop="dictStatus">
          <a-radio-group v-model="dictForm.dictStatus">
            <a-radio :value="1">启用</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="备注信息">
          <a-input v-model.trim="dictForm.comments"
                   :autoSize="{ minRows: 2, maxRows: 4}"
                   type="textarea"
                   placeholder="请输入备注信息" />
        </a-form-model-item>
      </a-form-model>
      <div slot="footer"
           class="dialog-footer">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button v-if="dialogStatus=='create'"
                  type="primary"
                  @click="createData">确定</a-button>
        <a-button v-else
                  type="primary"
                  @click="updateData">修改</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { STable } from '@/components'
import { fetchList, createDict, updateDict, deleteDict, batchDeleteDict, updateDictStatus, checkDictExist } from '@/api/system/base/dict'

export default {
  name: 'DictTable',
  components: { STable },
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
    }
  },
  data () {
    var validDictCode = (rule, value, callback) => {
      var keyData = {
        id: this.dictForm.id,
        dictCode: value,
        parentId: this.dictForm.parentId
      }
      checkDictExist(keyData).then(response => {
        if (!response.data) {
          callback(new Error('字典Code已存在'))
        } else {
          callback()
        }
      })
    }
    return {
      childShow: false,
      advanced: false,
      dictLabelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      dictWrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      parentList: [],
      list: [],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '添加'
      },
      dialogPvVisible: false,
      parentTreeQuery: {
        parentId: 0,
        dictName: '',
        dictCode: ''
      },
      treeQuery: {
        parentId: '',
        dictName: '',
        dictCode: ''
      },
      dictForm: {
        id: '',
        parentId: 0,
        dictName: '',
        dictCode: '',
        dictStatus: 1,
        dictOrder: '',
        children: [], // 必须加，否则新增的节点不显示
        comments: ''
      },
      // 表头
      parentColumns: [
        {
          title: '字典名称',
          align: 'center',
          width: 100,
          ellipsis: true,
          dataIndex: 'dictName'
        },
        {
          title: '字典值',
          align: 'center',
          width: 100,
          ellipsis: true,
          dataIndex: 'dictCode'
        },
        {
          title: '字典排序',
          align: 'center',
          width: 100,
          dataIndex: 'dictOrder'
        },
        {
          title: '字典状态',
          align: 'center',
          width: 100,
          dataIndex: 'dictStatus',
          scopedSlots: { customRender: 'dictStatus' }
        },
        {
          title: '备注信息',
          align: 'center',
          width: 100,
          ellipsis: true,
          dataIndex: 'comments'
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          width: 130,
          fixed: 'right',
          scopedSlots: { customRender: 'action' }
        }
      ],
      parentTotal: 0,
      selectedParentRowKeys: [],
      selectedParentRows: [],
      parentPagination: {
        defaultPageSize: 10,
        showQuickJumper: true,
        defaultCurrent: 1,
        showTotal: (parentTotal, range) => `共 ${parentTotal} 条`
      },
      // 加载数据方法 必须为 Promise 对象
      loadParentData: parameter => {
        return fetchList(Object.assign(parameter, this.parentTreeQuery))
          .then(res => {
            this.parentList = res.data.records
            return res.data
          })
      },
      dictTitle: '',
      total: 0,
      selectedRowKeys: [],
      selectedRows: [],
      pagination: {
        defaultPageSize: 10,
        showQuickJumper: true,
        defaultCurrent: 1,
        showTotal: (total, range) => `共 ${total} 条`
      },
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return fetchList(Object.assign(parameter, this.treeQuery))
          .then(res => {
            this.list = res.data.records
            return res.data
          })
      },
      rules: {
        dictName: [
          { required: true, message: '请输入字典名称', trigger: 'blur' },
          { min: 1, max: 32, message: '长度在 2 到 32 个字符', trigger: 'blur' }
        ],
        dictCode: [
          { required: true, message: '请输入字典值', trigger: 'blur' },
          { min: 1, max: 64, message: '长度在 2 到 64 个字符', trigger: 'blur' },
          { validator: validDictCode, trigger: 'blur' }
        ],
        dictOrder: [
          { required: true, message: '请输入字典排序', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, required: true, message: '排序只能是数字', trigger: 'blur' }
        ],
        comments: [
          { required: true, message: '请填写备注信息', trigger: 'blur' }
        ]
      },
      args: [null, null, null]
    }
  },
  created () {

  },
  methods: {
    // 点击字典列表时，查询字典数据
    queryChildren (record, index) {
      const that = this
      return {
        on: {
          click: () => {
            if (!that.childShow) {
              that.childShow = true
            }
            that.resetQuery()
            that.treeQuery.parentId = record.id
            that.dictTitle = '[' + record.dictName + '] '
            that.handleFilter()
            that.showChild = true
          }
        }
      }
    },
    // 刷新字典类型表，分页初始化
    handleParentFilter () {
      this.$refs.parentDictTable.refresh(true)
    },
    // 刷新字典类型表，分页留在当前页
    handleParentTableRefresh () {
      this.$refs.parentDictTable.refresh()
    },
    // 刷新字典值表，分页初始化
    handleFilter () {
      this.$refs.dictTable.refresh(true)
    },
    // 刷新字典值表，分页留在当前页
    handleTableRefresh () {
      this.$refs.dictTable.refresh()
    },
    // 字典类型表选中
    onParentSelectChange (selectedRowKeys, selectedRows) {
      this.selectedParentRowKeys = selectedRowKeys
      this.selectedParentRows = selectedRows
    },
    // 字典值表选中
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    // 重置字典类型表查询条件
    resetParentQuery () {
      this.parentTreeQuery = {
        parentId: 0,
        dictName: '',
        dictCode: ''
      }
    },
    // 重置字典值表查询条件
    resetQuery () {
      this.treeQuery.dictName = ''
      this.treeQuery.dictCode = ''
    },
    // 重置数据字典表单
    resetDictForm () {
      this.dictForm = {
        id: '',
        parentId: 0,
        dictName: '',
        dictCode: '',
        dictStatus: 1,
        dictOrder: '',
        comments: ''
      }
    },
    // 初始化新增字典弹出框及表单
    handleCreate (rootFlag) {
      this.resetDictForm()
      this.rootFlag = rootFlag
      if (rootFlag) {
        this.dictForm.parentId = 0
      } else {
        this.dictForm.parentId = this.treeQuery.parentId
      }
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if (this.$refs['dictForm']) {
          this.$refs['dictForm'].clearValidate()
        }
      })
    },
    // 执行数据字典新增操作
    createData () {
      this.$refs['dictForm'].validate(valid => {
        if (valid) {
          createDict(this.dictForm).then(response => {
            this.dialogFormVisible = false
            this.$message.success('创建成功')
            if (this.rootFlag) {
              this.handleParentFilter()
            } else {
              this.handleFilter()
            }
          })
        }
      })
    },
    // 初始化更新字典弹出框及表单
    handleUpdate (row, rootFlag) {
      this.rootFlag = rootFlag
      this.dictForm = Object.assign({}, row) // copy obj
      // JSON不接受循环对象——引用它们自己的对象
      delete this.dictForm.parent
      delete this.dictForm.children
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if (this.$refs['dictForm']) {
          this.$refs['dictForm'].clearValidate()
        }
      })
    },
    // 执行数据字典更新操作
    updateData () {
      this.$refs['dictForm'].validate(valid => {
        if (valid) {
          updateDict(this.dictForm).then(() => {
            this.dialogFormVisible = false
            this.$message.success('更新成功')
            if (this.rootFlag) {
              this.handleParentFilter()
            } else {
              this.handleFilter()
            }
          })
        }
      })
    },
    // 执行数据字典删除操作
    handleDelete (row, rootFlag) {
      this.rootFlag = rootFlag
      const that = this
      this.$confirm({
        title: '此操作将永久删除该字典：' + row.dictName + ', 是否继续?',
        content: '',
        onOk () {
          deleteDict(row.id).then(() => {
            that.$message.success('删除成功!')
            if (that.rootFlag) {
              that.handleParentFilter()
            }
            that.handleFilter()
          })
        },
        onCancel () {
          that.$message.info('已取消删除')
        }
      })
    },
    handleBatchDelete (rootFlag) {
      const that = this
      let dictNameList = []
      let deleteIds = []
      if (rootFlag) {
        dictNameList = that.selectedParentRows.map(function (n) {
          return n.dictName
        })
        deleteIds = that.selectedParentRowKeys
      } else {
        dictNameList = that.selectedRows.map(function (n) {
          return n.dictName
        })
        deleteIds = that.selectedRowKeys
      }
      that.$confirm({
        title: '以下数据字典将被全部删除，是否继续?',
        content: dictNameList.join(','),
        onOk () {
          that.listLoading = true
          batchDeleteDict(deleteIds).then(() => {
            that.listLoading = false
            that.$message.success('删除成功!')
            if (rootFlag) {
              that.selectedParentRowKeys = []
              that.selectedParentRows = []
              that.handleParentTableRefresh()
            } else {
              that.selectedRowKeys = []
              that.selectedRows = []
            }
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
      updateDictStatus(row.id, status).then(() => {
        this.listLoading = false
        row.dictStatus = status
        this.$message.success('状态修改成功')
      })
    }
  }
}
</script>
