import type { ValidationRule } from 'ant-design-vue/lib/form/Form';
import type { RuleObject } from 'ant-design-vue/lib/form/interface';
import { ref, computed, unref, Ref } from 'vue';
import { useI18n } from '/@/hooks/web/useI18n';
import { setAuthCacheWithTimeOut, removeAuthCacheWithTimeOut } from '/@/utils/auth';
import { NO_CLEAR_START_KEY } from '/@/enums/cacheEnum';

// 根据登录页面的tab key 调整了顺序
export enum LoginStateEnum {
  LOGIN,
  MOBILE,
  QR_CODE,
  REGISTER,
  RESET_PASSWORD,
}

const currentState = ref(LoginStateEnum.LOGIN);

const currentLoading = ref(false);

// 验证码登录相关配置
const captchaState = ref({
  // 获取token的模式
  grant_type: 'password',
  // 验证码是滑动验证码还是图片验证码
  loginCaptchaType: 'sliding',
  // 滑动验证码的形式
  slidingCaptchaType: 'blockPuzzle',
  // 图片验证码校验标识
  captchaKey: '',
  // 图片验证码的默认图片
  captchaImage:
    'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAEALAAAAAABAAEAAAICRAEAOw==',
});

export function useLoginState() {
  // 登录方式
  function setLoginState(state: LoginStateEnum) {
    currentState.value = state;
  }

  function handleBackLogin() {
    setLoginState(LoginStateEnum.LOGIN);
  }

  function setLoginLoading(isLoading: boolean) {
    currentLoading.value = isLoading;
  }

  const getLoginState = computed(() => currentState.value);

  const getLoginLoading = computed(() => currentLoading.value);

  return { setLoginState, getLoginState, handleBackLogin, setLoginLoading, getLoginLoading };
}

export function useCaptchaState() {
  function setGrantType(grant_type: string) {
    captchaState.value.grant_type = grant_type;
  }

  function setLoginCaptchaType(loginCaptchaType: string) {
    captchaState.value.loginCaptchaType = loginCaptchaType;
  }

  function setSlidingCaptchaType(slidingCaptchaType: string) {
    captchaState.value.slidingCaptchaType = slidingCaptchaType;
  }

  function setCaptchaKey(captchaKey: string) {
    captchaState.value.captchaKey = captchaKey;
  }

  function setCaptchaImage(captchaImage: string) {
    captchaState.value.captchaImage = captchaImage;
  }

  const getCaptchaState = computed(() => captchaState.value);

  return {
    setGrantType,
    setLoginCaptchaType,
    setSlidingCaptchaType,
    setCaptchaKey,
    setCaptchaImage,
    getCaptchaState,
  };
}

export function useFormValid<T extends Object = any>(formRef: Ref<any>) {
  async function validForm() {
    const form = unref(formRef);
    if (!form) return;
    const data = await form.validate();
    return data as T;
  }

  return { validForm };
}

export function useFormRules(formData?: Recordable) {
  const { t } = useI18n();

  const getAccountFormRule = computed(() => createRule(t('sys.login.accountPlaceholder')));
  const getPasswordFormRule = computed(() => createRule(t('sys.login.passwordPlaceholder')));
  const getCaptchaFormRule = computed(() => createRule(t('sys.login.captchaPlaceholder')));
  const getSmsFormRule = computed(() => createRule(t('sys.login.smsPlaceholder')));
  const getMobileFormRule = computed(() => createRule(t('sys.login.mobilePlaceholder')));

  const validatePolicy = async (_: RuleObject, value: boolean) => {
    return !value ? Promise.reject(t('sys.login.policyPlaceholder')) : Promise.resolve();
  };

  const validateConfirmPassword = (password: string) => {
    return async (_: RuleObject, value: string) => {
      if (!value) {
        return Promise.reject(t('sys.login.passwordPlaceholder'));
      }
      if (value !== password) {
        return Promise.reject(t('sys.login.diffPwd'));
      }
      return Promise.resolve();
    };
  };

  const getFormRules = computed((): { [k: string]: ValidationRule | ValidationRule[] } => {
    const accountFormRule = unref(getAccountFormRule);
    const passwordFormRule = unref(getPasswordFormRule);
    const captchaFormRule = unref(getCaptchaFormRule);
    const smsFormRule = unref(getSmsFormRule);
    const mobileFormRule = unref(getMobileFormRule);

    const mobileRule = {
      captcha: smsFormRule,
      phoneNumber: mobileFormRule,
    };

    const usernameRule = {
      username: accountFormRule,
      password: passwordFormRule,
    };

    if (
      captchaState.value.grant_type === 'captcha' &&
      captchaState.value.loginCaptchaType === 'image'
    ) {
      usernameRule['captcha_code'] = captchaFormRule;
      mobileRule['captcha_code'] = captchaFormRule;
    }
    switch (unref(currentState)) {
      // register form rules
      case LoginStateEnum.REGISTER:
        return {
          username: accountFormRule,
          password: passwordFormRule,
          confirmPassword: [
            { validator: validateConfirmPassword(formData?.password), trigger: 'change' },
          ],
          policy: [{ validator: validatePolicy, trigger: 'change' }],
          ...mobileRule,
        };

      // reset password form rules
      case LoginStateEnum.RESET_PASSWORD:
        return {
          username: accountFormRule,
          ...mobileRule,
        };

      // mobile form rules
      case LoginStateEnum.MOBILE:
        return mobileRule;

      // login form rules
      default:
        return usernameRule;
    }
  });
  return { getFormRules };
}

function createRule(message: string) {
  return [
    {
      required: true,
      message,
      trigger: 'change',
    },
  ];
}

// 判断是否记住密码, 并保存在LocalStorage中，最长一周 60 * 60 * 24 * 7 * 1000 = 604800000
export function loginRememberMe(rememberMe, username, password, globSetting) {
  const rememberAccountStartKey =
    NO_CLEAR_START_KEY + globSetting.tenantId + '_' + globSetting.clientId;
  const timeout = 604800000;
  if (rememberMe && username !== '' && password !== '') {
    setAuthCacheWithTimeOut(rememberAccountStartKey + '_USERNAME', username, timeout);
    setAuthCacheWithTimeOut(rememberAccountStartKey + '_PASSWORD', password, timeout);
    setAuthCacheWithTimeOut(rememberAccountStartKey + '_REMEMBER_ME', true, timeout);
  } else {
    removeAuthCacheWithTimeOut(rememberAccountStartKey + '_USERNAME');
    removeAuthCacheWithTimeOut(rememberAccountStartKey + '_PASSWORD');
    removeAuthCacheWithTimeOut(rememberAccountStartKey + '_REMEMBER_ME');
  }
}

// 判断是否记住手机号, 并保存在LocalStorage中，最长一周 60 * 60 * 24 * 7 * 1000 = 604800000
export function loginRememberMobile(rememberMoblie, phoneNumber, globSetting) {
  const rememberMobileStartKey =
    NO_CLEAR_START_KEY + globSetting.tenantId + '_' + globSetting.clientId;
  const timeout = 604800000;
  if (rememberMoblie && phoneNumber !== '') {
    setAuthCacheWithTimeOut(rememberMobileStartKey + '_PHONE_NUMBER', phoneNumber, timeout);
    setAuthCacheWithTimeOut(rememberMobileStartKey + '_REMEMBER_ME', true, timeout);
  } else {
    removeAuthCacheWithTimeOut(rememberMobileStartKey + '_PHONE_NUMBER');
    removeAuthCacheWithTimeOut(rememberMobileStartKey + '_REMEMBER_ME');
  }
}
