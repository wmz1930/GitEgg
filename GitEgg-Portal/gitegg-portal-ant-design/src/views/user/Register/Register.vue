<template>
  <div>
    <a-row class="register-header">
      <a-col :md="12" :sm="12" style="text-align: center;">
        <img src="~@/assets/logo.svg" class="logo" alt="logo"><span class="title">GitEgg</span><span class="sub-title">欢迎注册</span>
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
        <a-step title="填写账号信息" />
        <a-step title="完成" />
      </a-steps>
      <div class="content">
        <register-mobile v-show="currentTab === 0" @nextStep="nextStep" @setMobileSms="setMobileSms"/>
        <register-info ref="registerInfo"
                       v-show="currentTab === 1"
                       @nextStep="nextStep"
                       @prevStep="prevStep"
                       @setUserInfo="setUserInfo"
                       @submitRegister="submitRegister"/>
        <register-success ref="registerSuccess"
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
import RegisterMobile from './RegisterMobile'
import RegisterInfo from './RegisterInfo'
import RegisterSuccess from './RegisterSuccess'
import { userRegister } from '@/api/login'
export default {
  name: 'RegisterForm',
  components: {
    RegisterMobile,
    RegisterInfo,
    RegisterSuccess
  },
  data () {
    return {
      currentTab: 0,
      formRegister: {
        nickname: undefined,
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
      this.formRegister = {
        nickname: undefined,
        mobile: undefined,
        password: undefined,
        smsCode: undefined,
        code: undefined
      }
    },
    setMobileSms (params) {
      this.formRegister.mobile = params.phoneNumber
      this.formRegister.smsCode = params.smsCode
      this.formRegister.code = params.code
    },
    setUserInfo (params) {
      this.formRegister.nickname = params.nickname
      this.formRegister.password = params.password
    },
    submitRegister () {
      userRegister(this.formRegister).then(res => {
        this.$refs.registerInfo.releaseLoading()
        this.$refs.registerSuccess.successRegister(this.formRegister)
        this.nextStep()
      }).catch(err => {
        this.$refs.registerInfo.releaseLoading()
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
  .register-header{
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
