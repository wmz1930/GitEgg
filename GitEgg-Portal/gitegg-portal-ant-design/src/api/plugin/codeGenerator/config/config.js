import request from '@/utils/request'

export function queryConfigList (query) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/list',
    method: 'get',
    params: query
  })
}

export function queryConfig (query) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/query',
    method: 'get',
    params: query
  })
}

export function createConfig (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/create',
    method: 'post',
    data
  })
}

export function updateConfig (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/update',
    method: 'post',
    data
  })
}

export function updateConfigStatus (configId, status) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/status/' + configId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteConfig (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/batch/delete',
    method: 'post',
    data
  })
}

export function deleteConfig (id) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/delete/' + id,
    method: 'post'
  })
}

export function checkConfigExist (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/check',
    method: 'post',
    data
  })
}

export function copyConfig (query) {
  return request({
    url: '/gitegg-code-generator/code/generator/config/copy',
    method: 'get',
    params: query
  })
}
