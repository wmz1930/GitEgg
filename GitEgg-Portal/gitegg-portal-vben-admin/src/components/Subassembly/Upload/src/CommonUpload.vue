<template>
  <div class="clearfix">
    <Upload
      :listType="uploadType === 'image' ? 'picture-card' : 'text'"
      :file-list="fileList"
      @remove="handleRemove"
      :accept="getStringAccept"
      @preview="handlePreview"
      @change="handleChange"
      :before-upload="beforeUpload"
    >
      <div v-if="fileList.length < maxNumber">
        <template v-if="uploadType === 'image'">
          <plus-outlined />
          <div style="margin-top: 8px">{{ uploadBtnText }}</div>
        </template>
        <a-button v-else>
          <upload-outlined />
          {{ uploadBtnText }}
        </a-button>
      </div>
    </Upload>
    <Modal :visible="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
      <img alt="" style="width: 100%" :src="previewImage" />
    </Modal>
  </div>
</template>
<script lang="ts">
  import { PlusOutlined, UploadOutlined } from '@ant-design/icons-vue';
  import { defineComponent, ref, unref, onBeforeMount, watch, toRefs } from 'vue';
  import { message, Upload, Modal } from 'ant-design-vue';
  import { basicProps } from '/@/components/Upload/src/props';
  import { checkImgType, getBase64WithFile } from '/@/components/Upload/src/helper';
  import { buildUUID } from '/@/utils/uuid';
  import { useMessage } from '/@/hooks/web/useMessage';

  import type { UploadChangeParam } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useUploadType } from '/@/components/Upload/src/useUpload';

  import { queryDefaultDfs } from '/@/api/system/extension/dfs/dfs';
  import { dfsUpload, dfsBatchGetFileUrl } from '/@/api/system/extension/dfs/dfs_upload';

  function getBase64(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  }

  export default defineComponent({
    components: {
      PlusOutlined,
      UploadOutlined,
      Upload,
      Modal,
    },
    props: {
      ...basicProps,
      value: {
        type: String,
        default: '',
      },
      uploadBtnText: {
        type: String,
        default: '上传',
      },
      accessPrivate: {
        type: Boolean,
        default: true,
      },
      dfsCode: {
        type: String,
        default: '',
      },
      uploadType: {
        type: String,
        default: 'image',
      },
    },
    emits: ['change', 'register', 'update:value'],
    setup(props, { emit }) {
      const previewVisible = ref(false);
      const previewImage = ref('');
      const previewTitle = ref('');
      const loading = ref<boolean>(false);
      const fileList = ref<any[]>([]);
      const imgUrlList = ref<any[]>([]);
      const dfsConfig = ref(null) as any;
      const { accept, helpText, maxNumber, maxSize } = toRefs(props);

      const { t } = useI18n();
      const { createMessage } = useMessage();

      const { getStringAccept, getHelpText } = useUploadType({
        acceptRef: accept,
        helpTextRef: helpText,
        maxNumberRef: maxNumber,
        maxSizeRef: maxSize,
      });

      // 获取文件上传方式
      onBeforeMount(async () => {
        dfsConfig.value = await getDefaultDfs();
      });

      watch(
        () => props.value,
        (newVal, oldVal) => {
          if (newVal && newVal !== oldVal) {
            initImgList(newVal);
          }
        },
      );

      const getDefaultDfs = async () => {
        let queryParams = {};
        if (props.dfsCode && props.dfsCode !== '') {
          queryParams = { dfsCode: props.dfsCode };
        } else {
          queryParams = { accessControl: props.accessPrivate ? 0 : 1 };
        }
        return await queryDefaultDfs(queryParams).then((response) => {
          return response;
        });
      };

      const handleRemove = (file) => {
        const index = fileList.value.indexOf(file);
        const newFileList = fileList.value.slice();
        newFileList.splice(index, 1);
        fileList.value = newFileList;
        if (fileList.value && fileList.value.length > 0) {
          imgUrlList.value = imgUrlList.value.filter((item) => item.uid !== file.uid);
          emit('update:value', JSON.stringify(imgUrlList.value));
          emit('change', JSON.stringify(imgUrlList.value));
        } else {
          imgUrlList.value = [];
          emit('update:value', '');
          emit('change', '');
        }
      };

      const handleChange = (info: UploadChangeParam) => {
        if (info.file.status === 'uploading') {
          loading.value = true;
          return;
        }

        if (info.file.status === 'removed') {
          return;
        }

        if (info.file.status === 'done') {
          // Get this url from response in real world.
          loading.value = false;
        }

        if (info.file.status === 'error') {
          loading.value = false;
          message.error('upload error');
        }
      };

      const beforeUpload = (file: File) => {
        const { size, name } = file;
        const { maxSize } = props;
        // 设置最大值，则判断
        if (maxSize && file.size / 1024 / 1024 >= maxSize) {
          createMessage.error(t('component.upload.maxSizeMultiple', [maxSize]));
          return false;
        }

        const commonItem = {
          uuid: buildUUID(),
          file,
          size,
          name,
          percent: 0,
          type: name.split('.').pop(),
        };
        // 生成图片缩略图
        if (checkImgType(file)) {
          getBase64WithFile(file).then(({ result: thumbUrl }) => {
            fileList.value = [
              ...unref(fileList),
              {
                thumbUrl,
                ...commonItem,
              },
            ];
          });
        } else {
          fileList.value = [...unref(fileList), commonItem];
        }
        handleUpload(file);
        return false;
      };

      const handleUpload = (file) => {
        const formData = new FormData();
        formData.append('dfsCode', dfsConfig?.value?.dfsCode);
        formData.append('uploadFile', file);
        loading.value = true;
        dfsUpload(formData)
          .then((response) => {
            loading.value = false;
            if (response && response.length > 0) {
              file.url = response[0].fileUrl;
              response[0].uid = file.uid;
              imgUrlList.value.push(response[0]);
              emit('update:value', JSON.stringify(imgUrlList.value));
              emit('change', JSON.stringify(imgUrlList.value));
              return createMessage.success('上传成功');
            }
          })
          .catch((err) => {
            return createMessage.error('上传失败:' + err);
          });
      };

      const initImgList = async (imgUrls) => {
        if (imgUrls) {
          fileList.value = [];
          imgUrlList.value = JSON.parse(imgUrls);

          let imgNames = imgUrlList.value.map((item) => {
            return item.encodedFileName;
          });

          const accessImgList = await dfsBatchGetFileUrl({
            dfsCode: dfsConfig?.value?.dfsCode,
            fileNames: imgNames,
          });

          // 图片有可能是私有的，所以这里要查询可访问的链接地址
          const imgMap = {};
          if (accessImgList && accessImgList.length > 0) {
            accessImgList.forEach((item) => {
              const itemImg = item as any;
              imgMap[itemImg.fileName] = itemImg.fileUrl;
            });
          }

          imgUrlList.value.forEach((value) => {
            const accessUrl = imgMap[value.encodedFileName];
            const fileUrl = accessUrl && accessUrl !== '' ? accessUrl : value.fileUrl;
            fileList.value.push({
              uid: value.uid,
              name: value.fileName,
              fileName: value.fileName,
              status: 'done',
              encodedFileName: value.encodedFileName,
              url: fileUrl,
            });
          });
        }
      };

      const handleCancel = () => {
        previewVisible.value = false;
        previewTitle.value = '';
      };

      const handlePreview = async (file) => {
        if (props.uploadType === 'image') {
          if (!file.url && !file.preview) {
            file.preview = (await getBase64(file.file)) as string;
          }
          previewImage.value = file.url || file.preview;
          previewVisible.value = true;
          previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1);
        } else {
          var xml = new XMLHttpRequest();
          xml.open('GET', file.url, true);
          xml.responseType = 'blob';
          xml.onload = function () {
            var url = window.URL.createObjectURL(xml.response);
            var a = document.createElement('a');
            a.href = url;
            a.download = file.fileName;
            a.click();
          };
          xml.send();
        }
      };

      return {
        previewVisible,
        previewImage,
        fileList,
        handleCancel,
        handlePreview,
        previewTitle,
        handleChange,
        beforeUpload,
        handleUpload,
        handleRemove,
        maxNumber,
        getStringAccept,
        getHelpText,
      };
    },
  });
</script>
<style>
  /* you can make up upload button and sample style by using stylesheets */
  .ant-upload-select-picture-card i {
    color: #999;
    font-size: 32px;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
</style>
