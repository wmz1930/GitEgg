import { defHttp } from '/@/utils/http/axios';

export function getMiniappList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/wx/miniapp/list',
    params: query,
  });
}

export function createMiniapp(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/create',
    data,
  });
}

export function updateMiniapp(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/update',
    data,
  });
}

export function updateMiniappStatus(miniappId, status) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/status/' + miniappId + '/' + status,
  });
}

export function batchDeleteMiniapp(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/batch/delete',
    data,
  });
}

export function deleteMiniapp(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/delete/' + id,
  });
}

export function checkMiniappExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/check',
    params: data,
  });
}

export function exportMiniappList(query) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/export',
    responseType: 'blob',
    params: query,
  });
}

export function importMiniappList(formData) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/wx/miniapp/import',
    data: formData,
  });
}

export function downloadMiniappTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/wx/miniapp/download/template',
    responseType: 'blob',
    params: query,
  });
}
