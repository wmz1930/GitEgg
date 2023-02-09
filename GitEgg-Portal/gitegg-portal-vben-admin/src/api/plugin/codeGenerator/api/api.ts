import { defHttp } from '/@/utils/http/axios';

export function getGeneratorApiList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/api/list',
    params: query,
  });
}

export function getGeneratorApiListAll(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/api/all',
    params: query,
  });
}

export function createGeneratorApi(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/create',
    data,
  });
}

export function updateGeneratorApi(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/update',
    data,
  });
}

export function updateGeneratorApiStatus(generatorApiId, status) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/status/' + generatorApiId + '/' + status,
  });
}

export function batchDeleteGeneratorApi(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/batch/delete',
    data,
  });
}

export function deleteGeneratorApi(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/delete/' + id,
  });
}

export function checkGeneratorApiExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/check',
    data,
  });
}

export function exportGeneratorApiList(query) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/export',
    responseType: 'blob',
    params: query,
  });
}

export function importGeneratorApiList(formData) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/api/import',
    data: formData,
  });
}

export function downloadGeneratorApiTemplate(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/api/download/template',
    responseType: 'blob',
    params: query,
  });
}
