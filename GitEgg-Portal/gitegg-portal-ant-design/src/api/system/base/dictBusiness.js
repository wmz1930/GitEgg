import request from '@/utils/request'

export function fetchDictBusinessList (data) {
  return request({
    url: '/gitegg-service-base/business/dict/list',
    method: 'get',
    params: data
  })
}

export function queryDictBusinessList (data) {
  return request({
    url: '/gitegg-service-base/business/dict/list/all',
    method: 'get',
    params: data
  })
}

export function createDictBusiness (data) {
  return request({
    url: '/gitegg-service-base/business/dict/create',
    method: 'post',
    data
  })
}

export function updateDictBusiness (data) {
  return request({
    url: '/gitegg-service-base/business/dict/update',
    method: 'post',
    data
  })
}

export function updateDictBusinessStatus (dictBusinessId, dictBusinessStatus) {
  return request({
    url: '/gitegg-service-base/business/dict/status/' + dictBusinessId + '/' + dictBusinessStatus,
    method: 'post'
  })
}

export function deleteDictBusiness (id) {
  return request({
    url: '/gitegg-service-base/business/dict/delete/' + id,
    method: 'post'
  })
}

export function batchDeleteDictBusiness (data) {
  return request({
    url: '/gitegg-service-base/business/dict/batch/delete',
    method: 'post',
    data
  })
}

export function listDictBusiness (dictBusinessCode) {
  return request({
    url: '/gitegg-service-base/business/dict/query/' + dictBusinessCode,
    method: 'post'
  })
}

export function batchListDictBusiness (data) {
  return request({
    url: '/gitegg-service-base/business/dict/batch/query',
    method: 'post',
    data
  })
}

export function checkDictBusinessExist (data) {
  return request({
    url: '/gitegg-service-base/business/dict/check',
    method: 'post',
    data
  })
}
