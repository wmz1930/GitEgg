<template>
  <a-card :bordered="false" class="content">
    <router-view/>
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-model-item label="数据源">
              <select-asyn optKey="id"
                           optValue="id"
                           optLable="datasourceName"
                           optPlaceholder="请选择数据源"
                           queryKey="datasourceName"
                           :queryDataList="queryDatasourceListFunction"
                           @holderBack="clickSetListQuery"
              ></select-asyn>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="模块名称">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="请输入模块名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="服务名称">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="请输入服务名称"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="表名">
              <a-input
                v-model.trim="listQuery.id"
                placeholder="请输入表名"
                :max-length="32"
                @keyup.enter.native="handleFilter" />
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :md="6" :sm="24">
              <a-form-model-item label="展示类型">
                <a-select v-model="listQuery.formType" placeholder="请选择展示类型" allow-clear show-search :filter-option="filterOption">
                  <a-select-option v-for="item in formTypeDict.dictList" :key="item.id" :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="表单类型">
                <a-select v-model="listQuery.tableType" placeholder="请选择表单类型" allow-clear show-search :filter-option="filterOption">
                  <a-select-option v-for="item in tableTypeDict.dictList" :key="item.id" :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="联表类型">
                <a-select v-model="listQuery.tableShowType" placeholder="请选择联表类型" allow-clear show-search :filter-option="filterOption">
                  <a-select-option v-for="item in tableShowTypeDict.dictList" :key="item.id" :value="item.dictCode">
                    {{ item.dictName }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24" style="float: right;text-align: right;">
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
      ref="configTable"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
      :pagination="configPagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
      <span slot="formTypeFilter" slot-scope="text, record">
        {{ record.formType | formTypeFilter }}
      </span>
      <span slot="tableTypeFilter" slot-scope="text, record">
        {{ record.tableType | tableTypeFilter }}
      </span>
      <span slot="tableShowTypeFilter" slot-scope="text, record">
        {{ record.tableShowType | tableShowTypeFilter }}
      </span>
      <span slot="createTime" slot-scope="text, record">
        <span>{{ record.createTime | moment }}</span>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handleUpdate(record)">编辑</a>
        <a-divider type="vertical" />
        <router-link :to="'/plugin/code/generator/config/edit/'+record.id">
          配置规则
        </router-link>
        <a-divider type="vertical" />
        <a @click="processGenerateCode(record)">生成代码</a>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多 <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;" @click="copyTableConfig(record)">复制表配置</a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" @click="copyTableFieldConfig(record)">复制全部配置</a>
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
              :closable="false"
              :destroyOnClose="true"
              :maskClosable="false"
              :visible="dialogFormVisible"
              :width="800"
              @close="dialogResourceVisible = false">
      <a-form-model
        ref="configForm"
        :model="configForm"
        :rules="rules"
        class="model-config"
        :label-col="configLabelCol"
        :wrapper-col="configWrapperCol">
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="数据源" prop="datasourceId">
              <select-asyn optKey="id"
                           optValue="id"
                           optLable="datasourceName"
                           optPlaceholder="请选择数据源"
                           queryKey="datasourceName"
                           :defaultValue="configForm.datasourceName"
                           :queryDataList="queryDatasourceListFunction"
                           @holderBack="clickSetForm"
              ></select-asyn>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="服务名称" prop="serviceName">
              <a-input v-model="configForm.serviceName" placeholder="例：gitegg-service-system" :maxLength="64" />
            </a-form-model-item>
          </a-col>

        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="模块名称" prop="moduleName">
              <a-input v-model="configForm.moduleName" placeholder="输入模块名称，例：系统管理" :maxLength="32" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="模块代码" prop="moduleCode">
              <a-input v-model="configForm.moduleCode" placeholder="输入模块代码，例：system" :maxLength="32" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="表名" prop="tableName">
              <a-input v-model="configForm.tableName" placeholder="输入表名，例：t_sys_user" :maxLength="54" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="表别名" prop="tableAlias">
              <a-input v-model="configForm.tableAlias" placeholder="输入表别名，例：user" :maxLength="54" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="表前缀" prop="tablePrefix">
              <a-input v-model="configForm.tablePrefix" placeholder="输入表前缀，例：t_sys_" :maxLength="64" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="父级包名" prop="parentPackage">
              <a-input v-model="configForm.parentPackage" placeholder="例：com.gitegg.system" :maxLength="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="实体名称" prop="domainName">
              <a-input v-model="configForm.domainName" placeholder="用于该功能界面展示，及操作提示的名称" :maxLength="500" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="表单类型" prop="formType">
              <a-select v-model="configForm.formType" placeholder="请选择展示类型" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in formTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="表单列数" prop="formItemCol">
              <a-select v-model="configForm.formItemCol" placeholder="请选择表单列数" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in formColDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="数据展示" prop="tableShowType">
              <a-select v-model="configForm.tableShowType" placeholder="请选择数据展示类型" allow-clear show-search :filter-option="filterOption">
                <a-select-option v-for="item in tableShowTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="左树类型" prop="leftTreeType">
              <a-select v-model="configForm.leftTreeType"
                        placeholder="请选择左树类型"
                        :disabled="!(configForm.tableShowType === 'left_tree_table' || configForm.tableShowType === 'left_tree_tree_table' || configForm.tableShowType === 'tree_tree')"
                        allow-clear
                        show-search
                        :filter-option="filterOption">
                <a-select-option
                  v-for="item in treeTypeDict.dictList"
                  :key="item.id"
                  :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-form-model-item label="Controller请求路径" prop="tablePrefix">
            <a-input v-model="configForm.controllerPath" placeholder="输入controller请求路径，例：/system/user" :maxLength="100" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item label="后端代码保存路径" prop="serviceCodePath">
            <a-input v-model="configForm.serviceCodePath" placeholder="输入后端代码存放路径" :maxLength="1000" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item label="前端代码保存路径" prop="frontCodePath">
            <a-input v-model="configForm.frontCodePath" placeholder="输入前端代码存放路径" :maxLength="1000" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item label="页面文件目录自定义" prop="frontCodeDir">
            <a-input v-model="configForm.frontCodeDir" placeholder="存放.vue页面文件的目录名称，用于区分不同系统，例如：system" :maxLength="100" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="生成类型" prop="codeType">
              <a-select v-model="configForm.codeType" placeholder="生成类型" :filter-option="filterOption">
                <a-select-option v-for="item in codeTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="保存类型" prop="codeSaveType">
              <a-select v-model="configForm.codeSaveType" placeholder="保存类型" :filter-option="filterOption">
                <a-select-option v-for="item in codeSaveTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="状态处理" prop="statusHandling">
              <a-radio-group v-model="configForm.statusHandling" default-value="true">
                <a-radio v-for="item in yesOrNoDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="支持导出" prop="exportFlag">
              <a-radio-group v-model="configForm.exportFlag" :default-value="true">
                <a-radio v-for="item in yesOrNoDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="支持导入" prop="importFlag">
              <a-radio-group v-model="configForm.importFlag" :default-value="true">
                <a-radio v-for="item in yesOrNoDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12">
            <a-form-model-item label="联表类型" prop="tableType">
              <a-select v-model="configForm.tableType" placeholder="请选择联表类型" show-search :filter-option="filterOption">
                <a-select-option v-for="item in tableTypeDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item label="查询复用" prop="queryReuse">
              <a-radio-group v-model="configForm.queryReuse" :default-value="true">
                <a-radio v-for="item in yesOrNoDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-show="configForm.tableType === 'main_sub'">
          <a-col :md="12">
            <a-form-model-item label="是否继承" prop="extendsFlag">
              <a-radio-group v-model="configForm.extendsFlag" :default-value="true">
                <a-radio v-for="item in yesOrNoDict.dictList" :key="item.id" :value="item.dictCode">
                  {{ item.dictName }}
                </a-radio>
              </a-radio-group>
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
    import { STable, SelectAsyn } from '@/components'
    import { queryConfigList, createConfig, updateConfig, batchDeleteConfig, deleteConfig, copyConfig } from '@/api/plugin/codeGenerator/config/config'
    import { generateCode } from '@/api/plugin/codeGenerator/engine/engine'
    import { queryDatasourceList } from '@/api/plugin/codeGenerator/datasource/datasource'
    import moment from 'moment'
    import { batchListGeneratorDict } from '@/api/plugin/codeGenerator/dict/dict'
    let vm = {}
    export default {
        name: 'ConfigTable',
        components: { moment, STable, SelectAsyn },
        filters: {
            formColFilter (formCol) {
              return vm.formColDict.filterMap[formCol]
            },
            yesOrNoFilter (yesOrNo) {
              return vm.yesOrNoDict.filterMap[yesOrNo]
            },
            formTypeFilter (formType) {
              return vm.formTypeDict.filterMap[formType]
            },
            tableTypeFilter (tableType) {
              return vm.tableTypeDict.filterMap[tableType]
            },
            tableShowTypeFilter (tableShowType) {
              return vm.tableShowTypeDict.filterMap[tableShowType]
            },
            codeTypeFilter (codeType) {
              return vm.codeTypeDict.filterMap[codeType]
            }
        },
        data () {
            vm = this
            return {
                advanced: false,
                currentConfig: '',
                filterText: '',
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                treeTypeDict: {
                  dictCode: 'TREE_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                formTypeDict: {
                  dictCode: 'FORM_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                tableTypeDict: {
                  dictCode: 'TABLE_DATA_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                tableShowTypeDict: {
                  dictCode: 'TABLE_DATA_SHOW_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                formColDict: {
                  dictCode: 'FORM_COL',
                  dictList: [],
                  filterMap: {}
                },
                yesOrNoDict: {
                  dictCode: 'TRUE_FALSE',
                  dictList: [],
                  filterMap: {}
                },
                codeTypeDict: {
                  dictCode: 'CODE_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                codeSaveTypeDict: {
                  dictCode: 'CODE_SAVE_TYPE',
                  dictList: [],
                  filterMap: {}
                },
                listQuery: {
                    datasourceId: '',
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    controllerPath: '',
                    tableType: 'single',
                    tableShowType: undefined,
                    formItemCol: undefined,
                    leftTreeType: undefined,
                    frontCodePath: '',
                    serviceCodePath: '',
                    startDateTime: '',
                    endDateTime: ''
                },
                copyParams: {
                  id: undefined,
                  copyType: 'Table'
                },
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑代码生成配置',
                    create: '添加代码生成配置'
                },
                configForm: {
                    id: '',
                    datasourceId: undefined,
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    domainName: '',
                    controllerPath: '',
                    formType: 'Modal',
                    tableType: 'single',
                    extendsFlag: 'false',
                    tableShowType: undefined,
                    formItemCol: undefined,
                    leftTreeType: undefined,
                    frontCodePath: '',
                    serviceCodePath: '',
                    frontCodeDir: '',
                    importFlag: 'true',
                    exportFlag: 'true',
                    queryReuse: 'true',
                    statusHandling: 'true',
                    codeType: 'ALL',
                    codeSaveType: 'SERVER_DOWNLOAD'
                },
                // 表头
                columns: [
                    {
                        title: '数据源',
                        align: 'center',
                        dataIndex: 'datasourceName'
                    },
                    {
                        title: '模块名称',
                        align: 'center',
                        dataIndex: 'moduleName'
                    },
                    {
                        title: '模块代码',
                        align: 'center',
                        dataIndex: 'moduleCode'
                    },
                    {
                        title: '服务名称',
                        align: 'center',
                        dataIndex: 'serviceName'
                    },
                    {
                        title: '表名',
                        align: 'center',
                        dataIndex: 'tableName'
                    },

                    {
                        title: '表单类型',
                        align: 'center',
                        dataIndex: 'formType',
                        scopedSlots: { customRender: 'formTypeFilter' }
                    },
                    {
                        title: '联表类型',
                        align: 'center',
                        dataIndex: 'tableType',
                        scopedSlots: { customRender: 'tableTypeFilter' }
                    },
                    {
                        title: '展示类型',
                        align: 'center',
                        dataIndex: 'tableShowType',
                        scopedSlots: { customRender: 'tableShowTypeFilter' }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        dataIndex: 'action',
                        width: '280px',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                rules: {
                    // 字段校验，这里自己选择使用哪些校验
                    config: [
                        { required: true, message: '请输入config', trigger: 'blur' },
                        { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' }
                    ]
                },
                downloadLoading: false,
                configLabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                configWrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                configPagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 ${total} 条`
                },
                // 加载数据方法 必须为 Promise 对象
                loadData: parameter => {
                    return function () {}
                },
                // 获取数据源搜索的查询接口
                queryDatasourceListFunction: queryDatasourceList
            }
        },
        watch: {
            // filterText (val) {
            //   this.$refs['configTree'].filter(val)
            // }
        },
        created () {
            const that = this
            // this.getDataDictList(this.formTypeDict).then(function (result) {
            //   that.formTypeDict = result
            // })
            // this.getDataDictList(this.treeTypeDict).then(function (result) {
            //   that.treeTypeDict = result
            // })
            // this.getDataDictList(this.tableTypeDict).then(function (result) {
            //   that.tableTypeDict = result
            // })
            // this.getDataDictList(this.tableShowTypeDict).then(function (result) {
            //   that.tableShowTypeDict = result
            // })
            // this.getDataDictList(this.formColDict).then(function (result) {
            //   that.formColDict = result
            // })
            // this.getDataDictList(this.yesOrNoDict).then(function (result) {
            //   that.yesOrNoDict = result
            // })

            const dictList = [this.formTypeDict, this.treeTypeDict, this.tableTypeDict, this.tableShowTypeDict, this.formColDict, this.yesOrNoDict, this.codeTypeDict, this.codeSaveTypeDict]

            // const dictCodeList = [this.formTypeDict.dictCode, this.treeTypeDict.dictCode,
            // this.tableTypeDict.dictCode, this.tableShowTypeDict.dictCode, this.formColDict.dictCode,
            // this.yesOrNoDict.dictCode]

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
                  return queryConfigList(Object.assign(parameter, that.listQuery))
                        .then(res => {
                            that.list = res.data.records
                            return res.data
                  })
                }
                that.$nextTick(() => {
                  that.handleFilter()
                })
            })
        },
        methods: {
          //  async getDataDictList (dictParams) {
          //     const that = this
          //     that.listLoading = true
          //     await listDict(dictParams.dictCode).then(response => {
          //       dictParams.dictList = response.data
          //       dictParams.filterMap = {}
          //       dictParams.dictList.forEach((item, index, arr) => {
          //         dictParams.filterMap[item.id] = item.dictName
          //       })
          //       that.listLoading = false
          //     })
          //     return dictParams
          //   },
            async getBatchDataDictList (dictParams) {
              const that = this
              let result = {}
              that.listLoading = true
              await batchListGeneratorDict(dictParams).then(response => {
                result = response.data
                that.listLoading = false
              })
              return result
            },
            resetQuery () {
                this.listQuery = {
                        id: '',
                        datasourceId: undefined,
                        moduleName: '',
                        moduleCode: '',
                        serviceName: '',
                        tableName: '',
                        tableAlias: '',
                        tablePrefix: '',
                        parentPackage: '',
                        domainName: '',
                        controllerPath: '',
                        tableType: 'single',
                        tableShowType: undefined,
                        formItemCol: undefined,
                        leftTreeType: undefined,
                        startDateTime: '',
                        endDateTime: ''
                }
            },
            resetConfigForm () {
                this.configForm = {
                    id: '',
                    datasourceId: undefined,
                    moduleName: '',
                    moduleCode: '',
                    serviceName: '',
                    tableName: '',
                    tableAlias: '',
                    tablePrefix: '',
                    parentPackage: '',
                    controllerPath: '',
                    formType: 'Modal',
                    tableType: 'single',
                    extendsFlag: 'false',
                    tableShowType: undefined,
                    formItemCol: undefined,
                    leftTreeType: undefined,
                    frontCodePath: '',
                    serviceCodePath: '',
                    frontCodeDir: '',
                    importFlag: 'true',
                    exportFlag: 'true',
                    queryReuse: 'true',
                    statusHandling: 'true',
                    codeType: 'ALL',
                    codeSaveType: 'SERVER_DOWNLOAD'
                }
            },
            // 选中 option 调用
            clickSetListQuery (val) {
              this.listQuery.datasourceId = val
            },
            clickSetForm (val) {
              this.configForm.datasourceId = val
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
                queryConfigList(this.listQuery).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.configTable.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.configTable.refresh()
            },
            handleCreate () {
                this.resetConfigForm()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['configForm'].clearValidate()
                })
            },
            createData () {
                this.$refs['configForm'].validate(valid => {
                    if (valid) {
                        createConfig(this.configForm).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.configForm = Object.assign({}, row) // copy obj
                // 数据字典存的是字符串，数据库存的是tinyint，这里需要转换一下
                this.configForm.importFlag = this.configForm.importFlag + ''
                this.configForm.exportFlag = this.configForm.exportFlag + ''
                this.configForm.queryReuse = this.configForm.queryReuse + ''
                this.configForm.statusHandling = this.configForm.statusHandling + ''
                this.configForm.extendsFlag = this.configForm.extendsFlag + ''
                this.dialogStatus = 'update'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['configForm'].clearValidate()
                })
            },
            updateData () {
                this.$refs['configForm'].validate(valid => {
                    if (valid) {
                        updateConfig(this.configForm).then(() => {
                            this.dialogFormVisible = false
                            this.$message.success('更新成功')
                            this.handleTableRefresh()
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
                        deleteConfig(row.id).then(() => {
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
                var configList = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: configList.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDeleteConfig(that.selectedRowKeys).then(() => {
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
            copyTableConfig (row) {
                const that = this
                this.$confirm({
                    title: '请注意，确定是否仅复制表配置？',
                    content: '该操作将只会复制表的配置，不会复制表字段的配置，是否继续？',
                    onOk () {
                      that.copyParams.id = row.id
                      that.copyParams.copyType = 'Table'
                      that.processCopy()
                    },
                    onCancel () {

                    }
                })
            },
            copyTableFieldConfig (row) {
                const that = this
                this.$confirm({
                    title: '请注意，确定是否全部复制？',
                    content: '如果全部复制的话，会将表字段的所有配置都复制，是否继续？',
                    onOk () {
                      that.copyParams.id = row.id
                      that.copyParams.copyType = 'TableField'
                      that.processCopy()
                    },
                    onCancel () {

                    }
                })
            },
            processCopy () {
                this.$loading.show()
                copyConfig(this.copyParams).then(response => {
                    this.$loading.hide()
                    this.$message.success('复制成功')
                    this.handleFilter()
                })
            },
            processGenerateCode (row) {
              const that = this
               this.$confirm({
                    title: '生成代码将覆盖旧的代码，是否继续?',
                    content: '如果已经存在该模块代码，那么新生成的代码将会覆盖旧代码，请确认是否存在影响。',
                    onOk () {
                        that.$loading.show()
                        try {
                        generateCode({ id: row.id }).then(response => {
                            that.$loading.hide()
                            that.$message.success('执行成功')
                        })
                        } catch (e) {
                          that.$loading.hide()
                        }
                    },
                    onCancel () {
                        that.$message.info('已取消执行')
                    }
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

.model-config{
 padding-bottom: 20px;
}

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
