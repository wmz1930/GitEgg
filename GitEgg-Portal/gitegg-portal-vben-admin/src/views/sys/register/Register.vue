<template>
  <div class="social-bind relative w-full h-full">
    <a-row class="register-header">
      <a-col :md="12" :sm="12" style="text-align: center">
        <img src="../../../assets/svg/logo.svg" class="logo" alt="logo" /><span class="title"
          >GitEgg</span
        ><span class="sub-title">欢迎注册</span>
      </a-col>
      <a-col :md="12" :sm="12" style="text-align: center">
        <span class="has-account">已有账号？</span>
        <router-link :to="{ name: 'Login', query: { redirect: redirectPath } }" class="to-login"
          >请登录>>
        </router-link>
      </a-col>
    </a-row>
    <div style="clear: both"></div>
    <Card :bordered="false">
      <a-steps class="steps" :current="currentTab">
        <a-step title="验证手机号" />
        <a-step title="填写账号信息" />
        <a-step title="完成" />
      </a-steps>
      <div class="content">
        <div v-show="currentTab === 0">
          <register-mobile @next-step="nextStep" @set-mobile-sms="setMobileSms" />
        </div>
        <div v-show="currentTab === 1">
          <register-info
            ref="registerInfo"
            @next-step="nextStep"
            @prev-step="prevStep"
            @set-user-info="setUserInfo"
            @submit-register="submitRegister"
          />
        </div>
        <div v-show="currentTab === 2">
          <register-success ref="registerSuccess" @prev-step="prevStep" @finish="finish" />
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
  import { Row, Col, Steps, Card, Divider } from 'ant-design-vue';
  import type { RuleObject } from 'ant-design-vue/lib/form/interface';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRouter } from 'vue-router';

  import RegisterMobile from './RegisterMobile.vue';
  import RegisterInfo from './RegisterInfo.vue';
  import RegisterSuccess from './RegisterSuccess.vue';
  import { userRegister } from '/@/api/login';

  export default defineComponent({
    name: 'LoginForm',
    components: {
      [Col.name]: Col,
      [Row.name]: Row,
      [Steps.name]: Steps,
      [Steps.Step.name]: Steps.Step,
      Card,
      [Divider.name]: Divider,
      RegisterMobile,
      RegisterInfo,
      RegisterSuccess,
    },
    emits: [],
    setup(_, {}) {
      const { t } = useI18n();
      const { notification } = useMessage();
      const registerInfo = ref();
      const registerSuccess = ref();

      const formRef = ref();
      const loading = ref(false);
      const currentTab = ref(0);

      const formRegister = reactive({
        nickname: undefined,
        mobile: undefined,
        password: undefined,
        smsCode: undefined,
        code: undefined,
      });

      const formRules = {
        username: [{ required: true, message: '请输入账号', trigger: 'change' }] as RuleObject[],
        password: [{ required: true, message: '请输入密码', trigger: 'change' }] as RuleObject[],
      };

      function nextStep() {
        if (currentTab.value < 2) {
          currentTab.value += 1;
        }
      }

      function prevStep() {
        if (currentTab.value > 0) {
          currentTab.value -= 1;
        }
      }

      function finish() {
        currentTab.value = 0;
      }

      function setMobileSms(params) {
        formRegister.mobile = params.phoneNumber;
        formRegister.smsCode = params.smsCode;
        formRegister.code = params.code;
      }

      function setUserInfo(params) {
        formRegister.nickname = params.nickname;
        formRegister.password = params.password;
      }

      function submitRegister() {
        userRegister(formRegister)
          .then((res) => {
            registerSuccess.value.successRegister(formRegister);
            nextStep();
          })
          .catch((err) => {
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

      const router = useRouter();
      const { currentRoute } = router;
      const { query } = unref(currentRoute);

      return {
        t,
        nextStep,
        prevStep,
        finish,
        formRef,
        formRegister,
        setMobileSms,
        setUserInfo,
        submitRegister,
        loading,
        registerInfo,
        registerSuccess,
        formRules,
        currentTab,
        redirectPath: query.redirect,
      };
    },
  });
</script>

<style lang="less" scoped>
  .content {
    max-width: 500px;
    margin: 40px auto;
  }

  .steps {
    max-width: 750px;
    margin: 16px auto;
  }

  .logo {
    position: relative;
    height: 45px;
    margin-right: 10px;
    border-style: none;
    display: inline-block;
  }

  .register-header {
    height: 100px;
    line-height: 100px;
    -webkit-box-shadow: rgba(177, 175, 175, 0.4) 0px 0px 10px 1px;
    box-shadow: rgba(177, 175, 175, 0.4) 0px 0px 10px 1px;
  }

  .title,
  .sub-title {
    position: relative;
    font-size: 33px;
    color: #333;
    font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
    font-weight: 500;
    top: 2px;
  }

  .sub-title {
    margin-left: 10px;
    font-size: 24px;
    font-weight: 400;
  }

  .has-account,
  .to-login {
    font-size: 16px;
    color: #999;
    margin-top: 10px;
  }

  .to-login {
    color: #52c41a;
  }

  .footer {
    position: absolute;
    width: 100%;
    bottom: 0;
    padding: 0 16px;
    margin: 48px 0 24px;
    text-align: center;

    .links {
      margin-bottom: 8px;
      font-size: 14px;

      a {
        color: #999;
        transition: all 0.3s;

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
