<template>
  <div>
    <a-dropdown v-if="currentUser && currentUser.name" placement="bottomRight">
      <span class="ant-pro-account-avatar">
        <a-avatar size="small" :src="currentUser.avatar" class="antd-pro-global-header-index-avatar" />
        <span>{{ currentUser.name }}</span>
      </span>
      <template v-slot:overlay>
        <a-menu class="ant-pro-drop-down menu" :selected-keys="[]">
          <!-- <a-menu-item v-if="menu" key="center" @click="handleToCenter">
            <a-icon type="user" />
            {{ $t('menu.account.center') }}
          </a-menu-item>
          <a-menu-item v-if="menu" key="settings" @click="handleToSettings">
            <a-icon type="setting" />
            {{ $t('menu.account.settings') }}
          </a-menu-item>
          <a-menu-divider v-if="menu" /> -->
          <a-menu-item v-if="menu" key="settings" @click="handleUpdatePwd">
            <a-icon type="setting" />
            修改密码
          </a-menu-item>
          <a-menu-divider v-if="menu" />
          <a-menu-item key="logout" @click="handleLogout">
            <a-icon type="logout" />
            {{ $t('menu.account.logout') }}
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <span v-else>
      <a-spin size="small" :style="{ marginLeft: 8, marginRight: 8 }" />
    </span>
    <a-modal v-model="dialogFormVisible" title="修改密码" width="25%" :maskClosable="false">
      <a-form-model
        ref="changePwdForm"
        :model="changePwdForm"
        :rules="rules"
        class="changePwdForm"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        style="width: 400px;">
        <a-form-model-item label="旧密码" prop="oldPwd">
          <a-input v-model="changePwdForm.oldPwd" placeholder="输入旧密码" maxlength="32" type="password"/>
        </a-form-model-item>
        <a-form-model-item label="新密码" prop="newPwd">
          <a-input v-model="changePwdForm.newPwd" placeholder="输入新密码" maxlength="32" type="password"/>
        </a-form-model-item>
        <a-form-model-item label="新密码" prop="newPwdRe">
          <a-input v-model="changePwdForm.newPwdRe" placeholder="请再次输入新密码" maxlength="32" type="password"/>
        </a-form-model-item>
      </a-form-model>
      <div slot="footer" class="dialog-footer">
        <a-button @click="dialogFormVisible = false">取消</a-button>
        <a-button type="primary" @click="updatePwdData">确认</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { Modal } from 'ant-design-vue'
import { updatePwd } from '@/api/system/user'

export default {
  name: 'AvatarDropdown',
  props: {
    currentUser: {
      type: Object,
      default: () => null
    },
    menu: {
      type: Boolean,
      default: true
    }
  },
  data () {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.changePwdForm.newPwdRe !== '') {
          this.$refs.changePwdForm.validateField('newPwdRe')
        }
        callback()
      }
    }
    var validatePassRe = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.changePwdForm.newPwd) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 }
      },
      dialogFormVisible: false,
      changePwdForm: {
        oldPwd: '',
        newPwd: '',
        newPwdRe: ''
      },
      rules: {
        oldPwd: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
          { min: 6, max: 32, message: '长度在 6 到 32 个字符', trigger: 'blur' }
        ],
        newPwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          {
            min: 6,
            max: 32,
            message: '长度在 6 到 32 个字符',
            trigger: 'blur'
          },
          { validator: validatePass, trigger: 'blur' }
        ],
        newPwdRe: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          {
            min: 6,
            max: 32,
            message: '长度在 6 到 32 个字符',
            trigger: 'blur'
          },
          { validator: validatePassRe, trigger: 'blur' }
        ]
      },
      downloadLoading: false,
      userAccount: '',
      headImgUrl: ''
    }
  },
  methods: {
    handleToCenter () {
      this.$router.push({ path: '/account/center' })
    },
    handleToSettings () {
      this.$router.push({ path: '/account/settings' })
    },
    handleLogout (e) {
      Modal.confirm({
        title: this.$t('layouts.usermenu.dialog.title'),
        content: this.$t('layouts.usermenu.dialog.content'),
        onOk: () => {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1500)
          // }).catch(() => console.log('Oops errors!'))
          return this.$store.dispatch('Logout', new Date()).then(() => {
            this.$router.push({ name: 'login' })
            window.location.reload()
          })
        },
        onCancel () {}
      })
    },
    resetUpdatePwdForm () {
      this.changePwdForm = {
        oldPwd: '',
        newPwd: '',
        newPwdRe: ''
      }
    },
    handleUpdatePwd () {
      this.resetUpdatePwdForm()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['changePwdForm'].clearValidate()
      })
    },
    updatePwdData () {
      this.$refs['changePwdForm'].validate(valid => {
        if (valid) {
          updatePwd(this.changePwdForm).then(() => {
            this.dialogFormVisible = false
            Modal.confirm({
                  title: '密码修改成功',
                  content: '密码修改成功，请使用新密码重新登录！',
                  onOk: () => {
                    return this.$store.dispatch('Logout', new Date()).then(() => {
                      this.$router.push({ name: 'login' })
                      window.location.reload()
                    })
                  },
                  onCancel () {
                     return this.$store.dispatch('Logout', new Date()).then(() => {
                      this.$router.push({ name: 'login' })
                      window.location.reload()
                    })
                  }
                })
          })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.ant-pro-drop-down {
  /deep/ .action {
    margin-right: 8px;
  }
  /deep/ .ant-dropdown-menu-item {
    min-width: 160px;
  }
}
</style>
