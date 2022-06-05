<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="昵称"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-input :placeholder="$t('user.login.nickname.placeholder')"
                 v-decorator="['nickname', {rules: [ {required: true, message: '请输入昵称'},{ validator: this.validNickName }], validateTrigger: ['change','blur'] }]">
          <a-icon slot="prefix"
                  type="user"/>
        </a-input>
      </a-form-item>
      <a-popover
        placement="rightTop"
        :trigger="['focus']"
        :getPopupContainer="(trigger) => trigger.parentElement"
        v-model="state.passwordLevelChecked">
        <template slot="content">
          <div :style="{ width: '240px' }" >
            <div :class="['user-register', passwordLevelClass]">{{ $t(passwordLevelName) }}</div>
            <a-progress :percent="state.percent" :showInfo="false" :strokeColor=" passwordLevelColor " />
            <div style="margin-top: 10px;">
              <span>{{ $t('user.register.password.popover-message') }}
              </span>
            </div>
          </div>
        </template>
        <a-form-item
          label="登录密码"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          class="stepFormText"
        >
          <a-input-password
            type="password"
            @focus="()=>{this.state.passwordLevelChecked = true}"
            @blur="()=>{this.state.passwordLevelChecked = false}"
            :placeholder="$t('user.login.password.placeholder')"
            :max-length="64"
            v-decorator="['password', { rules: [{required: true, message: '请输入登录密码'},{ validator: this.handlePasswordLevel }], validateTrigger: ['change','blur'] }]" >
            <a-icon slot="prefix"
                    type="lock"/>
          </a-input-password>
        </a-form-item>
      </a-popover>
      <a-form-item
        label="确认密码"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        class="stepFormText"
      >
        <a-input-password
          type="password"
          :max-length="64"
          :placeholder="$t('user.login.password.twice.placeholder')"
          v-decorator="['rePassword', { rules: [{required: true, message: '请输入确认密码'}, { validator: this.handlePasswordCheck }], validateTrigger: ['change','blur'] }]" >
          <a-icon slot="prefix"
                  type="lock"/>
        </a-input-password>
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button :loading="loading" type="primary" @click="nextStep" style="float:right;">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import { scorePassword } from '@/utils/util'
import { checkUserExist } from '@/api/login'
const levelNames = {
  0: 'user.password.strength.short',
  1: 'user.password.strength.low',
  2: 'user.password.strength.medium',
  3: 'user.password.strength.strong'
}
const levelClass = {
  0: 'error',
  1: 'error',
  2: 'warning',
  3: 'success'
}
const levelColor = {
  0: '#ff0000',
  1: '#ff0000',
  2: '#ff7e05',
  3: '#52c41a'
}

export default {
  name: 'RegisterInfo',
  data () {
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      form: this.$form.createForm(this),
      loading: false,
      timer: 0,
      state: {
        level: 0,
        passwordLevel: 0,
        passwordLevelChecked: false,
        percent: 10,
        progressColor: '#FF0000',
        submitBtn: false
      }
    }
  },
  computed: {
    passwordLevelClass () {
      return levelClass[this.state.passwordLevel]
    },
    passwordLevelName () {
      return levelNames[this.state.passwordLevel]
    },
    passwordLevelColor () {
      return levelColor[this.state.passwordLevel]
    }
  },
  methods: {
    nextStep (e) {
      // e.preventDefault()
      const that = this
      const { form: { validateFields },
        state } = this
      that.loading = true
      state.submitBtn = true
      const validateFieldsKey = ['nickname', 'password', 'rePassword']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          const registerParams = { ...values }
          that.setUserInfo(registerParams)
          that.submitRegister()
        } else {
          setTimeout(() => {
            that.releaseLoading()
          }, 600)
        }
      })
    },
    prevStep () {
      this.$emit('prevStep')
    },
    setUserInfo (params) {
      this.$emit('setUserInfo', params)
    },
    submitRegister () {
      this.$emit('submitRegister')
    },
    releaseLoading () {
        this.loading = false
        this.state.submitBtn = false
    },
    handlePasswordLevel (rule, value, callback) {
      if (value === '' || value === undefined) {
       return callback()
      }
      if (value.length >= 6) {
        if (scorePassword(value) >= 30) {
          this.state.level = 1
        }
        if (scorePassword(value) >= 60) {
        this.state.level = 2
        }
        if (scorePassword(value) >= 80) {
        this.state.level = 3
        }
      } else {
        this.state.level = 0
        callback(new Error(this.$t('user.password.strength.msg')))
      }
      this.state.passwordLevel = this.state.level
      this.state.percent = this.state.level * 33
      callback()
    },
    handlePasswordCheck (rule, value, callback) {
      const password = this.form.getFieldValue('password')
      if (value === undefined || value === '') {
        return callback()
      }
      if (value && password && value.trim() !== password.trim()) {
        callback(new Error(this.$t('user.password.twice.msg')))
      }
      callback()
    },
    validNickName (rule, value, callback) {
      if (this.form.getFieldValue('nickname') && value && value !== '') {
        var params = {
          mobile: this.form.getFieldValue('nickname')
        }
        checkUserExist(params).then(response => {
          if (!response.data) {
              callback(new Error('该昵称已存在'))
            } else {
              callback()
            }
        })
      } else {
        callback()
      }
    }
  },
  beforeDestroy () {
    clearTimeout(this.timer)
  }
}
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
