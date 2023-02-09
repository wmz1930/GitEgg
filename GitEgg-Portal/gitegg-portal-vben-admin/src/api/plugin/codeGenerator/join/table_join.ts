import { defHttp } from '/@/utils/http/axios';

export function getTableJoinList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/table/join/list',
    params: query,
  });
}

export function createTableJoin(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/table/join/create',
    data,
  });
}

export function updateTableJoin(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/table/join/update',
    data,
  });
}

export function updateTableJoinStatus(tableJoinId, status) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/table/join/status/' + tableJoinId + '/' + status,
  });
}

export function batchDeleteTableJoin(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/table/join/batch/delete',
    data,
  });
}

export function deleteTableJoin(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/table/join/delete/' + id,
  });
}

export function checkTableJoinExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/table/join/check',
    data,
  });
}
