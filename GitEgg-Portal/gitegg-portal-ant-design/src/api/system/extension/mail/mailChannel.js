import request from '@/utils/request'

export function queryMailChannelList (query) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/list',
    method: 'get',
    params: query
  })
}

export function createMailChannel (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/create',
    method: 'post',
    data
  })
}

export function updateMailChannel (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/update',
    method: 'post',
    data
  })
}

export function updateMailChannelStatus (mailChannelId, status) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/status/' + mailChannelId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteMailChannel (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/batch/delete',
    method: 'post',
    data
  })
}

export function deleteMailChannel (id) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/delete/' + id,
    method: 'post'
  })
}

export function checkMailChannelExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/check',
    method: 'post',
    params: data
  })
}

export function downloadMailChannelList (query) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadMailChannel (formData) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/upload',
    method: 'post',
    data: formData
  })
}

export function downloadMailChannelTemplate (query) {
  return request({
    url: '/gitegg-service-extension/extension/mail/channel/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
