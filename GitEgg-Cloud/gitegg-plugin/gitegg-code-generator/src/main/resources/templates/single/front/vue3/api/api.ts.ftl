import { defHttp } from '/@/utils/http/axios';

<#if tableShowType?? && tableShowType == "tree_table">
export function get${entity}Tree(query) {
  return defHttp.get({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/tree',
    params: query,
  });
}

</#if>
export function get${entity}List(query) {
  return defHttp.get({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/list',
    params: query,
  });
}

export function create${entity}(data) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/create',
    data,
  });
}

export function update${entity}(data) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/update',
    data,
  });
}
<#list table.fields as field>
  <#if field?? && field.annotationColumnName?ends_with("status") && config.statusHandling == true>
    <#assign hasStatus=true/>
    <#assign statusName=field.propertyName/>
  </#if>
</#list>
<#if hasStatus?? && hasStatus == true>

export function update${entity}Status(${table.entityPath}Id, status) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/status/' + ${table.entityPath}Id + '/' + status,
  });
}
</#if>

export function batchDelete${entity}(data) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/batch/delete',
    data,
  });
}

export function delete${entity}(id) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/delete/' + id,
  });
}

export function check${entity}Exist(data) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/check',
    params: data,
  });
}
<#if config.exportFlag == true>

export function export${entity}List(query) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/export',
    responseType: 'blob',
    params: query,
  });
}
</#if>
<#if config.importFlag == true>

export function import${entity}List(formData) {
  return defHttp.post({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/import',
    data: formData,
  });
}

export function download${entity}Template(query) {
  return defHttp.get({
    url: '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/download/template',
    responseType: 'blob',
    params: query,
  });
}
</#if>