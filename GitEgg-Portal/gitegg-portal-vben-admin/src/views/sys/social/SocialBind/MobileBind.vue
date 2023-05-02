<template>
  <Form
    class="p-4 enter-x"
    :model="formData"
    :rules="formRules"
    ref="formRef"
    @keypress.enter="verifySendSms"
  >
    <FormItem name="phoneNumber" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.phoneNumber"
        :placeholder="t('sys.login.mobilePlaceholder')"
        class="fix-auto-fill"
      >
        <template #prefix>
          <icon icon="ant-design:mobile-outlined" />
        </template>
      </Input>
    </FormItem>

    <FormItem name="captcha_code" v-show="bindCaptchaType === 'image'">
      <ARow class="enter-x">
        <ACol :span="15">
          <Input
            size="large"
            v-model:value="formData.captchaCodeSms"
            class="v-code-img-input"
            :placeholder="t('sys.login.captchaPlaceholder')"
          />
        </ACol>
        <ACol :span="1" />
        <ACol :span="8">
          <img :src="captchaImage" alt="" @click="refreshImageCode" class="v-code-img" />
        </ACol>
      </ARow>
    </FormItem>

    <FormItem name="captcha" class="enter-x">
      <CountdownInput
        size="large"
        class="fix-auto-fill send-captcha"
        v-model:value="formData.captcha"
        :sendCodeApi="verifySendSms"
        :placeholder="t('sys.login.smsPlaceholder')"
        ref="smsButtonInput"
      >
        <template #prefix>
          <icon icon="ant-design:mail-outlined" />
        </template>
      </CountdownInput>
    </FormItem>

    <FormItem :style="{ 'text-align': 'right' }">
      <span class="has-account" style="float: left; color: #52c41a">
        <a-icon type="bell" />
        未注册的手机号验证后自动创建账号
      </span>
      <Button type="primary" @click="bindMobile" style="float: right">同意协议并绑定</Button>
    </FormItem>
  </Form>
  <Verify
    @success="verifySendSmsSuccess"
    :mode="'pop'"
    :captchaType="slidingCaptchaType"
    :imgSize="{ width: '330px', height: '155px' }"
    ref="verify"
  />
</template>
<script lang="ts">
  import { defineComponent, reactive, ref, unref, onMounted } from 'vue';
  import { Checkbox, Form, Input, Row, Col, Button } from 'ant-design-vue';
  import Verify from '/@/components/verifition/Verify.vue';
  import { CountdownInput } from '/@/components/CountDown';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRouter } from 'vue-router';
  import { getCaptchaType, getImageCaptcha, userBindMobile } from '/@/api/login';
  import { sendSmsCode, checkSmsCodePre } from '/@/api/system/extension/sms/sms';
  import type { RuleObject } from 'ant-design-vue/lib/form/interface';

  export default defineComponent({
    name: 'MobileBind',
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
      CountdownInput,
      Verify,
    },
    emits: ['bind-login', 'refresh-image-code'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { notification, createMessage } = useMessage();
      const router = useRouter();
      const { currentRoute } = router;

      const formRef = ref();
      const loading = ref(false);

      // 滑动验证码引用
      const verify = ref();
      const sendSmsLoading = ref(false);
      const bindCaptchaType = ref('sliding');
      const slidingCaptchaType = ref('blockPuzzle');
      const captchaKey = ref('');
      const captchaImage = ref(
        'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAEALAAAAAABAAEAAAICRAEAOw==',
      );

      const formData = reactive({
        phoneNumber: '',
        captcha: '',
        grant_type: 'sms_captcha',
        captcha_key: '',
        captcha_code: '',
        captchaCodeSms: '',
        rememberMobile: false,
      });

      const validatePhoneNumber = async (_: RuleObject, value: string) => {
        if (value && '' !== value) {
          if (!new RegExp(/^1[3|4|5|6|7|8|9][0-9]\d{8}$/).test(value)) {
            return Promise.reject('请输入正确的手机号码');
          }
        }
        return Promise.resolve();
      };

      const validateCaptcha = async (_: RuleObject, value: string) => {
        return new Promise<void>((resolve, reject) => {
          const params = {
            smsCode: 'sms_login_code',
            phoneNumber: formData.phoneNumber,
            verificationCode: value,
          };
          checkSmsCodePre(params)
            .then((response) => {
              if (!response) {
                reject(response.msg);
              } else {
                resolve();
              }
            })
            .catch((err) => {
              reject(err.message || '验证失败');
            });
        });
      };

      const formRules = {
        phoneNumber: [
          { required: true, message: '请输入手机号码', trigger: 'change' },
          validatePhoneNumber,
        ] as RuleObject[],
        captcha: [
          { required: true, message: '请输入短信验证码', trigger: 'change' },
          validateCaptcha,
        ] as RuleObject[],
      };

      // 挂载，判断是否有记住手机号
      onMounted(() => {
        queryCaptchaType();
      });

      // start
      async function bindMobile() {
        loading.value = true;
        const data = await formRef.value.validateFields();
        if (!data) {
          loading.value = false;
          return;
        }

        const bindParams = {} as any;
        bindParams.smsCode = 'sms_login_code';
        bindParams.code = formData.captcha;
        bindParams.phoneNumber = formData.phoneNumber;
        const { query } = unref(currentRoute);
        bindParams.socialKey = query.key;

        // 执行绑定操作
        userBindMobile(bindParams)
          .then(() => {
            // 绑定成功后，调用第三方登录方法
            emit('bind-login');
          })
          .catch((err) => {
            requestFailed(err);
          });
      }

      function refreshImageCode() {
        getImageCaptcha().then((res) => {
          const data = res.data;
          captchaKey.value = data.captchaKey;
          captchaImage.value = data.captchaImage;
        });
      }

      function queryCaptchaType() {
        getCaptchaType().then((res) => {
          bindCaptchaType.value = res;
          if (bindCaptchaType.value === 'image') {
            refreshImageCode();
          }
        });
      }

      // 发送短信验证码时必须进行验证码安全验证
      async function verifySendSms(): Promise<boolean> {
        sendSmsLoading.value = true;
        const data = await formRef.value.validateFields('phoneNumber');
        if (!data) {
          sendSmsLoading.value = false;
          return false;
        }
        if (bindCaptchaType.value === 'sliding') {
          verify.value.show();
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
        if (bindCaptchaType.value === 'sliding') {
          loginParams.captcha_type = 'sliding';
          loginParams.sliding_type = slidingCaptchaType.value;
          loginParams.captcha_verification = params.captchaVerification;
        } else if (bindCaptchaType.value === 'image') {
          loginParams.captcha_type = 'image';
          loginParams.captcha_key = captchaKey.value;
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
        sendSmsCode(loginParams)
          .then(() => {
            immediateStart();
            createMessage.success(`验证码已发送`);
          })
          .finally(() => {
            sendSmsLoading.value = false;
            createMessage.destroy('sendSmsLoading');
          });
      }

      // 倒计时按钮, 暴露倒计时回调的方法
      const smsButtonInput = ref();
      async function immediateStart() {
        smsButtonInput.value.immediateStart(true);
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
        formRef,
        formData,
        formRules,
        queryCaptchaType,
        loading,
        sendSmsLoading,
        refreshImageCode,
        slidingCaptchaType,
        verify,
        verifySendSms,
        verifySendSmsSuccess,
        immediateStart,
        smsButtonInput,
        bindMobile,
        bindCaptchaType,
        captchaImage,
      };
    },
  });
</script>
