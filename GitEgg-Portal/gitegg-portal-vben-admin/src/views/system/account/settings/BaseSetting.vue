<template>
  <CollapseContainer title="基本设置" :canExpan="false">
    <a-row :gutter="24">
      <a-col :span="14">
        <BasicForm @register="register" />
        <a-row>
          <a-col :span="18">
            <Button class="submit-btn" type="primary" @click="handleSubmit" :loading="loading">
              更新基本信息
            </Button>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="10">
        <div class="change-avatar">
          <div class="mb-2">头像</div>
          <CropperAvatar
            :uploadApi="uploadApi"
            :uploadParams="dfsConfig"
            :value="avatar"
            btnText="更换头像"
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </a-col>
    </a-row>
  </CollapseContainer>
</template>
<script lang="ts">
  import { Button, Row, Col } from 'ant-design-vue';
  import { computed, defineComponent, onMounted, ref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container';
  import { CropperAvatar } from '/@/components/Cropper';

  import { useMessage } from '/@/hooks/web/useMessage';
  import { updateUserInfo, updateUserAvatar } from '/@/api/sys/user';

  import headerImg from '/@/assets/images/header.jpg';
  import { baseSetschemas } from './data';
  import { useUserStore } from '/@/store/modules/user';
  import { uploadApi } from '/@/api/sys/upload';

  import { queryDefaultDfs } from '/@/api/system/extension/dfs/dfs';

  export default defineComponent({
    components: {
      BasicForm,
      CollapseContainer,
      Button,
      ARow: Row,
      ACol: Col,
      CropperAvatar,
    },
    setup() {
      const { createMessage } = useMessage();
      const userStore = useUserStore();
      const loading = ref(false);
      const dfsConfig = ref(null) as any;

      const [register, { setFieldsValue, validate }] = useForm({
        labelWidth: 120,
        schemas: baseSetschemas,
        showActionButtonGroup: false,
      });

      onMounted(async () => {
        const data = userStore.getUserInfo as any;
        // 转换省市区
        if (!data.areas || data.areas.length === 0) {
          data.areas = [
            data.province,
            data.city,
            data.area && data.area.trim() !== '' ? data.area : undefined,
          ];
        }
        setFieldsValue(data);
        const result = await getDefaultDfs();
        dfsConfig.value = result;
      });

      const avatar = computed(() => {
        const { avatar } = userStore.getUserInfo;
        return avatar || headerImg;
      });

      function updateAvatar({ src }) {
        const userinfo = userStore.getUserInfo;
        const values = { avatar: src };
        loading.value = true;
        updateUserAvatar(values)
          .then(() => {
            createMessage.success(`头像更新成功`);
            userinfo.avatar = src;
            userStore.setUserInfo(userinfo);
          })
          .catch(() => {
            createMessage.error('头像更新失败');
          })
          .finally(() => {
            loading.value = false;
          });
      }

      async function handleSubmit() {
        const values = await validate();
        loading.value = true;
        updateUserInfo(values)
          .then(() => {
            createMessage.success(`账号信息更新成功`);
          })
          .catch(() => {
            createMessage.error('账号信息更新失败');
          })
          .finally(() => {
            loading.value = false;
          });
      }

      const getDefaultDfs = async () => {
        let queryParams = { accessControl: 1 };
        return await queryDefaultDfs(queryParams).then((response) => {
          return response;
        });
      };

      return {
        avatar,
        register,
        uploadApi: uploadApi as any,
        updateAvatar,
        handleSubmit,
        loading,
        getDefaultDfs,
        dfsConfig,
      };
    },
  });
</script>

<style lang="less" scoped>
  .submit-btn {
    float: right;
  }

  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
