<template>
  <div class="overflow-hidden bg-white resource-tree">
    <BasicTree
      title="资源权限列表"
      toolbar
      search
      expandOnSearch
      ref="refTree"
      :clickRowToExpand="false"
      :treeData="treeData"
      :fieldNames="{ key: 'id', title: 'resourceName' }"
      @select="handleSelect"
      @check="handleCheck"
      v-bind="$attrs"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, onMounted } from 'vue';

  import { BasicTree, TreeItem, TreeActionType } from '/@/components/Tree';
  import { getResourceList } from '/@/api/system/resource';

  export default defineComponent({
    name: 'ResourceTree',
    components: { BasicTree },
    inheritAttrs: false,
    // props: treeProps,
    emits: ['select', 'check'],
    setup(_, { emit }) {
      const refTree = ref<Nullable<TreeActionType>>(null);
      const treeData = ref<TreeItem[]>([]);
      const isFirstLoaded = ref<Boolean>(false);
      const loading = ref(false);

      function handleSelect(value, node, extra) {
        emit('select', value[0], node, extra);
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
          result = (await getResourceList({})) as unknown as TreeItem[];
        } catch (e) {
          console.error(e);
        }
        loading.value = false;
        treeData.value = result;
        isFirstLoaded.value = true;
      }

      return { loading, refTree, treeData, handleSelect, handleCheck };
    },
  });
</script>
<style lang="less">
  .resource-tree .vben-tree .vben-tree-header {
    padding-top: 1rem !important;
    padding-bottom: 1rem !important;
  }

  .resource-tree .vben-tree .ant-spin-nested-loading .ant-spin-container {
    padding: 0.6rem;
  }
</style>
