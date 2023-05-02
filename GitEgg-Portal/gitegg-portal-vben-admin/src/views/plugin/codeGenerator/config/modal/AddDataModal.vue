<template>
  <BasicModal
    v-bind="$attrs"
    @register="addDataModal"
    :title="titleText"
    @ok="closeViewModal"
    @cancel="closeViewModal"
  >
    <DictBusinessTable v-if="openType === 'dict'" />
    <ApiTable v-if="openType === 'api'" />
    <ValidateTable v-if="openType === 'validate'" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import DictBusinessTable from '/@/views/system/base/dict/DictBusinessTable.vue';
  import ApiTable from '/@/views/plugin/codeGenerator/api/ApiTable.vue';
  import ValidateTable from '/@/views/plugin/codeGenerator/validate/ValidateTable.vue';

  export default defineComponent({
    name: 'AddDataModal',
    components: { BasicModal, DictBusinessTable, ApiTable, ValidateTable },
    emits: ['success'],
    setup(_, { emit }) {
      const openType = ref('');
      const titleText = ref('');
      const [addDataModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        openType.value = data.record.openType;
        titleText.value = data.record.titleText;
        setModalProps({ defaultFullscreen: true, useWrapper: true });
      });

      async function closeViewModal() {
        emit('success', { values: { openType: openType.value } });
        closeModal();
      }

      return { addDataModal, openType, titleText, closeViewModal };
    },
  });
</script>
