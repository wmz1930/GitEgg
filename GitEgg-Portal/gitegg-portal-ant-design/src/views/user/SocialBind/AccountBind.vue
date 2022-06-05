<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="账号"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-input :placeholder="$t('user.login.account.placeholder')"
                 v-decorator="['username', {rules: [ {required: true, message: '请输入账号/手机号/邮箱地址'}], validateTrigger: ['change'] }]">
          <a-icon slot="prefix"
                  type="user"/>
        </a-input>
      </a-form-item>
      <a-form-item
        label="登录密码"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        class="stepFormText"
      >
        <a-input-password
          type="password"
          :placeholder="$t('user.login.password.placeholder')"
          :max-length="64"
          v-decorator="['password', { rules: [{required: true, message: '请输入登录密码'}], validateTrigger: ['change','blur'] }]" >
          <a-icon slot="prefix"
                  type="lock"/>
        </a-input-password>
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button :loading="loading" type="primary" @click="bindAccount" style="float:right;">同意协议并绑定</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import { userBindAccount } from '@/api/login'
export default {
  name: 'RegisterInfo',
  data () {
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      form: this.$form.createForm(this),
      loading: false
    }
  },
  methods: {
    bindAccount (e) {
      e.preventDefault()
      const that = this
      const { form: { validateFields } } = this
      that.loading = true
      const validateFieldsKey = ['username', 'password']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          const bindParams = { ...values }
          bindParams.username = values.username
          bindParams.password = values.password
          bindParams.socialKey = this.$route.query.key
          // 执行绑定操作
          userBindAccount(bindParams).then(res => {
            // 绑定成功后，调用第三方登录方法
            this.$emit('bindLogin')
          }).catch(err => {
            that.releaseLoading()
            that.requestFailed(err)
          })
        } else {
          setTimeout(() => {
            that.releaseLoading()
          }, 600)
        }
      })
    },
    requestFailed (err) {
      this.$notification['error']({
        message: '错误',
        description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
    },
    releaseLoading () {
        this.loading = false
    }
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
