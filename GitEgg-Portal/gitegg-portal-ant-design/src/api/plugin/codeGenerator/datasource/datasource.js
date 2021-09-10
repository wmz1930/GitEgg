import request from '@/utils/request'

export function queryDatasourceList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/list',
    method: 'get',
    params: query
  })
}

export function createDatasource (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/create',
    method: 'post',
    data
  })
}

export function updateDatasource (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/update',
    method: 'post',
    data
  })
}

export function updateDatasourceStatus (datasourceId, status) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/status/' + datasourceId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteDatasource (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/batch/delete',
    method: 'post',
    data
  })
}

export function deleteDatasource (id) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/delete/' + id,
    method: 'post'
  })
}

export function checkDatasourceExist (data) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/check',
    method: 'post',
    params: data
  })
}

export function downloadDatasourceList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function downloadDatasourceTemplate (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/download/template',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

export function uploadDatasource (formData) {
  return request({
    url: '/gitegg-plugin-code/code/generator/datasource/upload',
    method: 'post',
    data: formData
  })
}
