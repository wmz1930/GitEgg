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
            <a-form-model-item label="状态">
              <a-select v-model="listQuery.fileStatus" placeholder="状态" allow-clear show-search :filter-option="filterOption">
                <a-select-option value="">
                  全部
                </a-select-option>
                <a-select-option v-for="item in statusOption" :key="item.key" :value="item.key">
                  {{ item.label }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <template v-if="advanced">
            <a-col :md="6" :sm="24">
              <a-form-model-item label="原文件名">
                <a-input v-model="listQuery.originalName" placeholder="输入原文件名" :maxLength="32" />
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="存储文件名">
                <a-input v-model="listQuery.fileName" placeholder="输入存储文件名" :maxLength="32" />
              </a-form-model-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
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
      ref="dfsFileTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="dfsFilePagination"
      :scroll="{ x: 1500 }"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.fileStatus | statusFilter">{{ record.fileStatus | statusNameFilter }}</a-tag>
      </span>
      <span slot="dfsRender" slot-scope="text, record">
        <span>{{ record.dfsType | dfsTypeFilter }}</span>
      </span>
      <span slot="accessControlRender" slot-scope="text, record">
        <span>{{ record.accessControl | accessControlFilter }}</span>
      </span>
      <span slot="createTime" slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="downLoadFile(record)">下载</a>
        <a-divider type="vertical" />
        <a @click="getFileUrl(record)">查看</a>

      </span>
    </s-table>

    <a-modal :title="textMap[dialogStatus]" :maskClosable="false" :visible="dialogFormVisible" :width="800" @cancel="() => dialogFormVisible = false">
      <a-form-model
        ref="dfsFileForm"
        :model="dfsFileForm"
        :rules="rules"
        :label-col="dfsFileLabelCol"
        :wrapper-col="dfsFileWrapperCol">
        <a-form-model-item label="主键" prop="id">
          <a-input v-model="dfsFileForm.id" placeholder="输入主键" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="分布式存储配置id" prop="dfsId">
          <a-input v-model="dfsFileForm.dfsId" placeholder="输入分布式存储配置id" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="文件访问地址" prop="accessUrl">
          <a-input v-model="dfsFileForm.accessUrl" placeholder="输入文件访问地址" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="原文件名" prop="originalName">
          <a-input v-model="dfsFileForm.originalName" placeholder="输入原文件名" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="存储文件名" prop="fileName">
          <a-input v-model="dfsFileForm.fileName" placeholder="输入存储文件名" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="文件类型" prop="fileExtension">
          <a-input v-model="dfsFileForm.fileExtension" placeholder="输入文件类型" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="文件大小" prop="fileSize">
          <a-input v-model="dfsFileForm.fileSize" placeholder="输入文件大小" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="状态" prop="fileStatus">
          <a-input v-model="dfsFileForm.fileStatus" placeholder="输入状态 0上传成功失败，1 上传成功" :maxLength="32" />
        </a-form-model-item>
        <a-form-model-item label="备注" prop="comments">
          <a-input v-model="dfsFileForm.comments" placeholder="输入备注" :maxLength="32" />
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
    import { queryDfsFileList, createDfsFile, updateDfsFile, updateDfsFileStatus, batchDeleteDfsFile, deleteDfsFile, checkDfsFileExist } from '@/api/system/extension/dfs/dfs_file'
    import { dfsGetFileUrl, dfsDownloadFileUrl } from '@/api/system/extension/dfs/dfs_upload'
    import { listDict } from '@/api/system/base/dict'
    import moment from 'moment'
    let vm = {}
    export default {
        name: 'DfsFileTable',
        components: { moment, STable },
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
                    '1': '成功',
                    '0': '失败'
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
            }
        },
        data () {
            vm = this
            // 增加或更新记录时，判断字段是否已经存在
            var validDfsFile = (rule, value, callback) => {
                var keyData = {
                    id: this.dfsFileForm.id,
                    checkField: 'dfsFile', // 这里改为字段名称
                    checkValue: value
                }
                checkDfsFileExist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            return {
                advanced: false,
                currentDfsFile: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                dfsTypeList: [],
                dfsTypeFilterMap: {},
                listQuery: {
                    dfsType: '',
                    originalName: '',
                    fileName: '',
                    fileStatus: '',
                    beginDateTime: '',
                    endDateTime: ''
                },
                fileDownload: {
                    dfsCode: '',
                    fileName: ''
                },
                statusOption: [{ label: '成功', key: '1' }, { label: '失败', key: '0' }],
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                dfsFileForm: {
                    id: '',
                    dfsId: '',
                    accessUrl: '',
                    originalName: '',
                    fileName: '',
                    fileExtension: '',
                    fileSize: '',
                    fileStatus: '',
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
                        title: '文件地址',
                        align: 'center',
                        width: 300,
                        dataIndex: 'accessUrl'
                    },
                    {
                        title: '原文件名',
                        align: 'center',
                        width: 300,
                        dataIndex: 'originalName'
                    },
                    {
                        title: '存储文件名',
                        align: 'center',
                        width: 300,
                        dataIndex: 'fileName'
                    },
                    {
                        title: '文件类型',
                        align: 'center',
                        width: 180,
                        dataIndex: 'fileExtension'
                    },
                    {
                        title: '文件大小',
                        align: 'center',
                        width: 180,
                        dataIndex: 'fileSize'
                    },
                    {
                        title: '文件状态',
                        align: 'center',
                        width: 180,
                        dataIndex: 'fileStatus',
                        scopedSlots: { customRender: 'status' }
                    },
                    {
                        title: '访问控制',
                        align: 'center',
                        dataIndex: 'accessControl',
                        width: 100,
                        scopedSlots: { customRender: 'accessControlRender' }
                    },
                    {
                        title: '备注',
                        align: 'center',
                        width: 100,
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
                rules: {
                    // 字段校验，这里自己选择使用哪些校验
                    dfsFile: [
                        { required: true, message: '请输入dfsFile', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
                        { validator: validDfsFile, trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                dfsFileLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                dfsFileWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                dfsFilePagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return queryDfsFileList(Object.assign(parameter, this.listQuery))
                        .then(res => {
                            this.list = res.data.records
                            return res.data
                        })
                }
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['dfsFileTree'].filter(val)
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
                    dfsType: '',
                    originalName: '',
                    fileName: '',
                    fileStatus: '',
                    beginDateTime: '',
                    endDateTime: ''
                }
            },
            resetDfsFileForm () {
                this.dfsFileForm = {
                    id: '',
                    dfsId: '',
                    accessUrl: '',
                    originalName: '',
                    fileName: '',
                    fileExtension: '',
                    fileSize: '',
                    fileStatus: '',
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
                queryDfsFileList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.dfsFileTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.dfsFileTable.refresh()
            },
            handleCreate () {
                this.resetDfsFileForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['dfsFileForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['dfsFileForm'].validate(valid => {
                    if (valid) {
                        createDfsFile(this.dfsFileForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.dfsFileForm = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['dfsFileForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['dfsFileForm'].validate(valid => {
                    if (valid) {
                        updateDfsFile(this.dfsFileForm).then(() => {
                            for (const v of this.list) {
                                if (v.id === this.dfsFileForm.id) {
                                    const index = this.list.indexOf(v)
                                    this.list.splice(index, 1, this.dfsFileForm)
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
                        deleteDfsFile(row.id).then(() => {
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
                var dfsFileList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: dfsFileList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteDfsFile(that.selectedRowKeys).then(() => {
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
                updateDfsFileStatus(row.id, status).then(() => {
                    this.listLoading = false
                    row.dfsFileStatus = status
                    this.$message.success('状态修改成功')
                })
            },
            handleDownload () {
                this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                    const tHeader = [
                        '主键',
                        '分布式存储配置id',
                        '文件访问地址',
                        '原文件名',
                        '存储文件名',
                        '文件类型',
                        '文件大小',
                        '状态 0上传成功失败，1 上传成功',
                        '备注'
                    ]
                    const filterVal = [
                        'id',
                        'dfsId',
                        'accessUrl',
                        'originalName',
                        'fileName',
                        'fileExtension',
                        'fileSize',
                        'fileStatus',
                        'comments'
                    ]
                    const data = this.formatJson(filterVal, this.list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: '分布式存储文件记录表数据导出列表'
                    })
                    this.downloadLoading = false
                })
            },
            formatJson (filterVal, jsonData) {
                return jsonData.map(v =>
                    filterVal.map(j => {
                        if (j === 'createTime') {
                            return moment(v[j])
                        } else if (j === 'dfsFileStatus') {
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
            getFileUrl (row) {
                this.listLoading = true
                this.fileDownload.dfsCode = row.dfsCode
                this.fileDownload.fileName = row.fileName
                dfsGetFileUrl(this.fileDownload).then(response => {
                    window.open(response.data)
                    this.listLoading = false
                })
            },
            downLoadFile (row) {
                this.listLoading = true
                this.fileDownload.dfsCode = row.dfsCode
                this.fileDownload.fileName = row.fileName
                this.fileDownload.responseType = 'blob'
                dfsDownloadFileUrl(this.fileDownload).then(response => {
                    const blob = new Blob([response.data])
                    var fileName = row.originalName
                    const elink = document.createElement('a')
                    elink.download = fileName
                    elink.style.display = 'none'
                    elink.href = URL.createObjectURL(blob)
                    document.body.appendChild(elink)
                    elink.click()
                    URL.revokeObjectURL(elink.href)
                    document.body.removeChild(elink)
                    this.listLoading = false
                })
            }
        }
    }
</script>
