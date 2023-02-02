import request from '@/utils/request'

export function querySmsChannelList (query) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/list',
    method: 'get',
    params: query
  })
}

export function querySmsChannelListAll (query) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/list/all',
    method: 'get',
    params: query
  })
}

export function createSmsChannel (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/create',
    method: 'post',
    data
  })
}

export function updateSmsChannel (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/update',
    method: 'post',
    data
  })
}

export function updateSmsChannelStatus (smsChannelId, status) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/status/' + smsChannelId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteSmsChannel (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/batch/delete',
    method: 'post',
    data
  })
}

export function deleteSmsChannel (id) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/delete/' + id,
    method: 'post'
  })
}

export function checkSmsChannelExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/check',
    method: 'post',
    data
  })
}

export function downloadSmsChannelList (query) {
  return request({
    url: '/gitegg-service-extension/extension/sms/channel/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
