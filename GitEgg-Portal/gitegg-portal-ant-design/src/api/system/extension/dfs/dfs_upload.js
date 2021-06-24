import request from '@/utils/request'

export function dfsUpload (formData) {
    return request({
      url: '/gitegg-service-extension/extension/upload/file',
      method: 'post',
      data: formData
    })
}
export function dfsGetFileUrl (query) {
    return request({
      url: '/gitegg-service-extension/extension/get/file/url',
      method: 'get',
      params: query
    })
}

export function dfsDownloadFileUrl (query) {
  return request({
    url: '/gitegg-service-extension/extension/get/file/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
