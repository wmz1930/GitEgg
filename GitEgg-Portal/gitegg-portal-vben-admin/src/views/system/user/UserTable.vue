<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <!--组织结构树-->
    <OrganizationTree helpMessage="再次点击选中的节点，可取消选中" @select="handleSelect" />
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
          <icon icon="ant-design:plus-outlined" /> 新增账号
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
        <Upload accept=".xls,.xlsx" :showUploadList="false" :beforeUpload="beforeImportUserList">
          <a-button type="primary"> <icon icon="ant-design:upload-outlined" /> 数据导入 </a-button>
        </Upload>
        <Dropdown
          class="mr-2"
          :trigger="['hover']"
          :dropMenuList="[
            {
              icon: 'ant-design:check-circle-outlined',
              text: '导出选中',
              event: '1',
              disabled: checkedKeys.length === 0,
              onClick: handleExportUserList.bind(null, true),
            },
            {
              icon: 'ant-design:check-circle-twotone',
              text: '导出全部',
              event: '1',
              onClick: handleExportUserList.bind(null, false),
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
        <a-button type="link" @click="handleDownloadTemplate">下载模板</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '详情',
                icon: 'clarity:info-standard-line',
                tooltip: '查看用户详情',
                onClick: handleView.bind(null, record),
              },
              {
                label: '编辑',
                icon: 'clarity:note-edit-line',
                tooltip: '编辑用户资料',
                onClick: handleEdit.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '数据权限',
                icon: 'ant-design:lock-twotone',
                color: 'error',
                tooltip: '数据权限',
                onClick: handleDataPermission.bind(null, record),
              },
              {
                label: '重置密码',
                icon: 'ant-design:undo-outlined',
                color: 'error',
                tooltip: '重置用户密码',
                popConfirm: {
                  title: '是否重置用户密码',
                  confirm: resetPassword.bind(null, record),
                },
              },
              {
                label: '删除用户',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                tooltip: '删除此用户',
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
    <UserDrawer @register="registerDrawer" @success="handleSuccess" />
    <!-- 编辑数据权限时的弹框 -->
    <UserDataPermissionDrawer @register="dataPermissionDrawer" @success="handleSuccess" />
    <!-- 查看详情时的弹框 -->
    <UserModal @register="registerModal" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { Upload } from 'ant-design-vue';
  import { merge } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import {
    getUserList,
    resetUserPassword,
    deleteUser,
    batchDeleteUser,
    exportUserList,
    importUserList,
    downloadUserTemplate,
  } from '/@/api/system/user';
  import { PageWrapper } from '/@/components/Page';
  import { OrganizationTree } from '/@/components/Subassembly/Tree';

  import { useDrawer } from '/@/components/Drawer';
  import UserDrawer from './UserDrawer.vue';
  import UserDataPermissionDrawer from './UserDataPermissionDrawer.vue';

  import { useModal } from '/@/components/Modal';
  import UserModal from './UserModal.vue';

  import { columns, getSearchFormConfig } from './user.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import Icon from '@/components/Icon/Icon.vue';

  import { handleDownloadBlod } from '/@/utils/file/download';

  import { useLoading } from '/@/components/Loading';

  // 这里演示打开新的tab页时需要
  // import { useGo } from '/@/hooks/web/usePage';

  export default defineComponent({
    name: 'UserManagement',
    components: {
      BasicTable,
      PageWrapper,
      OrganizationTree,
      UserDrawer,
      UserDataPermissionDrawer,
      UserModal,
      TableAction,
      Dropdown,
      Upload,
      Icon,
    },
    setup() {
      // 这里演示打开新的tab页时需要
      // const go = useGo();
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer: openUserDrawer }] = useDrawer();
      const [dataPermissionDrawer, { openDrawer: openDataPermissionDrawer }] = useDrawer();
      const [registerModal, { openModal }] = useModal();
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '...',
      });
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord, getForm }] = useTable({
        title: '账号列表',
        api: getUserList,
        rowKey: 'id',
        columns,
        useSearchForm: true,
        formConfig: getSearchFormConfig(),
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
        openUserDrawer(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        // 省市区、角色、状态转换
        handleUserRecord(record);
        openUserDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleView(record: Recordable) {
        // 省市区、角色、状态转换
        handleUserRecord(record);
        openModal(true, {
          record,
          isUpdate: true,
        });
        // 这里演示打开新的tab页
        // go('/system/cfg/permission/system/user/detail/' + record.id);
      }

      function handleDataPermission(record: Recordable) {
        // 省市区、角色、状态转换
        handleUserRecord(record);
        openDataPermissionDrawer(true, {
          record,
        });
      }

      function handleDelete(record: Recordable) {
        deleteUser(record.id)
          .then(() => {
            createMessage.success(`用户删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('用户删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteUser(checkedKeys.value)
          .then(() => {
            createMessage.success(`用户删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('用户删除失败');
          })
          .finally(() => {});
      }

      function resetPassword(record: Recordable) {
        resetUserPassword(record.id)
          .then(() => {
            createMessage.success(`用户密码重置成功`);
          })
          .catch(() => {
            createMessage.error('用户密码重置失败');
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

      function handleSelect(organizationId = '') {
        searchInfo.organizationId = organizationId;
        reload();
      }

      // 省市区、角色、状态转换
      function handleUserRecord(record: Recordable) {
        // 转换省市区
        if (!record.areas || record.areas.length === 0) {
          record.areas = [
            record.province,
            record.city,
            record.area && record.area.trim() !== '' ? record.area : undefined,
          ];
        }

        // 转换多角色
        if (!(record.roleIds instanceof Array)) {
          let roleIds = record.roleIds.split(',');
          record.roleIds = roleIds;
        }

        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.status = `${record.status}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      function getSearchParams() {
        const params: Recordable = merge(getForm()?.getFieldsValue(), searchInfo);
        return params;
      }

      //数据导出
      function handleExportUserList(exportChecked: boolean) {
        openFullLoading();
        let params = getSearchParams();
        if (exportChecked) {
          params.userIds = checkedKeys.value as string[];
        }
        exportUserList(params)
          .then((response) => {
            handleDownloadBlod('用户数据列表.xlsx', response);
          })
          .catch((err) => {
            createMessage.error('用户数据导出失败:' + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 数据导入
      function beforeImportUserList(file) {
        handleUploadUserList(file);
        return false;
      }

      function handleUploadUserList(file) {
        const formData = new FormData();
        formData.append('uploadFile', file);
        openFullLoading();
        importUserList(formData)
          .then(() => {
            createMessage.success(`用户数据导入成功`);
            reload();
          })
          .catch((err) => {
            createMessage.error(`用户数据导入失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 下载导入模板
      function handleDownloadTemplate() {
        openFullLoading();
        downloadUserTemplate(searchInfo)
          .then((response) => {
            handleDownloadBlod('用户模板批量导入模板.xlsx', response);
          })
          .catch((err) => {
            createMessage.error(`下载导入模板失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      return {
        registerTable,
        registerDrawer,
        dataPermissionDrawer,
        registerModal,
        handleCreate,
        handleEdit,
        handleDataPermission,
        handleDelete,
        handleBatchDelete,
        resetPassword,
        handleSuccess,
        handleSelect,
        handleView,
        onSelectChange,
        checkedKeys,
        searchInfo,
        handleExportUserList,
        beforeImportUserList,
        handleDownloadTemplate,
      };
    },
  });
</script>
