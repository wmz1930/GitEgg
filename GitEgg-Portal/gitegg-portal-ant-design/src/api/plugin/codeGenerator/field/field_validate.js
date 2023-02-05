import request from '@/utils/request'

export function queryFieldValidateList (query) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/list',
    method: 'get',
    params: query
  })
}

export function createFieldValidate (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/create',
    method: 'post',
    data
  })
}

export function updateFieldValidate (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/update',
    method: 'post',
    data
  })
}

export function updateFieldValidateStatus (fieldValidateId, status) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/status/' + fieldValidateId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteFieldValidate (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/batch/delete',
    method: 'post',
    data
  })
}

export function deleteFieldValidate (id) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/delete/' + id,
    method: 'post'
  })
}

export function checkFieldValidateExist (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/field/validate/check',
    method: 'post',
    data
  })
}
