<template>
  <div>
    <a-form>
      <a-result title="密码修改成功" :is-success="true" :sub-title="count + 's ' + successText" style="max-width: 560px; margin: 40px auto 0;">
        <div class="information">
          <a-row>
            <a-col :sm="8" :xs="24">手机号：</a-col>
            <a-col :sm="16" :xs="24">{{ phoneNumber }}</a-col>
          </a-row>
        </div>
        <template #extra>
          <a-button style="margin-left: 8px" @click="goPage">立即跳转</a-button>
        </template>
      </a-result>
    </a-form>
  </div>
</template>

<script>
import md5 from 'md5'
import { mapActions } from 'vuex'
export default {
  name: 'RecoverSuccess',
  data () {
    return {
      loading: false,
      successText: '后跳转到登录前的页面',
      phoneNumber: '',
      timer: undefined,
      count: 10,
      show: true
    }
  },
  methods: {
    ...mapActions(['Login', 'Logout']),
    getUrlKey: function (name) {
       // eslint-disable-next-line no-sparse-arrays
       return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(window.opener.location.href) || [, ''])[1].replace(/\+/g, '%20')) || null
    },
    successChangePassword (params) {
      this.phoneNumber = params.mobile
      this.successLogin(params)
    },
    // 注册成功之后，执行登录并跳转到登录前的界面
    successLogin (params) {
      const { Login } = this
      const loginParams = {
        grant_type: 'password',
        username: params.mobile,
        password: md5(params.password)
      }
      // 正常情况下，是直接登录跳转到之前的页面，如果发生错误，则提示跳转到登录页
      Login(loginParams)
        .then((res) => this.loginSuccess(res))
        .catch(err => this.loginError(err))
        .finally(() => {
          this.timerGo()
        })
    },
    loginSuccess (res) {
      this.successText = '后跳转到登录前的页面'
    },
    loginError (res) {
      this.successText = '后跳转到登录页面,请使用手机号+密码进行登录'
    },
    // 10秒后进入跳转页面
    timerGo () {
        const TIME_COUNT = 10
        if (!this.timer) {
        this.count = TIME_COUNT
        this.show = false
        this.timer = setInterval(() => {
        if (this.count > 0 && this.count <= TIME_COUNT) {
          this.count--
        } else {
          this.show = true
          clearInterval(this.timer)
          this.timer = undefined
          this.goPage()
        }
      }, 1000)
      }
    },
    goPage () {
      if (window.opener && this.getUrlKey('redirect')) {
        window.opener.location.href = window.opener.location.origin + this.getUrlKey('redirect')
      } else {
        window.location.href = window.location.origin + (this.$route.query.redirect || '')
      }
      window.close()
    }
  }
}
</script>
<style lang="less" scoped>
  .information {
    line-height: 22px;

    .ant-row:not(:last-child) {
      margin-bottom: 24px;
    }
  }
  .money {
    font-family: "Helvetica Neue",sans-serif;
    font-weight: 500;
    font-size: 20px;
    line-height: 14px;
  }
</style>
