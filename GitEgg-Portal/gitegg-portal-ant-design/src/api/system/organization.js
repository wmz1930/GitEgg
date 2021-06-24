import request from '@/utils/request'

export function fetchOrgList (data) {
  return request({
    url: '/gitegg-service-system/organization/tree',
    method: 'get',
    params: data
  })
}

export function createOrganization (data) {
  return request({
    url: '/gitegg-service-system/organization/create',
    method: 'post',
    data
  })
}

export function updateOrganization (data) {
  return request({
    url: '/gitegg-service-system/organization/update',
    method: 'post',
    data
  })
}

export function deleteOrganization (id) {
  return request({
    url: '/gitegg-service-system/organization/delete/' + id,
    method: 'post'
  })
}

export function updateOrganizationStatus (organizationId, status) {
  return request({
    url: '/gitegg-service-system/organization/status/' + organizationId + '/' + status,
    method: 'post'
  })
}

export function checkOrganizationExist (data) {
  return request({
    url: '/gitegg-service-system/organization/check',
    method: 'post',
    params: data
  })
}
