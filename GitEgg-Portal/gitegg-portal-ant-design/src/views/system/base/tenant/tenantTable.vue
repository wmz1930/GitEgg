<template>
  <a-card :bordered="false"
          class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6"
                 :sm="24">
            <a-form-model-item label="租户名称">
              <a-input v-model.trim="listQuery.tenantName"
                       placeholder="租户名称"
                       :max-length="32"
                       @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6"
                 :sm="24">
            <a-form-model-item label="域名">
              <a-input v-model.trim="listQuery.domainName"
                       placeholder="域名"
                       :max-length="32"
                       @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6"
                 :sm="24">
            <a-form-model-item label="状态">
              <a-select v-model="listQuery.tenantStatus"
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
          <template v-if="advanced">
            <a-col :md="6"
                   :sm="24">
              <a-form-model-item label="开始时间">
                <a-date-picker v-model.trim="listQuery.beginDateTime"
                               placeholder="开始时间"
                               valueFormat="YYYY-MM-DD"
                               style="width:100%;" />
              </a-form-model-item>
            </a-col>
            <a-col :md="6"
                   :sm="24">
              <a-form-model-item label="结束时间">
                <a-date-picker v-model.trim="listQuery.endDateTime"
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
                        @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px"
                        @click="resetQuery">重置</a-button>
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
                @click="handleCreate">新建</a-button>
      <a-button type="primary"
                icon="cloud-download"
                @click="handleDownload"
                style="margin-left: 8px">导出</a-button>
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

    <s-table ref="tenantTable"
             size="default"
             bordered
             :rowKey="row=>row.id"
             :columns="columns"
             :data="loadData"
             showPagination="auto"
             :pagination="tenantPagination"
             :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }">
      <span slot="status"
            slot-scope="text, record">
        <a-tag :color="record.tenantStatus | statusFilter">{{ record.tenantStatus | statusNameFilter }}</a-tag>
      </span>
      <span slot="createTime"
            slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action"
            slot-scope="text, record">
        <a @click="handleUpdate(record)">编辑</a>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多
            <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;"
                 v-if="record.tenantStatus!='1'"
                 size="mini"
                 type="success"
                 @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;"
                 v-if="record.tenantStatus!='0' && record.tenantStatus!='2'"
                 size="mini"
                 @click="handleModifyStatus(record,'0')">禁用
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

    <a-modal :title="textMap[dialogStatus]"
             :maskClosable="false"
             :visible="dialogFormVisible"
             :width="800"
             @cancel="() => dialogFormVisible = false">
      <a-form-model ref="tenantForm"
                    :model="tenantForm"
                    :rules="rules"
                    :label-col="tenantLabelCol"
                    :wrapper-col="tenantWrapperCol">
        <a-form-model-item label="租户名称"
                           prop="tenantName">
          <a-input v-model="tenantForm.tenantName"
                   placeholder="输入租户名称"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="域名"
                           prop="domainName">
          <a-input v-model="tenantForm.domainName"
                   placeholder="输入域名"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="背景图片"
                           prop="backgroundImage">
          <upload-image
            v-model.trim="tenantForm.backgroundImage"
            uploadBtnName="上传背景图片"
            :imgNumber="1"
          ></upload-image>
        </a-form-model-item>
        <a-form-model-item label="联系人"
                           prop="contacts">
          <a-input v-model="tenantForm.contacts"
                   placeholder="输入联系人"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="联系电话"
                           prop="contactNumber">
          <a-input v-model="tenantForm.contactNumber"
                   placeholder="输入联系电话"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="联系地址"
                           prop="address">
          <a-input v-model="tenantForm.address"
                   placeholder="输入联系地址"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="账号限额"
                           prop="accountLimit">
          <a-input v-model="tenantForm.accountLimit"
                   placeholder="输入账号限额"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="过期时间"
                           prop="expireTime">
          <a-input v-model="tenantForm.expireTime"
                   placeholder="输入过期时间"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="授权码"
                           prop="licenseKey">
          <a-input v-model="tenantForm.licenseKey"
                   placeholder="输入授权码"
                   :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="租户状态"
                           prop="tenantStatus">
          <a-radio-group v-model="tenantForm.tenantStatus"
                         name="tenantStatus">
            <a-radio :value="1">启用</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="备注"
                           prop="comments">
          <a-input v-model="tenantForm.comments"
                   placeholder="输入备注"
                   :maxLength="32" />
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
  </a-card>
</template>

<script>
import { STable } from '@/components'
import UploadImage from '@/components/Upload/UploadImage'
import { queryTenantList, createTenant, updateTenant, updateTenantStatus, batchDeleteTenant, deleteTenant, checkTenantExist } from '@/api/system/base/tenant'
import moment from 'moment'
export default {
  name: 'TenantTable',
  components: { moment, STable, UploadImage },
  filters: {
    statusFilter (status) {
      const statusMap = {
        '1': 'green',
        '2': '',
        '0': 'pink'
      }
      return statusMap[status]
    },
    statusNameFilter (status) {
      const statusNameMap = {
        '1': '启用',
        '0': '禁用'
      }
      return statusNameMap[status]
    }
  },
  data () {
    // 增加或更新记录时，判断字段是否已经存在
    var validTenant = (rule, value, callback) => {
      var keyData = {
        id: this.tenantForm.id,
        checkField: 'tenant', // 这里改为字段名称
        checkValue: value
      }
      checkTenantExist(keyData).then(response => {
        if (!response.data) {
          callback(new Error('记录已存在')) // 这里改为字段名称
        } else {
          callback()
        }
      })
    }
    return {
      advanced: false,
      currentTenant: '',
      filterText: '',
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        tenantName: '',
        domainName: '',
        tenantStatus: '',
        beginDateTime: '',
        endDateTime: ''
      },
      statusOption: [{ label: '启用', key: '1' }, { label: '禁用', key: '0' }],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '添加'
      },
      tenantForm: {
        id: '',
        tenantName: '',
        domainName: '',
        backgroundImage: '',
        contacts: '',
        contactNumber: '',
        address: '',
        accountLimit: '',
        expireTime: '',
        licenseKey: '',
        tenantStatus: 1,
        comments: ''
      },
      // 表头
      columns: [
        {
          title: '租户名称',
          align: 'center',
          dataIndex: 'tenantName'
        },
        {
          title: '域名',
          align: 'center',
          dataIndex: 'domainName'
        },
        {
          title: '联系人',
          align: 'center',
          dataIndex: 'contacts'
        },
        {
          title: '联系电话',
          align: 'center',
          dataIndex: 'contactNumber'
        },
        {
          title: '账号限额',
          align: 'center',
          dataIndex: 'accountLimit'
        },
        {
          title: '过期时间',
          align: 'center',
          dataIndex: 'expireTime'
        },
        {
          title: '授权码',
          align: 'center',
          dataIndex: 'licenseKey'
        },
        {
          title: '租户状态',
          align: 'center',
          dataIndex: 'tenantStatus',
          scopedSlots: { customRender: 'status' }
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '180px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      rules: {
        // 字段校验，这里自己选择使用哪些校验
        tenant: [
          { required: true, message: '请输入tenant', trigger: 'blur' },
          { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
          { validator: validTenant, trigger: 'blur' }
        ]
      },
      downloadLoading: false,
      tenantLabelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      tenantWrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      selectedRowKeys: [],
      selectedRows: [],
      tenantPagination: {
        defaultPageSize: 10,
        showQuickJumper: true,
        defaultCurrent: 1,
        showTotal: (total, range) => `共 ${total} 条`
      },
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return queryTenantList(Object.assign(parameter, this.listQuery))
          .then(res => {
            this.list = res.data
            return res
          })
      }
    }
  },
  watch: {
    // filterText (val) {
    //   this.$refs['tenantTree'].filter(val)
    // }
  },
  created () {
    this.getList()
  },
  methods: {
    resetQuery () {
      this.listQuery = {
        tenantName: '',
        domainName: '',
        tenantStatus: '',
        beginDateTime: '',
        endDateTime: ''
      }
    },
    resetTenantForm () {
      this.tenantForm = {
        id: '',
        tenantName: '',
        domainName: '',
        backgroundImage: '',
        contacts: '',
        contactNumber: '',
        address: '',
        accountLimit: '',
        expireTime: '',
        licenseKey: '',
        tenantStatus: 1,
        comments: ''
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
      queryTenantList(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.count
        this.listLoading = false
      })
    },
    handleFilter () {
      this.$refs.tenantTable.refresh(true)
    },
    handleTableRefresh () {
      this.$refs.tenantTable.refresh()
    },
    handleCreate () {
      this.resetTenantForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['tenantForm'].clearValidate()
      })
    },
    createData () {
      this.$refs['tenantForm'].validate(valid => {
        if (valid) {
          createTenant(this.tenantForm).then(() => {
            this.dialogFormVisible = false
            this.handleFilter()
            this.$message.success('创建成功')
          })
        }
      })
    },
    handleUpdate (row) {
      this.tenantForm = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['tenantForm'].clearValidate()
      })
    },
    updateData () {
      this.$refs['tenantForm'].validate(valid => {
        if (valid) {
          updateTenant(this.tenantForm).then(() => {
            for (const v of this.list) {
              if (v.id === this.tenantForm.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.tenantForm)
                break
              }
            }
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
          deleteTenant(row.id).then(() => {
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
      var tenantList = this.selectedRows.map(function (n) {
        return n.id
      })
      var that = this
      this.$confirm({
        title: '以下选中记录将被全部删除，是否继续?',
        content: tenantList.join(','),
        onOk () {
          that.listLoading = true
          batchDeleteTenant(that.selectedRowKeys).then(() => {
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
      updateTenantStatus(row.id, status).then(() => {
        this.listLoading = false
        row.tenantStatus = status
        this.$message.success('状态修改成功')
      })
    },
    handleDownload () {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [
          '主键',
          '租户名称',
          '域名',
          '背景图片',
          '联系人',
          '联系电话',
          '联系地址',
          '账号限额',
          '过期时间',
          '授权码',
          '租户状态 "0"禁用，"1" 启用,',
          '备注'
        ]
        const filterVal = [
          'id',
          'tenantName',
          'domainName',
          'backgroundImage',
          'contacts',
          'contactNumber',
          'address',
          'accountLimit',
          'expireTime',
          'licenseKey',
          'tenantStatus',
          'comments'
        ]
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: '租户信息表数据导出列表'
        })
        this.downloadLoading = false
      })
    },
    formatJson (filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === 'createTime') {
            return moment(v[j])
          } else if (j === 'tenantStatus') {
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
    }
  }
}
</script>
