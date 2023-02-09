import { defHttp } from '/@/utils/http/axios';

export function queryMailChannelList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/mail/channel/list',
    params: query,
  });
}

export function createMailChannel(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/create',
    data,
  });
}

export function updateMailChannel(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/update',
    data,
  });
}

export function updateMailChannelStatus(mailChannelId, status) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/status/' + mailChannelId + '/' + status,
  });
}

export function batchDeleteMailChannel(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/batch/delete',
    data,
  });
}

export function deleteMailChannel(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/delete/' + id,
  });
}

export function checkMailChannelExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/check',
    data,
  });
}

export function downloadMailChannelList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/mail/channel/download',
    method: 'get',
    responseType: 'blob',
    params: query,
  });
}

export function uploadMailChannel(formData) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/channel/upload',
    data: formData,
  });
}

export function downloadMailChannelTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/mail/channel/download/template',
    responseType: 'blob',
    params: query,
  });
}
