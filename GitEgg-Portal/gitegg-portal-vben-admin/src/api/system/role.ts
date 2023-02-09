import { defHttp } from '/@/utils/http/axios';

export function getRoleList(query) {
  return defHttp.get({
    url: '/gitegg-service-system/role/list',
    params: query,
  });
}

export function getRoleListAll(data) {
  return defHttp.get({
    url: '/gitegg-service-system/role/all',
    data,
  });
}

export function createRole(data) {
  return defHttp.post({
    url: '/gitegg-service-system/role/create',
    data,
  });
}

export function updateRole(data) {
  return defHttp.post({
    url: '/gitegg-service-system/role/update',
    data,
  });
}

export function updateRoleStatus(roleId, status) {
  return defHttp.post({
    url: '/gitegg-service-system/role/status/' + roleId + '/' + status,
  });
}

export function batchDeleteRole(data) {
  return defHttp.post({
    url: '/gitegg-service-system/role/batch/delete',
    data,
  });
}

export function deleteRole(id) {
  return defHttp.post({
    url: '/gitegg-service-system/role/delete/' + id,
  });
}

export function queryRoleResource(roleId) {
  return defHttp.get({
    url: '/gitegg-service-system/role/resource/' + roleId,
  });
}

export function updateRoleResources(data) {
  return defHttp.post({
    url: '/gitegg-service-system/role/resource/update',
    data,
  });
}

export function checkRoleExist(data) {
  return defHttp.post({
    url: '/gitegg-service-system/role/check',
    data,
  });
}

export function exportRoleList(data) {
  return defHttp.post({
    url: '/gitegg-service-system/role/export',
    responseType: 'blob',
    data,
  });
}

export function importRoleList(formData) {
  return defHttp.post({
    url: '/gitegg-service-system/role/import',
    data: formData,
  });
}

export function downloadRoleTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-system/role/download/template',
    responseType: 'blob',
    params: query,
  });
}
