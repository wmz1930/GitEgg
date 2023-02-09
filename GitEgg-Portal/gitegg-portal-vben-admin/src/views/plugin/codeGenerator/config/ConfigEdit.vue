<template>
  <PageWrapper title="" contentBackground content="" contentClass="p-4">
    <div class="step-form-form">
      <a-steps :current="current">
        <a-step v-for="item in steps" :key="item.title" :title="item.title">
          <template #icon>
            <Icon :icon="item.icon" :size="18" />
          </template>
        </a-step>
      </a-steps>
    </div>
    <div class="mt-5">
      <TableJoin
        :config="config"
        v-show="
          steps[current] &&
          steps[current].content === 'union-table' &&
          (config?.tableType === 'join_query' || config?.tableType === 'main_sub')
        "
        ref="tableJoin"
      />
      <TableField
        :config="config"
        :fields="fieldList"
        v-show="steps[current] && steps[current].content === 'filed-config'"
        v-if="fieldInit"
        ref="tableConfig"
      />
      <TableForm
        :config="config"
        :fields="fieldList"
        v-show="steps[current] && steps[current].content === 'form-config'"
        v-if="fieldInit"
        ref="tableForm"
      />
      <TableFormValid
        :config="config"
        :fields="fieldList"
        v-show="steps[current] && steps[current].content === 'form-valid'"
        v-if="fieldInit"
        ref="tableFormValid"
      />
      <TableList
        :config="config"
        :fields="fieldList"
        v-show="steps[current] && steps[current].content === 'list-config'"
        v-if="fieldInit"
        ref="tableList"
      />
    </div>
    <div class="back-action">
      <a-button style="margin-left: 12px" @click="backToList"> 返回列表 </a-button>
    </div>
    <div class="steps-action">
      <a-button :disabled="current <= 0" style="margin-right: 18px" @click="prev">
        上一步
      </a-button>

      <a-button
        v-if="current > 0 && current < steps.length - 1"
        style="margin-right: 18px"
        type="primary"
        ghost
        @click="editFieldList"
      >
        保存
      </a-button>

      <a-button v-if="current == steps.length - 1" type="primary" @click="editFieldList">
        配置完成
      </a-button>

      <a-button v-if="current < steps.length - 1" type="primary" @click="next"> 下一步 </a-button>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, toRefs, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import TableJoin from './configSteps/TableJoin.vue';
  import TableField from './configSteps/TableField.vue';
  import TableForm from './configSteps/TableForm.vue';
  import TableFormValid from './configSteps/TableFormValid.vue';
  import TableList from './configSteps/TableList.vue';
  import { PageWrapper } from '/@/components/Page';
  import { Steps } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLoading } from '/@/components/Loading';
  import { useGo } from '/@/hooks/web/usePage';

  import { queryConfig } from '/@/api/plugin/codeGenerator/config/config';
  import { getFieldListAll, editField } from '/@/api/plugin/codeGenerator/field/field';

  export default defineComponent({
    name: 'ConfigEditSteps',
    components: {
      TableJoin,
      TableField,
      TableForm,
      PageWrapper,
      TableFormValid,
      TableList,
      [Steps.name]: Steps,
      [Steps.Step.name]: Steps.Step,
      Icon,
    },
    setup() {
      const go = useGo();
      const route = useRoute();
      // 获取config id
      const configId = ref(route.params?.id);
      const config = ref<any>({});
      const fieldList = ref<any[]>([]);
      const fieldInit = ref<boolean>(false);
      const current = ref(0);
      const steps = ref<StepProps[]>([]);
      const { createMessage } = useMessage();
      const [openFullLoading, closeFullLoading] = useLoading({
        tip: '',
      });

      // 定义step对象
      interface StepProps {
        title: string;
        content: string;
        icon: string;
      }

      const stepsMulti = [
        {
          title: '联表配置',
          content: 'union-table',
          icon: 'ant-design:appstore-outlined',
        },
      ] as StepProps[];

      const stepsSingle = [
        {
          title: '字段配置',
          content: 'filed-config',
          icon: 'ant-design:apartment-outlined',
        },
        {
          title: '表单配置',
          content: 'form-config',
          icon: 'ant-design:file-text-outlined',
        },
        {
          title: '表单校验',
          content: 'form-valid',
          icon: 'ant-design:carry-out-outlined',
        },
        {
          title: '列表配置',
          content: 'list-config',
          icon: 'ant-design:table-outlined',
        },
      ] as StepProps[];

      const state = reactive({
        initSetp2: false,
        initSetp3: false,
      });

      function handleTableJoinNext(step1Values: any) {
        current.value++;
        state.initSetp2 = true;
        console.log(step1Values);
      }

      function handleStepPrev() {
        current.value--;
      }

      function handleTableFieldNext(step2Values: any) {
        current.value++;
        state.initSetp3 = true;
        console.log(step2Values);
      }

      function handleRedo() {
        current.value = 0;
        state.initSetp2 = false;
        state.initSetp3 = false;
      }

      // 挂载后查询配置
      onMounted(() => {
        getConfig();
      });

      function getConfig() {
        openFullLoading();
        steps.value = [];
        queryConfig({ id: configId.value }).then((response) => {
          config.value = response;
          closeFullLoading();
          if (config.value.tableType === 'join_query' || config.value.tableType === 'main_sub') {
            steps.value = steps.value.concat(stepsMulti);
          } else {
            getFieldList();
          }
          steps.value = steps.value.concat(stepsSingle);
        });
      }

      function getFieldList() {
        if (!fieldInit.value) {
          openFullLoading();
          getFieldListAll({ generationId: configId.value }).then((response) => {
            fieldList.value = response;
            fieldInit.value = true;
            closeFullLoading();
          });
        }
      }

      function editFieldList() {
        openFullLoading();
        let fileds = [];
        fieldList.value.forEach(function (fieldData) {
          fileds = fileds.concat(fieldData.fieldDTOList);
        });
        editField(fileds).then(() => {
          createMessage.success('数据保存成功');
          closeFullLoading();
          fieldInit.value = false;
          getFieldList();
        });
      }

      function next() {
        current.value++;
        // 字段配置
        if (steps.value[current.value].content === 'filed-config') {
          getFieldList();
        }
      }

      function prev() {
        current.value--;
      }

      function backToList() {
        go('/plugin/code/generator/config/table');
      }

      return {
        current,
        handleTableJoinNext,
        handleTableFieldNext,
        handleRedo,
        handleStepPrev,
        ...toRefs(state),
        steps,
        config,
        fieldList,
        fieldInit,
        editFieldList,
        next,
        prev,
        backToList,
      };
    },
  });
</script>
<style lang="less" scoped>
  .step-form-content {
    padding: 24px;
    background-color: @component-background;
  }

  .step-form-form {
    width: 90%;
    margin: 0 auto;
  }

  .steps-action {
    margin-top: 24px;
    float: right;
  }

  .back-action {
    margin-top: 24px;
    float: left;
  }
</style>
