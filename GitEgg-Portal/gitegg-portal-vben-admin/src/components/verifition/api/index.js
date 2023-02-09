/**
 * 此处可直接引用自己项目封装好的 axios 配合后端联调
 */

import { defHttp } from '/@/utils/http/axios';
// import request from "@/api/axios.js"       //调用项目封装的axios

//获取验证图片  以及token
export function reqGet(data) {
  return defHttp.post({
    url: '/gitegg-oauth/oauth/captcha',
    data,
  });
}

//滑动或者点选验证
export function reqCheck(data) {
  return defHttp.post({
    url: '/gitegg-oauth/oauth/captcha/check',
    data,
  });
}
