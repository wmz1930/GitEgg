import request from '@/utils/request'

export function fetchList (data) {
  return request({
    url: '/gitegg-service-base/dict/list',
    method: 'get',
    params: data
  })
}

export function createDict (data) {
  return request({
    url: '/gitegg-service-base/dict/create',
    method: 'post',
    data
  })
}

export function updateDict (data) {
  return request({
    url: '/gitegg-service-base/dict/update',
    method: 'post',
    data
  })
}

export function updateDictStatus (dictId, status) {
  return request({
    url: '/gitegg-service-base/dict/status/' + dictId + '/' + status,
    method: 'post'
  })
}

export function deleteDict (id) {
  return request({
    url: '/gitegg-service-base/dict/delete/' + id,
    method: 'post'
  })
}

export function batchDeleteDict (data) {
  return request({
    url: '/gitegg-service-base/dict/batch/delete',
    method: 'post',
    data
  })
}

export function listDict (dictCode) {
  return request({
    url: '/gitegg-service-base/dict/list/' + dictCode,
    method: 'post'
  })
}

export function checkDictExist (data) {
  return request({
    url: '/gitegg-service-base/dict/check',
    method: 'post',
    params: data
  })
}
