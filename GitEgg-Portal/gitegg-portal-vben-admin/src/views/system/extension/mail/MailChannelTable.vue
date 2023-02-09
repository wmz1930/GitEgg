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
          <icon icon="ant-design:plus-outlined" /> 新增邮件渠道
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
        <Upload
          accept=".xls,.xlsx"
          :showUploadList="false"
          :beforeUpload="beforeImportSmsTemplateList"
        >
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
              onClick: handleExportSmsTemplateList.bind(null, true),
            },
            {
              icon: 'ant-design:check-circle-twotone',
              text: '导出全部',
              event: '1',
              onClick: handleExportSmsTemplateList.bind(null, false),
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
        <a-button
          preIcon="ant-design:mail-outlined"
          @click="handleTest"
          :loading="sendLoading"
          type="dashed"
          >测试发送系统邮件配置
        </a-button>
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
                label: '测试',
                icon: 'ant-design:copy-outlined',
                onClick: handleTest.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '删除',
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除邮件渠道',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <MailChannelDrawer @register="registerDrawer" @success="handleSuccess" />

    <BasicModal
      v-bind="$attrs"
      v-model:visible="visible"
      @register="registerModal"
      title="邮件发送测试"
    >
      <template #footer>
        <a-button @click="visible = false">取消</a-button>
        <a-button
          preIcon="ant-design:mail-outlined"
          @click="handleTestOk"
          :loading="sendLoading"
          type="primary"
          >立即发送
        </a-button>
      </template>
      <BasicForm @register="registerForm" />
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, nextTick } from 'vue';
  import { Upload } from 'ant-design-vue';
  import { merge } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { BasicModal, useModal } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import {
    deleteMailChannel,
    batchDeleteMailChannel,
    downloadMailChannelList,
    uploadMailChannel,
    downloadMailChannelTemplate,
    queryMailChannelList,
  } from '/@/api/system/extension/mail/mailChannel';

  import { useDrawer } from '/@/components/Drawer';
  import MailChannelDrawer from './drawer/MailChannelDrawer.vue';
  import { mailColumn, mailChannelSearch, modalForm } from './mail.channel.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { Dropdown } from '/@/components/Dropdown';
  import { Icon } from '/@/components/Icon';

  import { handleDownloadBlod } from '/@/utils/file/download';
  import { useLoading } from '/@/components/Loading';
  import { testSendSimpleMail } from '/@/api/system/extension/mail/sendMailTest';

  export default defineComponent({
    name: 'SmsTemplateManagement',
    components: {
      BasicTable,
      MailChannelDrawer,
      TableAction,
      Dropdown,
      Icon,
      Upload,
      BasicModal,
      BasicForm,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const { createMessage } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const visible = ref<boolean>(false);
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '...',
      });
      const modalObj = {
        mailTo: '',
        mailSubject: 'GitEgg系统邮件发送测试',
        mailContent: 'GitEgg系统邮件发送测试内容',
      };
      const sendLoading = ref<boolean>(false);
      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 90,
        baseColProps: { span: 24 },
        schemas: modalForm,
        showActionButtonGroup: false,
      });
      const [registerModal] = useModal();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord, getForm }] = useTable({
        title: '邮件渠道列表',
        api: queryMailChannelList,
        rowKey: 'id',
        columns: mailColumn,
        formConfig: {
          autoAdvancedLine: 1,
          labelWidth: 120,
          schemas: mailChannelSearch,
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

      function handleCreate() {
        openDrawer(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        handleSmsTemplateRecord(record);
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }
      async function handleTest(record: Recordable) {
        visible.value = true;
        await nextTick();
        resetFields();
        setFieldsValue({
          channelCode: record.channelCode,
          ...modalObj,
        });
      }
      // 发送邮件
      async function handleTestOk() {
        const values = await validate();
        sendLoading.value = true;
        testSendSimpleMail(values)
          .then(() => {
            sendLoading.value = false;
            visible.value = false;
            createMessage.success(`邮件发送成功`);
          })
          .catch((err) => {
            createMessage.error('邮件发送失败' + err);
          });
      }
      function handleDelete(record: Recordable) {
        deleteMailChannel(record.id)
          .then(() => {
            createMessage.success(`邮件邮件渠道删除成功`);
            reload();
          })
          .catch(() => {
            createMessage.error('邮件邮件渠道删除失败');
          })
          .finally(() => {});
      }

      function handleBatchDelete() {
        batchDeleteMailChannel(checkedKeys.value)
          .then(() => {
            createMessage.success(`邮件邮件渠道删除成功`);
            checkedKeys.value = [];
            reload();
          })
          .catch(() => {
            createMessage.error('邮件邮件渠道删除失败');
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
      function handleSmsTemplateRecord(record: Recordable) {
        // 数据字典配置的都是字符串，这个在后台返回用户状态Number类型时需要进行转换
        record.channelStatus = `${record.channelStatus}`;
      }

      // 跨页选中
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }

      // 获取搜索参数
      function getSearchParams() {
        const params: Recordable = merge(getForm()?.getFieldsValue(), searchInfo);
        return params;
      }

      //数据导出
      function handleExportSmsTemplateList(exportChecked: boolean) {
        openFullLoading();
        let params = getSearchParams();
        if (exportChecked) {
          params.SmsTemplateIds = checkedKeys.value as string[];
        }
        downloadMailChannelList(params)
          .then((response) => {
            console.log(params);
            handleDownloadBlod('邮件渠道数据列表.xlsx', response);
            closeFullLoading();
          })
          .catch((err) => {
            createMessage.error('邮件渠道数据导出失败:' + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 数据导入
      function beforeImportSmsTemplateList(file) {
        handleUploadSmsTemplateList(file);
        return false;
      }

      function handleUploadSmsTemplateList(file) {
        const formData = new FormData();
        formData.append('uploadFile', file);
        openFullLoading();
        uploadMailChannel(formData)
          .then(() => {
            createMessage.success(`邮件渠道数据导入成功`);
            reload();
          })
          .catch((err) => {
            createMessage.error(`邮件渠道数据导入失败: ` + err);
          })
          .finally(() => {
            closeFullLoading();
          });
      }

      // 下载导入模板
      function handleDownloadTemplate() {
        openFullLoading();
        downloadMailChannelTemplate(searchInfo)
          .then((response) => {
            handleDownloadBlod('邮件渠道模板批量导入模板.xlsx', response);
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
        handleCreate,
        handleEdit,
        handleDelete,
        handleBatchDelete,
        handleSuccess,
        handleSmsTemplateRecord,
        onSelectChange,
        checkedKeys,
        searchInfo,
        handleExportSmsTemplateList,
        beforeImportSmsTemplateList,
        handleDownloadTemplate,
        visible,
        handleTest,
        handleTestOk,
        registerForm,
        modalObj,
        registerModal,
        setFieldsValue,
        sendLoading,
      };
    },
  });
</script>
<style lang="less" scoped></style>
