import { defHttp } from '/@/utils/http/axios';

export function querySmsChannelList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/sms/channel/list',
    params: query,
  });
}

export function querySmsChannelListAll(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/sms/channel/list/all',
    params: query,
  });
}

export function createSmsChannel(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/channel/create',
    data,
  });
}

export function updateSmsChannel(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/channel/update',
    data,
  });
}

export function updateSmsChannelStatus(smsChannelId, status) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/channel/status/' + smsChannelId + '/' + status,
  });
}

export function batchDeleteSmsChannel(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/channel/batch/delete',
    data,
  });
}

export function deleteSmsChannel(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/channel/delete/' + id,
  });
}

export function checkSmsChannelExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/channel/check',
    data,
  });
}

export function downloadSmsChannelList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/sms/channel/download',
    responseType: 'blob',
    params: query,
  });
}
