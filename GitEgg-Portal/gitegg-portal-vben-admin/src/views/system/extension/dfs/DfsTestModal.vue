<template>
  <BasicModal
    v-bind="$attrs"
    @register="userModal"
    :title="getTitle"
    @ok="closeViewModal"
    width="800"
  >
    <BasicForm @register="dfsTestForm">
      <template #upload="{ model, field }">
        <Upload
          v-model:value="model[field]"
          :file-list="fileList"
          :remove="handleRemove"
          :before-upload="beforeUpload"
        >
          <Button style="width: 100%"> <Icon icon="ion:upload" /> 选择文件 </Button>
        </Upload>
        <Button
          type="primary"
          :disabled="fileList.length === 0"
          :loading="loading"
          style="margin-top: 16px"
          @click="handleUpload"
        >
          <Icon icon="ant-design:caret-right-outlined" />{{ loading ? 'Uploading' : '开始上传' }}
        </Button>
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { dfsUploadTestForm } from './dfs.data';

  import { useMessage } from '/@/hooks/web/useMessage';
  import { Upload, Button } from 'ant-design-vue';
  import Icon from '@/components/Icon/Icon.vue';

  import { dfsUpload } from '/@/api/system/extension/dfs/dfs_upload';

  export default defineComponent({
    name: 'DfsTestModal',
    components: { BasicModal, BasicForm, Upload, Button, Icon },
    setup(_, {}) {
      const rowId = ref('');

      const loading = ref<boolean>(false);
      const fileList = ref<any[]>([]);
      const imgUrlList = ref<any[]>([]);

      const { createMessage } = useMessage();

      const [dfsTestForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        baseColProps: { lg: 24, md: 24 },
        schemas: dfsUploadTestForm,
        showActionButtonGroup: false,
        disabled: true,
        actionColOptions: {
          span: 24,
        },
      });

      const [userModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ confirmLoading: false });
        rowId.value = data.record.id;
        fileList.value = [];
        setFieldsValue({
          ...data.record,
        });
      });

      const getTitle = '文件上传测试';

      async function closeViewModal() {
        closeModal();
      }

      const handleRemove = (file) => {
        const index = fileList.value.indexOf(file);
        const newFileList = fileList.value.slice();
        newFileList.splice(index, 1);
        fileList.value = newFileList;
        imgUrlList.value = [];
        if (fileList.value && fileList.value.length > 0) {
          for (let i = 0; i < fileList.value.length; i++) {
            imgUrlList.value.push(fileList[i].url);
          }
        } else {
        }
      };

      const beforeUpload = (file: File) => {
        fileList.value = [...fileList.value, file];
        return false;
      };

      const handleUpload = async (file) => {
        const values = await validate();
        const formData = new FormData();
        formData.append('dfsCode', values.dfsCode);
        fileList.value.forEach((file) => {
          formData.append('uploadFile', file);
        });
        loading.value = true;
        dfsUpload(formData)
          .then((response) => {
            loading.value = false;
            if (response && response.length > 0) {
              file.url = response[0].fileUrl;
              imgUrlList.value.push(file.url);
              return createMessage.success('上传成功');
            }
          })
          .catch((err) => {
            loading.value = false;
            return createMessage.error('上传失败:' + err);
          });
      };

      return {
        userModal,
        dfsTestForm,
        getTitle,
        closeViewModal,
        fileList,
        handleRemove,
        beforeUpload,
        loading,
        handleUpload,
      };
    },
  });
</script>
