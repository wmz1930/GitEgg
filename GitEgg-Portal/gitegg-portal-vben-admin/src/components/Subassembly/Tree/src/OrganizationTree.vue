<template>
  <div class="overflow-hidden m-4 mr-0 org-tree">
    <BasicTree
      title="组织机构列表"
      toolbar
      search
      expandOnSearch
      treeWrapperClassName="h-[calc(100%-55px)] overflow-auto"
      ref="refTree"
      :clickRowToExpand="false"
      :treeData="treeData"
      :fieldNames="{ key: 'id', title: 'organizationName' }"
      @select="handleSelect"
      @check="handleCheck"
      v-bind="$attrs"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, onMounted } from 'vue';

  import { BasicTree, TreeItem, TreeActionType } from '/@/components/Tree';
  import { getOrganizationList } from '/@/api/system/organization';

  export default defineComponent({
    name: 'OrganizationTree',
    components: { BasicTree },
    inheritAttrs: false,
    // props: treeProps,
    emits: ['select', 'check'],
    setup(_, { emit }) {
      const refTree = ref<Nullable<TreeActionType>>(null);
      const treeData = ref<TreeItem[]>([]);
      const isFirstLoaded = ref<Boolean>(false);
      const loading = ref(false);

      function handleSelect(keys) {
        emit('select', keys[0]);
      }

      function handleCheck(keys, e) {
        emit('check', keys, e);
      }

      onMounted(() => {
        fetch();
      });

      async function fetch() {
        loading.value = true;
        treeData.value = [];
        let result;
        try {
          result = (await getOrganizationList({})) as unknown as TreeItem[];
        } catch (e) {
          console.error(e);
        }
        loading.value = false;
        treeData.value = result;
        isFirstLoaded.value = true;
      }

      function reLoading() {
        fetch();
      }

      return { loading, refTree, treeData, handleSelect, handleCheck, reLoading };
    },
  });
</script>
<style lang="less">
  .org-tree .vben-tree .vben-tree-header {
    padding-top: 1rem !important;
    padding-bottom: 1rem !important;
  }

  .org-tree .vben-tree .ant-spin-nested-loading .ant-spin-container {
    padding: 0.6rem;
  }
</style>
