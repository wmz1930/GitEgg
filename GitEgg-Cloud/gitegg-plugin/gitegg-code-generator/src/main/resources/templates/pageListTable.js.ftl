import request from '@/utils/request'

export function query${entity}List (query) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/list',
    method: 'get',
    params: query
  })
}

export function create${entity} (data) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/create',
    method: 'post',
    data
  })
}

export function update${entity} (data) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/update',
    method: 'post',
    data
  })
}

<#list table.fields as field>
<#if field.annotationColumnName?contains("status")>
export function update${entity}Status (<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>Id, status) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/status/' + <#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>Id + '/' + status,
    method: 'post'
  })
}
</#if>
</#list>

export function batchDelete${entity} (data) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/batch/delete',
    method: 'post',
    data
  })
}

export function delete${entity} (id) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/delete/' + id,
    method: 'post'
  })
}

export function check${entity}Exist (data) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/check',
    method: 'post',
    params: data
  })
}

<#if config.exportFlag == true>
export function download${entity}List (query) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
</#if>

<#if config.importFlag == true>
export function upload${entity} (formData) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/upload',
    method: 'post',
    data: formData
  })
}

export function download${entity}Template (query) {
  return request({
    url: '/${config.serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
</#if>