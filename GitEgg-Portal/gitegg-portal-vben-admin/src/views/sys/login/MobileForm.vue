<template>
  <Form
    class="p-4 enter-x"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    v-show="getShow"
    @keypress.enter="handleLoginValid"
  >
    <FormItem name="phoneNumber" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.phoneNumber"
        :placeholder="t('sys.login.mobile')"
        class="fix-auto-fill"
      >
        <template #prefix>
          <icon icon="ant-design:mobile-outlined" />
        </template>
      </Input>
    </FormItem>

    <FormItem name="captcha_code" v-if="captchaState.loginCaptchaType === 'image'">
      <ARow class="enter-x">
        <ACol :span="15">
          <Input
            size="large"
            v-model:value="formData.captcha_code"
            class="v-code-img-input"
            :placeholder="t('sys.login.captcha')"
          />
        </ACol>
        <ACol :span="1" />
        <ACol :span="8">
          <img
            :src="captchaState.captchaImage"
            alt=""
            @click="refreshImageCode"
            class="v-code-img"
          />
        </ACol>
      </ARow>
    </FormItem>

    <FormItem name="captcha" class="enter-x">
      <CountdownInput
        size="large"
        class="fix-auto-fill send-captcha"
        v-model:value="formData.captcha"
        :sendCodeApi="captchaVerifySendSms"
        :placeholder="t('sys.login.smsCode')"
        :loading="sendSmsLoading"
        ref="countButtonInput"
      >
        <template #prefix>
          <icon icon="ant-design:mail-outlined" />
        </template>
      </CountdownInput>
    </FormItem>

    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem>
          <!-- No logic, you need to deal with it yourself -->
          <Checkbox v-model:checked="formData.rememberMobile" size="small">
            {{ t('sys.login.rememberMobile') }}
          </Checkbox>
        </FormItem>
      </ACol>
      <ACol :span="12">
        <FormItem :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <!-- <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ t('sys.login.forgetPassword') }}
          </Button> -->
        </FormItem>
      </ACol>
    </ARow>
  </Form>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref, unref, computed, onMounted } from 'vue';
  import { Checkbox, Form, Input, Row, Col, Button } from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import {
    LoginStateEnum,
    useLoginState,
    useCaptchaState,
    useFormRules,
    useFormValid,
    loginRememberMobile,
  } from './useLogin';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { getAuthCacheWithTimeOut } from '/@/utils/auth';
  import { useGlobSetting } from '/@/hooks/setting';
  import { NO_CLEAR_START_KEY } from '/@/enums/cacheEnum';
  import { getSmsCaptcha } from '/@/api/login';

  export default defineComponent({
    name: 'LoginForm',
    components: {
      [Col.name]: Col,
      [Row.name]: Row,
      Checkbox,
      Form,
      FormItem: Form.Item,
      Input,
      Icon,
      CountdownInput,
      Button,
    },
    emits: ['show-verify', 'refresh-image-code'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { notification, createErrorModal, createMessage } = useMessage();
      const { prefixCls } = useDesign('login');
      const userStore = useUserStore();
      const globSetting = useGlobSetting();

      const { setLoginState, getLoginState, setLoginLoading } = useLoginState();
      const { getCaptchaState } = useCaptchaState();
      const { getFormRules } = useFormRules();

      const formRef = ref();
      const loading = ref(false);
      const verify = ref();
      const rememberMobile = ref(false);

      const sendSmsLoading = ref(false);

      const formData = reactive({
        phoneNumber: '',
        captcha: '',
        grant_type: 'sms_captcha',
        captcha_key: '',
        captcha_code: '',
        rememberMobile: false,
      });

      const { validForm } = useFormValid(formRef);

      const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE);

      const captchaState = computed(() => unref(getCaptchaState));

      // 挂载，判断是否有记住手机号
      onMounted(() => {
        const rememberAccountStartKey =
          NO_CLEAR_START_KEY + globSetting.tenantId + '_' + globSetting.clientId;
        const rememberMobile = getAuthCacheWithTimeOut(
          rememberAccountStartKey + '_REMEMBER_ME',
        ) as boolean;
        if (rememberMobile) {
          const phoneNumber = getAuthCacheWithTimeOut(
            rememberAccountStartKey + '_PHONE_NUMBER',
          ) as string;
          if (phoneNumber !== '') {
            formData.phoneNumber = phoneNumber;
            formData.rememberMobile = rememberMobile;
          }
        }
      });

      // 发送短信验证码时必须进行验证码安全验证
      async function captchaVerifySendSms(): Promise<boolean> {
        sendSmsLoading.value = true;
        const data = await formRef.value.validateFields('phoneNumber');
        if (!data) return false;
        if (captchaState.value.loginCaptchaType === 'sliding') {
          emit('show-verify', { verifyType: 'sendSms' });
        } else {
          verifySendSmsSuccess(undefined);
        }
        return false;
      }

      // 滑动验证码二次校验并提交登录
      async function verifySendSmsSuccess(params) {
        // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
        const data = await formRef.value.validateFields('phoneNumber');
        if (!data) return;
        const loginParams = {} as any;
        loginParams.grant_type = 'sms_captcha';
        loginParams.phoneNumber = formData.phoneNumber;
        loginParams.code = formData.captcha;
        loginParams.smsCode = 'sms_login_code';
        // 判断是图片验证码还是滑动验证码
        if (captchaState.value.loginCaptchaType === 'sliding') {
          loginParams.captcha_type = 'sliding';
          loginParams.sliding_type = captchaState.value.slidingCaptchaType;
          loginParams.captcha_verification = params.captchaVerification;
        } else if (captchaState.value.loginCaptchaType === 'image') {
          loginParams.captcha_type = 'image';
          loginParams.captcha_key = captchaState.value.captchaKey;
          loginParams.captcha_code = formData.captcha_code;
        }
        getCaptcha(loginParams);
      }

      function getCaptcha(loginParams) {
        createMessage.loading({
          content: '验证码发送中...',
          duration: 0,
          key: 'sendSmsLoading',
        });
        loginParams.phoneNumber = formData.phoneNumber;
        loginParams.smsCode = 'sms_login_code';
        getSmsCaptcha(loginParams)
          .then((response) => {
            if (response && response.success) {
              // 验证码发送成功开始倒计时
              immediateStart();
              createMessage.success(`验证码已发送`);
            } else {
              createMessage.error(`验证码发送失败：${response.msg}`);
            }
          })
          .finally(() => {
            sendSmsLoading.value = false;
            createMessage.destroy('sendSmsLoading');
          });
      }

      // 倒计时按钮, 暴露倒计时回调的方法
      const countButtonInput = ref();
      async function immediateStart() {
        countButtonInput.value.immediateStart(true);
      }

      // 当loginCaptchaType = image时刷新图片验证码的方法
      function refreshImageCode() {
        emit('refresh-image-code', {});
      }

      async function handleLoginValid() {
        const data = await validForm();
        if (!data) return;
        // 如果是授权码模式，并且是滑动验证码，那么需要进行验证码验证
        if (
          formData.grant_type === 'sms_captcha' &&
          captchaState.value.loginCaptchaType === 'sliding'
        ) {
          emit('show-verify', { verifyType: 'login' });
        } else {
          handleLoginSubmit(undefined);
        }
      }

      // 滑动验证码二次校验并提交登录
      async function handleLoginSubmit(params) {
        // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
        try {
          loading.value = true;
          const userInfo = await userStore.login({
            phoneNumber: formData.phoneNumber,
            code: formData.captcha, //这个是短信验证码
            smsCode: 'sms_login_code',
            grant_type: 'sms_captcha',
            captcha_type: captchaState.value.loginCaptchaType,
            captcha_key: captchaState.value.captchaKey,
            captcha_code: formData.captcha_code, //这个是图形验证码
            sliding_type: captchaState.value.slidingCaptchaType,
            captcha_verification: params ? params?.captchaVerification : undefined,
            mode: 'none', //不要默认的错误提示
          });

          if (userInfo) {
            // 登录成功后，判断记住手机号
            const rememberMobile = formData.rememberMobile;
            const phoneNumber = formData.phoneNumber;
            loginRememberMobile(rememberMobile, phoneNumber, globSetting);
            notification.success({
              message: t('sys.login.loginSuccessTitle'),
              description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.nickname}`,
              duration: 3,
            });
          }
        } catch (error) {
          const errMsg = (error as unknown as Error).message || t('sys.api.networkExceptionMsg');
          if (errMsg?.indexOf('错误码:427') !== -1) {
            // 密码错误次数超过最大限值，请选择验证码模式登录
            formData.grant_type = 'sms_captcha';
            // 如果是滑动验证码，直接弹出滑动验证码界面
            if (captchaState.value.loginCaptchaType === 'sliding') {
              emit('show-verify', { verifyType: 'login' });
            }
          } else {
            createErrorModal({
              title: t('sys.api.errorTip'),
              content: errMsg,
              getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
            });
          }
        } finally {
          setLoginLoading(false);
          loading.value = false;
        }
      }

      return {
        t,
        prefixCls,
        formRef,
        formData,
        getFormRules,
        rememberMobile,
        useCaptchaState,
        handleLoginValid,
        handleLoginSubmit,
        loading,
        sendSmsLoading,
        setLoginState,
        LoginStateEnum,
        getShow,
        refreshImageCode,
        captchaState,
        verify,
        captchaVerifySendSms,
        verifySendSmsSuccess,
        immediateStart,
        countButtonInput,
      };
    },
  });
</script>
