<template>
  <PageWrapper
    :title="`${config.domainName!}` + rowId + `操作界面`"
    content="这是${config.domainName!}操作界面。"
    contentBackground
    @back="goBack"
  >
    <template #extra>
      <a-button type="primary" default @click="closeCurrent"> 取消 </a-button>
      <a-button type="primary" @click="handleSubmit"> 保存 </a-button>
    </template>
    <template #footer>
    </template>
    <div class="pt-4 m-4 desc-wrap">
       <BasicForm @register="registerForm" />
    </div>
  </PageWrapper>
</template>

<script>
  import { defineComponent, ref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useRoute } from 'vue-router';
  import { PageWrapper } from '/@/components/Page';
  import { useGo } from '/@/hooks/web/usePage';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { Tabs } from 'ant-design-vue';
  import { formSchema } from './${dataTsName}';
  import { query${entity}, create${entity}, update${entity} } from '/@/api/${vueJsPath}';
  export default defineComponent({
    name: '${entity}${config.formType}',
    components: { PageWrapper, ATabs: Tabs, ATabPane: Tabs.TabPane },
    setup() {
      const route = useRoute();
      const go = useGo();
      // 此处可以得到ID
      const rowId = ref(route.params?.id);
      const detail = ref();
      const { setTitle, closeCurrent } = useTabs();

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 90,
        baseColProps: { span: 24 },
        schemas: formSchema,
        showActionButtonGroup: false,
      });

      // 通过id查询详情
      if(rowId && rowId.value && rowId.value !== ''){
        get${entity}();
      }

      // 设置Tab的标题（不会影响页面标题）
      setTitle('详情：' + rowId.value);

      async function handleSubmit() {
        try {
          const values = await validate();
          set${config.formType}Props({ confirmLoading: true });
          if (unref(isUpdate)) {
            await update${entity}(values);
          } else {
            await create${entity}(values);
          }
          // 此处可以添加处理成功后的逻辑
        } finally {
          goBack();
        }
      }

      // 查询详情
      async function get${entity}() {
        detail.value = await query${entity}({id: rowId.value});
        setFieldsValue({
          ...detail,
        });
      }

      // 页面左侧点击返回链接时的操作
      function goBack() {
        // 本例的效果时点击返回始终跳转到账号列表页，实际应用时可返回上一页
        go('${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/table');
      }
      return { rowId, handleSubmit, goBack, closeCurrent };
    },
  });
</script>

<style></style>