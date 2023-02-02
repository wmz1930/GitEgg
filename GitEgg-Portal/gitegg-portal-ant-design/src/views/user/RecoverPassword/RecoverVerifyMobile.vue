<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="手机号"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-input-group
          style="display: inline-block; vertical-align: middle"
          :placeholder="$t('user.login.mobile.placeholder')"
          :compact="true"
        >
          <a-select defaultValue="+86" style="width: 80px">
            <a-select-option value="+86">+86</a-select-option>
          </a-select>
          <a-input
            :style="{width: 'calc(100% - 80px)'}"
            :placeholder="$t('user.login.mobile.placeholder')"
            v-decorator="['phoneNumber', {rules: [{ required: true, pattern: /^1[34578]\d{9}$/, message: $t('user.phone-number.required') }, { validator:validatePhoneNumber, trigger: 'blur' }], validateTrigger: 'change'}]"
          >
            <a-icon slot="prefix"
                    type="mobile"/>
          </a-input>
        </a-input-group>
      </a-form-item>
      <a-form-item v-if="loginCaptchaType === 'image'"
                   label="图片验证码"
                   :labelCol="labelCol"
                   :wrapperCol="wrapperCol"
      >
        <a-input-group
          style="display: inline-block; vertical-align: middle"
          :compact="true"
        >
          <a-input
            :placeholder="$t('user.verification-code.required')"
            :style="{width: 'calc(100% - 100px)', paddingRight: '15px'}"
            class="captcha-input"
            v-decorator="[ 'captchaCodeSms', {rules: [{ required: true, message: $t('user.verification-code.required') }], validateTrigger: 'change'}]">
            >
            <a-icon slot="prefix"
                    type="safety-certificate"/>
          </a-input>
          <img :src="captchaImage"
               class="v-code-img"
               style="width: 100px;display: inline-block;"
               @click="refreshImageCode">
        </a-input-group>
      </a-form-item>
      <a-form-item
        label="短信验证码"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-input-group
          style="display: inline-block; vertical-align: middle"
          :compact="true"
        >
          <a-input
            :placeholder="$t('user.login.mobile.verification-code.placeholder')"
            :style="{width: 'calc(100% - 100px)', paddingRight: '15px'}"
            class="captcha-input"
            :max-length="6"
            v-decorator="['captcha', {rules: [{ required: true, message: $t('user.verification-code.required') }, { validator:validSmsCode, trigger: 'blur' }], validateTrigger: 'blur'}]">
            >
            <a-icon slot="prefix"
                    type="mail"/>
          </a-input>
          <a-button class="getCaptcha"
                    tabindex="-1"
                    style="width: 100px"
                    :disabled="state.smsSendBtn"
                    @click.stop.prevent="captchaVerifySendSms"
                    v-text="!state.smsSendBtn && $t('user.register.get-verification-code') || (state.time +' s')"></a-button>
        </a-input-group>
      </a-form-item>
      <a-form-item :wrapperCol="{span: 24}">
        <a-button type="primary" @click="nextStep" style="float:right;">下一步</a-button>
      </a-form-item>
    </a-form>
    <Verify @success="verifySendSmsSuccess"
            :mode="'pop'"
            :captchaType="slidingCaptchaType"
            :imgSize="{ width: '330px', height: '155px' }"
            ref="verify"></Verify>
  </div>
</template>

<script>
import Verify from '@/components/verifition/Verify'
import { serialize } from '@/utils/util'
import { getCaptchaType, getImageCaptcha, smsPwdSend, checkSmsCode, checkUserExist } from '@/api/login'

export default {
  name: 'RecoverVerifyMobile',
    components: {
    Verify
  },
  data () {
    var validatePhoneNumber = (rule, value, callback) => {
      if (this.form.getFieldValue('phoneNumber') && value && value.length === 11) {
      var params = {
        mobile: this.form.getFieldValue('phoneNumber')
      }
      checkUserExist(params).then(response => {
        if (response.data) {
          callback(new Error('该手机号未注册'))
        } else {
          callback()
        }
      })
      }
    }
    var validSmsCode = (rule, value, callback) => {
      if (this.form.getFieldValue('phoneNumber') && value && value.length === 6) {
        var params = {
          smsCode: 'sms_change_info_code',
          phoneNumber: this.form.getFieldValue('phoneNumber'),
          verificationCode: value
        }
        checkSmsCode(params).then(response => {
          if (!response.data) {
            callback(new Error('验证码错误'))
          } else {
            callback()
          }
        })
      }
    }
    return {
      loginBtn: false,
      state: {
        time: 60,
        loginBtn: false,
        loginType: 0,
        smsSendBtn: false
      },
      interval: 0,
      validSmsCode,
      validatePhoneNumber,
      sendSms: false,
      loginCaptchaType: 'sliding',
      slidingCaptchaType: 'blockPuzzle',
      captchaKey: '',
      captchaCode: '',
      captchaImage: 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAEALAAAAAABAAEAAAICRAEAOw==',
      inputCodeNull: true,
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      form: this.$form.createForm(this)
    }
  },
  created () {
    this.queryCaptchaType()
  },
  methods: {
    checkSmsCode,
    nextStep () {
      const that = this
      const { form: { validateFields } } = that
      // 先校验，通过表单校验后，才进入下一步
      validateFields((err, values) => {
        if (!err) {
          const registerParams = { ...values }
          registerParams.smsCode = 'sms_change_info_code'
          registerParams.code = values.captcha
          that.setMobileSms(registerParams)
          that.$emit('nextStep')
        }
      })
    },
    setMobileSms (params) {
      this.$emit('setMobileSms', params)
    },
    refreshImageCode () {
      getImageCaptcha().then(res => {
        const data = res.data
        this.captchaKey = data.captchaKey
        this.captchaImage = data.captchaImage
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
            state.smsSendBtn = false
            this.$refs.verify.show()
          } else {
            this.verifySendSmsSuccess()
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
          const registerParams = { ...values }
          registerParams.code = values.captcha
          registerParams.smsCode = 'sms_change_info_code'
          // 判断是图片验证码还是滑动验证码
          if (this.loginCaptchaType === 'sliding') {
            registerParams.captcha_type = 'sliding'
            registerParams.sliding_type = this.slidingCaptchaType
            registerParams.captcha_verification = params.captchaVerification
          } else if (this.loginCaptchaType === 'image') {
            registerParams.captcha_type = 'image'
            registerParams.captcha_key = this.captchaKey
            registerParams.captcha_code = values.captchaCodeSms
          }

          this.getCaptcha(registerParams)
        } else {
          state.smsSendBtn = false
        }
      })
    },
    getCaptcha (registerParams) {
      const { state } = this
      state.smsSendBtn = true
      this.interval = window.setInterval(() => {
        if (state.time-- <= 0) {
          state.time = 60
          state.smsSendBtn = false
          window.clearInterval(this.interval)
        }
      }, 1000)

      const hide = this.$message.loading('验证码发送中..', 0)
      smsPwdSend(serialize(registerParams)).then(res => {
        setTimeout(hide, 1)
        if (res.success && res.data) {
            this.$notification['success']({
                  message: '提示',
                  description: '验证码发送成功',
                  duration: 8
            })
          } else {
                state.time = 60
                state.smsSendBtn = false
                window.clearInterval(this.interval)
          }
      }).catch(err => {
          setTimeout(hide, 1)
          clearInterval(this.interval)
          state.time = 60
          state.smsSendBtn = false
          this.requestFailed(err)
      })
    },
    requestFailed (err) {
      this.$notification['error']({
        message: '错误',
        description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
      this.registerBtn = false
    }
  },
  beforeDestroy () {
    window.clearInterval(this.interval)
  }
}
</script>

<style lang="less" scoped>
  .v-code-img {
    height: 32px;
    float: right;
    width: 98%;
    cursor: pointer;
    opacity: 0.8;
    filter: alpha(opacity=60);
  }
  .captcha-input{
    padding-right: '15px';
  }
</style>
