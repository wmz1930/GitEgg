import { defHttp } from '/@/utils/http/axios';

export function getResourceList(data) {
  return defHttp.get({
    url: '/gitegg-service-system/resource/tree',
    params: data,
  });
}

export function createResource(data) {
  return defHttp.post({
    url: '/gitegg-service-system/resource/create',
    data,
  });
}

export function updateResource(data) {
  return defHttp.post({
    url: '/gitegg-service-system/resource/update',
    data,
  });
}

export function updateResourceStatus(resourceId, status) {
  return defHttp.post({
    url: '/gitegg-service-system/resource/status/' + resourceId + '/' + status,
  });
}

export function deleteResource(id) {
  return defHttp.post({
    url: '/gitegg-service-system/resource/delete/' + id,
  });
}

export function checkResourceExist(data) {
  return defHttp.post({
    url: '/gitegg-service-system/resource/check',
    data,
  });
}
