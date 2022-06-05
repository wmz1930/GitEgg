#菜单
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      0,
      0,
      '${table.comment!}',
      '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:table',
      '2',
      'jiaoseguanli',
      '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/table',
      '<#if vueTablePath?? && vueTablePath != "">${vueTablePath}<#else>${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/table</#if>',
      2,
      <#if vueTablePath?? && vueTablePath != "">1<#else>0</#if>,
      1,
      '${table.entityPath}Table',
      '1',
      '${table.comment!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );
#查询数据列表
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '获取${table.comment!}列表',
      '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:list',
      '4',
      'xitongrizhi',
      '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/list',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/list',
      2,
      1,
      1,
      NULL,
      '1',
      '获取${table.comment!}列表数据',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#添加
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '添加${table.comment!}',
      '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:create',
      '4',
      'xitongrizhi',
      '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/create',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/create',
      2,
      1,
      1,
      NULL,
      '1',
      '添加${table.comment!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#更新
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '更新${table.comment!}',
      '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:update',
      '4',
      'xitongrizhi',
      '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/update',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/update',
      2,
      1,
      1,
      NULL,
      '1',
      '更新${table.comment!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#删除
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '删除${table.comment!}',
      '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:delete',
      '4',
      'xitongrizhi',
      '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/delete',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/delete/{${table.entityPath}Id}',
      2,
      1,
      1,
      NULL,
      '1',
      '删除${table.comment!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#批量删除
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
  VALUES
  (
      ${maxId},
      0,
      '批量删除${table.comment!}',
      '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:batch:delete',
      '4',
      'xitongrizhi',
      '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/batch/delete',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/batch/delete',
      2,
      1,
      1,
      NULL,
      '1',
      '批量删除${table.comment!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

<#list table.fields as field>
<#if field.annotationColumnName?contains("status")>
#更新状态
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${table.comment!}状态修改',
   '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:status',
   '4',
   'xitongrizhi',
   '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/status',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/status/{${table.entityPath}Id}/{${table.entityPath}Status}',
   2,
   1,
   1,
   NULL,
   '1',
   '批量删除${table.comment!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
</#if>
</#list>

#校验是否存在
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${table.comment!}字段校验是否存在',
   '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:check',
   '4',
   'xitongrizhi',
   '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/check',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/check',
   2,
   1,
   1,
   NULL,
   '1',
   '字段校验是否存在${table.comment!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
<#if config.exportFlag == true>
#数据导出
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${table.comment!}数据导出',
   '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:download',
   '4',
   'xitongrizhi',
   '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/download',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/download',
   2,
   1,
   1,
   NULL,
   '1',
   '数据导出${table.comment!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
</#if>

<#if config.importFlag == true>
#下载数据导入模板
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${table.comment!}数据导入模板下载',
   '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:download:template',
   '4',
   'xitongrizhi',
   '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/download/template',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/download/template',
   2,
   1,
   1,
   NULL,
   '1',
   '数据导入模板下载${table.comment!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);

#数据导入
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${table.comment!}数据导入',
   '${config.serviceName?replace("-",":")}:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:upload',
   '4',
   'xitongrizhi',
   '${config.serviceName?replace("-","/")}/<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/upload',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/upload',
   2,
   1,
   1,
   NULL,
   '1',
   '数据导入${table.comment!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
</#if>