<template>
  <div>
  </div>
</template>
<script>
import { socialLoginCallback } from '@/api/login'
import { mapActions } from 'vuex'
export default {
  name: 'SocialCallback',
  created () {
    this.$loading.show({ tip: '登录中......' })
    const query = this.$route.query
    const socialType = this.$route.params.socialType
    this.socialCallback(socialType, query)
  },
  methods: {
    ...mapActions(['Login']),
    getUrlKey: function (name) {
      let redirect = '/'
      if (window.opener) {
        redirect = window.opener.location.href
      }
      // eslint-disable-next-line no-sparse-arrays
      return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(redirect) || [, ''])[1].replace(/\+/g, '%20')) || null
    },
    socialCallback (socialType, parameter) {
      const that = this
      socialLoginCallback(socialType, parameter).then(res => {
        that.$loading.hide()
        const bindResult = res.data
        if (bindResult && bindResult !== '') {
          if (bindResult.success && bindResult.data) {
            // 授权后发现已绑定，那么直接调用第三方登录
            this.socialLogin(bindResult.data)
          } else if (bindResult.code === 601) {
            // 授权后没有绑定则跳转到绑定界面
            that.$router.push({ name: 'socialBind', query: { redirect: this.getUrlKey('redirect'), key: bindResult.data } })
          } else if (bindResult.code === 602) {
            // 该账号已绑定多个账号，请联系系统管理员，或者到个人中心解绑
            this.$notification['error']({
              message: '错误',
              description: ((res.response || {}).data || {}).message || '该账号已绑定多个账号，请联系系统管理员，或者到个人中心解绑',
              duration: 4
            })
          } else {
            // 提示获取第三方登录失败
            this.$notification['error']({
              message: '错误',
              description: '第三方登录失败，请稍后再试',
              duration: 4
            })
          }
        } else {
          // 提示获取第三方登录失败
            this.$notification['error']({
              message: '错误',
              description: '第三方登录失败，请稍后再试',
              duration: 4
            })
        }
      })
    },
    // 第三方登录后回调
    socialLogin (key) {
      const { Login } = this
      // 执行登录操作
      const loginParams = {
        grant_type: 'social',
        social_key: key
      }
      this.$loading.show({ tip: '登录中......' })
      Login(loginParams)
        .then((res) => this.loginSuccess(res))
        .catch(err => this.loginError(err))
        .finally(() => {
           this.$loading.hide()
           if (this.getUrlKey('redirect')) {
              if (window.opener) {
                window.opener.location.href = window.opener.location.origin + this.getUrlKey('redirect')
              } else {
                window.location.href = window.location.origin + this.getUrlKey('redirect')
              }
           } else {
            if (window.opener) {
                window.opener.location.reload()
              } else {
                window.location.reload()
              }
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
<style>
</style>
