import request from '@/utils/request'

export function queryDataPermissionRoleList (query) {
  return request({
    url: '/gitegg-service-system/data/permission/role/list',
    method: 'get',
    params: query
  })
}

export function createDataPermissionRole (data) {
  return request({
    url: '/gitegg-service-system/data/permission/role/create',
    method: 'post',
    data
  })
}

export function updateDataPermissionRole (data) {
  return request({
    url: '/gitegg-service-system/data/permission/role/update',
    method: 'post',
    data
  })
}

export function updateDataPermissionRoleStatus (dataPermissionRoleId, status) {
  return request({
    url: '/gitegg-service-system/data/permission/role/status/' + dataPermissionRoleId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteDataPermissionRole (data) {
  return request({
    url: '/gitegg-service-system/data/permission/role/batch/delete',
    method: 'post',
    data
  })
}

export function deleteDataPermissionRole (id) {
  return request({
    url: '/gitegg-service-system/data/permission/role/delete/' + id,
    method: 'post'
  })
}

export function checkDataPermissionRoleExist (data) {
  return request({
    url: '/gitegg-service-system/data/permission/role/check',
    method: 'post',
    params: data
  })
}

export function queryRoleDataPermission (permissionId) {
  return request({
    url: '/gitegg-service-system/data/permission/role/get/roles/' + permissionId,
    method: 'get'
  })
}

export function updateRoleDataPermission (data) {
  return request({
    url: '/gitegg-service-system/data/permission/role/batch/role/update',
    method: 'post',
    data
  })
}
