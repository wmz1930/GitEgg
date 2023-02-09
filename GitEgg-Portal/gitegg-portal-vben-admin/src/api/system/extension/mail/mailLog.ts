import { defHttp } from '/@/utils/http/axios';

export function queryMailLogList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/mail/log/list',
    params: query,
  });
}

export function createMailLog(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/log/create',
    data,
  });
}

export function updateMailLog(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/log/update',
    data,
  });
}

export function batchDeleteMailLog(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/log/batch/delete',
    data,
  });
}

export function deleteMailLog(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/log/delete/' + id,
  });
}

export function checkMailLogExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/log/check',
    params: data,
  });
}

export function downloadMailLogList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/mail/log/download',
    method: 'get',
    responseType: 'blob',
    params: query,
  });
}

export function uploadMailLog(formData) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/mail/log/upload',
    data: formData,
  });
}

export function downloadMailLogTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/mail/log/download/template',
    method: 'get',
    responseType: 'blob',
    params: query,
  });
}
