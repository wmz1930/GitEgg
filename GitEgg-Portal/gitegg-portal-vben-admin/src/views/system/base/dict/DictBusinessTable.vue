<template>
  <PageWrapper dense contentClass="flex">
    <!--数据表格-->
    <DictBusinessEditTable
      title="业务数据字典类型"
      createText="新建业务数据字典类型"
      :class="childrenShow ? 'w-1/2' : ''"
      :searchInfo="parentSearchInfo"
      @show-children="queryChildrenDict"
      ref="parentDictRef"
    />
    <!--数据表格-->
    <div v-show="childrenShow" class="w-1/2 dict-child">
      <DictBusinessEditTable
        :title="parentDictName + '业务数据字典值列表'"
        createText="新建业务数据字典值"
        :immediate="false"
        :searchInfo="childrenSearchInfo"
        :actionWidth="150"
        tableType="children"
        ref="childrenDictRef"
      />
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { PageWrapper } from '/@/components/Page';

  import { DictBusinessEditTable } from '/@/components/Subassembly/Table';

  export default defineComponent({
    name: 'DictManagement',
    components: {
      PageWrapper,
      DictBusinessEditTable,
    },
    setup() {
      const checkedKeys = ref(Array<string | number>());
      const childrenSearchInfo = reactive<Recordable>({});
      const parentSearchInfo = reactive<Recordable>({});
      parentSearchInfo.parentId = 0;
      let childrenShow = ref(false);
      // 父级数据字典引用
      const parentDictRef = ref();
      // 子数据字典引用
      const childrenDictRef = ref();

      const parentDictName = ref('');

      // 点击字典列表时，查询字典数据
      function queryChildrenDict(record) {
        parentDictName.value = '【' + record?.dictName + '】';
        childrenSearchInfo.parentId = record.id;
        childrenShow.value = true;
        childrenDictRef.value.reloadTable(record);
      }

      return {
        checkedKeys,
        parentDictRef,
        childrenDictRef,
        parentDictName,
        childrenSearchInfo,
        parentSearchInfo,
        queryChildrenDict,
        childrenShow,
      };
    },
  });
</script>
<style lang="less">
  .dict-child .vben-basic-table-form-container {
    padding-left: 0px !important;
  }
</style>
