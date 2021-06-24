import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/gitegg-service-base/dict/tree',
    method: 'get',
    params: data
  })
}

export function createDict(data) {
  return request({
    url: '/gitegg-service-base/dict/create',
    method: 'post',
    data
  })
}

export function updateDict(data) {
  return request({
    url: '/gitegg-service-base/dict/update',
    method: 'post',
    data
  })
}

export function deleteDict(id) {
  return request({
    url: '/gitegg-service-base/dict/delete/' + id,
    method: 'post'
  })
}
