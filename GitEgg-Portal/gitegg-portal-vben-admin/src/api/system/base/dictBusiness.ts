import { defHttp } from '/@/utils/http/axios';

export function getDictBusinessList(data) {
  return defHttp.get({
    url: '/gitegg-service-base/business/dict/list',
    params: data,
  });
}

export function queryDictBusinessList(data) {
  return defHttp.get({
    url: '/gitegg-service-base/business/dict/list/all',
    params: data,
  });
}

export function createDictBusiness(data) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/create',
    data,
  });
}

export function updateDictBusiness(data) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/update',
    data,
  });
}

export function updateDictBusinessStatus(dictBusinessId, dictBusinessStatus) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/status/' + dictBusinessId + '/' + dictBusinessStatus,
  });
}

export function deleteDictBusiness(id) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/delete/' + id,
  });
}

export function batchDeleteDictBusiness(data) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/batch/delete',
    data,
  });
}

export function listDictBusiness(dictBusinessCode) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/query/' + dictBusinessCode,
  });
}

export function batchListDictBusiness(data) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/batch/query',
    data,
  });
}

export function checkDictBusinessExist(data) {
  return defHttp.post({
    url: '/gitegg-service-base/business/dict/check',
    data,
  });
}
