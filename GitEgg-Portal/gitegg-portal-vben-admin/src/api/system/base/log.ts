import { defHttp } from '/@/utils/http/axios';

export function fetchList(query) {
  return defHttp.get({
    url: '/gitegg-service-base/log/list',
    params: query,
  });
}
