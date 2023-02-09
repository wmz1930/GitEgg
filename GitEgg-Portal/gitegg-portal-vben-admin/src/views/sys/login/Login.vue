<template>
  <div :class="prefixCls" class="relative w-full h-full px-4 starry-sky">
    <div class="flex items-center absolute right-4 top-4">
      <AppDarkModeToggle class="enter-x mr-2" v-if="!sessionTimeout" />
      <AppLocalePicker
        class="text-white enter-x xl:text-gray-600"
        :show-text="false"
        v-if="!sessionTimeout && showLocale"
      />
    </div>

    <span class="-enter-x xl:hidden">
      <AppLogo :alwaysShowTitle="true" />
    </span>

    <div class="container relative h-full py-2 mx-auto sm:px-10">
      <div class="flex h-full">
        <div class="hidden min-h-full pl-4 mr-4 xl:flex xl:flex-col xl:w-6/12">
          <AppLogo class="-enter-x" />
          <div class="my-auto">
            <img :alt="title" src="../../../assets/svg/logo.svg" class="w-1/7 -mt-16 -enter-x" />
            <div class="mt-10 font-medium text-white -enter-x text-center">
              <span class="inline-block mt-4 text-3xl"> {{ t('sys.login.signInTitle') }}</span>
            </div>
            <div class="mt-5 font-normal text-white dark:text-gray-500 -enter-x">
              {{ t('sys.login.signInDesc') }}
            </div>
          </div>
        </div>
        <div class="flex w-full h-full py-5 xl:h-auto xl:py-0 xl:my-0 xl:w-6/12">
          <div
            :class="`${prefixCls}-form`"
            class="relative w-full px-5 py-8 mx-auto my-auto rounded-md shadow-md xl:ml-16 xl:bg-transparent sm:px-8 xl:p-4 xl:shadow-none sm:w-3/4 lg:w-2/4 xl:w-auto enter-x"
          >
            <a-tabs
              v-model:activeKey="loginActiveKey"
              centered
              class="login-type"
              :tabBarGutter="80"
              @change="handleLoginTypeChange"
            >
              <a-tab-pane key="LOGIN" :tab="t('sys.login.signInFormTitle')">
                <LoginForm
                  ref="loginFormRef"
                  @show-verify="handleShowVerify"
                  @refresh-image-code="refreshImageCode"
                />
              </a-tab-pane>
              <a-tab-pane key="MOBILE" :tab="t('sys.login.mobileSignInFormTitle')" force-render>
                <MobileForm
                  ref="mobileFormRef"
                  @show-verify="handleShowVerify"
                  @refresh-image-code="refreshImageCode"
                />
              </a-tab-pane>
              <!-- <a-tab-pane key="QR_CODE" tab="扫码登录">
                <QrCodeForm ref="qrCodeFormRef" />
              </a-tab-pane> -->
            </a-tabs>

            <!-- <ForgetPasswordForm
              ref="forgetPasswordFormRef"
              @show-verify="handleShowVerify"
              @refresh-image-code="refreshImageCode"
            />
            <RegisterForm
              ref="registerFormRef"
              @show-verify="handleShowVerify"
              @refresh-image-code="refreshImageCode"
            /> -->
            <Button
              v-show="getLoginBtnShow"
              type="primary"
              size="large"
              block
              @click="handleLogin"
              :loading="loading"
              class="login-button"
            >
              {{ t('sys.login.loginButton') }}
            </Button>
            <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider>

            <div
              class="flex justify-evenly enter-x third-login"
              :class="`${prefixCls}-sign-in-way`"
            >
              <a @click="openSocialLogin('wechat_mp')" :loading="socialLoading">
                <WechatFilled />
              </a>
              <a @click="openSocialLogin('qq')" :loading="socialLoading">
                <QqCircleFilled />
              </a>
              <a @click="openSocialLogin('github')" :loading="socialLoading">
                <GithubFilled />
              </a>
              <a @click="openSocialLogin('dingtalk')" :loading="socialLoading">
                <DingtalkCircleFilled />
              </a>
              <a @click="openRegister()">{{ t('sys.login.registerNow') }} >></a>
            </div>
          </div>
        </div>
      </div>
      <div class="footer -enter-x">
        <div class="links">
          <a href="_self">帮助</a>
          <a href="_self">隐私</a>
          <a href="_self">条款</a>
          <a href="_self">友链</a>
          <a href="_self">声明</a>
          <a href="_self">授权</a>
        </div>
        <div class="copyright">
          Copyrights &copy; 2021 GitEgg All Rights Reserved.
          <a href="https://beian.miit.gov.cn/" target="_blank">苏ICP备15017208号-1</a>
        </div>
      </div>
    </div>
  </div>
  <Verify
    @success="handleSubmit"
    :mode="'pop'"
    :captchaType="captchaState.slidingCaptchaType"
    :imgSize="{ width: '330px', height: '155px' }"
    ref="verifyRef"
  />
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, onBeforeMount } from 'vue';
  import { AppLogo } from '/@/components/Application';
  import { AppLocalePicker, AppDarkModeToggle } from '/@/components/Application';
  import LoginForm from './LoginForm.vue';
  import MobileForm from './MobileForm.vue';
  // import QrCodeForm from './QrCodeForm.vue';
  import { useGlobSetting } from '/@/hooks/setting';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useLocaleStore } from '/@/store/modules/locale';
  import { useRouter } from 'vue-router';
  import { Tabs, Divider, Button } from 'ant-design-vue';
  import {
    GithubFilled,
    WechatFilled,
    QqCircleFilled,
    DingtalkCircleFilled,
  } from '@ant-design/icons-vue';
  import { LoginStateEnum, useLoginState, useCaptchaState } from './useLogin';

  import Verify from '/@/components/verifition/Verify.vue';

  import { getCaptchaType, getImageCaptcha, getSocialLoginUrl } from '/@/api/login';

  export default defineComponent({
    name: 'Login',
    components: {
      AppLogo,
      AppLocalePicker,
      AppDarkModeToggle,
      LoginForm,
      MobileForm,
      // QrCodeForm,
      Verify,
      ATabs: Tabs,
      ATabPane: Tabs.TabPane,
      Divider,
      Button,
      GithubFilled,
      WechatFilled,
      QqCircleFilled,
      DingtalkCircleFilled,
    },
    props: {
      sessionTimeout: {
        type: Boolean,
      },
    },
    setup(_, {}) {
      const globSetting = useGlobSetting();
      const { prefixCls } = useDesign('login');
      const { t } = useI18n();
      const localeStore = useLocaleStore();
      const { getLoginState, setLoginState, getLoginLoading, setLoginLoading } = useLoginState();

      const {
        setGrantType,
        setLoginCaptchaType,
        setSlidingCaptchaType,
        setCaptchaImage,
        getCaptchaState,
      } = useCaptchaState();

      const showLocale = localeStore.getShowPicker;
      const title = computed(() => globSetting?.title ?? '');
      const getLoginBtnShow = computed(
        () =>
          unref(getLoginState) === LoginStateEnum.LOGIN ||
          unref(getLoginState) === LoginStateEnum.MOBILE,
      );
      const loading = computed(() => unref(getLoginLoading));
      const slidingVerifyType = ref('login');
      const loginActiveKey = ref('LOGIN');
      handleLoginTypeChange(loginActiveKey.value);

      // 滑动验证码引用
      const verifyRef = ref();
      // 账号密码登录
      const loginFormRef = ref();
      // 手机号验证码登录
      const mobileFormRef = ref();
      // 用户注册
      const registerFormRef = ref();
      // 忘记密码
      const forgetPasswordFormRef = ref();

      const captchaState = unref(getCaptchaState);

      const loginState = ref(getLoginState);

      const socialLoading = ref(false);

      // 获取系统配置的验证码方式
      onBeforeMount(async () => {
        const loginCaptchaType = await queryCaptchaType();
        setLoginCaptchaType(loginCaptchaType);
        if (captchaState.loginCaptchaType === 'image') {
          // 加载图片验证码
          refreshImageCode();
        }
      });

      // 获取系统配置的验证码方式loginCaptchaType：sliding: 滑动验证码 image: 图片验证码
      async function queryCaptchaType(): Promise<string | ''> {
        return await getCaptchaType().then((res) => {
          return res;
        });
      }

      // 当loginCaptchaType = image时刷新图片验证码的方法
      function refreshImageCode() {
        getImageCaptcha().then((res) => {
          captchaState.captchaKey = res.captchaKey;
          captchaState.captchaImage = res.captchaImage;
        });
      }

      function handleShowVerify({ verifyType }) {
        if (verifyType && verifyType !== '') {
          slidingVerifyType.value = verifyType;
        } else {
          slidingVerifyType.value = 'login';
        }
        verifyRef.value.show();
      }

      function handleLogin() {
        setLoginLoading(true);
        if (loginState.value === LoginStateEnum.LOGIN) {
          loginFormRef.value.handleLoginValid();
        } else if (loginState.value === LoginStateEnum.MOBILE) {
          mobileFormRef.value.handleLoginValid();
        }
      }

      // 点击滑动验证码，成功之后的回调
      function handleSubmit(params) {
        if (loginState.value === LoginStateEnum.LOGIN) {
          loginFormRef.value.handleLoginSubmit(params);
        } else if (loginState.value === LoginStateEnum.MOBILE) {
          // 判断是发送验证码还是登录
          if (slidingVerifyType.value === 'sendSms') {
            mobileFormRef.value.verifySendSmsSuccess(params);
          } else {
            mobileFormRef.value.handleLoginSubmit(params);
          }
        }
      }

      function handleLoginTypeChange(activeKey) {
        if (activeKey === 'LOGIN') {
          setLoginState(LoginStateEnum.LOGIN);
        } else if (activeKey === 'MOBILE') {
          setLoginState(LoginStateEnum.MOBILE);
        }
      }

      const router = useRouter();
      const { currentRoute } = router;
      const { query } = unref(currentRoute);

      function openRegister() {
        const { href } = router.resolve({
          name: 'Register', // 这里是跳转页面的name
          query: {
            // 要传的参数
            redirect: query.redirect,
          },
        });
        window.open(href);
      }

      function openSocialLogin(socialType) {
        socialLoading.value = true;
        getSocialLoginUrl(socialType).then((res) => {
          socialLoading.value = false;
          window.open(res);
        });
      }

      return {
        t,
        prefixCls,
        loginFormRef,
        mobileFormRef,
        registerFormRef,
        forgetPasswordFormRef,
        queryCaptchaType,
        refreshImageCode,
        captchaState,
        setGrantType,
        setLoginCaptchaType,
        setSlidingCaptchaType,
        setCaptchaImage,
        getCaptchaState,
        verifyRef,
        handleShowVerify,
        handleSubmit,
        showLocale,
        title,
        loginActiveKey,
        loading,
        handleLogin,
        setLoginState,
        LoginStateEnum,
        handleLoginTypeChange,
        getLoginBtnShow,
        openRegister,
        openSocialLogin,
        socialLoading,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-login';
  @logo-prefix-cls: ~'@{namespace}-app-logo';
  @countdown-prefix-cls: ~'@{namespace}-countdown-input';
  @dark-bg: #293146;

  html[data-theme='dark'] {
    .@{prefix-cls} {
      // background-color: @dark-bg;
      background: linear-gradient(65deg, #1f1f1f, #293146);

      &::before {
        background-image: url(/@/assets/svg/login-bg-dark.svg);
      }

      .ant-input,
      .ant-input-password {
        background-color: #232a3b;
      }

      .ant-btn:not(.ant-btn-link):not(.ant-btn-primary) {
        border: 1px solid #4a5569;
      }

      &-form {
        background: transparent !important;
      }

      .app-iconify {
        color: #fff;
      }

      .ant-checkbox-inner {
        background-color: transparent !important;
        border: 1px solid #1890ff !important;
      }

      .ant-checkbox-inner:hover {
        border: 1px solid #40a9ff !important;
      }

      .ant-checkbox-checked .ant-checkbox-inner::after {
        border: 2px solid #1890ff !important;
        border-top: 0 !important;
        border-left: 0 !important;
      }

      .ant-checkbox-wrapper-checked .ant-checkbox-checked .ant-checkbox-inner:hover {
        border: 1px solid #40a9ff !important;
        border-radius: 2px;
      }

      .ant-btn-primary:not(.ant-btn-background-ghost):not([disabled]) {
        color: #c9d1d9 !important;
      }
    }
  }

  .@{prefix-cls} {
    min-height: 100%;
    overflow: hidden;
    @media (max-width: @screen-xl) {
      background-color: #293146;

      .@{prefix-cls}-form {
        background-color: #fff;
      }
    }

    .@{logo-prefix-cls} {
      position: absolute;
      top: 12px;
      height: 30px;

      &__title {
        font-size: 16px;
        color: #fff;
        font-weight: 400;
      }

      img {
        width: 32px;
      }
    }

    .container {
      .@{logo-prefix-cls} {
        display: flex;
        width: 60%;
        height: 80px;
      }
    }

    .v-code-img-input {
      min-width: 70% !important;
    }

    .v-code-img {
      display: inline-block;
      vertical-align: top;
      height: 40px;
      width: 100%;
      border-radius: 1px;
      border: 1px solid #40a9ff !important;
      cursor: pointer;
      opacity: 0.8;
      filter: alpha(opacity=60);
    }

    &-sign-in-way {
      .anticon {
        font-size: 22px;
        color: #888;
        cursor: pointer;

        &:hover {
          color: @primary-color;
        }
      }
    }

    input:not([type='checkbox']) {
      min-width: 360px;

      @media (max-width: @screen-xl) {
        min-width: 320px;
      }

      @media (max-width: @screen-lg) {
        min-width: 260px;
      }

      @media (max-width: @screen-md) {
        min-width: 240px;
      }

      @media (max-width: @screen-sm) {
        min-width: 160px;
      }
    }

    .@{countdown-prefix-cls} input {
      min-width: unset;
    }

    .ant-divider-inner-text {
      font-size: 12px;
      color: @text-color-secondary;
    }
  }

  // gitegg 自定义处理start
  .@{prefix-cls}-form {
    border-radius: 2px;
    font-family: 'neo';
    overflow: hidden;
    -webkit-box-shadow: rgba(43, 190, 210, 0.4) 0px 0px 10px 1px;
    background: linear-gradient(45deg, #236c86, #2a7494);
    box-shadow: rgba(43, 190, 210, 0.4) 0px 0px 10px 1px;

    input:not([type='checkbox']) {
      color: #1890ff !important;
      display: inline-block;
      background: transparent;
      caret-color: #1890ff !important;
      border-radius: 0px;
      font-weight: 400;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 0px #1890ff inset !important;
        -webkit-text-fill-color: #1890ff !important;
      }
    }

    input::-webkit-input-placeholder {
      color: #1890ff !important;
    }

    input::-moz-placeholder {
      color: #1890ff !important;
    }

    input::-ms-input-placeholder {
      color: #1890ff !important;
    }

    input:hover {
      background: transparent;
    }

    .ant-input-suffix {
      width: 16px;
    }

    input.fix-auto-fill,
    .fix-auto-fill input {
      -webkit-text-fill-color: #1890ff !important;
      box-shadow: inherit !important;
    }

    .ant-input-affix-wrapper {
      background-color: transparent;
      border: 1px solid #1890ff !important;

      .ant-input-prefix .app-iconify {
        color: #1890ff !important;
      }
    }

    .ant-input-affix-wrapper:hover {
      border-color: 1px solid #40a9ff !important;
    }

    .ant-checkbox-wrapper-checked .ant-checkbox-checked .ant-checkbox-inner:hover {
      border: 1px solid #40a9ff !important;
    }

    .ant-input-password-icon {
      color: #1890ff !important;
    }

    .ant-checkbox-inner {
      background-color: transparent !important;
      border: 1px solid #1890ff !important;
    }

    .ant-checkbox-inner:hover {
      border: 1px solid #40a9ff !important;
    }

    .ant-checkbox-input:hover {
      border: 1px solid #40a9ff !important;
    }

    .ant-form-item-has-error :not(.ant-input-disabled):not(.ant-input-borderless).ant-input,
    .ant-form-item-has-error
      :not(.ant-input-affix-wrapper-disabled):not(.ant-input-affix-wrapper-borderless).ant-input-affix-wrapper,
    .ant-form-item-has-error
      :not(.ant-input-number-affix-wrapper-disabled):not(.ant-input-number-affix-wrapper-borderless).ant-input-number-affix-wrapper,
    .ant-form-item-has-error :not(.ant-input-disabled):not(.ant-input-borderless).ant-input:hover,
    .ant-form-item-has-error
      :not(.ant-input-affix-wrapper-disabled):not(.ant-input-affix-wrapper-borderless).ant-input-affix-wrapper:hover,
    .ant-form-item-has-error
      :not(.ant-input-number-affix-wrapper-disabled):not(.ant-input-number-affix-wrapper-borderless).ant-input-number-affix-wrapper:hover {
      background-color: transparent;
      border-color: #ed6f6f;
    }

    .ant-tabs-top > .ant-tabs-nav::before,
    .ant-tabs-bottom > .ant-tabs-nav::before,
    .ant-tabs-top > div > .ant-tabs-nav::before,
    .ant-tabs-bottom > div > .ant-tabs-nav::before {
      border-bottom: 0px solid #f0f0f0 !important;
    }

    .ant-tabs-tab.ant-tabs-tab-active .ant-tabs-tab-btn {
      color: #40a9ff !important;
    }

    .ant-tabs-tab {
      font-size: 16px !important;
    }

    .ant-tabs-ink-bar {
      background: #40a9ff !important;
    }

    .ant-tabs {
      color: #1890ff !important;
    }

    .ant-tabs-tab:hover {
      color: #40a9ff;
    }

    .ant-checkbox-checked .ant-checkbox-inner::after {
      border: 2px solid #1890ff !important;
      border-top: 0 !important;
      border-left: 0 !important;
    }

    .ant-checkbox-wrapper,
    .ant-btn-link {
      color: #1890ff !important;
    }

    .ant-checkbox-wrapper,
    .ant-btn-link:hover {
      color: #40a9ff !important;
    }

    .ant-checkbox-checked::after {
      border: 1px solid #40a9ff;
    }

    .ant-checkbox-checked .ant-checkbox-inner::after:hover {
      border: 2px solid #40a9ff !important;
    }

    button.login-button {
      width: 100%;
      cursor: pointer;
      color: rgba(255, 255, 255, 0.85);
      padding: 6px 15px;
      font-size: 16px;
      background-color: transparent;
      border: 1px solid #1890ff !important;
      position: relative;
      overflow: hidden;
    }

    button.login-button:hover {
      box-shadow: 1px 1px 25px 10px rgba(64, 169, 255, 0.4) !important;
      background-color: transparent !important;
    }

    button.login-button:before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(120deg, transparent, rgba(64, 169, 255, 0.4), transparent);
      transition: all 650ms;
    }

    button.login-button:hover:before {
      left: 100%;
    }

    .send-captcha button {
      color: rgba(255, 255, 255, 0.85);
      background-color: transparent;
      border: 1px solid #1890ff !important;
    }

    .send-captcha button:hover {
      border: 1px solid #40a9ff !important;
      color: rgba(255, 255, 255, 0.85);
      background-color: transparent;
    }
  }

  .my-auto img {
    margin-left: auto;
    margin-right: auto;
    border-radius: 28%;
    -webkit-box-shadow: rgb(129 176 40 / 80%) 0px 0px 10px 1px;
    box-shadow: rgb(129 176 40 / 80%) 0px 0px 10px 1px;
  }

  .vben-login .ant-divider-inner-text {
    color: rgba(255, 255, 255, 0.85);
  }

  .login-type {
    margin-top: 25px;
  }

  .third-login {
    margin-bottom: 25px;

    a {
      color: rgba(255, 255, 255, 0.85);
    }

    a:hover {
      color: rgba(255, 255, 255, 1);
    }

    span.anticon:not(.app-iconify) {
      color: rgba(255, 255, 255, 0.8) !important;
    }

    span.anticon:not(.app-iconify):hover {
      color: rgb(255, 255, 255) !important;
    }
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
        color: rgba(255, 255, 255, 0.85);
        transition: all 0.3s;

        &:not(:last-child) {
          margin-right: 40px;
        }
      }
    }

    .copyright {
      color: rgba(255, 255, 255, 0.85);
      font-size: 14px;

      a {
        color: rgba(255, 255, 255, 0.85);
        transition: all 0.3s;
      }
    }
  }

  .starry-sky {
    width: 100%;
    height: 100%;
    position: absolute;
    background: linear-gradient(65deg, #10455e, #24bfd5);
    background-size: 100% 100%;
    z-index: 0;

    > canvas {
      width: 100%;
      height: 100%;
    }
  }
  // gitegg 自定义处理end

  // 滑动验证码样式修改 start
  .verifybox {
    @keyframes btn-anim1 {
      0% {
        left: -100%;
      }

      50%,
      100% {
        left: 100%;
      }
    }

    @keyframes btn-anim2 {
      0% {
        top: -100%;
      }

      50%,
      100% {
        top: 100%;
      }
    }

    @keyframes btn-anim3 {
      0% {
        right: -100%;
      }

      50%,
      100% {
        right: 100%;
      }
    }

    @keyframes btn-anim4 {
      0% {
        bottom: -100%;
      }

      50%,
      100% {
        bottom: 100%;
      }
    }
    position: relative;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    border-radius: 2px;
    border: 1px solid #40a9ff !important;
    background-color: rgba(16, 69, 94, 0.95);
    -webkit-box-shadow: 0 0 10px rgba(2, 41, 82, 0.85);
    box-shadow: 0 0 10px rgba(2, 41, 82, 0.3);
    left: 50%;
    top: 50%;
    -webkit-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);

    overflow: hidden;

    .verify-refresh {
      color: #1890ff;
    }

    .verify-refresh:hover {
      color: #40a9ff;
    }

    .slid-span {
      position: absolute;
      display: block;
    }

    .slid-span:nth-child(1) {
      top: 0;
      left: -100%;
      width: 100%;
      height: 2px;
      background: linear-gradient(90deg, transparent, #03e9f4);
      animation: btn-anim1 1s linear infinite;
    }

    .slid-span:nth-child(2) {
      top: -100%;
      right: 0;
      width: 2px;
      height: 100%;
      background: linear-gradient(180deg, transparent, #03e9f4);
      animation: btn-anim2 1s linear infinite;
      animation-delay: 0.25s;
    }

    .slid-span:nth-child(3) {
      bottom: 0;
      right: -100%;
      width: 100%;
      height: 2px;
      background: linear-gradient(270deg, transparent, #03e9f4);
      animation: btn-anim3 1s linear infinite;
      animation-delay: 0.5s;
    }

    .slid-span:nth-child(4) {
      bottom: -100%;
      left: 0;
      width: 2px;
      height: 100%;
      background: linear-gradient(360deg, transparent, #03e9f4);
      animation: btn-anim4 1s linear infinite;
      animation-delay: 0.75s;
    }
  }

  .verifybox-top {
    // background: rgba(2, 41, 82, 0.8) transparent;
    padding: 0 15px;
    height: 40px;
    line-height: 40px;
    text-align: left;
    font-size: 14px;
    color: #40a9ff;
    border-bottom: 1px solid #40a9ff;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    background: transparent;
  }

  .verifybox-close {
    position: absolute;
    color: #1890ff;
    top: 1px;
    right: 9px;
    width: 24px;
    height: 24px;
    text-align: center;
    cursor: pointer;
  }

  .icon-close:before {
    color: #1890ff;
  }

  .verifybox-close:hover {
    color: #40a9ff;
  }

  .verify-bar-area {
    position: relative;
    // background: rgb(146, 148, 248,.3);
    background: transparent;
    text-align: center;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    border-radius: 0px;
    border: 1px solid rgb(146, 148, 248, 0.3);
  }

  .verify-img-panel {
    margin: 0;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    border: 1px solid rgb(146, 148, 248, 0.3);
    border-radius: 0px;
    position: relative;
    // padding: 0 1px;
  }

  .verify-bar-area .verify-left-bar {
    position: absolute;
    top: -1px;
    left: -1px;
    background: transparent;
    cursor: pointer;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    border: 1px solid rgb(146, 148, 248, 0.3) !important;
  }

  // /deep/ .verify-bar-area .verify-left-bar:hover {
  //   box-shadow: 1px 1px 25px 10px rgba(146, 148, 248, 0.4);
  // }

  .verify-bar-area .verify-left-bar:hover {
    background: rgba(146, 148, 248, 0.4);
    color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px rgba(146, 148, 248, 0.4), 0 0 25px rgba(146, 148, 248, 0.4),
      0 0 50px rgba(146, 148, 248, 0.4), 0 0 100px rgba(146, 148, 248, 0.4);
  }

  .verify-bar-area .verify-move-block {
    position: absolute;
    top: 0px;
    left: 0;
    background: transparent !important;
    cursor: pointer;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    -webkit-box-shadow: 0 0 2px #40a9ff;
    box-shadow: 0 0 2px #40a9ff;
  }

  .verify-bar-area .verify-move-block .verify-icon {
    font-size: 18px;
    color: rgb(146, 148, 248, 0.9) !important;
  }

  .verify-bar-area .verify-msg {
    color: #40a9ff;
  }

  // 滑动验证码样式修改 end
</style>
