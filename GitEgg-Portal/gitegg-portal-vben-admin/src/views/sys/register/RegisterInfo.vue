<template>
  <div>
    <Form class="p-4 enter-x" :model="formData" :rules="formRules" ref="formRef">
      <FormItem label="昵称" name="nickname" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <Input
          size="large"
          v-model:value="formData.nickname"
          :maxlength="16"
          placeholder="请输入昵称"
          class="fix-auto-fill"
        >
          <template #prefix>
            <icon icon="ant-design:user-outlined" />
          </template>
        </Input>
      </FormItem>
      <a-popover placement="rightTop" trigger="focus" v-model:visible="state.passwordLevelChecked">
        <template #content>
          <div :style="{ width: '240px' }">
            <div :class="['user-register', passwordLevelClass]">{{ t(passwordLevelName) }}</div>
            <a-progress
              :percent="state.percent"
              :showInfo="false"
              :strokeColor="passwordLevelColor"
            />
            <div style="margin-top: 10px">
              <span>{{ t('sys.register.passwordPopoverMessage') }} </span>
            </div>
          </div>
        </template>
        <FormItem
          label="登录密码"
          name="password"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          class="stepFormText"
        >
          <InputPassword
            type="password"
            size="large"
            @focus="passwordLevelShow(true)"
            @blur="passwordLevelShow(false)"
            :max-length="64"
            v-model:value="formData.password"
            :placeholder="t('sys.register.passwordPlaceholder')"
          >
            <template #prefix>
              <icon icon="ant-design:lock-outlined" />
            </template>
          </InputPassword>
        </FormItem>
      </a-popover>
      <FormItem
        label="确认密码"
        name="rePassword"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        class="stepFormText"
      >
        <InputPassword
          type="password"
          size="large"
          :max-length="64"
          v-model:value="formData.rePassword"
          :placeholder="t('sys.register.confirmPasswordPlaceholder')"
        >
          <template #prefix>
            <icon icon="ant-design:lock-outlined" />
          </template>
        </InputPassword>
      </FormItem>
      <FormItem :wrapperCol="{ span: 19, offset: 5 }">
        <a-button :loading="loading" type="primary" @click="nextStep" style="float: right"
          >提交</a-button
        >
      </FormItem>
    </Form>
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive, ref, computed } from 'vue';
  import { Checkbox, Form, Input, Row, Col, Button, Progress, Popover } from 'ant-design-vue';
  import type { RuleObject } from 'ant-design-vue/lib/form/interface';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDesign } from '/@/hooks/web/useDesign';

  import { scorePassword } from '/@/utils';
  import { checkUserExist } from '/@/api/login';

  export default defineComponent({
    name: 'LoginForm',
    components: {
      [Col.name]: Col,
      [Row.name]: Row,
      Checkbox,
      Button,
      Form,
      FormItem: Form.Item,
      Input,
      AProgress: Progress,
      APopover: Popover,
      InputPassword: Input.Password,
      Icon,
    },
    emits: ['prev-step', 'set-user-info', 'submit-register'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { prefixCls } = useDesign('login');

      const formRef = ref();
      const loading = ref(false);

      const state = reactive({
        level: 0,
        passwordLevel: 0,
        passwordLevelChecked: false,
        percent: 10,
        progressColor: '#FF0000',
        submitBtn: false,
      });

      const labelCol = { lg: { span: 5 }, sm: { span: 5 } };
      const wrapperCol = { lg: { span: 19 }, sm: { span: 19 } };

      const handlePasswordLevel = async (_: RuleObject, value: string) => {
        if (value === '' || value === undefined) {
          return Promise.resolve();
        }
        if (value.length >= 8) {
          if (scorePassword(value) >= 30) {
            state.level = 1;
          }
          if (scorePassword(value) >= 60) {
            state.level = 2;
          }
          if (scorePassword(value) >= 80) {
            state.level = 3;
          }
        } else {
          state.level = 0;
          return Promise.reject(new Error(t('sys.register.userPasswordStrengthMsg')));
        }
        state.passwordLevel = state.level;
        state.percent = state.level * 33;
        return Promise.resolve();
      };

      const handlePasswordCheck = async (_: RuleObject, value: string) => {
        const password = formData.password;
        if (value === undefined || value === '') {
          return Promise.resolve();
        }
        if (value && password && value.trim() !== password.trim()) {
          return Promise.reject(new Error(t('sys.register.userPasswordTwiceMsg')));
        }
        return Promise.resolve();
      };

      const validNickName = async (_: RuleObject, value: string) => {
        if (value && value !== '') {
          return new Promise<void>((resolve, reject) => {
            const params = {
              nickname: value,
            };
            checkUserExist(params)
              .then((response) => {
                if (!response) {
                  reject('昵称已存在');
                } else {
                  resolve();
                }
              })
              .catch((err) => {
                reject(err.message || '验证失败');
              });
          });
        } else {
          return Promise.resolve();
        }
      };

      const formData = reactive({
        nickname: '',
        password: '',
        rePassword: '',
      });

      const formRules = {
        nickname: [
          {
            required: true,
            message: '请输入昵称',
            trigger: 'change',
          },
          {
            required: true,
            type: 'string',
            min: 2,
            max: 16,
            message: '昵称长度范围为2-16个字符',
            trigger: 'change',
          },
          { validator: validNickName, trigger: 'change' },
        ] as RuleObject[],
        password: [
          { required: true, message: '请输入密码', trigger: 'change' },
          { validator: handlePasswordLevel, trigger: 'change' },
        ] as RuleObject[],
        rePassword: [
          { required: true, message: '请输入确认密码', trigger: 'change' },
          { validator: handlePasswordCheck, trigger: 'change' },
        ] as RuleObject[],
      };

      // 不同级别的密码国际化提示信息
      const levelNames = {
        0: 'sys.register.userPasswordStrengthShort',
        1: 'sys.register.userPasswordStrengthLow',
        2: 'sys.register.userPasswordStrengthMedium',
        3: 'sys.register.userPasswordStrengthStrong',
      };

      // 不同级别的样式
      const levelClass = {
        0: 'error',
        1: 'error',
        2: 'warning',
        3: 'success',
      };

      // 不同级别的颜色
      const levelColor = {
        0: '#ff0000',
        1: '#ff0000',
        2: '#ff7e05',
        3: '#52c41a',
      };

      const passwordLevelClass = computed(() => levelClass[state.passwordLevel]);

      const passwordLevelName = computed(() => levelNames[state.passwordLevel]);

      const passwordLevelColor = computed(() => levelColor[state.passwordLevel]);

      function passwordLevelShow(show) {
        state.passwordLevelChecked = show;
      }

      async function nextStep() {
        loading.value = true;
        state.submitBtn = true;

        const data = await formRef.value.validateFields();
        if (!data) {
          loading.value = false;
          state.submitBtn = false;
          return;
        }

        setUserInfo(formData);
        submitRegister();
      }

      function prevStep() {
        emit('prev-step');
      }

      function setUserInfo(params) {
        emit('set-user-info', params);
      }

      function submitRegister() {
        emit('submit-register');
      }

      return {
        t,
        prefixCls,
        formRef,
        formData,
        loading,
        formRules,
        nextStep,
        prevStep,
        setUserInfo,
        submitRegister,
        labelCol,
        wrapperCol,
        state,
        passwordLevelClass,
        passwordLevelName,
        passwordLevelColor,
        passwordLevelShow,
      };
    },
  });
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
