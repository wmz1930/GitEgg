import { defHttp } from '/@/utils/http/axios';

export function testSendSimpleMail(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/test/send/simple',
    data,
  });
}

export function testSendHtmlMail(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/test/send/html',
    data,
  });
}

export function testSendTemplateMail(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/test/send/template',
    data,
  });
}

export function testSendAttachmentMail(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/test/send/attachment',
    data,
  });
}

export function testSendAsyncAttachmentMail(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/test/send/async/attachment',
    data,
  });
}
