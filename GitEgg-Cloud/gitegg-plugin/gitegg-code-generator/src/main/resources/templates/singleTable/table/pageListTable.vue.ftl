<template>
  <a-card :bordered="false" class="content">
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline">
        <a-row :gutter="48">
<#list fields as field>
<#if field?? && field.queryTerm == true>
          <a-col :md="6" :sm="24">
            <a-form-model-item label="${field.comment}" prop="${field.entityName}">
<#if field.controlType == "INPUT_TEXT">
              <a-input
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="请输入${field.comment}"
                :max-length="${field.maxLength}"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "TEXTAREA">
              <a-textarea
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="请输入${field.comment}"
                :auto-size="{ minRows: 3, maxRows: 5 }"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "INPUT_NUMBER">
              <a-input-number
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="${field.comment}"
                :min="${field.min}"
                :max="${field.max}"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "SELECT" || field.controlType == "SELECT_MULTI" || field.controlType == "RADIO" || field.controlType == "SWITCH" || field.controlType == "CHECKBOX">
<#assign dictSelect=true/>
<#macro Multiple controlType>
<#if controlType == "SELECT_MULTI">
                        mode="multiple"
<#else>
                        mode="default"
</#if>
</#macro>
              <a-select v-model.trim="list${entity}Query.${field.entityName}"
                        placeholder="${field.comment}"
                        show-search
                        <@Multiple controlType="${field.controlType}"/>
                        :filter-option="filterOption"
                        @keyup.enter.native="handleFilter" >
                <a-select-option :key="item.id + index"
                                 v-for="(item,index) in ${field.entityName}Dict.dictList"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
</#if>
<#if field.controlType == "DTAE_PICKER" || field.controlType == "DTAE_TIME_PICKER">
<#macro DateTime controlType>
<#if controlType == "DTAE_TIME_PICKER">
                             show-time
                             valueFormat="YYYY-MM-DD HH:mm:ss"
</#if>
</#macro>
              <a-date-picker v-model.trim="list${entity}Query.${field.entityName}"
                             placeholder="${field.comment}"
                             <@DateTime controlType="${field.controlType}"/>
                             style="width:100%;"/>
</#if>
<#if field.controlType == "TIME_PICKER">
              <a-time-picker v-model.trim="list${entity}Query.${field.entityName}"
                             placeholder="${field.comment}"
                             style="width:100%;"/>
</#if>
<#if field.controlType == "ProvinceSelect">
<#assign provinceSelect=true/>
              <a-cascader v-model="list${entity}Query.${field.entityName}"
                          :options="provinceOptions"
                          placeholder="输选择${field.comment}"
                          style="width:100%;"
                          @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "OrganizationTreeSelect">
<#assign organizationTree=true/>
              <a-input
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "RoleSelect">
<#assign roleTree=true/>
              <a-input
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "ResourceTreeSelect">
<#assign resourceTree=true/>
              <a-input
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "MenuTreeSelect">
<#assign menuTree=true/>
              <a-input
                v-model.trim="list${entity}Query.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}"
                @keyup.enter.native="handleFilter" />
</#if>
<#if field.controlType == "RATE">
              <a-rate v-model.trim="list${entity}Query.${field.entityName}"
                      placeholder="${field.comment}"
                      @keyup.enter.native="handleFilter"/>
</#if>
            </a-form-model-item>
          </a-col>
</#if>
</#list>
          <template v-if="advanced">
            <a-col :md="6" :sm="24">
              <a-form-model-item label="开始时间">
                <a-date-picker v-model.trim="list${entity}Query.beginDateTime"
                               placeholder="开始时间"
                               valueFormat="YYYY-MM-DD"
                               style="width:100%;"/>
              </a-form-model-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-model-item label="结束时间">
                <a-date-picker v-model.trim="list${entity}Query.endDateTime"
                               placeholder="结束时间"
                               valueFormat="YYYY-MM-DD"
                               style="width:100%;"/>
              </a-form-model-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 6 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="handleFilter">查询</a-button>
              <a-button style="margin-left: 8px" @click="reset${entity}Query">重置</a-button>
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
<#if config.exportFlag == true>
      <a-button type="primary" icon="cloud-download" @click="handleDownload" style="margin-left: 8px;">导出</a-button>
</#if>
<#if config.importFlag == true>
      <a-upload name="uploadFile" :show-upload-list="false" :before-upload="beforeUpload">
        <a-button> <a-icon type="upload" /> 导入 </a-button>
      </a-upload>
      <a href="javascript:;" @click="handleDownloadTemplate" style="margin-left: 8px;">下载导入模板</a>
</#if>
    </div>
<#list table.fields as field>
    <#if field?? && field.annotationColumnName?ends_with("status") && config.statusHandling == true>
        <#assign hasStatus=true/>
        <#assign statusName=field.propertyName/>
    </#if>
</#list>
<#list fields as field>
     <#if field?? && field.fieldName?ends_with("status") && config.statusHandling == true>
         <#assign hasStatus=true/>
         <#assign statusName=field.entityName/>
     </#if>
 </#list>
    <s-table
      ref="${table.entityPath}Table"
      size="default"
      bordered
      :rowKey="row=>row.id"
      :columns="columns"
      :data="loadData"
      :scroll="{x:1500}"
      showPagination="auto"
      :pagination="${table.entityPath}Pagination"
      :rowSelection="{ selectedRowKeys: this.selectedRowKeys, onChange: this.onSelectChange }"
    >
<#-- ----------  所有的字典类型 设置Filter  ---------->
<#list dictCodeFields as field>
    <#if field??>
      <span slot="${field.entityName}Slot" slot-scope="text, record">
        {{ record.${field.entityName} | ${field.entityName}DictFilter }}
      </span>
    </#if>
</#list>
<#------------  END 所有的字典类型 设置Filter  ---------->
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
<#if hasStatus?? && hasStatus == true>
            <a-menu-item>
              <a href="javascript:;" v-if="record.${statusName}!='1'" size="mini" type="success" @click="handleModifyStatus(record,'1')">启用
              </a>
              <a href="javascript:;" v-if="record.${statusName}!='0' && record.${statusName}!='2'" size="mini" @click="handleModifyStatus(record,'0')">禁用
              </a>
            </a-menu-item>
</#if>
            <a-menu-item>
              <a href="javascript:;" @click="handleDelete(record)">删除</a>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <#if config.formType = "modal">
    <a-modal :title="textMap[dialogStatus]"
             :maskClosable="false"
             :visible="dialogFormVisible"
             :width="800"
             @cancel="() => dialogFormVisible = false">
    <#elseif config.formType == "drawer">
    <a-drawer :title="textMap[dialogStatus]"
              placement="right"
              :visible="dialogFormVisible"
              :width="700"
              :closable="false"
              :destroyOnClose="true"
              :maskClosable="false"
              @cancel="() => dialogFormVisible = false">
    </#if>
    <#if config.formType = "modal" || config.formType == "drawer">
      <a-form-model
        ref="${table.entityPath}Form"
        :model="${table.entityPath}Form"
        :rules="rules"
        :label-col="${table.entityPath}LabelCol"
        :wrapper-col="${table.entityPath}WrapperCol">
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list formFields as field>
<#if (field_index + 1)%2 != 0>
        <a-row>
</#if>
          <a-col :md="${config.formItemCol}" :sm="24">
            <a-form-model-item label="${field.comment}" prop="${field.entityName}">
<#if field.controlType == "INPUT_TEXT">
              <a-input
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="请输入${field.comment}"
                :max-length="${field.maxLength}" />
</#if>
<#if field.controlType == "TEXTAREA">
              <a-textarea
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="请输入${field.comment}"
                :auto-size="{ minRows: 3, maxRows: 5 }" />
</#if>
<#if field.controlType == "INPUT_NUMBER">
              <a-input-number
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="${field.comment}"
                <#if field.min??>:min="${field.min}"</#if>
                <#if field.max??>:max="${field.max}"</#if> />
</#if>
<#if field.controlType == "RADIO">
            <#assign dictSelect=true/>
              <a-radio-group v-model="${table.entityPath}Form.${field.entityName}"
                             name="${table.entityPath}Form.Radio${field.entityName}">
                <a-radio :key="item.id + index" v-for="(item,index) in ${field.entityName}Dict.dictList" :value="item.dictCode">{{ item.dictName }}</a-radio>
              </a-radio-group>
</#if>
<#if field.controlType == "CHECKBOX">
            <#assign dictSelect=true/>
              <a-checkbox-group v-model="${table.entityPath}Form.${field.entityName}" name="${table.entityPath}Form.RadioCheckbox${field.entityName}">
                <a-checkbox :key="item.id + index" v-for="(item,index) in ${field.entityName}Dict.dictList" :value="item.dictCode">
                  {{ item.dictName }}
                </a-checkbox>
              </a-checkbox-group>
</#if>
<#if field.controlType == "SWITCH">
            <#assign dictSelect=true/>
              <a-switch v-model="${table.entityPath}Form.${field.entityName}"
                        name="${table.entityPath}Form.Switch${field.entityName}"
                        checked-children="开"
                        un-checked-children="关"
                        default-checked>
                <span :key="item.id + index" v-for="(item,index) in ${field.entityName}Dict.dictList" :slot="item.dictCode" >{{ item.dictName }}</span>
              </a-switch>
</#if>
<#if field.controlType == "SELECT" || field.controlType == "SELECT_MULTI">
            <#assign dictSelect=true/>
            <#macro Multiple controlType>
                <#if controlType == "SELECT_MULTI">
                mode="multiple"
                <#else>
                mode="default"
                </#if>
            </#macro>
              <a-select
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="${field.comment}"
                show-search
                <@Multiple controlType="${field.controlType}"/>
                :filter-option="filterOption" >
                <a-select-option v-for="item in ${field.entityName}Dict.dictList"
                                 :key="item.dictCode"
                                 :label="item.dictName"
                                 :value="item.dictCode">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
</#if>
<#if field.controlType == "DTAE_PICKER" || field.controlType == "DTAE_TIME_PICKER">
            <#macro DateTime controlType>
                <#if controlType == "DTAE_TIME_PICKER">
                             show-time
                             valueFormat="YYYY-MM-DD HH:mm:ss"
                </#if>
            </#macro>
              <a-date-picker v-model.trim="${table.entityPath}Form.${field.entityName}"
                             placeholder="${field.comment}"
                             <@DateTime controlType="${field.controlType}"/>
                             style="width:100%;"/>
</#if>
<#if field.controlType == "TIME_PICKER">
              <a-time-picker v-model.trim="${table.entityPath}Form.${field.entityName}"
                             placeholder="${field.comment}"
                             style="width:100%;"/>
</#if>
<#if field.controlType == "ProvinceSelect">
            <#assign provinceSelect=true/>
              <a-cascader v-model="${table.entityPath}Form.${field.entityName}"
                          :options="provinceOptions"
                          placeholder="输选择${field.comment}"
                          style="width:100%;" />
</#if>
<#if field.controlType == "OrganizationTreeSelect">
            <#assign organizationTree=true/>
              <a-input
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}" />
</#if>
<#if field.controlType == "RoleSelect">
            <#assign roleTree=true/>
              <a-input
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}" />
</#if>
<#if field.controlType == "ResourceTreeSelect">
            <#assign resourceTree=true/>
              <a-input
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}" />
</#if>
<#if field.controlType == "MenuTreeSelect">
            <#assign menuTree=true/>
              <a-input
                v-model.trim="${table.entityPath}Form.${field.entityName}"
                placeholder="${field.comment}"
                :max-length="${field.maxLength}" />
</#if>
<#if field.controlType == "RATE">
              <a-rate v-model.trim="${table.entityPath}Form.${field.entityName}"
                      placeholder="${field.comment}" />
</#if>
<#if field.controlType == "UPLOAD_FILE">
<#assign uploadFile=true/>
              <upload-file
                      v-model.trim="${table.entityPath}Form.${field.entityName}"
                      uploadBtnName="上传文件"
                      :fileNumber="1"
              ></upload-file>
</#if>
<#if field.controlType == "UPLOAD_IMG">
<#assign uploadImg=true/>
              <upload-image
                      v-model.trim="${table.entityPath}Form.${field.entityName}"
                      uploadBtnName="上传图片"
                      :imgNumber="1"
              ></upload-image>
</#if>
            </a-form-model-item>
          </a-col>
<#if (field_index + 1)%2 == 0 || !field?has_next>
        </a-row>
</#if>
</#list>
<#------------  END 字段循环遍历  ---------->
      </a-form-model>
    </#if>
    <#if config.formType = "modal">
      <div slot="footer" class="dialog-footer">
    <#elseif config.formType == "drawer">
      <div class="footer-button">
    </#if>
    <#if config.formType = "modal" || config.formType == "drawer">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</a-button>
        <a-button v-else type="primary" @click="updateData">修改</a-button>
      </div>
    </#if>
    <#if config.formType = "modal">
    </a-modal>
    <#elseif config.formType == "drawer">
    </a-drawer>
    </#if>
  </a-card>
</template>
<#list fields as field>
   <#if field?? && field.fieldUnique == true>
       <#assign checkExist=true/>
   </#if>
</#list>
<script>
    import { STable } from '@/components'
    <#if uploadFile?? && uploadFile == true>
    import UploadFile from '@/components/Upload/UploadFile'
    </#if>
    <#if uploadImg?? && uploadImg == true>
    import UploadImage from '@/components/Upload/UploadImage'
    </#if>
    import { query${entity}List, create${entity}, update${entity}, <#if hasStatus?? && hasStatus == true>update${entity}Status, </#if>delete${entity}<#if checkExist?? && checkExist == true>, check${entity}Exist</#if><#if config.exportFlag == true>, batchDelete${entity}, download${entity}List</#if><#if config.importFlag == true>, upload${entity}, download${entity}Template</#if> } from '@/api/${vueJsPath}'
    import moment from 'moment'
    <#if provinceSelect?? && provinceSelect == true>
    import Data from '@/api/pcaa'
    </#if>
    <#if config.importFlag == true || config.exportFlag == true>
    import { handleDownloadBlod } from '@/utils/util'
    </#if>
    <#if dictSelect?? && dictSelect == true>
    import { batchListDictBusiness } from '@/api/system/base/dictBusiness'
    let vm = {}
    </#if>
    export default {
        name: '${entity}Table',
        components: { moment, STable<#if uploadFile?? && uploadFile == true>, UploadFile</#if><#if uploadImg?? && uploadImg == true>, UploadImage</#if> },
        filters: {
            <#-- ----------  所有的字典类型 字段循环遍历  ---------->
            <#list dictCodeFields as field>
            <#if field??>
            // ${field.comment}数据字典展示
            ${field.entityName}DictFilter (dictCode) {
                return vm.${field.entityName}Dict.filterMap[dictCode]
            }<#if field?has_next>,</#if>
            </#if>
            </#list>
            <#------------  END 所有的字典类型 字段循环遍历  ---------->
        },
        data () {
            <#if dictSelect?? && dictSelect == true>
            vm = this
            </#if>
            <#if checkExist?? && checkExist == true>
            // 增加或更新记录时，判断字段是否已经存在
            <#-- ----------  BEGIN 字段循环遍历  ---------->
            <#list fields as field>
            <#if field?? && field.fieldUnique == true>
            var valid${field.entityName?cap_first} = (rule, value, callback) => {
                var keyData = {
                    id: this.${table.entityPath}Form.id,
                    checkField: '${field.fieldName}',
                    checkValue: value
                }
                check${entity}Exist(keyData).then(response => {
                    if (!response.data) {
                        callback(new Error('记录已存在')) // 这里改为字段名称
                    } else {
                        callback()
                    }
                })
            }
            </#if>
            </#list>
            <#------------  END 字段循环遍历  ---------->
            </#if>
            return {
                advanced: false,
                current${entity}: '',
                filterText: '',
                <#if provinceSelect?? && provinceSelect == true>
                provinceOptions: null,
                </#if>
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                list${entity}Query: {
                <#-- ----------  BEGIN 字段循环遍历  ---------->
                <#list fields as field>
                <#if field?? && field.queryTerm == true>
                    ${field.entityName}: <#if field.defaultValue?? && field.defaultValue != "">'${field.defaultValue}'<#else>undefined</#if>, // ${field.comment}
                </#if>
                </#list>
                <#------------  END 字段循环遍历  ---------->
                    beginDateTime: '',
                    endDateTime: ''
                },
                <#-- ----------  所有的字典类型 字段循环遍历  ---------->
                <#list dictCodeFields as field>
                <#if field??>
                ${field.entityName}Dict: {
                    dictCode: '<#if field.dictCode?? && field.dictCode !?length gt 0>${field.dictCode}<#else>${field.entityName}DictCode</#if>',
                    dictList: [],
                    filterMap: {}
                }, // ${field.comment}数据字典列表
                </#if>
                </#list>
                <#------------  END 所有的字典类型 字段循环遍历  ---------->
                dialogFormVisible: false,
                dialogStatus: '',
                textMap: {
                    update: '编辑',
                    create: '添加'
                },
                ${table.entityPath}Form: {
                    <#list formFields as field>
                    ${field.entityName}: <#if field.defaultValue?? && field.defaultValue != "">'${field.defaultValue}'<#else>undefined</#if><#if field?? && field?has_next>,</#if>
                    </#list>
                },
                // 表头
                columns: [
                    <#-- ----------  BEGIN 字段循环遍历  ---------->
                    <#list fields as field>
                    <#if field?? && field.listShow == true>
                    {
                        title: '${field.comment?replace("'","\"")}',
                        align: 'center',
                        width: 200,
                        ellipsis: true,
<#if field?? && field.dictCode !?length gt 0>
                        scopedSlots: { customRender: '${field.entityName}Slot' },
</#if>
                        dataIndex: '${field.entityName}'
                    },
                    </#if>
                    </#list>
                    <#------------  END 字段循环遍历  ---------->
                    {
                        title: '操作',
                        dataIndex: 'action',
                        width: 180,
                        fixed: 'right',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                rules: {
                    <#-- ----------  BEGIN 字段循环遍历  ---------->
                    // 字段校验
                    <#list formFields as field>
                    <#if field?? && (field.required == true || field.fieldUnique == true || (field.maxLength?? && field.maxLength gt 0) || (field.validates?? && field.validates?size != 0))>
                    ${field.entityName}: [
                        <#if field.maxLength?? && field.maxLength gt 0 && field.entityType == 'String'>
                        { min: ${field.minLength}, max: ${field.maxLength}, message: '长度在 ${field.minLength} 到 ${field.maxLength} 个字符', trigger: 'blur', type: 'string' }<#if field.fieldUnique == true || field.required == true || field.validateValue?? || field.validateRegular??>,</#if>
                        </#if>
                        <#if field.fieldUnique == true>
                        { validator: valid${field.entityName?cap_first}, trigger: 'blur' }<#if field.required == true || field.validateValue?? || field.validateRegular??>,</#if>
                        </#if>
                        <#if field.required == true>
                        { required: true, message: '请输入${field.comment}', trigger: 'blur' }<#if field.validateValue?? || field.validateRegular??>,</#if>
                        </#if>
                        <#if field.validateValue??>
                        { pattern: /${field.validateValue}/, required: true, message: '请输入正确的${field.comment}', trigger: 'blur' }<#if field.validateRegular??>,</#if>
                        </#if>
                        <#if field.validateRegular??>
                        { pattern: /${field.validateRegular}/, required: true, message: '请输入正确的${field.comment}', trigger: 'blur' }
                        </#if>
                    ]<#if field?has_next>,</#if>
                    </#if>
                    </#list>
                    <#------------  END 字段循环遍历  ---------->
                },
                downloadLoading: false,
                ${table.entityPath}LabelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                ${table.entityPath}WrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                selectedRowKeys: [],
                selectedRows: [],
                ${table.entityPath}Pagination: {
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    defaultCurrent: 1,
                    showTotal: (total, range) => `共 <#noparse>${total}</#noparse> 条`
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
            <#------------  END 所有的字典类型 字段循环遍历  ---------->
            <#if dictSelect?? && dictSelect == true>
            const dictList = [<#list dictCodeFields as field><#if field??>that.${field.entityName}Dict<#if field?has_next>, </#if></#if></#list>]
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
                    return query${entity}List(Object.assign(parameter, that.list${entity}Query))
                        .then(res => {
                            that.list = res.data
                            return res
                        })
                }
                that.$nextTick(() => {
                    that.handleFilter()
                })
            })
            <#else>
                that.loadData = function (parameter) {
                    return query${entity}List(Object.assign(parameter, that.list${entity}Query))
                        .then(res => {
                            that.list = res.data
                            return res
                        })
                }
                that.$nextTick(() => {
                    that.handleFilter()
                })
            </#if>
            <#if provinceSelect?? && provinceSelect == true>
                that.getAreaList()
            </#if>
        },
        methods: {
          <#if provinceSelect?? && provinceSelect == true>
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
          </#if>
          <#-- ----------  所有的字典类型 字段循环遍历  ---------->
          <#if dictSelect?? && dictSelect == true>
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
            </#if>
            <#------------  END 所有的字典类型 字段循环遍历  ---------->
            reset${entity}Query () {
                this.list${entity}Query = {
                    <#-- ----------  BEGIN 字段循环遍历  ---------->
                    <#list fields as field>
                    <#if field?? && field.queryTerm == true>
                        ${field.entityName}: <#if field.defaultValue?? && field.defaultValue != "">'${field.defaultValue}'<#else>undefined</#if>, // ${field.comment}
                    </#if>
                    </#list>
                    <#------------  END 字段循环遍历  ---------->
                        beginDateTime: '',
                        endDateTime: ''
                }
            },
            reset${entity}Form () {
                <#-- ----------  BEGIN 字段循环遍历  ---------->
                this.${table.entityPath}Form = {
                <#list formFields as field>
                    ${field.entityName}: <#if field.defaultValue?? && field.defaultValue != "">'${field.defaultValue}'<#else>undefined</#if><#if field?has_next>,</#if> // ${field.comment}
                </#list>
                <#------------  END 字段循环遍历  ---------->
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
                query${entity}List(this.list${entity}Query).then(response => {
                    this.list = response.data
                    this.total = response.count
                    this.listLoading = false
                })
            },
            handleFilter () {
                this.$refs.${table.entityPath}Table.refresh(true)
            },
            handleTableRefresh () {
                this.$refs.${table.entityPath}Table.refresh()
            },
            handleCreate () {
                this.reset${entity}Form()
                this.dialogStatus = 'create'
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['${table.entityPath}Form'].clearValidate()
                })
            },
            createData () {
                this.$refs['${table.entityPath}Form'].validate(valid => {
                    if (valid) {
                        create${entity}(this.${table.entityPath}Form).then(() => {
                            this.dialogFormVisible = false
                            this.handleFilter()
                            this.$message.success('创建成功')
                        })
                    }
                })
            },
            handleUpdate (row) {
                this.${table.entityPath}Form = Object.assign({}, row) // copy obj
                this.dialogStatus = 'update'
                <#if hasStatus?? && hasStatus == true>
                this.${table.entityPath}Form.${statusName} = row.${statusName} + ''
                </#if>
                this.dialogFormVisible = true
                this.$nextTick(() => {
                    this.$refs['${table.entityPath}Form'].clearValidate()
                })
            },
            updateData () {
                this.$refs['${table.entityPath}Form'].validate(valid => {
                    if (valid) {
                        update${entity}(this.${table.entityPath}Form).then(() => {
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
                        delete${entity}(row.id).then(() => {
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
                var ${table.entityPath}List = this.selectedRows.map(function (n) {
                    return n.id
                })
                var that = this
                this.$confirm({
                    title: '以下选中记录将被全部删除，是否继续?',
                    content: ${table.entityPath}List.join(','),
                    onOk () {
                        that.listLoading = true
                        batchDelete${entity}(that.selectedRowKeys).then(() => {
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
            <#if hasStatus?? && hasStatus == true>
            handleModifyStatus (row, status) {
                this.listLoading = true
                update${entity}Status(row.id, status).then(() => {
                    this.listLoading = false
                    row.${statusName} = status
                    this.$message.success('状态修改成功')
                })
            },
            </#if>
            <#if config.exportFlag == true>
            handleDownload () {
                this.downloadLoading = true
                download${entity}List(this.list${entity}Query).then(response => {
                    handleDownloadBlod('${table.comment!}数据列表.xlsx', response)
                    this.listLoading = false
                })
            },
            </#if>
            <#if config.importFlag == true>
            beforeUpload (file) {
                this.handleUpload(file)
                return false
            },
            handleUpload (file) {
                this.uploadedFileName = ''
                const formData = new FormData()
                formData.append('uploadFile', file)
                this.uploading = true
                upload${entity}(formData).then(() => {
                    this.uploading = false
                    this.$message.success('${table.comment!}数据导入成功')
                    this.handleFilter()
                }).catch(err => {
                    console.log('uploading', err)
                    this.$message.error('${table.comment!}数据导入失败')
                })
            },
            handleDownloadTemplate () {
                this.downloadLoading = true
                download${entity}Template(this.list${entity}Query).then(response => {
                    handleDownloadBlod('${table.comment!}批量上传模板.xlsx', response)
                    this.listLoading = false
                })
            },
            </#if>
            filterOption (input, option) {
              return (
                      option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
              )
            }
        }
    }
</script>
<#if config.formType == "drawer">
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
</#if>