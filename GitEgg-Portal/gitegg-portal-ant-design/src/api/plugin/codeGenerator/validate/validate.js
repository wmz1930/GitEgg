import request from '@/utils/request'

export function queryValidateList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/list',
    method: 'get',
    params: query
  })
}

export function queryValidateListAll (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/list/all',
    method: 'get',
    params: query
  })
}

export function createValidate (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/create',
    method: 'post',
    data
  })
}

export function updateValidate (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/update',
    method: 'post',
    data
  })
}

export function updateValidateStatus (validateId, status) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/status/' + validateId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteValidate (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/batch/delete',
    method: 'post',
    data
  })
}

export function deleteValidate (id) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/delete/' + id,
    method: 'post'
  })
}

export function checkValidateExist (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/check',
    method: 'post',
    params: data
  })
}

export function downloadValidateList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadValidate (formData) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/upload',
    method: 'post',
    data: formData
  })
}

export function downloadValidateTemplate (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/validate/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
