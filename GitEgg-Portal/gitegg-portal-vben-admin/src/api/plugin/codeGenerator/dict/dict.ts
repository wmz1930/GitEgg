import { defHttp } from '/@/utils/http/axios';

export function getGeneratorDictList(data) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/dict/list',
    params: data,
  });
}

export function queryGeneratorDictList(data) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/dict/list/all',
    params: data,
  });
}

export function createGeneratorDict(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/create',
    data,
  });
}

export function updateGeneratorDict(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/update',
    data,
  });
}

export function updateGeneratorDictStatus(generatorDictId, generatorDictStatus) {
  return defHttp.post({
    url:
      '/gitegg-code-generator/code/generator/dict/status/' +
      generatorDictId +
      '/' +
      generatorDictStatus,
  });
}

export function deleteGeneratorDict(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/delete/' + id,
  });
}

export function batchDeleteGeneratorDict(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/batch/delete',
    data,
  });
}

export function listGeneratorDict(generatorDictCode) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/query/' + generatorDictCode,
  });
}

export function batchListGeneratorDict(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/batch/query',
    data,
  });
}

export function checkGeneratorDictExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/dict/check',
    data,
  });
}
