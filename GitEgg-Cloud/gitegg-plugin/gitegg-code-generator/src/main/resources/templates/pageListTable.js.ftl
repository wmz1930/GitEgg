import request from '@/utils/request'

export function query${entity}List (query) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/list',
    method: 'get',
    params: query
  })
}

export function create${entity} (data) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/create',
    method: 'post',
    data
  })
}

export function update${entity} (data) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/update',
    method: 'post',
    data
  })
}

export function update${entity}Status (${table.entityPath}Id, status) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/status/' + ${table.entityPath}Id + '/' + status,
    method: 'post'
  })
}

export function batchDelete${entity} (data) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/batch/delete',
    method: 'post',
    data
  })
}

export function delete${entity} (id) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/delete/' + id,
    method: 'post'
  })
}

export function check${entity}Exist (data) {
  return request({
    url: '/gitegg-service-${package.ModuleName}/${package.ModuleName}/${table.entityPath}/check',
    method: 'post',
    params: data
  })
}
