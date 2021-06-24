import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/gitegg-oauth/oauth/token',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/gitegg-oauth/oauth/logout',
    method: 'post'
  })
}

// 获取用户权限及菜单等个人信息，不要解析jwt的数据，菜单不要放到jwt中，否则jwt长度不可控
export function getInfo() {
  return request({
    url: '/gitegg-service-system/auth/user/info',
    method: 'get'
  })
}

export function getCaptchaType() {
  return request({
    url: '/gitegg-oauth/oauth/captcha/type',
    method: 'get'
  })
}

export function getImageCaptcha() {
  return request({
    url: '/gitegg-oauth/oauth/captcha/image',
    method: 'get'
  })
}
