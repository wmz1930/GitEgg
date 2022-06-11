import request from '@/utils/request'

export function queryJustAuthConfigList (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/list',
    method: 'get',
    params: query
  })
}

export function createJustAuthConfig (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/create',
    method: 'post',
    data
  })
}

export function updateJustAuthConfig (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/update',
    method: 'post',
    data
  })
}

export function updateJustAuthConfigStatus (justAuthConfigId, status) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/status/' + justAuthConfigId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteJustAuthConfig (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/batch/delete',
    method: 'post',
    data
  })
}

export function deleteJustAuthConfig (id) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/delete/' + id,
    method: 'post'
  })
}

export function checkJustAuthConfigExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/check',
    method: 'post',
    params: data
  })
}

export function downloadJustAuthConfigList (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadJustAuthConfig (formData) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/upload',
    method: 'post',
    data: formData
  })
}

export function downloadJustAuthConfigTemplate (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/config/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
