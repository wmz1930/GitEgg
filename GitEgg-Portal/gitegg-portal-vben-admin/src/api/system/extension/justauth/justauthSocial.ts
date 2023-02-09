import { defHttp } from '/@/utils/http/axios';

export function queryJustAuthSocialList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/social/list',
    params: query,
  });
}

export function createJustAuthSocial(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/social/create',
    data,
  });
}

export function updateJustAuthSocial(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/social/update',
    data,
  });
}

export function batchDeleteJustAuthSocial(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/social/batch/delete',
    data,
  });
}

export function deleteJustAuthSocial(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/social/delete/' + id,
  });
}

export function checkJustAuthSocialExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/justauth/social/check',
    params: data,
  });
}

export function downloadJustAuthSocialList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/justauth/social/download',
    responseType: 'blob',
    params: query,
  });
}
