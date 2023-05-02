<template>
  <div class="social-bind relative w-full h-full">
    <a-row class="bind-header">
      <a-col :md="12" :sm="12" style="text-align: center">
        <img src="../../../../assets/svg/logo.svg" class="logo" alt="logo" />
        <span class="title">GitEgg</span>
        <span class="sub-title">联合登录</span>
      </a-col>
      <a-col :md="12" :sm="12" style="text-align: center" />
    </a-row>
    <div style="clear: both"></div>
    <Card :bordered="false">
      <span class="bind-tips">为了给您更好的服务，请绑定已有账号，便于您下次快速登录</span>
      <a-steps class="steps step-style" v-model:current="currentTab" type="navigation" size="small">
        <a-step status="finish" title="短信验证码绑定">
          <icon icon="ant-design:mobile-outlined" />
        </a-step>
        <a-step status="finish" title="账号密码绑定">
          <icon icon="ant-design:user-outlined" />
        </a-step>
      </a-steps>
      <div class="content">
        <div v-show="currentTab === 0">
          <mobile-bind @bind-login="bindLogin" />
        </div>
        <div v-show="currentTab === 1">
          <account-bind ref="registerInfo" @bind-login="bindLogin" />
        </div>
      </div>
    </Card>
    <div class="footer">
      <a-divider />
      <div class="links">
        <a href="_self">帮助</a>
        <a href="_self">隐私</a>
        <a href="_self">条款</a>
        <a href="_self">友链</a>
        <a href="_self">声明</a>
        <a href="_self">授权</a>
      </div>
      <div class="copyright">
        Copyrights &copy; 2021 GitEgg All Rights Reserved. 苏ICP备15017208号-1
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive, ref, unref } from 'vue';
  import { Card, Form, Input, Row, Col, Button, Steps, Divider } from 'ant-design-vue';
  import type { RuleObject } from 'ant-design-vue/lib/form/interface';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRouter } from 'vue-router';
  import { useLoading } from '/@/components/Loading';
  import { useUserStore } from '/@/store/modules/user';

  import MobileBind from './MobileBind.vue';
  import AccountBind from './AccountBind.vue';

  export default defineComponent({
    name: 'LoginForm',
    components: {
      [Col.name]: Col,
      [Row.name]: Row,
      Card,
      Button,
      Form,
      FormItem: Form.Item,
      Input,
      InputPassword: Input.Password,
      Icon,
      [Steps.name]: Steps,
      [Steps.Step.name]: Steps.Step,
      [Divider.name]: Divider,
      MobileBind,
      AccountBind,
    },
    emits: ['bind-login'],
    setup(_, {}) {
      const { t } = useI18n();
      const { notification } = useMessage();

      const router = useRouter();
      const { currentRoute } = router;
      const userStore = useUserStore();

      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '登录中......',
      });

      const formRef = ref();
      const loading = ref(false);
      const currentTab = ref(0);

      const formData = reactive({
        username: '',
        password: '',
        socialKey: '',
      });

      const formRules = {
        username: [{ required: true, message: '请输入账号', trigger: 'change' }] as RuleObject[],
        password: [{ required: true, message: '请输入密码', trigger: 'change' }] as RuleObject[],
      };

      function getUrlKey(name) {
        let redirect = '/';
        if (window.opener) {
          redirect = window.opener.location.href;
        }
        // eslint-disable-next-line no-sparse-arrays
        return (
          decodeURIComponent(
            (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(redirect) || [
              ,
              '',
            ])[1].replace(/\+/g, '%20'),
          ) || null
        );
      }

      // 绑定成功之后，执行登录并跳转到登录前的界面
      function bindLogin() {
        openFullLoading();
        const { query } = unref(currentRoute);
        const loginParams = {
          grant_type: 'social',
          social_key: query.key as string,
        };
        // 正常情况下，是直接登录跳转到之前的页面，如果发生错误，则提示跳转到登录页
        userStore
          .login(loginParams)
          .then((res) => loginSuccess(res))
          .catch((err) => loginError(err))
          .finally(() => {
            closeFullLoading();
          });
      }

      function loginSuccess(response) {
        notification.success({
          message: '提示',
          description: '第三方登录成功,欢迎 ' + response.nickname || '',
          duration: 4,
        });
      }

      function loginError(err) {
        notification.error({
          message: '错误',
          description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
          duration: 4,
        });
        if (getUrlKey('redirect')) {
          window.opener.location.href = window.opener.location.origin + getUrlKey('redirect');
        } else {
          window.opener.location.reload();
        }
      }

      return {
        t,
        formRef,
        formData,
        loading,
        bindLogin,
        formRules,
        currentTab,
      };
    },
  });
</script>

<style lang="less" scoped>
  .steps {
    max-width: 600px;
    margin: 16px auto;
  }

  .step-style {
    margin-bottom: '60px';
    box-shadow: '0px -1px 0 0 #e8e8e8 inset';
  }

  .content {
    max-width: 500px;
    margin: 40px auto;
  }

  .logo {
    display: inline-block;
    position: relative;
    height: 45px;
    margin-right: 10px;
    border-style: none;
  }

  .bind-header {
    height: 100px;
    box-shadow: rgb(177 175 175 / 40%) 0 0 10px 1px;
    box-shadow: rgb(177 175 175 / 40%) 0 0 10px 1px;
    line-height: 100px;
  }

  .title,
  .sub-title {
    position: relative;
    top: 2px;
    color: #333;
    font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
    font-size: 33px;
    font-weight: 500;
  }

  .sub-title {
    margin-left: 10px;
    font-size: 24px;
    font-weight: 400;
  }

  .has-account,
  .to-login {
    margin-top: 10px;
    color: #999;
    font-size: 16px;
  }

  .bind-tips {
    display: inline-block;
    width: 100%;
    color: #52c41a;
    text-align: center;
  }

  .social-bind .ant-steps-navigation .ant-steps-item::after {
    border: 0 solid rgb(0 0 0 / 25%) !important;
  }

  .footer {
    position: absolute;
    bottom: 0;
    width: 100%;
    margin: 48px 0 24px;
    padding: 0 16px;
    text-align: center;

    .links {
      margin-bottom: 8px;
      font-size: 14px;

      a {
        transition: all 0.3s;
        color: #999;

        &:not(:last-child) {
          margin-right: 40px;
        }
      }
    }

    .copyright {
      color: #999;
      font-size: 14px;
    }
  }
</style>
