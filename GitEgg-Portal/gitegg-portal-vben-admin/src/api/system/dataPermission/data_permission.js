import { defHttp } from '/@/utils/http/axios';

export function getDataPermissionRoleList(data) {
  return defHttp.get({
    url: '/gitegg-service-system/data/permission/role/list',
    params: data,
  });
}

export function createDataPermissionRole(data) {
  return defHttp.post({
    url: '/gitegg-service-system/data/permission/role/create',
    data,
  });
}

export function updateDataPermissionRole(data) {
  return defHttp.post({
    url: '/gitegg-service-system/data/permission/role/update',
    data,
  });
}

export function updateDataPermissionRoleStatus(dataPermissionRoleId, status) {
  return defHttp.post({
    url:
      '/gitegg-service-system/data/permission/role/status/' + dataPermissionRoleId + '/' + status,
  });
}

export function batchDeleteDataPermissionRole(data) {
  return defHttp.post({
    url: '/gitegg-service-system/data/permission/role/batch/delete',
    data,
  });
}

export function deleteDataPermissionRole(id) {
  return defHttp.post({
    url: '/gitegg-service-system/data/permission/role/delete/' + id,
  });
}

export function checkDataPermissionRoleExist(data) {
  return defHttp.post({
    url: '/gitegg-service-system/data/permission/role/check',
    data,
  });
}

export function queryRoleDataPermission(permissionId) {
  return defHttp.get({
    url: '/gitegg-service-system/data/permission/role/get/roles/' + permissionId,
  });
}

export function updateRoleDataPermission(data) {
  return defHttp.post({
    url: '/gitegg-service-system/data/permission/role/batch/role/update',
    data,
  });
}
