import request from '@/utils/request'

export function queryMailLogList (query) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/list',
    method: 'get',
    params: query
  })
}

export function createMailLog (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/create',
    method: 'post',
    data
  })
}

export function updateMailLog (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/update',
    method: 'post',
    data
  })
}

export function batchDeleteMailLog (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/batch/delete',
    method: 'post',
    data
  })
}

export function deleteMailLog (id) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/delete/' + id,
    method: 'post'
  })
}

export function checkMailLogExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/check',
    method: 'post',
    data
  })
}

export function downloadMailLogList (query) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadMailLog (formData) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/upload',
    method: 'post',
    data: formData
  })
}

export function downloadMailLogTemplate (query) {
  return request({
    url: '/gitegg-service-extension/extension/mail/log/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
