import request from '@/utils/request'

export function queryTenantList (query) {
  return request({
    url: '/gitegg-service-base/base/tenant/list',
    method: 'get',
    params: query
  })
}

export function createTenant (data) {
  return request({
    url: '/gitegg-service-base/base/tenant/create',
    method: 'post',
    data
  })
}

export function updateTenant (data) {
  return request({
    url: '/gitegg-service-base/base/tenant/update',
    method: 'post',
    data
  })
}

export function updateTenantStatus (tenantId, status) {
  return request({
    url: '/gitegg-service-base/base/tenant/status/' + tenantId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteTenant (data) {
  return request({
    url: '/gitegg-service-base/base/tenant/batch/delete',
    method: 'post',
    data
  })
}

export function deleteTenant (id) {
  return request({
    url: '/gitegg-service-base/base/tenant/delete/' + id,
    method: 'post'
  })
}

export function checkTenantExist (data) {
  return request({
    url: '/gitegg-service-base/base/tenant/check',
    method: 'post',
    params: data
  })
}
