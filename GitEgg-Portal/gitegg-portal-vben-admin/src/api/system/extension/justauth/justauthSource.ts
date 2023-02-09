import { defHttp } from '/@/utils/http/axios';

export function queryJustAuthSourceList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/source/list',
    params: query,
  });
}

export function createJustAuthSource(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/source/create',
    data,
  });
}

export function updateJustAuthSource(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/source/update',
    data,
  });
}

export function updateJustAuthSourceStatus(justAuthSourceId, status) {
  return defHttp.post({
    url:
      '/gitegg-service-extension/extension/justauth/source/status/' +
      justAuthSourceId +
      '/' +
      status,
  });
}

export function batchDeleteJustAuthSource(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/source/batch/delete',
    data,
  });
}

export function deleteJustAuthSource(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/source/delete/' + id,
  });
}

export function checkJustAuthSourceExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/source/check',
    params: data,
  });
}

export function downloadJustAuthSourceList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/source/download',
    responseType: 'blob',
    params: query,
  });
}

export function uploadJustAuthSource(formData) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/source/upload',
    data: formData,
  });
}

export function downloadJustAuthSourceTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/source/download/template',
    responseType: 'blob',
    params: query,
  });
}
