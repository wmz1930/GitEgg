import request from '@/utils/request'

export function fetchGeneratorDictList (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/list',
    method: 'get',
    params: data
  })
}

export function queryGeneratorDictList (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/list/all',
    method: 'get',
    params: data
  })
}

export function createGeneratorDict (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/create',
    method: 'post',
    data
  })
}

export function updateGeneratorDict (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/update',
    method: 'post',
    data
  })
}

export function updateGeneratorDictStatus (generatorDictId, generatorDictStatus) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/status/' + generatorDictId + '/' + generatorDictStatus,
    method: 'post'
  })
}

export function deleteGeneratorDict (id) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/delete/' + id,
    method: 'post'
  })
}

export function batchDeleteGeneratorDict (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/batch/delete',
    method: 'post',
    data
  })
}

export function listGeneratorDict (generatorDictCode) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/query/' + generatorDictCode,
    method: 'post'
  })
}

export function batchListGeneratorDict (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/batch/query',
    method: 'post',
    data
  })
}

export function checkGeneratorDictExist (data) {
  return request({
    url: '/gitegg-code-generator/code/generator/dict/check',
    method: 'post',
    data
  })
}
