import request from '@/utils/request'

export function fetchList (query) {
  return request({
    url: '/gitegg-service-system/user/list',
    method: 'get',
    params: query
  })
}

export function createUser (data) {
  return request({
    url: '/gitegg-service-system/user/create',
    method: 'post',
    data
  })
}

export function updateUser (data) {
  return request({
    url: '/gitegg-service-system/user/update',
    method: 'post',
    data
  })
}

export function updateUserInfo (data) {
  return request({
    url: '/gitegg-service-system/user/update/info',
    method: 'post',
    data
  })
}

export function queryUserInfo (data) {
  return request({
    url: '/gitegg-service-system/auth/user/info',
    method: 'get',
    data
  })
}

export function updatePwd (data) {
  return request({
    url: '/gitegg-service-system/user/password/change',
    method: 'post',
    data
  })
}

export function resetUserPassword (id) {
  return request({
    url: '/gitegg-service-system/user/password/reset/' + id,
    method: 'post'
  })
}

export function updateUserStatus (userId, status) {
  return request({
    url: '/gitegg-service-system/user/status/' + userId + '/' + status,
    method: 'post'
  })
}

export function deleteUser (id) {
  return request({
    url: '/gitegg-service-system/user/delete/' + id,
    method: 'post'
  })
}

export function batchDeleteUser (data) {
  return request({
    url: '/gitegg-service-system/user/batch/delete',
    method: 'post',
    data
  })
}

export function fetchRoleList (data) {
  return request({
    url: '/gitegg-service-system/role/all',
    method: 'get',
    data
  })
}

export function updateUserDataPermission (data) {
  return request({
    url: '/gitegg-service-system/user/update/organization/data/permission',
    method: 'post',
    data
  })
}

export function checkUserExist (data) {
  return request({
    url: '/gitegg-service-system/user/check',
    method: 'post',
    data
  })
}

export function fetchOrganizationDataList (query) {
  return request({
    url: '/gitegg-service-system/user/organization/data/permission/list',
    method: 'get',
    params: query
  })
}

export function batchDeleteOrganizationData (data) {
  return request({
    url: '/gitegg-service-system/user/organization/data/permission/batch/delete',
    method: 'post',
    data
  })
}
