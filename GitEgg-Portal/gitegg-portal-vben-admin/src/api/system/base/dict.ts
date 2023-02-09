import { defHttp } from '/@/utils/http/axios';

export function getDictList(query) {
  return defHttp.get({
    url: '/gitegg-service-base/dict/list',
    params: query,
  });
}

export function queryDictList(query) {
  return defHttp.get({
    url: '/gitegg-service-base/dict/list/all',
    params: query,
  });
}

export function createDict(data) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/create',
    data,
  });
}

export function updateDict(data) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/update',
    data,
  });
}

export function updateDictStatus(dictId, status) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/status/' + dictId + '/' + status,
  });
}

export function deleteDict(id) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/delete/' + id,
  });
}

export function batchDeleteDict(data) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/batch/delete',
    data,
  });
}

export function listDict(dictCode) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/query/' + dictCode,
  });
}

export function batchListDict(data) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/batch/query',
    data,
  });
}

export function checkDictExist(data) {
  return defHttp.post({
    url: '/gitegg-service-base/dict/check',
    data,
  });
}
