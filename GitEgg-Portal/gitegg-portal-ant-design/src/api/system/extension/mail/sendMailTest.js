import request from '@/utils/request'

export function testSendSimpleMail (data) {
  return request({
    url: '/gitegg-service-extension/extension/mail/test/send/simple',
    method: 'post',
    data
  })
}

export function testSendHtmlMail (data) {
    return request({
      url: '/gitegg-service-extension/extension/mail/test/send/html',
      method: 'post',
      data
    })
}

export function testSendTemplateMail (data) {
    return request({
      url: '/gitegg-service-extension/extension/mail/test/send/template',
      method: 'post',
      data
    })
}

export function testSendAttachmentMail (data) {
    return request({
      url: '/gitegg-service-extension/extension/mail/test/send/attachment',
      method: 'post',
      data
    })
}

export function testSendAsyncAttachmentMail (data) {
    return request({
      url: '/gitegg-service-extension/extension/mail/test/send/async/attachment',
      method: 'post',
      data
    })
}
