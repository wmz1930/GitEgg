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
          <icon icon="ant-design:plus-outlined" /> 新建储存
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
      <template #toolbar> </template>

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
                label: '测试',
                icon: 'ant-design:cloud-upload-outlined',
                onClick: handleUploadTest.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '设为默认',
                icon: 'ant-design:cloud-server-outlined',
                color: 'error',
                disabled: record.dfsDefault === 1,
                onClick: handleModifyDefault.bind(null, record),
              },
              {
                label: '删除',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <DfsDrawer @register="registerDrawer" @success="handleSuccess" />
    <DfsTestModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    queryDfsList,
    updateDfsDefault,
    batchDeleteDfs,
    deleteDfs,
  } from '/@/api/system/extension/dfs/dfs';

  import { dfsColumns, dfsSearchForm } from './dfs.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import DfsDrawer from './DfsDrawer.vue';
  import DfsTestModal from './DfsTestModal.vue';
  import { Dropdown } from '/@/components/Dropdown';
  import Icon from '@/components/Icon/Icon.vue';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';

  export default defineComponent({
    name: 'DfsTable',
    components: {
      BasicTable,
      TableAction,
      Dropdown,
      DfsDrawer,
      DfsTestModal,
      Icon,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage, createConfirm } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerModal, { openModal }] = useModal();

      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '储存列表',
        api: queryDfsList,
        rowKey: 'id',
        columns: dfsColumns,
        formConfig: {
          labelWidth: 100,
          schemas: dfsSearchForm,
          showAdvancedButton: false,
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
          width: 180,
          title: '操作',
          dataIndex: 'action',
        },
      });

      // 状态转换
      function handleSmsTemplateRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.dfsStatus = `${record.dfsStatus}`;
        record.accessControl = `${record.accessControl}`;
        record.dfsDefault = `${record.dfsDefault}`;
      }

      function handleEdit(record: Recordable) {
        handleSmsTemplateRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleCreate() {
        openDrawer(true, {
          isUpdate: false,
        });
      }

      function handleDelete(record: Recordable) {
        deleteDfs(record.id)
          .then(() => {
            createMessage.success(`储存删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('储存删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteDfs(checkedKeys.value)
          .then(() => {
            createMessage.success(`储存删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('储存删除失败');
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

      // 设置为默认存储方式
      function handleModifyDefault(record: Recordable) {
        createConfirm({
          iconType: 'warning',
          title: '确定要设置该存储为默认存储?',
          content: '当不指定存储方式时，系统使用此存储方式为默认存储方式。',
          onOk: async () => {
            updateDfsDefault(record.id).then(() => {
              createMessage.success(`已设置为默认存储方式`);
              reload();
            });
          },
        });
      }

      //存储测试
      function handleUploadTest(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      // 跨页选中;
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      return {
        registerTable,
        checkedKeys,
        searchInfo,
        handleCreate,
        handleEdit,
        handleDelete,
        handleBatchDelete,
        registerDrawer,
        registerModal,
        handleSuccess,
        handleModifyDefault,
        handleUploadTest,
        onSelectChange,
      };
    },
  });
</script>
