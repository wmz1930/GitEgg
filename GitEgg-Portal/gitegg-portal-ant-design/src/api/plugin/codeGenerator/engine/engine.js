import request from '@/utils/request'

export function queryTableList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/engine/table/list',
    method: 'get',
    params: query
  })
}

export function queryTableFieldList (query) {
  return request({
    url: '/gitegg-plugin-code/code/generator/engine/table/field/list',
    method: 'get',
    params: query
  })
}
