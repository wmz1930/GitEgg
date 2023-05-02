<template>
  <div>
    <BasicTable
      @register="registerTable"
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
          <icon icon="ant-design:plus-outlined" /> 新增代码定制
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
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '编辑',
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '配置规则',
                icon: 'clarity:cog-line',
                onClick: handleEditConfig.bind(null, record),
              },
              {
                label: '生成代码',
                icon: 'clarity:shuffle-line',
                onClick: processGenerateCode.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '复制表配置',
                icon: 'clarity:copy-line',
                onClick: copyTableConfig.bind(null, record),
              },
              {
                label: '复制全部配置',
                icon: 'clarity:copy-solid',
                onClick: copyTableFieldConfig.bind(null, record),
              },
              {
                label: '删除代码定制',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除代码定制',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <ConfigTableDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    getConfigList,
    deleteConfig,
    batchDeleteConfig,
    copyConfig,
  } from '/@/api/plugin/codeGenerator/config/config';

  import { generateCode } from '/@/api/plugin/codeGenerator/engine/engine';

  import { useDrawer } from '/@/components/Drawer';
  import ConfigTableDrawer from './ConfigTableDrawer.vue';

  import { columns, searchFormSchema } from './config.table.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLoading } from '/@/components/Loading';

  // 打开新的tab页时需要
  import { useGo } from '/@/hooks/web/usePage';

  import { Dropdown } from '/@/components/Dropdown';
  import Icon from '@/components/Icon/Icon.vue';

  export default defineComponent({
    name: 'ConfigManagement',
    components: {
      BasicTable,
      ConfigTableDrawer,
      TableAction,
      Dropdown,
      Icon,
    },
    setup() {
      // 打开新的tab页
      const go = useGo();
      const checkedKeys = ref(Array<string | number>());
      const { createMessage, createConfirm } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '...',
      });
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '代码定制列表',
        api: getConfigList,
        rowKey: 'id',
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        handleSearchInfoFn(info) {
          return info;
        },
        actionColumn: {
          width: 300,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleCreate() {
        openDrawer(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        handleConfigRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        deleteConfig(record.id)
          .then(() => {
            createMessage.success(`代码定制删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('代码定制删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteConfig(checkedKeys.value)
          .then(() => {
            createMessage.success(`代码定制删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('代码定制删除失败');
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
          createConfirm({
            iconType: 'warning',
            title: '代码定制创建成功，是否进入到详细生成规则配置界面?',
            content: '规则配置可以详细配置展示的列表字段、表单字段、表单校验、数据字典等内容',
            onOk: async () => {
              handleEditConfig(values);
            },
          });
        }
      }

      function copyTableConfig(record) {
        createConfirm({
          iconType: 'warning',
          title: '请注意，确定是否仅复制表配置？',
          content: '该操作将只会复制表的配置，不会复制表字段的配置，是否继续？',
          onOk: async () => {
            openFullLoading();
            await copyConfig({ id: record.id, copyType: 'Table' })
              .then(() => {
                createMessage.success(`已成功复制：${record.moduleName}表配置`);
                reload();
              })
              .catch(() => {
                closeFullLoading();
                createMessage.error(`复制：${record.moduleName}表配置失败`);
              })
              .finally(() => {
                closeFullLoading();
                reload();
              });
          },
          onCancel: async () => {
            closeFullLoading();
          },
        });
      }

      function copyTableFieldConfig(record) {
        createConfirm({
          iconType: 'warning',
          title: '请注意，确定是否全部复制？',
          content: '如果全部复制的话，会将表字段的所有配置都复制，是否继续？',
          onOk: async () => {
            openFullLoading();
            await copyConfig({ id: record.id, copyType: 'TableField' })
              .then(() => {
                createMessage.success(`已成功复制：${record.moduleName}所有配置`);
                reload();
              })
              .catch(() => {
                closeFullLoading();
                createMessage.error(`复制：${record.moduleName}所有配置失败`);
              })
              .finally(() => {
                closeFullLoading();
              });
          },
          onCancel: async () => {
            closeFullLoading();
          },
        });
      }

      function processGenerateCode(record) {
        createConfirm({
          iconType: 'warning',
          title: '生成代码将覆盖旧的代码，是否继续?',
          content: '如果已经存在该模块代码，那么新生成的代码将会覆盖旧代码，请确认是否存在影响。',
          onOk: async () => {
            openFullLoading();
            await generateCode({ id: record.id })
              .then(() => {
                createMessage.success(`已成功生成：${record.moduleName}代码`);
                reload();
              })
              .catch(() => {
                closeFullLoading();
                createMessage.error(`生成：${record.moduleName}代码失败`);
              })
              .finally(() => {
                closeFullLoading();
              });
          },
          onCancel: async () => {
            closeFullLoading();
          },
        });
      }

      // 状态转换
      function handleConfigRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.statusHandling = `${record.statusHandling}`;
        record.queryReuse = `${record.queryReuse}`;
        record.exportFlag = `${record.exportFlag}`;
        record.importFlag = `${record.importFlag}`;
        record.extendsFlag = `${record.extendsFlag}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      function handleEditConfig(record: Recordable) {
        // 打开编辑tab页，后台配置不以 / 开头，这里跳转的时候要以 / 开头
        go('/plugin/code/generator/config/edit/' + record.id);
      }

      return {
        registerTable,
        registerDrawer,
        handleCreate,
        handleEdit,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handleConfigRecord,
        onSelectChange,
        checkedKeys,
        searchInfo,
        copyTableConfig,
        copyTableFieldConfig,
        processGenerateCode,
        handleEditConfig,
      };
    },
  });
</script>
