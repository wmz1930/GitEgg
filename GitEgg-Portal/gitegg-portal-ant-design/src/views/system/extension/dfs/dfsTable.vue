<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="存储类型">
              <a-select v-model="listQuery.dfsType" default-value="">
                <a-select-option value="">
                  全部
                </a-select-option>
                <a-select-option v-for="item in dfsTypeList" :key="item.id" :label="item.dictName" :value="item.id">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="存储编号">
              <a-input
                v-model.trim="listQuery.dfsCode"
                placeholder="存储编号"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="状态">
              <a-select v-model="listQuery.dfsStatus" placeholder="状态" show-search :filter-option="filterOption">
                <a-select-option value="">
                  全部
                </a-select-option>
                <a-select-option v-for="item in statusOption" :key="item.key" :value="item.key">
                  {{ item.label }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form-model>
    </div>

    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleCreate">新建</a-button>
      <a-button type="primary" icon="cloud-download" @click="handleDownload" style="margin-left: 8px">导出</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="handleBatchDelete"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <s-table
      ref="dfsTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="dfsPagination"
      :scroll="{ x: 1500 }"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.dfsStatus | statusFilter">{{ record.dfsStatus | statusNameFilter }}</a-tag>
      </span>
      <span slot="dfsRender" slot-scope="text, record">
        <span>{{ record.dfsType | dfsTypeFilter }}</span>
      </span>
      <span slot="defaultRender" slot-scope="text, record">
        <a-tag :color="record.dfsDefault | defaultColorFilter">{{ record.dfsDefault | defaultFilter }}</a-tag>
      </span>
      <span slot="accessControlRender" slot-scope="text, record">
        <span>{{ record.accessControl | accessControlFilter }}</span>
      </span>
      <span slot="createTime" slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handleUpdate(record)">编辑</a>
        <a-divider type="vertical" />
        <a @click="handleUploadTest(record)">测试</a>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多 <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;" v-if="record.dfsDefault !== 1" size="mini" type="success" @click="handleModifyDefault(record)">设为默认
              </a>
              <a href="javascript:;" v-else size="mini" disabled>设为默认
              </a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" v-if="record.dfsStatus!=1" size="mini" type="success" @click="handleModifyStatus(record,1)">启用
              </a>
              <a href="javascript:;" v-if="record.dfsStatus!=0 && record.dfsStatus!=2" size="mini" @click="handleModifyStatus(record,0)">禁用
              </a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" @click="handleDelete(record)">删除</a>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <a-modal :title="textMap[dialogStatus]" :maskClosable="false" :visible="dialogFormVisible" :width="800" @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="dfsForm"
        :model="dfsForm"
        :rules="rules"
        :label-col="dfsLabelCol"
        :wrapper-col="dfsWrapperCol">
        <a-form-model-item label="存储分类" prop="dfsType">
          <a-select v-model="dfsForm.dfsType">
            <a-select-option v-for="item in dfsTypeList" :key="item.id" placeholder="请选择分布式存储分类" :label="item.dictName" :value="item.id">
              {{ item.dictName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="存储编号" prop="dfsCode">
          <a-input v-model="dfsForm.dfsCode" placeholder="输入分布式存储编号" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="访问地址前缀" prop="accessUrlPrefix">
          <a-input v-model="dfsForm.accessUrlPrefix" placeholder="输入分布式文件访问地址前缀" :maxLength="255" />
        </a-form-model-item>
        <a-form-model-item label="文件上传地址" prop="uploadUrl">
          <a-input v-model="dfsForm.uploadUrl" placeholder="输入文件上传地址" :maxLength="255" />
        </a-form-model-item>
        <a-form-model-item label="空间名称" prop="bucket">
          <a-input v-model="dfsForm.bucket" placeholder="输入空间名称" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="应用ID" prop="appId">
          <a-input v-model="dfsForm.appId" placeholder="输入应用ID" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="区域" prop="region">
          <a-input v-model="dfsForm.region" placeholder="输入区域" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="accessKey" prop="accessKey">
          <a-input v-model="dfsForm.accessKey" placeholder="输入accessKey" :maxLength="60" />
        </a-form-model-item>
        <a-form-model-item label="secretKey" prop="secretKey">
          <a-input v-model="dfsForm.secretKey" placeholder="输入secretKey" :maxLength="60" />
        </a-form-model-item>
        <a-form-model-item label="是否默认存储" prop="dfsDefault">
          <a-select v-model="dfsForm.dfsDefault" placeholder="默认存储" show-search :filter-option="filterOption">
            <a-select-option v-for="item in defaultOption" :key="item.key" :value="item.key">
              {{ item.label }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="状态" prop="dfsStatus">
          <a-radio-group v-model="dfsForm.dfsStatus"
                         prop="dfsStatus">
            <a-radio v-for="item in statusOption" :key="item.key" :value="item.key">{{ item.label }}</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="访问控制" prop="accessControl">
          <a-radio-group v-model="dfsForm.accessControl"
                         prop="accessControl">
            <a-radio v-for="item in accessControlOption" :key="item.key" :value="item.key">{{ item.label }}</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="备注" prop="comments">
          <a-textarea v-model="dfsForm.comments" placeholder="输入备注信息" :maxLength="255" />
        </a-form-model-item>
      </a-form-model>
      <div slot="footer" class="dialog-footer">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</a-button>
        <a-button v-else type="primary" @click="updateData">修改</a-button>
      </div>
    </a-modal>

    <a-modal title="文件上传测试" :maskClosable="false" :visible="dialogTestUploadVisible" :width="800" @cancel="() => dialogTestUploadVisible = false">
      <a-form-model
        ref="uploadForm"
        :model="uploadForm"
        :rules="uploadFormRules"
        :label-col="dfsLabelCol"
        :wrapper-col="dfsWrapperCol">
        <a-input v-model="uploadForm.dfsCode" type="hidden" />
        <a-form-model-item label="存储类型">
          <span>{{ uploadForm.dfsType | dfsTypeFilter }}</span>
        </a-form-model-item>
        <a-form-model-item label="测试文件" prop="testFile">
          <a-upload v-model="uploadForm.uploadFile" :file-list="fileList" :remove="handleRemove" :before-upload="beforeUpload">
            <a-button> <a-icon type="upload" /> 选择文件 </a-button>
          </a-upload>
          <a-button
            type="primary"
            :disabled="fileList.length === 0"
            :loading="uploading"
            style="margin-top: 16px"
            @click="handleUpload"
          >
            <a-icon type="caret-right" />{{ uploading ? 'Uploading' : '开始上传' }}
          </a-button>
        </a-form-model-item>
      </a-form-model>
      <div slot="footer" class="dialog-footer">
        <a-button @click="dialogTestUploadVisible = false">取消</a-button>
        <a-button type="primary" @click="dialogTestUploadVisible = false">确定</a-button>
      </div>
    </a-modal>
  </a-card>
</template>

<script>
    import { STable } from '@/components'
    import { queryDfsList, createDfs, updateDfs, updateDfsStatus, updateDfsDefault, batchDeleteDfs, deleteDfs } from '@/api/system/extension/dfs/dfs'
    import { dfsUpload } from '@/api/system/extension/dfs/dfs_upload'
    import { listDict } from '@/api/system/base/dict'
    import moment from 'moment'
    let vm = {}
    export default {
        name: 'DfsTable',
        components: { moment, STable },
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
            accessControlFilter (acc) {
                const accessControlMap = {
                    1: '公开',
                    0: '私有'
                }
                return accessControlMap[acc]
            },
            dfsTypeFilter (dfsType) {
              return vm.dfsTypeFilterMap[dfsType]
            },
            defaultColorFilter (def) {
                const defaultColorMap = {
                    1: 'green',
                    0: ''
                }
                return defaultColorMap[def]
            },
            defaultFilter (def) {
                const defaultMap = {
                    1: '是',
                    0: '否'
                }
                return defaultMap[def]
            }
        },
        data () {
            // 增加或更新记录时，判断字段是否已经存在
            // var validDfs = (rule, value, callback) => {
            //     var keyData = {
            //         id: this.dfsForm.id,
            //         checkField: 'dfs', // 这里改为字段名称
            //         checkValue: value
            //     }
            //     checkDfsExist(keyData).then(response => {
            //         if (!response.data) {
            //             callback(new Error('记录已存在')) // 这里改为字段名称
            //         } else {
            //             callback()
            //         }
            //     })
            // }
            vm = this
            return {
                advanced: false,
                currentDfs: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                dfsTypeList: [],
                dfsTypeFilterMap: {},
                listQuery: {
                    id: '',
                    dfsType: '',
                    dfsCode: '',
                    dfsStatus: ''
                },
                statusOption: [{ label: '启用', key: 1 }, { label: '禁用', key: 0 }],
                defaultOption: [{ label: '是', key: 1 }, { label: '否', key: 0 }],
                accessControlOption: [{ label: '公开', key: 1 }, { label: '私有', key: 0 }],
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                dfsForm: {
                    id: '',
                    dfsType: '',
                    dfsCode: '',
                    accessUrlPrefix: '',
                    uploadUrl: '',
                    bucket: '',
                    appId: '',
                    region: '',
                    accessKey: '',
                    secretKey: '',
                    dfsDefault: 0,
                    dfsStatus: 1,
                    accessControl: 0,
                    comments: ''
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
                        title: '存储分类',
                        align: 'center',
                        dataIndex: 'dfsType',
                        width: 180,
                        scopedSlots: { customRender: 'dfsRender' }
                    },
                    {
                        title: '存储编号',
                        align: 'center',
                        width: 180,
                        dataIndex: 'dfsCode'
                    },
                    {
                        title: '访问地址',
                        align: 'center',
                        width: 300,
                        dataIndex: 'accessUrlPrefix'
                    },
                    {
                        title: '上传地址',
                        align: 'center',
                        width: 300,
                        dataIndex: 'uploadUrl'
                    },
                    {
                        title: '空间名称',
                        align: 'center',
                        width: 180,
                        dataIndex: 'bucket'
                    },
                    // {
                    //     title: 'accessKey',
                    //     align: 'center',
                    //     dataIndex: 'accessKey'
                    // },
                    // {
                    //     title: 'secretKey',
                    //     align: 'center',
                    //     width: 180,
                    //     dataIndex: 'secretKey'
                    // },
                    {
                        title: '状态',
                        align: 'center',
                        dataIndex: 'dfsStatus',
                        width: 100,
                        scopedSlots: { customRender: 'status' }
                    },
                    {
                        title: '默认存储',
                        align: 'center',
                        dataIndex: 'dfsDefault',
                        width: 100,
                        scopedSlots: { customRender: 'defaultRender' }
                    },
                    {
                        title: '访问控制',
                        align: 'center',
                        dataIndex: 'accessControl',
                        width: 100,
                        scopedSlots: { customRender: 'accessControlRender' }
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
                    accessUrlPrefix: [
                        { required: true, message: '请输入文件访问地址前缀', trigger: 'blur' },
                        { min: 2, max: 255, message: '地址长度超出范围', trigger: 'blur' }
                    ],
                    uploadUrl: [
                        { required: true, message: '请输入分布式存储上传地址', trigger: 'blur' },
                        { min: 2, max: 255, message: '地址长度超出范围', trigger: 'blur' }
                    ]
                },
                uploadForm: {
                    dfsType: '',
                    dfsCode: '',
                    uploadFile: null
                },
                uploadFormRules: {
                    uploadFile: [
                        { required: true, message: '请选择上传文件', trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                dfsLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                dfsWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                dfsPagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryDfsList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data
                            return res
                        })
                },
                dialogTestUploadVisible: false,
                fileList: [],
                uploading: false,
                uploadedFileName: ''
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['dfsTree'].filter(val)
            // }
        },
        created () {
            this.queryDfsTypeList()
        },
        methods: {
            queryDfsTypeList () {
              const that = this
              that.listLoading = true
              listDict('DFS_TYPE').then(response => {
                that.dfsTypeList = response.data
                that.dfsTypeFilterMap = {}
                that.dfsTypeList.forEach((item, index, arr) => {
                  that.dfsTypeFilterMap[item.id] = item.dictName
                })
                that.listLoading = false
                that.getList()
              })
            },
            resetQuery () {
                this.listQuery = {
                    id: '',
                    dfsType: '',
                    dfsCode: '',
                    dfsStatus: ''
                }
            },
            resetDfsForm () {
                this.dfsForm = {
                    id: '',
                    dfsType: '',
                    dfsCode: '',
                    accessUrlPrefix: '',
                    uploadUrl: '',
                    bucket: '',
                    appId: '',
                    region: '',
                    accessKey: '',
                    secretKey: '',
                    dfsDefault: 0,
                    dfsStatus: 1,
                    accessControl: 0,
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
                queryDfsList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.dfsTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.dfsTable.refresh()
            },
            handleCreate () {
                this.resetDfsForm()
                if (this.dfsTypeList && this.dfsTypeList.length > 0) {
                    this.dfsForm.dfsType = this.dfsTypeList[0].id
                }
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['dfsForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['dfsForm'].validate(valid => {
                    if (valid) {
                        createDfs(this.dfsForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.dfsForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['dfsForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['dfsForm'].validate(valid => {
                    if (valid) {
                        updateDfs(this.dfsForm).then(() => {
                            for (const v of this.list) {
                                if (v.id === this.dfsForm.id) {
                                    const index = this.list.indexOf(v)
                                    this.list.splice(index, 1, this.dfsForm)
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
                        deleteDfs(row.id).then(() => {
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
                var dfsList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: dfsList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteDfs(that.selectedRowKeys).then(() => {
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
                var that = this
                this.$confirm({
                    title: '确定要' + this.statusNameFilter(status) + '该存储?',
                    content: '',
                    onOk () {
                          this.listLoading = true
                          updateDfsStatus(row.id, status).then(() => {
                              this.listLoading = false
                              row.dfsStatus = status
                              this.$message.success('状态修改成功')
                          })
                    },
                    onCancel () {
                        that.$message.info('已取消' + this.statusNameFilter(status))
                    }
                })
            },
            handleModifyDefault (row) {
                var that = this
                this.$confirm({
                    title: '确定要设置该存储为默认存储?',
                    content: '',
                    onOk () {
                          that.listLoading = true
                          updateDfsDefault(row.id).then(() => {
                              that.listLoading = false
                              that.handleTableRefresh(true)
                              that.$message.success('已设置为默认')
                          })
                    },
                    onCancel () {
                        that.$message.info('已取消')
                    }
                })
            },
            handleDownload () {
                this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                    const tHeader = [
                        '主键',
                        '分布式存储分类',
                        '分布式存储编号',
                        '分布式存储访问地址',
                        '空间名称',
                        '应用ID',
                        '区域',
                        'accessKey',
                        'secretKey',
                        '是否默认存储 0否，1是',
                        '状态 0禁用，1 启用',
                        '备注'
                    ]
                    const filterVal = [
                        'id',
                        'dfsType',
                        'dfsCode',
                        'dfsUrl',
                        'bucket',
                        'appId',
                        'region',
                        'accessKey',
                        'secretKey',
                        'dfsDefault',
                        'dfsStatus',
                        'comments'
                    ]
                    const data = this.formatJson(filterVal, this.list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: '分布式存储配置表数据导出列表'
                    })
                    this.downloadLoading = false
                })
            },
            formatJson (filterVal, jsonData) {
                return jsonData.map(v =>
                    filterVal.map(j => {
                        if (j === 'createTime') {
                            return moment(v[j])
                        } else if (j === 'dfsStatus') {
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
            handleUploadTest (row) {
                this.fileList = []
                this.uploading = false
                this.uploadForm.dfsType = row.dfsType
                this.uploadForm.dfsCode = row.dfsCode
                this.uploadForm.uploadFile = null
                this.dialogTestUploadVisible = true
            },
            handleRemove (file) {
                const index = this.fileList.indexOf(file)
                const newFileList = this.fileList.slice()
                newFileList.splice(index, 1)
                this.fileList = newFileList
            },
            beforeUpload (file) {
                this.fileList = [...this.fileList, file]
                return false
            },
            handleUpload () {
                this.uploadedFileName = ''
                const { fileList } = this
                const formData = new FormData()
                formData.append('dfsCode', this.uploadForm.dfsCode)
                fileList.forEach(file => {
                  formData.append('uploadFile', file)
                })
                this.uploading = true
                dfsUpload(formData).then(() => {
                    this.fileList = []
                    this.uploading = false
                    this.$message.success('上传成功')
                }).catch(err => {
                  console.log('uploading', err)
                  this.$message.error('上传失败')
                })
            }
        }
    }
</script>
