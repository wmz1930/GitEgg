import { defHttp } from '/@/utils/http/axios';

export function getSmsTemplateList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/sms/template/list',
    params: query,
  });
}

export function createSmsTemplate(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/create',
    data,
  });
}

export function updateSmsTemplate(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/update',
    data,
  });
}

export function updateSmsTemplateStatus(smsTemplateId, status) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/status/' + smsTemplateId + '/' + status,
  });
}

export function batchDeleteSmsTemplate(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/batch/delete',
    data,
  });
}

export function deleteSmsTemplate(id) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/delete/' + id,
  });
}

export function checkSmsTemplateExist(data) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/check',
    data,
  });
}

export function exportSmsTemplateList(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/sms/template/download',
    responseType: 'blob',
    params: query,
  });
}

export function importSmsTemplateList(formData) {
  return defHttp.post({
    url: '/gitegg-service-extension/extension/sms/template/upload',
    data: formData,
  });
}

export function downloadSmsTemplateTemplate(query) {
  return defHttp.get({
    url: '/gitegg-service-extension/extension/sms/template/download/template',
    responseType: 'blob',
    params: query,
  });
}
