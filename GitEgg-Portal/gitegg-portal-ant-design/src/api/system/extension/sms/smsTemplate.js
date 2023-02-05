import request from '@/utils/request'

export function querySmsTemplateList (query) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/list',
    method: 'get',
    params: query
  })
}

export function createSmsTemplate (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/create',
    method: 'post',
    data
  })
}

export function updateSmsTemplate (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/update',
    method: 'post',
    data
  })
}

export function updateSmsTemplateStatus (smsTemplateId, status) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/status/' + smsTemplateId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteSmsTemplate (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/batch/delete',
    method: 'post',
    data
  })
}

export function deleteSmsTemplate (id) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/delete/' + id,
    method: 'post'
  })
}

export function checkSmsTemplateExist (data) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/check',
    method: 'post',
    data
  })
}

export function downloadSmsTemplateList (query) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadSmsTemplate (formData) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/upload',
    method: 'post',
    data: formData
  })
}

export function downloadSmsTemplateTemplate (query) {
  return request({
    url: '/gitegg-service-extension/extension/sms/template/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
