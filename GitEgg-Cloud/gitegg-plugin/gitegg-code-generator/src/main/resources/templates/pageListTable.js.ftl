import request from '@/utils/request'

export function query${entity}List (query) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/list',
    method: 'get',
    params: query
  })
}

export function create${entity} (data) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/create',
    method: 'post',
    data
  })
}

export function update${entity} (data) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/update',
    method: 'post',
    data
  })
}

export function update${entity}Status (<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>Id, status) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/status/' + <#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>Id + '/' + status,
    method: 'post'
  })
}

export function batchDelete${entity} (data) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/batch/delete',
    method: 'post',
    data
  })
}

export function delete${entity} (id) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/delete/' + id,
    method: 'post'
  })
}

export function check${entity}Exist (data) {
  return request({
    url: '/${serviceName}/${package.ModuleName}/<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")}<#else>${table.entityPath}</#if>/check',
    method: 'post',
    params: data
  })
}
