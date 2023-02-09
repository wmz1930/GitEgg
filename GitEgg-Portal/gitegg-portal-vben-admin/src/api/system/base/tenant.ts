import { defHttp } from '/@/utils/http/axios';

export function queryTenantList(query) {
  return defHttp.get({
    url: '/gitegg-service-base/base/tenant/list',
    method: 'get',
    params: query,
  });
}

export function createTenant(data) {
  return defHttp.post({
    url: '/gitegg-service-base/base/tenant/create',
    data,
  });
}

export function updateTenant(data) {
  return defHttp.post({
    url: '/gitegg-service-base/base/tenant/update',
    data,
  });
}

export function updateTenantStatus(tenantId, status) {
  return defHttp.post({
    url: '/gitegg-service-base/base/tenant/status/' + tenantId + '/' + status,
  });
}

export function batchDeleteTenant(data) {
  return defHttp.post({
    url: '/gitegg-service-base/base/tenant/batch/delete',
    data,
  });
}

export function deleteTenant(id) {
  return defHttp.post({
    url: '/gitegg-service-base/base/tenant/delete/' + id,
  });
}

export function checkTenantExist(data) {
  return defHttp.post({
    url: '/gitegg-service-base/base/tenant/check',
    data,
  });
}
