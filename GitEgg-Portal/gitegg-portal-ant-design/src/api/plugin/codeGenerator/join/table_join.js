import request from '@/utils/request'

export function queryTableJoinList (query) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/list',
    method: 'get',
    params: query
  })
}

export function createTableJoin (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/create',
    method: 'post',
    data
  })
}

export function updateTableJoin (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/update',
    method: 'post',
    data
  })
}

export function updateTableJoinStatus (tableJoinId, status) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/status/' + tableJoinId + '/' + status,
    method: 'post'
  })
}

export function batchDeleteTableJoin (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/batch/delete',
    method: 'post',
    data
  })
}

export function deleteTableJoin (id) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/delete/' + id,
    method: 'post'
  })
}

export function checkTableJoinExist (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/table/join/check',
    method: 'post',
    params: data
  })
}
