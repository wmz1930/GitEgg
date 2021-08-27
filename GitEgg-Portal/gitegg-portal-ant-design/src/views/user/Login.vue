<template>
  <div class="main login-container">
    <a-form id="formLogin"
            class="user-layout-login"
            ref="formLogin"
            :form="form"
            @submit="captchaVerify">
      <a-tabs :activeKey="customActiveKey"
              :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
              @change="handleTabClick">
        <a-tab-pane key="tab_account"
                    :tab="$t('user.login.tab-login-credentials')">
          <a-alert v-if="isLoginError"
                   type="error"
                   showIcon
                   style="margin-bottom: 24px;"
                   :message="loginErrorMsg" />
          <a-form-item>
            <a-input size="large"
                     type="text"
                     :placeholder="$t('user.login.username.placeholder')"
                     :max-length="32"
                     v-decorator="[ 'username', {rules: [{ required: true, message: $t('user.userName.required') }, { validator: handleUsernameOrEmail }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix"
                      type="user"
                      :style="{ color: '#1890ff' }" />
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input-password size="large"
                              :placeholder="$t('user.login.password.placeholder')"
                              :max-length="64"
                              v-decorator="[ 'password', {rules: [{ required: true, message: $t('user.password.required') }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix"
                      type="lock"
                      :style="{ color: '#1890ff' }" />
            </a-input-password>
          </a-form-item>
          <a-row :gutter="0"
                 v-if="loginCaptchaType === 'image' && grantType !== 'password'">
            <a-col :span="14">
              <a-form-item>
                <a-input v-decorator="[ 'captchaCode', {rules: [{ required: true, message: $t('user.verification-code.required') }], validateTrigger: 'blur'}]"
                         size="large"
                         type="text"
                         :max-length="5"
                         :placeholder="$t('user.verification-code.required')">
                  <a-icon slot="prefix"
                          type="safety-certificate"
                          :style="{ fontSize: '20px', color: '#1890ff' }" />
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="10">
              <img :src="captchaImage"
                   class="v-code-img"
                   @click="refreshImageCode">
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="phone_account"
                    :tab="$t('user.login.tab-login-mobile')"
                    class="color:#1890ff;">
          <a-alert v-if="isPhoneLoginError"
                   type="error"
                   showIcon
                   style="margin-bottom: 24px;"
                   :message="loginPhoneErrorMsg" />
          <a-form-item>
            <a-input size="large"
                     type="text"
                     :max-length="11"
                     :placeholder="$t('user.login.mobile.placeholder')"
                     v-decorator="['phoneNumber', {rules: [{ required: true, pattern: /^1[34578]\d{9}$/, message: $t('user.phone-number.required') }], validateTrigger: 'change'}]">
              <a-icon slot="prefix"
                      type="mobile"
                      :style="{ color: '#1890ff' }" />
            </a-input>
          </a-form-item>

          <a-row :gutter="0"
                 v-if="loginCaptchaType === 'image'">
            <a-col :span="14">
              <a-form-item>
                <a-input v-decorator="[ 'captchaCodeSms', {rules: [{ required: true, message: $t('user.verification-code.required') }], validateTrigger: 'blur'}]"
                         :max-length="5"
                         size="large"
                         type="text"
                         :placeholder="$t('user.verification-code.required')">
                  <a-icon slot="prefix"
                          type="safety-certificate"
                          :style="{ fontSize: '20px', color: '#1890ff' }" />
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="10">
              <img :src="captchaImage"
                   class="v-code-img"
                   @click="refreshImageCode">
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col class="gutter-row"
                   :span="16">
              <a-form-item>
                <a-input size="large"
                         type="text"
                         :placeholder="$t('user.login.mobile.verification-code.placeholder')"
                         :max-length="6"
                         v-decorator="['captcha', {rules: [{ required: true, message: $t('user.verification-code.required') }], validateTrigger: 'blur'}]">
                  <a-icon slot="prefix"
                          type="mail"
                          :style="{ color: '#1890ff' }" />
                </a-input>
              </a-form-item>
            </a-col>
            <a-col class="gutter-row"
                   :span="8">
              <a-button class="getCaptcha"
                        tabindex="-1"
                        :disabled="state.smsSendBtn"
                        @click.stop.prevent="captchaVerifySendSms"
                        v-text="!state.smsSendBtn && $t('user.register.get-verification-code') || (state.time+' s')"></a-button>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>

      <a-form-item>
        <a-checkbox v-decorator="['rememberMe', { valuePropName: 'checked', initialValue: false }]">{{ $t('user.login.remember-me') }}</a-checkbox>
        <router-link :to="{ name: 'recover', params: { user: 'aaa'} }"
                     class="forge-password"
                     style="float: right;">{{ $t('user.login.forgot-password') }}</router-link>
      </a-form-item>

      <a-form-item style="margin-top:24px">
        <a-button size="large"
                  type="primary"
                  htmlType="submit"
                  class="login-button"
                  :loading="state.loginBtn"
                  :disabled="state.loginBtn">{{ $t('user.login.login') }}</a-button>
      </a-form-item>

      <div class="user-login-other">
        <span>{{ $t('user.login.sign-in-with') }}</span>
        <a>
          <a-icon class="item-icon"
                  type="wechat"></a-icon>
        </a>
        <a>
          <a-icon class="item-icon"
                  type="qq"></a-icon>
        </a>
        <a>
          <a-icon class="item-icon"
                  type="github"></a-icon>
        </a>
        <a>
          <a-icon class="item-icon"
                  type="alipay-circle"></a-icon>
        </a>
        <router-link class="register"
                     :to="{ name: 'register' }">{{ $t('user.login.signup') }}</router-link>
      </div>
    </a-form>

    <Verify @success="verifySuccess"
            :mode="'pop'"
            :captchaType="slidingCaptchaType"
            :imgSize="{ width: '330px', height: '155px' }"
            ref="verify"></Verify>
  </div>
</template>

<script>
import md5 from 'md5'
import storage from 'store'
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import Verify from '@/components/verifition/Verify'
import { mapActions } from 'vuex'
import { timeFix, serialize } from '@/utils/util'
import { getCaptchaType, getImageCaptcha, getSmsCaptcha } from '@/api/login'

export default {
  components: {
    TwoStepCaptcha,
    Verify
  },
  data () {
    return {
      customActiveKey: 'tab_account',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      isLoginError: false,
      isPhoneLoginError: false,
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        smsSendBtn: false
      },
      validatorRules: {
        username: {
          rules: [{ required: true, message: '请输入用户名!', validator: 'click' }]
        },
        password: {
          rules: [{ required: true, message: '请输入密码!', validator: 'click' }]
        },
        phoneNumber: { rules: [{ validator: this.validatePhoneNumber }] },
        captchaCode: { rule: [{ required: true, message: '请输入验证码!' }] },
        inputCode: {
          rules: [
            { required: true, message: '请输入验证码!' },
            { validator: this.validateInputCode }
          ]
        }
      },
      sendSms: false,
      grantType: 'password',
      loginCaptchaType: 'sliding',
      slidingCaptchaType: 'blockPuzzle',
      loginErrorMsg: '用户名或密码错误',
      loginPhoneErrorMsg: '手机号或短信验证码错误',
      captchaKey: '',
      captchaCode: '',
      captchaImage: 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAEALAAAAAABAAEAAAICRAEAOw==',
      inputCodeNull: true
    }
  },
  created () {
    this.queryCaptchaType()
    this.$nextTick(() => {
        const rememberMe = storage.get(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-rememberMe')
        if (rememberMe) {
          const username = storage.get(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-username')
          const password = storage.get(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-password')
          if (username !== '' && password !== '') {
            this.form.setFieldsValue({ 'username': username })
            this.form.setFieldsValue({ 'password': password })
            this.form.setFieldsValue({ 'rememberMe': true })
          }
        }
    })
  },
  methods: {
    ...mapActions(['Login', 'Logout']),
    // handler
    handleUsernameOrEmail (rule, value, callback) {
      const { state } = this
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        state.loginType = 0
      } else {
        state.loginType = 1
      }
      callback()
    },
    verifySuccess (params) {
      if (!this.sendSms) {
        this.verifyLoginSuccess(params)
      } else {
        this.verifySendSmsSuccess(params)
      }
    },
    // 滑动验证码校验
    captchaVerify (e) {
      e.preventDefault()
      this.sendSms = false
      const {
        form: { validateFields },
        state,
        customActiveKey
      } = this

      state.loginBtn = true
      const validateFieldsKey = customActiveKey === 'tab_account' ? ['username', 'password', 'vcode', 'verkey'] : ['phoneNumber', 'captcha', 'vcode', 'verkey']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          if (this.grantType === 'password' && customActiveKey === 'tab_account') {
            this.verifyLoginSuccess()
          } else {
            if (this.loginCaptchaType === 'sliding') {
              this.$refs.verify.show()
            } else {
              this.verifyLoginSuccess()
            }
          }
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    // 滑动验证码二次校验并提交登录
    verifyLoginSuccess (params) {
      // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      const {
        form: { validateFields },
        state,
        customActiveKey,
        Login
      } = this
      state.loginBtn = true
      const validateFieldsKey = customActiveKey === 'tab_account' ? ['username', 'password', 'captchaCode', 'captchaKey'] : ['phoneNumber', 'captcha', 'captchaCodeSms', 'captchaKey']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          const loginParams = { ...values }
          delete loginParams.username
          loginParams[!state.loginType ? 'email' : 'username'] = values.username
          loginParams.client_id = process.env.VUE_APP_CLIENT_ID
          loginParams.client_secret = process.env.VUE_APP_CLIENT_SECRET

          if (this.grantType === 'password' && customActiveKey === 'tab_account') {
              loginParams.grant_type = 'password'
              loginParams.password = md5(values.password)
          } else {
            if (customActiveKey === 'tab_account') {
              loginParams.grant_type = 'captcha'
              loginParams.password = md5(values.password)
              loginParams.captcha_key = this.captchaKey
              loginParams.captcha_code = values.captchaCode
            } else {
              loginParams.grant_type = 'sms_captcha'
              loginParams.phone_number = values.phoneNumber
              loginParams.code = values.captcha
              loginParams.smsCode = 'aliLoginCode'
              loginParams.captcha_key = this.captchaKey
              loginParams.captcha_code = values.captchaCodeSms
            }
            // 判断是图片验证码还是滑动验证码
            if (this.loginCaptchaType === 'sliding') {
              loginParams.captcha_type = 'sliding'
              loginParams.sliding_type = this.slidingCaptchaType
              loginParams.captcha_verification = params ? params.captchaVerification : undefined
            } else if (this.loginCaptchaType === 'image') {
              loginParams.captcha_type = 'image'
              loginParams.captcha_key = this.captchaKey
            }
          }
          Login(loginParams)
            .then((res) => this.loginSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              state.loginBtn = false
            })
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    // 发送短信验证码时必须进行验证码安全验证
    captchaVerifySendSms (e) {
      e.preventDefault()
      this.sendSms = true
      const {
        form: { validateFields },
        state
      } = this
      state.smsSendBtn = true
      const validateFieldsKey = ['phoneNumber', 'captchaCodeSms', 'captchaKey']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          if (this.loginCaptchaType === 'sliding') {
            this.$refs.verify.show()
          } else {
            this.verifySuccess()
          }
        } else {
          setTimeout(() => {
            state.smsSendBtn = false
          }, 600)
        }
      })
    },
    // 滑动验证码二次校验并提交登录
    verifySendSmsSuccess (params) {
      // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      const {
        form: { validateFields },
        state
      } = this

      state.smsSendBtn = true
      const validateFieldsKey = ['phoneNumber', 'captchaCodeSms', 'captchaKey']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          const loginParams = { ...values }

          loginParams.client_id = process.env.VUE_APP_CLIENT_ID
          loginParams.client_secret = process.env.VUE_APP_CLIENT_SECRET

          loginParams.grant_type = 'sms_captcha'
          loginParams.phone_number = values.phoneNumber
          loginParams.code = values.captcha
          loginParams.smsCode = 'aliLoginCode'
          // 判断是图片验证码还是滑动验证码
          if (this.loginCaptchaType === 'sliding') {
            loginParams.captcha_type = 'sliding'
            loginParams.sliding_type = this.slidingCaptchaType
            loginParams.captcha_verification = params.captchaVerification
          } else if (this.loginCaptchaType === 'image') {
            loginParams.captcha_type = 'image'
            loginParams.captcha_key = this.captchaKey
            loginParams.captcha_code = values.captchaCodeSms
          }

          this.getCaptcha(loginParams)
        }
      })
    },
    queryCaptchaType () {
      getCaptchaType().then(res => {
        this.loginCaptchaType = res.data
        if (this.loginCaptchaType === 'image') {
          this.refreshImageCode()
        }
      })
    },
    refreshImageCode () {
      getImageCaptcha().then(res => {
        const data = res.data
        this.captchaKey = data.captchaKey
        this.captchaImage = data.captchaImage
      })
    },
    handleTabClick (key) {
      this.customActiveKey = key
      // this.form.resetFields()
    },
    handleSubmit (e) {
      e.preventDefault()
    },
    getCaptcha (loginParams) {
      // e.preventDefault()
      const { form: { validateFields }, state } = this

      validateFields(['phoneNumber'], { force: true }, (err, values) => {
        if (!err) {
          state.smsSendBtn = true
          this.isPhoneLoginError = false
          this.loginPhoneErrorMsg = ''
          const interval = window.setInterval(() => {
            if (state.time-- <= 0) {
              state.time = 60
              state.smsSendBtn = false
              window.clearInterval(interval)
            }
          }, 1000)

          const hide = this.$message.loading('验证码发送中..', 0)
          loginParams.phoneNumber = values.phoneNumber
          loginParams.smsCode = 'aliLoginCode'
          getSmsCaptcha(serialize(loginParams)).then(res => {
            setTimeout(hide, 1)
            if (res.success && res.data) {
              if (res.data.success) {
                this.isPhoneLoginError = false
                this.$notification['success']({
                  message: '提示',
                  description: '验证码获取成功',
                  duration: 8
                })
              } else {
                this.isPhoneLoginError = true
                this.loginPhoneErrorMsg = res.data.message
                state.time = 60
                state.smsSendBtn = false
                window.clearInterval(interval)
              }
            }
          }).catch(err => {
            setTimeout(hide, 1)
            clearInterval(interval)
            state.time = 60
            state.smsSendBtn = false
            this.requestFailed(err)
          })
        }
      })
    },
    loginSuccess (res) {
     // 判断是否记住密码
      const rememberMe = this.form.getFieldValue('rememberMe')
      const username = this.form.getFieldValue('username')
      const password = this.form.getFieldValue('password')
      if (rememberMe && username !== '' && password !== '') {
          storage.set(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-username', username, 60 * 60 * 24 * 7 * 1000)
          storage.set(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-password', password, 60 * 60 * 24 * 7 * 1000)
          storage.set(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-rememberMe', true, 60 * 60 * 24 * 7 * 1000)
      } else {
         storage.remove(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-username')
         storage.remove(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-password')
         storage.remove(process.env.VUE_APP_TENANT_ID + '-' + process.env.VUE_APP_CLIENT_ID + '-rememberMe')
      }
      this.$router.push({ path: '/' })
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)
      this.isLoginError = false
    },
    requestFailed (err) {
      const errMsg = err && err.msg ? err.msg : '系统异常，请稍后再试'
      if (err && err.code === 427) {
        // 密码错误次数超过最大限值，请选择验证码模式登录
        if (this.customActiveKey === 'tab_account') {
            this.grantType = 'captcha'
            this.isLoginError = true
            this.loginErrorMsg = errMsg
        } else {
            this.grantType = 'sms_captcha'
            this.isPhoneLoginError = true
            this.loginPhoneErrorMsg = errMsg
        }

        if (this.loginCaptchaType === 'sliding') {
            this.$refs.verify.show()
        }
      } else if (err) {
            if (this.customActiveKey === 'tab_account') {
                this.isLoginError = true
                this.loginErrorMsg = errMsg
            } else {
                this.isPhoneLoginError = true
                this.loginPhoneErrorMsg = errMsg
            }
      }
    }
  }
}
</script>

<style lang="less" scoped>

.user-layout-login {
  min-height: 450px;
  padding: 50px 35px 50px 35px;
  background: rgba(109, 109, 109, 0.23);
  border: 0px solid rgb(221, 222, 225);
  box-shadow: 1px 1px 50px rgba(0,0,0,.3);

  /deep/ .ant-tabs-nav .ant-tabs-tab {
      color: #1890ff!important;
  }

  /deep/ .ant-tabs-nav .ant-tabs-tab:hover {
      color: #40a9ff!important;
  }

  /deep/ .ant-input {
    color: #1890ff!important;
    display: inline-block;
    background: transparent;
    caret-color: #1890ff!important;
    border-radius: 0px;
    border: 1px solid #1890ff!important;
    font-weight: 400;
    &:-webkit-autofill {
      box-shadow: 0 0 0px 1000px #283443 inset !important;
      -webkit-text-fill-color: #1890ff !important;
    }

  }

  /deep/ .ant-input::-webkit-input-placeholder {
      color: #1890ff!important;
  }

  /deep/ .ant-input::-moz-placeholder {
      color: #1890ff!important;
  }

  /deep/ .ant-input::-ms-input-placeholder {
      color: #1890ff!important;
  }

  /deep/ .ant-input:hover {
    border: 1px solid #40a9ff!important;
    background: transparent;
  }

  /deep/ .has-error .ant-form-explain, .has-error .ant-form-split {
    color: rgba(255,163,158, 0.85);;
  }

  /deep/ .has-error .ant-input-affix-wrapper:hover .ant-input:not(.ant-input-disabled) {
    border-color: rgba(255,163,158, 0.85);;
  }

  /deep/ .ant-input-password-icon {
    color: #1890ff!important;
  }

  /deep/ .ant-checkbox-wrapper {
    color: #1890ff!important;
  }

  /deep/ .ant-checkbox-wrapper:hover {
    color: #40a9ff!important;
  }

  /deep/ .ant-checkbox-inner {
    background-color: transparent;
    border: 1px solid #1890ff;
  }

  /deep/ .ant-checkbox-inner:hover {
    border: 1px solid #40a9ff;
  }

  /deep/ .ant-checkbox-checked .ant-checkbox-inner::after {
    position: absolute;
    display: table;
    border: 2px solid #40a9ff;
    border-top: 0;
    border-left: 0;
    -webkit-transform: rotate(45deg) scale(1) translate(-50%, -50%);
    transform: rotate(45deg) scale(1) translate(-50%, -50%);
    opacity: 1;
    -webkit-transition: all 0.2s cubic-bezier(0.12, 0.4, 0.29, 1.46) 0.1s;
    transition: all 0.2s cubic-bezier(0.12, 0.4, 0.29, 1.46) 0.1s;
    content: ' ';
  }

  /deep/ .ant-alert-error {
    background-color: transparent;
    border: 1px solid rgba(255,163,158, 0.85);
    color: #ffffff;
  }

  /deep/ .ant-alert-message {
    color: rgba(255,163,158, 0.85);
  }

  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
    font-size: 12px;
    color: #ffffff;
    background-color: transparent;
    border: 1px solid #1890ff!important;
    position: relative;
    overflow: hidden;
  }

  .getCaptcha:hover {
    border: 1px solid #40a9ff!important;
  }

  .forge-password {
    font-size: 14px;
  }

  .v-code-img {
    height: 40px;
    float: right;
    width: 98%;
    border-radius: 1px;
    border: 1px solid #40a9ff !important;
    cursor: pointer;
    opacity: 0.8;
    filter: alpha(opacity=60);
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
    background-color: transparent;
    border: 1px solid #40a9ff!important;
    position: relative;
    overflow: hidden;
  }

  button.login-button:hover {
    box-shadow: 1px 1px 25px 10px rgba(64,169,255, 0.4)!important;
  }

  button.login-button:before {
    content: "";
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(
      120deg,
      transparent,
      rgba(64,169,255, 0.4),
      transparent
    );
    transition: all 650ms;
  }

  button.login-button:hover:before {
    left: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 18px;
      color: rgba(255, 255, 255, 0.8);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff!important;
      }
    }

    span{
      color: #ffffff;
    }

    .register {
      float: right;
    }
  }
}

  /deep/ .verifybox {
    position: relative;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    border-radius: 2px;
    border: 1px solid #40a9ff!important;
    background-color: rgba(16,69,94,.95);
    -webkit-box-shadow: 0 0 10px rgba(2,41,82,.85);
    box-shadow: 0 0 10px rgba(2,41,82,.3);
    left: 50%;
    top: 50%;
    -webkit-transform: translate(-50%,-50%);
    transform: translate(-50%,-50%);

      .verify-refresh{
        color: #1890ff;
      }

      .verify-refresh:hover{
        color: #40a9ff;
      }

      overflow: hidden;
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

      @keyframes btn-anim1 {
        0% {
          left: -100%;
        }
        50%,100% {
          left: 100%;
        }
      }

      .slid-span:nth-child(2) {
        top: -100%;
        right: 0;
        width: 2px;
        height: 100%;
        background: linear-gradient(180deg, transparent, #03e9f4);
        animation: btn-anim2 1s linear infinite;
        animation-delay: .25s
      }

      @keyframes btn-anim2 {
        0% {
          top: -100%;
        }
        50%,100% {
          top: 100%;
        }
      }

      .slid-span:nth-child(3) {
        bottom: 0;
        right: -100%;
        width: 100%;
        height: 2px;
        background: linear-gradient(270deg, transparent, #03e9f4);
        animation: btn-anim3 1s linear infinite;
        animation-delay: .5s
      }

      @keyframes btn-anim3 {
        0% {
          right: -100%;
        }
        50%,100% {
          right: 100%;
        }
      }

      .slid-span:nth-child(4) {
        bottom: -100%;
        left: 0;
        width: 2px;
        height: 100%;
        background: linear-gradient(360deg, transparent, #03e9f4);
        animation: btn-anim4 1s linear infinite;
        animation-delay: .75s
      }

      @keyframes btn-anim4 {
        0% {
          bottom: -100%;
        }
        50%,100% {
          bottom: 100%;
        }
      }
  }

  /deep/ .verifybox-top {
    background: rgba(2,41,82,.8);
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

  /deep/ .verifybox-close {
    position: absolute;
    color: #1890ff;
    top: 1px;
    right: 9px;
    width: 24px;
    height: 24px;
    text-align: center;
    cursor: pointer;
  }

  /deep/ .verifybox-close:hover{
     color: #40a9ff;
  }

  /deep/ .verify-bar-area {
    position: relative;
    // background: rgb(146, 148, 248,.3);
    background: transparent;
    text-align: center;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    border-radius: 0px;
    border: 1px solid rgb(146, 148, 248, .3);
  }

  /deep/ .verify-img-panel {
    margin: 0;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    border: 1px solid rgb(146, 148, 248, .3);
    border-radius: 0px;
    position: relative;
    // padding: 0 1px;
 }

  /deep/ .verify-bar-area .verify-left-bar {
      position: absolute;
      top: -1px;
      left: -1px;
      background: transparent;
      cursor: pointer;
      -webkit-box-sizing: content-box;
      box-sizing: content-box;
      border: 1px solid rgb(146, 148, 248, .3)!important;
    }

  // /deep/ .verify-bar-area .verify-left-bar:hover {
  //   box-shadow: 1px 1px 25px 10px rgba(146, 148, 248, 0.4);
  // }

  /deep/ .verify-bar-area .verify-left-bar:hover {
  background: rgba(146, 148, 248, 0.4);
  color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(146, 148, 248, 0.4),
              0 0 25px rgba(146, 148, 248, 0.4),
              0 0 50px rgba(146, 148, 248, 0.4),
              0 0 100px rgba(146, 148, 248, 0.4);
}

  /deep/ .verify-bar-area .verify-move-block {
    position: absolute;
    top: 0px;
    left: 0;
    background: transparent!important;
    cursor: pointer;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    -webkit-box-shadow: 0 0 2px #40a9ff;
    box-shadow: 0 0 2px #40a9ff;
  }

  /deep/ .verify-bar-area .verify-move-block .verify-icon {
    font-size: 18px;
    color: rgb(146, 148, 248, .9)!important;
  }
</style>
