import request from '@/utils/request'

export function query${entity}List (data) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/main/list',
    method: 'post',
    data
  })
}

export function create${entity} (data) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/create',
    method: 'post',
    data
  })
}

export function update${entity} (data) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/update',
    method: 'post',
    data
  })
}
<#list mainFormFields as field>
  <#if field?? && field.fieldName?ends_with("status") && config.statusHandling == true>
    <#assign hasMainStatus=true/>
    <#assign statusMainName=field.entityName/>
  </#if>
</#list>
<#list formFields as field>
  <#if field?? && field.fieldName?ends_with("status") && config.statusHandling == true>
    <#assign hasStatus=true/>
    <#assign statusName=field.entityName/>
  </#if>
</#list>
<#if hasStatus?? && hasStatus == true>

export function update${entity}Status (${table.entityPath}Id, status) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/status/' + ${table.entityPath}Id + '/' + status,
    method: 'post'
  })
}
<#elseif hasMainStatus?? && hasMainStatus == true>

export function update${entity}Status (${mainEntityName?uncap_first}Id, status) {
  return request({
  url: '/${config.serviceName}<#if mainControllerPath?? && mainControllerPath != "">${mainControllerPath}<#else>/${mainEntityName?uncap_first}</#if>/status/' + ${mainEntityName?uncap_first}Id + '/' + status,
  method: 'post'
  })
}
</#if>

export function batchDelete${entity} (data) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/batch/delete',
    method: 'post',
    data
  })
}

export function delete${entity} (id) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/delete/' + id,
    method: 'post'
  })
}

export function check${entity}Exist (data) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/check',
    method: 'post',
    params: data
  })
}
<#if config.exportFlag == true>

export function download${entity}List (data) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/download',
    method: 'post',
    data,
    responseType: 'blob'
  })
}
</#if>
<#if config.importFlag == true>

export function upload${entity} (formData) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/upload',
    method: 'post',
    data: formData
  })
}

export function download${entity}Template (query) {
  return request({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
</#if>