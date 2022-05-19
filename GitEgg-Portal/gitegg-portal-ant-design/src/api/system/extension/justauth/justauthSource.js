import request from '@/utils/request'

export function queryJustAuthSourceList (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/list',
    method: 'get',
    params: query
  })
}

export function createJustAuthSource (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/create',
    method: 'post',
    data
  })
}

export function updateJustAuthSource (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/update',
    method: 'post',
    data
  })
}

export function updateJustAuthSourceStatus (justAuthSourceId, status) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/status/' + justAuthSourceId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteJustAuthSource (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/batch/delete',
    method: 'post',
    data
  })
}

export function deleteJustAuthSource (id) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/delete/' + id,
    method: 'post'
  })
}

export function checkJustAuthSourceExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/check',
    method: 'post',
    params: data
  })
}

export function downloadJustAuthSourceList (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadJustAuthSource (formData) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/upload',
    method: 'post',
    data: formData
  })
}

export function downloadJustAuthSourceTemplate (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/source/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
