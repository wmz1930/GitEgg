<template>
  <div>
    <a-row class="recover-header">
      <a-col :md="12" :sm="12" style="text-align: center;">
        <img src="~@/assets/logo.svg" class="logo" alt="logo"><span class="title">GitEgg</span><span class="sub-title">找回密码</span>
      </a-col>
      <a-col :md="12" :sm="12" style="text-align: center;">
        <span class="has-account">已有账号？</span>
        <router-link :to="{ name: 'login', query: { redirect: this.$route.query.redirect } }"
                     class="to-login">请登录>> </router-link>
      </a-col>
    </a-row>
    <div style="clear: both;"></div>
    <a-card :bordered="false">
      <a-steps class="steps" :current="currentTab">
        <a-step title="验证手机号" />
        <a-step title="填写新密码" />
        <a-step title="完成" />
      </a-steps>
      <div class="content">
        <recover-verify-mobile v-show="currentTab === 0" @nextStep="nextStep" @setMobileSms="setMobileSms"/>
        <recover-password ref="RecoverPassword"
                          v-show="currentTab === 1"
                          @nextStep="nextStep"
                          @prevStep="prevStep"
                          @setUserInfo="setUserInfo"
                          @submitUpdatePwd="submitUpdatePwd"/>
        <recover-success ref="RecoverSuccess"
                         v-show="currentTab === 2"
                         @prevStep="prevStep"
                         @finish="finish"/>
      </div>
    </a-card>
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
        Copyrights &copy; 2021 GitEgg All Rights Reserved.   苏ICP备15017208号-1
      </div>
    </div>

  </div>
</template>

<script>
import RecoverVerifyMobile from './RecoverVerifyMobile'
import RecoverPassword from './RecoverPassword'
import RecoverSuccess from './RecoverSuccess'
import { smsChangePassword } from '@/api/login'
export default {
  name: 'Recover',
  components: {
    RecoverVerifyMobile,
    RecoverPassword,
    RecoverSuccess
  },
  data () {
    return {
      currentTab: 0,
      formUpdatePwd: {
        mobile: undefined,
        password: undefined,
        smsCode: undefined,
        code: undefined
      }
    }
  },
  methods: {
    nextStep () {
      if (this.currentTab < 2) {
        this.currentTab += 1
      }
    },
    prevStep () {
      if (this.currentTab > 0) {
        this.currentTab -= 1
      }
    },
    finish () {
      this.currentTab = 0
    },
    resetForm () {
      this.formUpdatePwd = {
        mobile: undefined,
        password: undefined,
        smsCode: undefined,
        code: undefined
      }
    },
    setMobileSms (params) {
      this.formUpdatePwd.mobile = params.phoneNumber
      this.formUpdatePwd.smsCode = params.smsCode
      this.formUpdatePwd.code = params.code
    },
    setUserInfo (params) {
      this.formUpdatePwd.password = params.password
    },
    submitUpdatePwd () {
      smsChangePassword(this.formUpdatePwd).then(res => {
        this.$refs.RecoverPassword.releaseLoading()
        this.$refs.RecoverSuccess.successChangePassword(this.formUpdatePwd)
        this.nextStep()
      }).catch(err => {
        this.$refs.RecoverPassword.releaseLoading()
        this.requestFailed(err)
      })
    },
    requestFailed (err) {
      this.$notification['error']({
        message: '错误',
        description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
  .logo {
    position: relative;
    height: 45px;
    margin-right: 10px;
    border-style: none;
  }
  .recover-header{
    height: 100px;
    line-height: 100px;
    -webkit-box-shadow: rgba(177, 175, 175, 0.4) 0px 0px 10px 1px;
    box-shadow: rgba(177, 175, 175,.4) 0px 0px 10px 1px;
  }
  .title, .sub-title {
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
  .has-account,.to-login {
    font-size: 16px;
    color: #999;
    margin-top:10px;
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
