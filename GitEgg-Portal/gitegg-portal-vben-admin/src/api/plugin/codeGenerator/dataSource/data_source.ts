import { defHttp } from '/@/utils/http/axios';

export function getDataSourceList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/datasource/list',
    params: query,
  });
}

export function getDataSourceAll(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/datasource/all',
    params: query,
  });
}

export function createDataSource(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/create',
    data,
  });
}

export function updateDataSource(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/update',
    data,
  });
}

export function updateDataSourceStatus(datasourceId, status) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/status/' + datasourceId + '/' + status,
  });
}

export function batchDeleteDataSource(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/batch/delete',
    data,
  });
}

export function deleteDataSource(id) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/delete/' + id,
  });
}

export function checkDataSourceExist(data) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/check',
    data,
  });
}

export function exportDataSourceList(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/datasource/download',
    responseType: 'blob',
    params: query,
  });
}

export function importDataSourceList(formData) {
  return defHttp.post({
    url: '/gitegg-code-generator/code/generator/datasource/upload',
    data: formData,
  });
}

export function downloadDataSourceTemplate(query) {
  return defHttp.get({
    url: '/gitegg-code-generator/code/generator/datasource/download/template',
    responseType: 'blob',
    params: query,
  });
}
