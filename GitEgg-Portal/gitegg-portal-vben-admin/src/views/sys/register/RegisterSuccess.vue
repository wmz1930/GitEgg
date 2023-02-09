<template>
  <div>
    <a-form>
      <a-result
        title="注册成功"
        :is-success="true"
        :sub-title="count + 's ' + successText"
        style="max-width: 560px; margin: 40px auto 0"
      >
        <div class="information">
          <a-row>
            <a-col :sm="8" :xs="24">昵称：</a-col>
            <a-col :sm="16" :xs="24">{{ nickname }}</a-col>
          </a-row>
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

<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { Card, Form, Input, Row, Col, Button, Steps, Result } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useLoading } from '/@/components/Loading';
  import { useUserStore } from '/@/store/modules/user';

  import { encryptByMd5 } from '/@/utils/cipher';
  export default defineComponent({
    name: 'LoginForm',
    components: {
      [Col.name]: Col,
      [Row.name]: Row,
      [Steps.name]: Steps,
      [Steps.Step.name]: Steps.Step,
      [Result.name]: Result,
      AForm: Form,
    },
    emits: ['bind-login'],
    setup(_, {}) {
      const { t } = useI18n();
      const userStore = useUserStore();

      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '登录中......',
      });

      const timer = ref();
      const count = ref(10);
      const show = ref(true);
      const nickname = ref('');
      const phoneNumber = ref('');
      const successText = ref('后跳转到登录前的页面');

      function getUrlKey(name) {
        // eslint-disable-next-line no-sparse-arrays
        return (
          decodeURIComponent(
            (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(
              window.opener.location.href,
            ) || [, ''])[1].replace(/\+/g, '%20'),
          ) || null
        );
      }

      function successRegister(params) {
        nickname.value = params.nickname;
        phoneNumber.value = params.mobile;
        successLogin(params);
      }

      // 注册成功之后，执行登录并跳转到登录前的界面
      function successLogin(params) {
        openFullLoading();
        // 正常情况下，是直接登录跳转到之前的页面，如果发生错误，则提示跳转到登录页
        userStore
          .login({
            grant_type: 'password',
            username: params.mobile,
            password: encryptByMd5(params.password),
            mode: 'none', //不要默认的错误提示
          })
          .then(() => loginSuccess())
          .catch(() => loginError())
          .finally(() => {
            closeFullLoading();
            timerGo();
          });
      }

      function loginSuccess() {
        successText.value = '后跳转到登录前的页面';
      }

      function loginError() {
        successText.value = '后跳转到登录页面,请使用手机号+密码进行登录';
      }

      // 10秒后进入跳转页面
      function timerGo() {
        const TIME_COUNT = 10;
        if (!timer.value) {
          count.value = TIME_COUNT;
          show.value = false;
          timer.value = setInterval(() => {
            if (count.value > 0 && count.value <= TIME_COUNT) {
              count.value--;
            } else {
              show.value = true;
              clearInterval(timer.value);
              timer.value = undefined;
              goPage();
            }
          }, 1000);
        }
      }

      function goPage() {
        if (getUrlKey('redirect')) {
          window.opener.location.href = window.opener.location.origin + getUrlKey('redirect');
        } else {
          window.opener.location.reload();
        }
      }

      return {
        t,
        nickname,
        phoneNumber,
        timer,
        count,
        show,
        successText,
        goPage,
        successRegister,
      };
    },
  });
</script>

<style lang="less" scoped>
  .information {
    line-height: 22px;

    .ant-row:not(:last-child) {
      margin-bottom: 24px;
    }
  }

  .money {
    font-family: 'Helvetica Neue', sans-serif;
    font-weight: 500;
    font-size: 20px;
    line-height: 14px;
  }
</style>
