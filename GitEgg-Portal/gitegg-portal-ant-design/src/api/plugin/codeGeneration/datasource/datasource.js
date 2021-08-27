import request from '@/utils/request'

export function queryCodeGenerationDatasourceList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/list',
    method: 'get',
    params: query
  })
}

export function createCodeGenerationDatasource (data) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/create',
    method: 'post',
    data
  })
}

export function updateCodeGenerationDatasource (data) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/update',
    method: 'post',
    data
  })
}

export function updateCodeGenerationDatasourceStatus (codeGenerationDatasourceId, status) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/status/' + codeGenerationDatasourceId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteCodeGenerationDatasource (data) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/batch/delete',
    method: 'post',
    data
  })
}

export function deleteCodeGenerationDatasource (id) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/delete/' + id,
    method: 'post'
  })
}

export function checkCodeGenerationDatasourceExist (data) {
  return request({
    url: '/gitegg-plugin-code/code/generation/datasource/check',
    method: 'post',
    params: data
  })
}
