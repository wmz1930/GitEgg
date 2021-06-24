import request from '@/utils/request'

export function fetchList (query) {
  return request({
    url: '/gitegg-service-base/log/list',
    method: 'get',
    params: query
  })
}
