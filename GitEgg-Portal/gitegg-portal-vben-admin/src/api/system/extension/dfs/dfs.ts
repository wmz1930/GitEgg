import { defHttp } from '/@/utils/http/axios';

export function queryDfsList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/dfs/list',
    params: query,
  });
}

export function createDfs(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/create',
    data,
  });
}

export function updateDfs(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/update',
    data,
  });
}

export function updateDfsStatus(dfsId, dfsStatus) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/status/' + dfsId + '/' + dfsStatus,
  });
}

export function updateDfsDefault(dfsId) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/default/' + dfsId,
  });
}

export function batchDeleteDfs(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/batch/delete',
    data,
  });
}

export function deleteDfs(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/delete/' + id,
  });
}

export function checkDfsExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/dfs/check',
    params: data,
  });
}

export function queryDefaultDfs(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/dfs/query/default',
    params: query,
  });
}
