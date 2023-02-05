import request from '@/utils/request'

export function queryJustAuthSocialList (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/list',
    method: 'get',
    params: query
  })
}

export function createJustAuthSocial (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/create',
    method: 'post',
    data
  })
}

export function updateJustAuthSocial (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/update',
    method: 'post',
    data
  })
}

export function batchDeleteJustAuthSocial (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/batch/delete',
    method: 'post',
    data
  })
}

export function deleteJustAuthSocial (id) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/delete/' + id,
    method: 'post'
  })
}

export function checkJustAuthSocialExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/check',
    method: 'post',
    data
  })
}

export function downloadJustAuthSocialList (query) {
  return request({
    url: '/gitegg-service-extension/extension/justauth/social/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
