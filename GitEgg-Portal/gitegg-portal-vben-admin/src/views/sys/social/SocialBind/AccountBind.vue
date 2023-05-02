<template>
  <div>
    <Form
      class="p-4 enter-x"
      :model="formData"
      :rules="formRules"
      ref="formRef"
      @keypress.enter="bindAccount"
    >
      <FormItem name="username" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.username"
          :placeholder="t('sys.login.userName')"
          class="fix-auto-fill"
        >
          <template #prefix>
            <icon icon="ant-design:user-outlined" />
          </template>
        </Input>
      </FormItem>
      <FormItem name="password" class="enter-x">
        <InputPassword
          size="large"
          visibilityToggle
          v-model:value="formData.password"
          :placeholder="t('sys.login.password')"
        >
          <template #prefix>
            <icon icon="ant-design:lock-outlined" />
          </template>
        </InputPassword>
      </FormItem>
      <FormItem>
        <Button
          type="primary"
          size="large"
          block
          @click="bindAccount"
          :loading="loading"
          class="login-button"
          style="float: right"
        >
          同意协议并绑定
        </Button>
      </FormItem>
    </Form>
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive, ref, unref } from 'vue';
  import { Checkbox, Form, Input, Row, Col, Button } from 'ant-design-vue';
  import type { RuleObject } from 'ant-design-vue/lib/form/interface';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useRouter } from 'vue-router';
  import { userBindAccount } from '/@/api/login';

  export default defineComponent({
    name: 'LoginForm',
    components: {
      [Col.name]: Col,
      [Row.name]: Row,
      Checkbox,
      Button,
      Form,
      FormItem: Form.Item,
      Input,
      InputPassword: Input.Password,
      Icon,
    },
    emits: ['bind-login'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { notification } = useMessage();
      const { prefixCls } = useDesign('login');

      const router = useRouter();
      const { currentRoute } = router;

      const formRef = ref();
      const loading = ref(false);

      const formData = reactive({
        username: '',
        password: '',
        socialKey: '',
      });

      const formRules = {
        username: [{ required: true, message: '请输入账号', trigger: 'change' }] as RuleObject[],
        password: [{ required: true, message: '请输入密码', trigger: 'change' }] as RuleObject[],
      };

      async function bindAccount() {
        loading.value = true;
        const data = await formRef.value.validateFields();
        if (!data) {
          loading.value = false;
          return;
        }

        const bindParams = {} as any;
        bindParams.username = formData.username;
        bindParams.password = formData.password;
        const { query } = unref(currentRoute);
        bindParams.socialKey = query.key;
        // 执行绑定操作
        userBindAccount(bindParams)
          .then(() => {
            // 绑定成功后，调用第三方登录方法
            emit('bind-login');
          })
          .catch((err) => {
            loading.value = false;
            requestFailed(err);
          });
      }

      function requestFailed(err) {
        notification.error({
          message: '错误',
          description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
          duration: 4,
        });
      }

      return {
        t,
        prefixCls,
        formRef,
        formData,
        loading,
        bindAccount,
        formRules,
      };
    },
  });
</script>

<style lang="less" scoped>
  .stepFormText {
    margin-bottom: 24px;

    .ant-form-item-label,
    .ant-form-item-control {
      line-height: 22px;
    }
  }
</style>
