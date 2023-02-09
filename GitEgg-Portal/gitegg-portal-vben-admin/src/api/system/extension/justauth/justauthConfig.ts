import { defHttp } from '/@/utils/http/axios';

export function queryJustAuthConfigList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/config/list',
    params: query,
  });
}

export function createJustAuthConfig(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/config/create',
    data,
  });
}

export function updateJustAuthConfig(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/config/update',
    data,
  });
}

export function updateJustAuthConfigStatus(justAuthConfigId, status) {
  return defHttp.post({
    url:
      '/gitegg-service-extension/extension/justauth/config/status/' +
      justAuthConfigId +
      '/' +
      status,
  });
}

export function batchDeleteJustAuthConfig(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/config/batch/delete',
    data,
  });
}

export function deleteJustAuthConfig(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/config/delete/' + id,
  });
}

export function checkJustAuthConfigExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/config/check',
    params: data,
  });
}

export function downloadJustAuthConfigList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/config/download',
    responseType: 'blob',
    params: query,
  });
}

export function uploadJustAuthConfig(formData) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/config/upload',
    data: formData,
  });
}

export function downloadJustAuthConfigTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/config/download/template',
    responseType: 'blob',
    params: query,
  });
}
