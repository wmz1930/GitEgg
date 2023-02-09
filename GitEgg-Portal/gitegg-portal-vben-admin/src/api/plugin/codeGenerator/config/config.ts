import { defHttp } from '/@/utils/http/axios';

export function getConfigList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/config/list',
    params: query,
  });
}

export function queryConfig(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/config/query',
    params: query,
  });
}

export function createConfig(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/config/create',
    data,
  });
}

export function updateConfig(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/config/update',
    data,
  });
}

export function updateConfigStatus(configId, status) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/config/status/' + configId + '/' + status,
  });
}

export function batchDeleteConfig(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/config/batch/delete',
    data,
  });
}

export function deleteConfig(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/config/delete/' + id,
  });
}

export function checkConfigExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/config/check',
    data,
  });
}

export function copyConfig(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/config/copy',
    params: query,
  });
}
