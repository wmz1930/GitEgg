import { defHttp } from '/@/utils/http/axios';

export function getValidateList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/validate/list',
    params: query,
  });
}

export function getValidateListAll(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/validate/list/all',
    params: query,
  });
}

export function createValidate(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/create',
    data,
  });
}

export function updateValidate(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/update',
    data,
  });
}

export function updateValidateStatus(validateId, status) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/status/' + validateId + '/' + status,
  });
}

export function batchDeleteValidate(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/batch/delete',
    data,
  });
}

export function deleteValidate(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/delete/' + id,
  });
}

export function checkValidateExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/check',
    data,
  });
}

export function exportValidateList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/validate/download',
    responseType: 'blob',
    params: query,
  });
}

export function importValidateList(formData) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/validate/upload',
    data: formData,
  });
}

export function downloadValidateTemplate(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/validate/download/template',
    responseType: 'blob',
    params: query,
  });
}
