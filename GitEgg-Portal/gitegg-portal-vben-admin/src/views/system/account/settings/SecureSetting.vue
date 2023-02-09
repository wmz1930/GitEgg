<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <ListItem>
        <ListItemMeta>
          <template #title>
            账户密码
            <div class="extra"> <a-button type="link" @click="handlePwdModal">修改</a-button> </div>
          </template>
          <template #description>
            <div>当前密码强度：：强</div>
          </template>
        </ListItemMeta>
      </ListItem>
      <ListItem>
        <ListItemMeta>
          <template #title>
            密保手机
            <div class="extra"> <a-button type="link" disabled>修改</a-button> </div>
          </template>
          <template #description>
            <div>已绑定手机：：{{ userInfo.mobile }}</div>
          </template>
        </ListItemMeta>
      </ListItem>
      <ListItem>
        <ListItemMeta>
          <template #title>
            安全邮箱
            <div class="extra"> <a-button type="link" disabled>修改</a-button> </div>
          </template>
          <template #description>
            <div>已绑定邮箱：：{{ userInfo.email }}</div>
          </template>
        </ListItemMeta>
      </ListItem>
      <ListItem>
        <ListItemMeta>
          <template #title>
            密保问题
            <div class="extra"> <a-button type="link" disabled>修改</a-button> </div>
          </template>
          <template #description>
            <div>未设置密保问题，密保问题可有效保护账户安全</div>
          </template>
        </ListItemMeta>
      </ListItem>
      <ListItem>
        <ListItemMeta>
          <template #title>
            MFA 设备
            <div class="extra"> <a-button type="link" disabled>修改</a-button> </div>
          </template>
          <template #description>
            <div>未绑定 MFA 设备，绑定后，可以进行二次确认</div>
          </template>
        </ListItemMeta>
      </ListItem>
    </List>
  </CollapseContainer>
  <!-- 修改密码时的弹框 -->
  <ChangePwdModal @register="registerModal" />
</template>
<script lang="ts">
  import { List } from 'ant-design-vue';
  import { defineComponent, onMounted, ref } from 'vue';
  import { CollapseContainer } from '/@/components/Container/index';

  import { useUserStore } from '/@/store/modules/user';

  import { useModal } from '/@/components/Modal';
  import ChangePwdModal from './ChangePwdModal.vue';

  export default defineComponent({
    components: {
      CollapseContainer,
      List,
      ListItem: List.Item,
      ListItemMeta: List.Item.Meta,
      ChangePwdModal,
    },
    setup() {
      const [registerModal, { openModal: openPwdModal }] = useModal();
      const userStore = useUserStore();
      const userInfo = ref({} as any);

      onMounted(async () => {
        userInfo.value = userStore.getUserInfo as any;
      });

      function handlePwdModal() {
        // 省市区、角色、状态转换
        openPwdModal(true, {
          isUpdate: true,
        });
      }

      return {
        userInfo,
        registerModal,
        handlePwdModal,
      };
    },
  });
</script>
<style lang="less" scoped>
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    font-weight: normal;
    color: #1890ff;
    cursor: pointer;
  }
</style>
