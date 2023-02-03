<template>
  <PageWrapper dense contentClass="flex">
    <!--数据表格-->
    <${entity!}EditTable
      title="${config.domainName!}父列表"
      createText="新建${config.domainName!}"
      :class="childrenShow ? 'w-1/2' : ''"
      :searchInfo="parentSearchInfo"
      @show-children="queryChildren${entity!}"
      ref="parent${entity!}Ref"
    />
    <!--数据表格-->
    <div v-show="childrenShow" class="w-1/2 right-child">
      <${entity!}EditTable
        :title="parent${entity!}Name + '${config.domainName!}子列表'"
        createText="新建${config.domainName!}"
        :immediate="false"
        :searchInfo="childrenSearchInfo"
        :actionWidth="150"
        tableType="children"
        ref="children${entity!}Ref"
      />
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { PageWrapper } from '/@/components/Page';

  import ${entity!}EditTable from './${entity!}EditTable.vue';

  export default defineComponent({
    name: '${entity!}Management',
    components: {
      PageWrapper,
      ${entity!}EditTable,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const childrenSearchInfo = reactive<Recordable>({});
      const parentSearchInfo = reactive<Recordable>({});
      parentSearchInfo.parentId = 0;
      let childrenShow = ref(false);
      // 父级引用
      const parent${entity!}Ref = ref();
      // 子引用
      const children${entity!}Ref = ref();

      const parent${entity!}Name = ref('');

      // 点击列表时，查询子数据
      function queryChildren${entity!}(record) {
        // TODO 这里需要改为要显示的名称
        parent${entity!}Name.value = '【' + record?.id + '】';
        childrenSearchInfo.parentId = record.id;
        childrenShow.value = true;
        children${entity!}Ref.value.reloadTable(record);
      }

      return {
        checkedKeys,
        parent${entity!}Ref,
        children${entity!}Ref,
        parent${entity!}Name,
        childrenSearchInfo,
        parentSearchInfo,
        queryChildren${entity!},
        childrenShow,
      };
    },
  });
</script>
<style lang="less">
  .right-child .vben-basic-table-form-container {
    padding-left: 0px !important;
  }
</style>
