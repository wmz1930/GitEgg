<template>
  <div>
    <BasicTable
      v-bind="$attrs"
      @register="registerTable"
      :rowSelection="{
        type: 'checkbox',
        selectedRowKeys: checkedKeys,
        onChange: onSelectChange,
        preserveSelectedRowKeys: true,
      }"
    >
      <template #tableTitle>
        <a-button type="primary" @click="handleCreate" class="mr-2">
          <icon icon="ant-design:plus-outlined" /> 新增联表
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
                label: '删除',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除联表',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <TableJoinDrawer @register="registerDrawer" @success="handleSuccess" :config="config" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, watch, nextTick } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    getTableJoinList,
    deleteTableJoin,
    batchDeleteTableJoin,
  } from '/@/api/plugin/codeGenerator/join/table_join';

  import { useDrawer } from '/@/components/Drawer';
  import TableJoinDrawer from './table-join/TableJoinDrawer.vue';

  import { columns } from './table-join/table.join.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import { Icon } from '/@/components/Icon';

  export default defineComponent({
    name: 'TableJoinManagement',
    components: {
      BasicTable,
      TableJoinDrawer,
      TableAction,
      Dropdown,
      Icon,
    },
    props: {
      config: {
        type: Object,
        default: () => {},
      },
    },
    setup(props) {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerResourceDrawer, { openDrawer: openResourceDrawer }] = useDrawer();
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '联表列表',
        api: getTableJoinList,
        rowKey: 'id',
        columns,
        formConfig: {
          labelWidth: 120,
        },
        immediate: false,
        useSearchForm: false,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        resizeHeightOffset: 70,
        beforeFetch(info) {
          info.generationId = props.config.id;
          return info;
        },
        actionColumn: {
          width: 180,
          title: '操作',
          dataIndex: 'action',
        },
      });

      watch(
        () => props.config,
        (newVal, oldVal) => {
          if (newVal && newVal !== oldVal) {
            nextTick(() => {
              reload();
            });
          }
        },
        { deep: true },
      );

      function handleCreate() {
        openDrawer(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        handleTableJoinRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleResource(record: Recordable) {
        openResourceDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        deleteTableJoin(record.id)
          .then(() => {
            createMessage.success(`联表删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('联表删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteTableJoin(checkedKeys.value)
          .then(() => {
            createMessage.success(`联表删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('联表删除失败');
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

      // 状态转换
      function handleTableJoinRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.tableJoinStatus = `${record.tableJoinStatus}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      return {
        registerTable,
        registerDrawer,
        registerResourceDrawer,
        handleCreate,
        handleEdit,
        handleResource,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handleTableJoinRecord,
        onSelectChange,
        checkedKeys,
      };
    },
  });
</script>
