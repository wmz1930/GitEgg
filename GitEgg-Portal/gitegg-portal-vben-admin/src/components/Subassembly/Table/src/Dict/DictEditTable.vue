<template>
  <!--数据表格-->
  <BasicTable
    title="数据字典表"
    @register="registerTable"
    :searchInfo="searchInfo"
    :rowSelection="{
      type: 'checkbox',
      selectedRowKeys: checkedKeys,
      onChange: onSelectChange,
      preserveSelectedRowKeys: true,
    }"
    v-bind="$attrs"
  >
    <template #toolbar>
      <a-button type="primary" @click="handleCreate" class="mr-2">
        <icon icon="ant-design:plus-outlined" /> {{ createText }}
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
              label: '查看',
              icon: 'ant-design:eye-outlined',
              ifShow: record.parentId === '0' || record.parentId === '' || record.parentId === null,
              tooltip: '查看数据字典值列表',
              onClick: handleShowChildren.bind(null, record),
            },
            {
              label: '编辑',
              icon: 'clarity:note-edit-line',
              tooltip: '编辑数据字典资料',
              onClick: handleEdit.bind(null, record),
            },
          ]"
          :dropDownActions="[
            {
              label: '删除数据字典',
              icon: 'ant-design:delete-outlined',
              color: 'error',
              tooltip: '删除此数据字典',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </template>
  </BasicTable>
  <!-- 新增编辑时的弹框 -->
  <DictModal @register="registerModal" @success="handleSuccess" />
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { getDictList, deleteDict, batchDeleteDict } from '/@/api/system/base/dict';
  import { useModal } from '/@/components/Modal';
  import DictModal from './DictModal.vue';

  import { columns, getSearchFormConfig } from './dict.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import { Icon } from '/@/components/Icon';

  export default defineComponent({
    name: 'DictEdit',
    components: {
      BasicTable,
      DictModal,
      TableAction,
      Dropdown,
      Icon,
    },
    inheritAttrs: false,
    emits: ['show-children', 'check'],
    setup(_, { attrs, emit }) {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerModal, { openModal: openDictModal }] = useModal();
      const searchInfo = reactive<Recordable>(attrs.searchInfo as Recordable);
      const createText = ref(attrs?.createText ? attrs?.createText : '新建数据字典');
      // 当前父节点
      let currentParentInfo = ref();

      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        api: getDictList,
        rowKey: 'id',
        columns,
        useSearchForm: true,
        formConfig: getSearchFormConfig(),
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        clickToRowSelect: false,
        childrenColumnName: 'false',
        handleSearchInfoFn(info) {
          return info;
        },
        actionColumn: {
          width: attrs?.actionWidth ? (attrs?.actionWidth as number) : 200,
          title: '操作',
          dataIndex: 'action',
        },
      });

      function handleCreate() {
        openDictModal(true, {
          isUpdate: false,
          parentDict: currentParentInfo,
        });
      }

      function handleEdit(record: Recordable) {
        // 状态转换
        handleDictRecord(record);
        openDictModal(true, {
          record,
          isUpdate: true,
          parentDict: currentParentInfo,
        });
      }

      function handleShowChildren(record: Recordable) {
        // 回调查询
        emit('show-children', record);
      }

      function handleDict(record: Recordable) {
        // 状态转换
        handleDictRecord(record);
        openDictModal(true, {
          record,
        });
      }

      function handleDelete(record: Recordable) {
        deleteDict(record.id)
          .then(() => {
            createMessage.success(`数据字典删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('数据字典删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteDict(checkedKeys.value)
          .then(() => {
            createMessage.success(`数据字典删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('数据字典删除失败');
          })
          .finally(() => {});
      }

      function handleSuccess({ isUpdate, values }) {
        createMessage.success(`操作成功`);
        if (isUpdate) {
          // 演示不刷新表格直接更新内部数据。
          // 注意：updateTableDataRecord要求表格的rowKey属性为string并且存在于每一行的record的keys中
          updateTableDataRecord(values.id, values);
        } else {
          reload();
        }
      }

      // 点击字典列表时，查询字典数据
      function queryChildrenDict(record) {
        return {
          on: {
            click: () => {
              emit('show-children', record);
            },
          },
        };
      }

      // 自定义刷新table方法
      function reloadTable(record) {
        currentParentInfo = record;
        reload();
      }

      // 状态转换
      function handleDictRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回数据字典状态Number类型时需要进行转换
        record.dictStatus = `${record.dictStatus}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        handleEdit,
        handleShowChildren,
        handleDict,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        queryChildrenDict,
        onSelectChange,
        checkedKeys,
        searchInfo,
        reloadTable,
        createText,
        currentParentInfo,
      };
    },
  });
</script>
