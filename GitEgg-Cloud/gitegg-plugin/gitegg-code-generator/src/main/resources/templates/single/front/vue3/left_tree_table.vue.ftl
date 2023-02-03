<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <!--树选择-->
    <${config.leftTreeType}
      helpMessage="再次点击选中的节点，可取消选中"
      @select="handleSelect"
      class="m-4"
    />
    <!--数据表格-->
    <BasicTable
      @register="registerTable"
      class="w-3/4 xl:w-4/5"
      :searchInfo="searchInfo"
      :rowSelection="{
        type: 'checkbox',
        selectedRowKeys: checkedKeys,
        onChange: onSelectChange,
        preserveSelectedRowKeys: true,
      }"
    >
      <template #tableTitle>
        <a-button type="primary" @click="handleCreate" class="mr-2">
          <icon icon="ant-design:plus-outlined" /> 新增${config.domainName!}
        </a-button>
        <Dropdown
          v-if="checkedKeys.length > 0"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:delete-outlined',
              text: '批量删除',
              event: '1',
              popConfirm: {
                title: '是否确认删除',
                placement: 'right',
                confirm: handleBatchDelete.bind(null),
              },
            },
          ]"
          popconfirm
        >
          <slot name="more"></slot>
          <a-button v-if="!$slots.more">
            批量操作
            <icon icon="ant-design:down-outlined" />
          </a-button>
        </Dropdown>
      </template>
      <template #toolbar>
<#if config.importFlag == true>
        <Upload accept=".xls,.xlsx" :showUploadList="false" :beforeUpload="beforeImport${entity}List">
          <a-button type="primary"> <icon icon="ant-design:upload-outlined" /> 数据导入 </a-button>
        </Upload>
</#if>
<#if config.exportFlag == true>
        <Dropdown
          class="mr-2"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:check-circle-outlined',
              text: '导出选中',
              event: '1',
              disabled: checkedKeys.length === 0,
              onClick: handleExport${entity}List.bind(null, true),
            },
            {
              icon: 'ant-design:check-circle-twotone',
              text: '导出全部',
              event: '1',
              onClick: handleExport${entity}List.bind(null, false),
            },
          ]"
          popconfirm
        >
          <slot name="more"></slot>
          <a-button v-if="!$slots.more">
            数据导出
            <icon icon="ant-design:down-outlined" />
          </a-button>
        </Dropdown>
</#if>
<#if config.importFlag == true>
        <a-button type="link" @click="handleDownloadTemplate">下载模板</a-button>
</#if>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '编辑',
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '删除${config.domainName!}',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除${config.domainName!}',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
<#if config.formType == "Drawer" ||  config.formType == "Modal">
    <${entity}${config.formType} @register="register${config.formType}" @success="handleSuccess" />
</#if>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
<#if config.importFlag == true || config.exportFlag == true>
  import { Upload } from 'ant-design-vue';
</#if>
<#if config.exportFlag == true>
  import { merge } from 'lodash-es';
</#if>
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    get${entity}List,
    delete${entity},
    batchDelete${entity},
<#if config.exportFlag == true>
    export${entity}List,
</#if>
<#if config.importFlag == true>
    import${entity}List,
    download${entity}Template,
</#if>
  } from '/@/api/${vueJsPath}';
<#if config.formType == "Drawer" ||  config.formType == "Modal">
  import { use${config.formType} } from '/@/components/${config.formType}';
  import ${entity}${config.formType} from './${entity}${config.formType}.vue';
<#elseif config.formType == "Tab">
  // 打开新的tab页
  import { useGo } from '/@/hooks/web/usePage';
</#if>
  import { PageWrapper } from '/@/components/Page';
  import { ${config.leftTreeType} } from '/@/components/Subassembly/Tree';

  import { columns, searchFormSchema } from './${table.entityPath}.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import { Icon } from '/@/components/Icon';
<#if config.importFlag == true || config.exportFlag == true>
  import { handleDownloadBlod } from '/@/utils/file/download';
  import { useLoading } from '/@/components/Loading';
</#if>

  export default defineComponent({
    name: '${entity}Management',
    components: {
      BasicTable,
      PageWrapper,
      ${config.leftTreeType},
<#if config.formType == "Drawer" ||  config.formType == "Modal">
      ${entity}${config.formType},
</#if>
      TableAction,
      Dropdown,
      Icon,
<#if config.importFlag == true || config.exportFlag == true>
      Upload,
</#if>
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
<#if config.formType == "Drawer" ||  config.formType == "Modal">
      const [register${config.formType}, { open${config.formType} }] = use${config.formType}();
<#elseif config.formType == "Tab">
      // 打开新的tab页
      const go = useGo();
</#if>
<#if config.importFlag == true || config.exportFlag == true>
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '',
      });
</#if>
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord, getForm }] = useTable({
        title: '${config.domainName!}列表',
        api: get${entity}List,
        rowKey: 'id',
        columns,
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        handleSearchInfoFn(info) {
          return info;
        },
        actionColumn: {
          width: 180,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleCreate() {
<#if config.formType == "Drawer" ||  config.formType == "Modal">
        open${config.formType}(true, {
          isUpdate: false,
        });
<#elseif config.formType == "Tab">
        // 打开新页面
        go('/${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/edit' + record.id);
</#if>
      }
      function handleEdit(record: Recordable) {
<#if config.formType == "Drawer" ||  config.formType == "Modal">
        handle${entity}Record(record);
        open${config.formType}(true, {
          record,
          isUpdate: true,
        });
      }
<#elseif config.formType == "Tab">
        // 打开新页面
        go('/${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/edit' + record.id);
</#if>

      function handleDelete(record: Recordable) {
        delete${entity}(record.id)
          .then(() => {
            createMessage.success(`${config.domainName!}删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('${config.domainName!}删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDelete${entity}(checkedKeys.value)
          .then(() => {
            createMessage.success(`${config.domainName!}删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('${config.domainName!}删除失败');
          })
          .finally(() => {});
      }

      function handleSuccess({ isUpdate, values }) {
        createMessage.success(`操作成功`);
        if (isUpdate) {
          // 演示不刷新表格直接更新内部数据。
          // 注意：updateTableDataRecord要求表格的rowKey属性为string并且存在于每一行的record的keys中
          const result = updateTableDataRecord(values.id, values);
          console.log(result);
        } else {
          reload();
        }
      }
      // 状态等数据展示前的转换转换
      function handle${entity}Record(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回状态Number类型时需要进行转换
<#if hasStatus?? && hasStatus == true>
        record.${statusName} = `<#noparse>${</#noparse>record.${statusName}<#noparse>}</#noparse>`;
</#if>
      }
      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }
<#if config.tableShowType == "left_tree_table">
<#if config.leftTreeType?? && config.leftTreeType == "OrganizationTree">

      function handleSelect(organizationId = '') {
        searchInfo.organizationId = organizationId;
        reload();
      }
<#elseif  config.leftTreeType?? && config.leftTreeType == "OrganizationTree">

    function handleSelect(resourceId = '') {
      searchInfo.resourceId = resourceId;
      reload();
    }
</#if>
</#if>
<#if config.exportFlag == true>

      function getSearchParams() {
        const params: Recordable = merge(getForm()?.getFieldsValue(), searchInfo);
        return params;
      }

      //数据导出
      function handleExport${entity}List(exportChecked: boolean) {
        openFullLoading();
        let params = getSearchParams();
        if (exportChecked) {
          params.${table.entityPath}Ids = checkedKeys.value as string[];
        }
        export${entity}List(params)
          .then((response) => {
            handleDownloadBlod('${config.domainName!}数据列表.xlsx', response);
            closeFullLoading();
          })
          .catch((err) => {
            createMessage.error('${config.domainName!}数据导出失败:' + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }
</#if>
<#if config.importFlag == true>

      // 数据导入
      function beforeImport${entity}List(file) {
        handleUpload${entity}List(file);
        return false;
      }

      function handleUpload${entity}List(file) {
        const formData = new FormData();
        formData.append('uploadFile', file);
        openFullLoading();
        import${entity}List(formData)
          .then(() => {
            createMessage.success(`${config.domainName!}数据导入成功`);
            reload();
          })
          .catch((err) => {
            createMessage.error(`${config.domainName!}数据导入失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 下载导入模板
      function handleDownloadTemplate() {
        openFullLoading();
        download${entity}Template(searchInfo)
          .then((response) => {
            handleDownloadBlod('${config.domainName!}模板批量导入模板.xlsx', response);
          })
          .catch((err) => {
            createMessage.error(`下载导入模板失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }
</#if>

      return {
        registerTable,
<#if config.formType == "Drawer" || config.formType == "Modal">
        register${config.formType},
</#if>
        handleCreate,
        handleEdit,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handle${entity}Record,
        onSelectChange,
        checkedKeys,
        searchInfo,
        handleSelect,
<#if config.exportFlag == true>
        handleExport${entity}List,
</#if>
<#if config.importFlag == true>
        beforeImport${entity}List,
        handleDownloadTemplate,
</#if>
      };
    },
  });
</script>
