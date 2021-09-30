import request from '@/utils/request'

export function queryFieldList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/list',
    method: 'get',
    params: query
  })
}

export function queryFieldListAll (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/list/all',
    method: 'get',
    params: query
  })
}

export function createField (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/create',
    method: 'post',
    data
  })
}

export function editField (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/edit',
    method: 'post',
    data
  })
}

export function updateField (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/update',
    method: 'post',
    data
  })
}

export function updateFieldStatus (fieldId, status) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/status/' + fieldId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteField (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/batch/delete',
    method: 'post',
    data
  })
}

export function deleteField (id) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/delete/' + id,
    method: 'post'
  })
}

export function checkFieldExist (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/field/check',
    method: 'post',
    params: data
  })
}
