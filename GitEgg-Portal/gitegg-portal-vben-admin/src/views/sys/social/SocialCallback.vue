<template>
  <div> </div>
</template>
<script lang="ts">
  import { defineComponent, unref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { socialLoginCallback } from '/@/api/login';
  import { useLoading } from '/@/components/Loading';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    name: 'LoginForm',
    components: {},
    emits: ['show-verify', 'refresh-image-code'],
    setup(_, {}) {
      const router = useRouter();
      const { currentRoute } = router;
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '登录中......',
      });

      const { notification } = useMessage();
      const userStore = useUserStore();

      // 第三方登录回调
      onMounted(() => {
        openFullLoading();
        const { params, query } = unref(currentRoute);
        const socialType = params.socialType;
        socialCallback(socialType, query);
      });

      function getUrlKey(name) {
        let redirect = '/';
        if (window.opener) {
          redirect = window.opener.location.href;
        }
        // eslint-disable-next-line no-sparse-arrays
        return (
          decodeURIComponent(
            (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(redirect) || [
              ,
              '',
            ])[1].replace(/\+/g, '%20'),
          ) || null
        );
      }

      function socialCallback(socialType, parameter) {
        socialLoginCallback(socialType, parameter).then((res) => {
          closeFullLoading();
          const bindResult = res;
          if (bindResult && bindResult !== '') {
            if (bindResult.success && bindResult.data) {
              // 授权后发现已绑定，那么直接调用第三方登录
              socialLogin(bindResult.data);
            } else if (bindResult.code === 601) {
              // 授权后没有绑定则跳转到绑定界面
              router.push({
                name: 'SocialBind',
                query: { redirect: getUrlKey('redirect'), key: bindResult.data },
              });
            } else if (bindResult.code === 602) {
              // 该账号已绑定多个账号，请联系系统管理员，或者到个人中心解绑
              notification.error({
                message: '错误',
                description:
                  ((res.response || {}).data || {}).message ||
                  '该账号已绑定多个账号，请联系系统管理员，或者到个人中心解绑',
                duration: 4,
              });
            } else {
              // 提示获取第三方登录失败
              notification.error({
                message: '错误',
                description: '第三方登录失败，请稍后再试',
                duration: 4,
              });
            }
          } else {
            // 提示获取第三方登录失败
            notification.error({
              message: '错误',
              description: '第三方登录失败，请稍后再试',
              duration: 4,
            });
          }
        });
      }

      // 第三方登录后回调
      function socialLogin(key) {
        // 执行登录操作
        const loginParams = {
          grant_type: 'social',
          social_key: key,
        };
        openFullLoading();
        userStore
          .login(loginParams)
          .then((response) => loginSuccess(response))
          .catch((err) => loginError(err))
          .finally(() => {
            closeFullLoading();
          });
      }

      function loginSuccess(response) {
        notification.success({
          message: '提示',
          description: '第三方登录成功,欢迎 ' + response.nickname || '',
          duration: 3,
        });
      }

      function loginError(err) {
        notification.error({
          message: '错误',
          description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
          duration: 4,
        });
        if (getUrlKey('redirect')) {
          if (window.opener) {
            window.opener.location.href = window.opener.location.origin + getUrlKey('redirect');
          } else {
            window.location.href = window.location.origin + getUrlKey('redirect');
          }
        } else {
          if (window.opener) {
            window.opener.location.reload();
          } else {
            window.location.reload();
          }
        }
      }

      return {};
    },
  });
</script>
