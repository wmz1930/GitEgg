<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="数据源名称">
              <a-input
                v-model.trim="listQuery.datasourceName"
                placeholder="数据源名称"
                :max-length="50"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="数据库驱动">
              <a-input
                v-model.trim="listQuery.driver"
                placeholder="数据库驱动"
                :max-length="50"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="数据库类型">
              <a-select v-model="listQuery.dbType" placeholder="数据库类型" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in dbTypeDictList" :key="item.dictCode" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <span class="table-page-search-submitButtons" style="{ float: 'right', overflow: 'hidden' }">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
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
      <a-upload
        name="uploadFile"
        :show-upload-list="false"
        :before-upload="beforeUpload"
      >
        <a-button> <a-icon type="upload" /> 导入 </a-button>
      </a-upload>
      <a href="javascript:;" @click="handleDownloadTemplate" style="margin-left: 8px;">下载导入模板</a>
    </div>

    <s-table
      ref="datasourceTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="datasourcePagination"
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

    <a-modal :title="textMap[dialogStatus]" :maskClosable="false" :visible="dialogFormVisible" :width="800" @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="datasourceForm"
        :model="datasourceForm"
        :rules="rules"
        :label-col="datasourceLabelCol"
        :wrapper-col="datasourceWrapperCol">
        <a-form-model-item label="数据源名称" prop="datasourceName">
          <a-input v-model="datasourceForm.datasourceName" placeholder="输入数据源名称" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="连接地址" prop="url">
          <a-input v-model="datasourceForm.url" placeholder="输入连接地址" :maxLength="500" />
        </a-form-model-item>
        <a-form-model-item label="用户名" prop="username">
          <a-input v-model="datasourceForm.username" placeholder="输入用户名" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="密码" prop="password">
          <a-input v-model="datasourceForm.password" placeholder="输入密码" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="数据库驱动" prop="driver">
          <a-input v-model="datasourceForm.driver" placeholder="输入数据库驱动" :maxLength="64" />
        </a-form-model-item>
        <a-form-model-item label="数据库类型" prop="dbType">
          <a-select v-model="datasourceForm.dbType" placeholder="数据库类型" show-search :filter-option="filterOption">
            <a-select-option v-for="item in dbTypeDictList" :key="item.dictCode" :value="item.dictCode">
              {{ item.dictName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="备注" prop="comments">
          <a-input v-model="datasourceForm.comments" placeholder="输入备注" :maxLength="255" />
        </a-form-model-item>
      </a-form-model>
      <div slot="footer" class="dialog-footer">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</a-button>
        <a-button v-else type="primary" @click="updateData">修改</a-button>
      </div>
    </a-modal>
  </a-card>
</template>

<script>
    import { STable } from '@/components'
    import { queryDatasourceList, createDatasource, updateDatasource,
              batchDeleteDatasource, deleteDatasource, checkDatasourceExist, downloadDatasourceList, downloadDatasourceTemplate, uploadDatasource } from '@/api/plugin/codeGenerator/datasource/datasource'
    import moment from 'moment'
    import { listDict } from '@/api/system/base/dict'
    import { handleDownloadBlod } from '@/utils/util'
    let vm = {}
    export default {
        name: 'DatasourceTable',
        components: { moment, STable },
        filters: {
            // 数据库类型数据字典展示
            dbTypeDictFilter (dictCode) {
                return vm.dbTypeDictFilterMap[dictCode]
            }
        },
        data () {
            vm = this
            var validDatasourceName = (rule, value, callback) => {
                var keyData = {
                    id: this.datasourceForm.id,
                    checkField: 'datasource_name',
                    checkValue: value
                }
                checkDatasourceExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('数据源名称已存在'))
                    } else {
                        callback()
                    }
                })
            }
            return {
                currentDatasource: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                dbTypeDictList: [],
                dbTypeDictFilterMap: {},
                listQuery: {
                    id: '',
                    datasourceName: '',
                    url: '',
                    username: '',
                    password: '',
                    driver: '',
                    dbType: undefined,
                    comments: ''
                },
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                datasourceForm: {
                    id: '',
                    datasourceName: '',
                    url: '',
                    username: '',
                    password: '',
                    driver: '',
                    dbType: undefined,
                    comments: ''
                },
                // 表头
                columns: [
                    {
                        title: '数据源名称',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
                        dataIndex: 'datasourceName'
                    },
                    {
                        title: '连接地址',
                        align: 'center',
                        ellipsis: true,
                        dataIndex: 'url'
                    },
                    {
                        title: '用户名',
                        align: 'center',
                        width: 150,
                        ellipsis: true,
                        dataIndex: 'username'
                    },
                    {
                        title: '密码',
                        align: 'center',
                        width: 150,
                        ellipsis: true,
                        dataIndex: 'password'
                    },
                    {
                        title: '数据库驱动',
                        align: 'center',
                        width: 150,
                        ellipsis: true,
                        dataIndex: 'driver'
                    },
                    {
                        title: '数据库类型',
                        align: 'center',
                        width: 150,
                        ellipsis: true,
                        dataIndex: 'dbType'
                    },
                    {
                        title: '创建日期',
                        align: 'center',
                        dataIndex: 'createTime',
                        width: 200,
                        scopedSlots: { customRender: 'createTime' }
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        width: '180px',
                        fixed: 'right',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                rules: {
                    datasourceName: [
                        { required: true, message: '请输入数据源名称', trigger: 'blur' },
                        { min: 1, max: 64, message: '长度在 1 到 64 个字符', trigger: 'blur' },
                        { validator: validDatasourceName, trigger: 'blur' }
                    ],
                    url: [
                        { required: true, message: '请输入数据库连接地址', trigger: 'blur' },
                        { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur' }
                    ],
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                datasourceLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                datasourceWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                datasourcePagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryDatasourceList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                }
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['datasourceTree'].filter(val)
            // }
        },
        created () {
            this.queryDbTypeDictList()
        },
        methods: {
          queryDbTypeDictList () {
                const that = this
                that.listLoading = true
                listDict('DB_TYPE').then(response => {
                    this.dbTypeDictList = response.data
                    this.dbTypeDictList.forEach((item, index, arr) => {
                        this.dbTypeDictFilterMap[item.dictCode] = item.dictName
                    })
                    that.listLoading = false
                    that.getList()
                })
            },
            resetQuery () {
                this.listQuery = {
                        id: '',
                        datasourceName: '',
                        url: '',
                        username: '',
                        password: '',
                        driver: '',
                        dbType: undefined,
                        comments: ''
                }
            },
            resetDatasourceForm () {
                this.datasourceForm = {
                    id: '',
                    datasourceName: '',
                    url: '',
                    username: '',
                    password: '',
                    driver: '',
                    dbType: undefined,
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
                queryDatasourceList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.datasourceTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.datasourceTable.refresh()
            },
            handleCreate () {
                this.resetDatasourceForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['datasourceForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['datasourceForm'].validate(valid => {
                    if (valid) {
                        createDatasource(this.datasourceForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.datasourceForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['datasourceForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['datasourceForm'].validate(valid => {
                    if (valid) {
                        updateDatasource(this.datasourceForm).then(() => {
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
                        deleteDatasource(row.id).then(() => {
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
                var datasourceList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: datasourceList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteDatasource(that.selectedRowKeys).then(() => {
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
            beforeUpload (file) {
                this.handleUpload(file)
                return false
            },
            handleUpload (file) {
                this.uploadedFileName = ''
                const formData = new FormData()
                formData.append('uploadFile', file)
                this.uploading = true
                uploadDatasource(formData).then(() => {
                    this.uploading = false
                    this.$message.success('数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                  console.log('uploading', err)
                  this.$message.error('数据导入失败')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                downloadDatasourceList(this.listQuery).then(response => {
                  handleDownloadBlod('数据源配置列表.xlsx', response)
                  this.listLoading = false
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                downloadDatasourceTemplate(this.listQuery).then(response => {
                  handleDownloadBlod('数据源配置上传模板.xlsx', response)
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
