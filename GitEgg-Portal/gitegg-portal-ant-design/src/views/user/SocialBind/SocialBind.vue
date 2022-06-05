<template>
  <div class="social-bind">
    <a-row class="bind-header">
      <a-col :md="12" :sm="12" style="text-align: center;">
        <img src="~@/assets/logo.svg" class="logo" alt="logo"><span class="title">GitEgg</span><span class="sub-title">联合登录</span>
      </a-col>
      <a-col :md="12" :sm="12" style="text-align: center;">
      </a-col>
    </a-row>
    <div style="clear: both;"></div>
    <a-card :bordered="false">
      <span class="bind-tips">为了给您更好的服务，请绑定已有账号，便于您下次快速登录</span>
      <a-steps class="steps"
               v-model="currentTab"
               type="navigation"
               size="small"
               :style="stepStyle">
        <a-step status="finish" title="短信验证码绑定" ><a-icon slot="icon" type="mobile"/></a-step>
        <a-step status="finish" title="账号密码绑定" ><a-icon slot="icon" type="user"/></a-step>
      </a-steps>
      <div class="content">
        <mobile-bind v-show="currentTab === 0" @bindLogin="bindLogin"/>
        <account-bind ref="registerInfo"
                      v-show="currentTab === 1"
                      @bindLogin="bindLogin"/>
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
import { mapActions } from 'vuex'
import MobileBind from './MobileBind'
import AccountBind from './AccountBind'
export default {
  name: 'RegisterForm',
  components: {
    MobileBind,
    AccountBind
  },
  data () {
    return {
      currentTab: 0,
      stepStyle: {
        marginBottom: '60px',
        boxShadow: '0px -1px 0 0 #e8e8e8 inset'
      }
    }
  },
  methods: {
    ...mapActions(['Login', 'Logout']),
    getUrlKey: function (name) {
       // eslint-disable-next-line no-sparse-arrays
       return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(window.opener.location.href) || [, ''])[1].replace(/\+/g, '%20')) || null
    },
    // 绑定成功之后，执行登录并跳转到登录前的界面
    bindLogin () {
      const { Login } = this
      this.$loading.show({ tip: '登录中......' })
      const loginParams = {
        grant_type: 'social',
        social_key: this.$route.query.key
      }
      // 正常情况下，是直接登录跳转到之前的页面，如果发生错误，则提示跳转到登录页
      Login(loginParams)
        .then((res) => this.loginSuccess(res))
        .catch(err => this.loginError(err))
        .finally(() => {
           this.$loading.hide()
           if (this.getUrlKey('redirect')) {
              window.opener.location.href = window.opener.location.origin + this.getUrlKey('redirect')
           } else {
              window.opener.location.reload()
           }
           window.close()
        })
    },
    loginSuccess (res) {
      this.$notification['success']({
         message: '提示',
         description: '第三方登录成功',
         duration: 4
      })
    },
    loginError (err) {
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
    max-width: 600px;
    margin: 16px auto;
  }
  .logo {
    position: relative;
    height: 45px;
    margin-right: 10px;
    border-style: none;
  }
  .bind-header{
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
  .bind-tips {
    text-align: center;
    width:100%;
    display: inline-block;
    color: #52c41a;
  }
  .social-bind .ant-steps-navigation .ant-steps-item::after {
    border: 0px solid rgba(0, 0, 0, 0.25)!important;
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
