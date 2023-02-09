import { defHttp } from '/@/utils/http/axios';

export function getFieldList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/field/list',
    params: query,
  });
}

export function getFieldListAll(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/field/list/all',
    params: query,
  });
}

export function createField(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/create',
    data,
  });
}

export function editField(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/edit',
    data,
  });
}

export function updateField(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/update',
    data,
  });
}

export function updateFieldStatus(fieldId, status) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/status/' + fieldId + '/' + status,
  });
}

export function batchDeleteField(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/batch/delete',
    data,
  });
}

export function deleteField(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/delete/' + id,
  });
}

export function checkFieldExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/field/check',
    data,
  });
}
