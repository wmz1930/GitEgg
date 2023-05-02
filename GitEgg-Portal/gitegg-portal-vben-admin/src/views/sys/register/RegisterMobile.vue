<template>
  <div>
    <Form
      class="p-4 enter-x"
      :model="formData"
      :rules="formRules"
      ref="formRef"
      @keypress.enter="verifySendSms"
    >
      <FormItem label="手机号" name="phoneNumber" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <InputGroup style="display: inline-block; vertical-align: middle" :compact="true">
          <FormItemRest>
            <a-select defaultValue="+86" style="width: 80px" size="large">
              <a-select-option value="+86">+86</a-select-option>
            </a-select>
          </FormItemRest>
          <Input
            size="large"
            :style="{ width: 'calc(100% - 80px)' }"
            :placeholder="t('sys.login.mobilePlaceholder')"
            v-model:value="formData.phoneNumber"
          >
            <template #prefix>
              <icon icon="ant-design:mobile-outlined" />
            </template>
          </Input>
        </InputGroup>
      </FormItem>

      <FormItem
        :label="t('sys.login.captcha')"
        name="captcha_code"
        v-show="bindCaptchaType === 'image'"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
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

      <FormItem
        :label="t('sys.login.smsCode')"
        name="captcha"
        class="enter-x"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
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
      <FormItem :wrapperCol="{ span: 24 }">
        <a-button type="primary" @click="nextStep" style="float: right">下一步</a-button>
      </FormItem>
    </Form>
    <Verify
      @success="verifySendSmsSuccess"
      :mode="'pop'"
      :captchaType="slidingCaptchaType"
      :imgSize="{ width: '330px', height: '155px' }"
      ref="verify"
    />
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive, ref, unref, onMounted } from 'vue';
  import { Checkbox, Form, Input, Row, Col, Button, Select } from 'ant-design-vue';
  import Verify from '/@/components/verifition/Verify.vue';
  import { CountdownInput } from '/@/components/CountDown';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {
    getCaptchaType,
    getImageCaptcha,
    smsRegisterSend,
    checkSmsCode,
    checkUserExist,
  } from '/@/api/login';
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
      FormItemRest: Form.ItemRest,
      InputGroup: Input.Group,
      Input,
      [Input.Group.name]: Input.Group,
      [Select.name]: Select,
      ASelectOption: Select.Option,
      Icon,
      CountdownInput,
      Verify,
    },
    emits: ['set-mobile-sms', 'refresh-image-code', 'next-step'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { createMessage } = useMessage();

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

      const labelCol = { lg: { span: 5 }, sm: { span: 5 } };
      const wrapperCol = { lg: { span: 19 }, sm: { span: 19 } };

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
        var params = {
          mobile: value,
        };
        const result = await checkUserExist(params);
        if (!result && value !== '') {
          return Promise.reject(new Error('该手机号已注册'));
        }
        return Promise.resolve();
      };

      const validateCaptcha = async (_: RuleObject, value: string) => {
        if (value && value.length == 6) {
          const params = {
            smsCode: 'sms_register_code',
            phoneNumber: formData.phoneNumber,
            verificationCode: value,
          };
          const result = await checkSmsCode(params);
          if (!result) {
            return Promise.reject(new Error('短信验证码错误'));
          }
        }
        return Promise.resolve();
      };

      const formRules = {
        phoneNumber: [
          { required: true, message: '请输入手机号码', trigger: 'change' },
          { validator: validatePhoneNumber, trigger: 'change' },
        ] as RuleObject[],
        captcha: [
          { required: true, message: '请输入短信验证码', trigger: 'change' },
          { validator: validateCaptcha, trigger: 'change' },
        ] as RuleObject[],
      };

      // 挂载，判断是否有记住手机号
      onMounted(() => {
        queryCaptchaType();
      });

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
        const registerParams = {} as any;
        registerParams.phoneNumber = formData.phoneNumber;
        registerParams.code = formData.captcha;
        registerParams.smsCode = 'sms_register_code';
        // 判断是图片验证码还是滑动验证码
        if (bindCaptchaType.value === 'sliding') {
          registerParams.captcha_type = 'sliding';
          registerParams.sliding_type = slidingCaptchaType.value;
          registerParams.captcha_verification = params.captchaVerification;
        } else if (bindCaptchaType.value === 'image') {
          registerParams.captcha_type = 'image';
          registerParams.captcha_key = captchaKey.value;
          registerParams.captcha_code = formData.captcha_code;
        }
        getCaptcha(registerParams);
      }

      function getCaptcha(registerParams) {
        createMessage.loading({
          content: '验证码发送中...',
          duration: 0,
          key: 'sendSmsLoading',
        });
        registerParams.phoneNumber = formData.phoneNumber;
        registerParams.smsCode = 'sms_register_code';
        smsRegisterSend(registerParams)
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

      // 下一步
      async function nextStep() {
        loading.value = true;
        const data = await formRef.value.validateFields();
        if (!data) {
          loading.value = false;
          return;
        }
        const registerParams = {} as any;
        registerParams.smsCode = 'sms_register_code';
        registerParams.code = formData.captcha;
        registerParams.phoneNumber = formData.phoneNumber;
        setMobileSms(registerParams);
        emit('next-step');
      }

      function setMobileSms(params) {
        emit('set-mobile-sms', params);
      }

      return {
        t,
        formRef,
        formData,
        formRules,
        labelCol,
        wrapperCol,
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
        nextStep,
        bindCaptchaType,
        captchaImage,
      };
    },
  });
</script>
