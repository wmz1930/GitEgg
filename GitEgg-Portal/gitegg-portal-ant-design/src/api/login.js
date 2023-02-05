import request from '@/utils/request'

const userApi = {
  Login: '/gitegg-oauth/oauth/token',
  Logout: '/gitegg-oauth/oauth/logout',
  CaptchaType: '/gitegg-oauth/oauth/captcha/type',
  ImageCaptcha: '/gitegg-oauth/oauth/captcha/image',
  ForgePassword: '/auth/forge-password',
  twoStepCode: '/auth/2step-code',
  SendSms: '/gitegg-oauth/oauth/sms/captcha/send',
  SendSmsErr: '/account/sms_err',
  // get my info
  UserInfo: '/gitegg-service-system/account/user/info',
  UserMenu: '/user/nav',
  // Social login
  SocialLoginUrl: '/gitegg-oauth/social/login/',
  SocialLoginCallback: '/gitegg-oauth/social/',
  // Social bind
  SocialBindMobile: '/gitegg-oauth/social/bind/mobile',
  SocialBindAccount: '/gitegg-oauth/social/bind/account',
  // sms
  SmsSend: '/gitegg-service-extension/extension/sms/code/send',
  SmsCheckPre: '/gitegg-service-extension/extension/sms/check/code/pre',
  SmsCheck: '/gitegg-service-extension/extension/sms/check/code',
  // account
  SmsRegisterSend: '/gitegg-service-system/account/register/sms/send',
  Register: '/gitegg-service-system/account/register',
  CheckUserExist: '/gitegg-service-system/account/register/check',
  // change password
  PwdSmsSend: '/gitegg-service-system/account/pwd/sms/send',
  SmsChangePassword: '/gitegg-service-system/account/pwd/update'
}

export default userApi

/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
export function login (parameter) {
  return request({
    url: userApi.Login,
    authenticationScheme: 'Basic',
    method: 'post',
    headers: { 'Authorization': `Basic ${process.env.VUE_APP_CLIENT_SECRET}` },
    skipAuthRefresh: true,
    data: parameter
  })
}

export function logout (parameter) {
  return request({
    url: userApi.Logout,
    method: 'post',
    skipAuthRefresh: true,
    data: parameter
  })
}

export function getCaptchaType () {
  return request({
    url: userApi.CaptchaType,
    method: 'get'
  })
}

export function getImageCaptcha () {
  return request({
    url: userApi.ImageCaptcha,
    method: 'get'
  })
}

export function getSmsCaptcha (parameter) {
  return request({
    url: userApi.SendSms,
    method: 'post',
    data: parameter
  })
}

export function getInfo () {
  return request({
    url: userApi.UserInfo,
    method: 'get'
  })
}

export function getCurrentUserNav () {
  return request({
    url: userApi.UserMenu,
    method: 'get'
  })
}

/**
 * get user 2step code open?
 * @param parameter {*}
 */
export function get2step (parameter) {
  return request({
    url: userApi.twoStepCode,
    method: 'post',
    data: parameter
  })
}

export function getSocialLoginUrl (socialType) {
  return request({
    url: userApi.SocialLoginUrl + socialType,
    method: 'get'
  })
}

export function socialLoginCallback (socialType, parameter) {
  return request({
    url: userApi.SocialLoginCallback + socialType + '/callback',
    method: 'get',
    params: parameter
  })
}

export function sendSmsCode (parameter) {
  return request({
    url: userApi.SmsSend,
    method: 'post',
    data: parameter
  })
}

export function checkSmsCode (parameter) {
  return request({
    url: userApi.SmsCheckPre,
    method: 'get',
    params: parameter
  })
}

export function smsRegisterSend (parameter) {
  return request({
    url: userApi.SmsRegisterSend,
    method: 'post',
    data: parameter
  })
}

export function checkUserExist (data) {
  return request({
    url: userApi.CheckUserExist,
    method: 'post',
    data
  })
}

export function userRegister (parameter) {
  return request({
    url: userApi.Register,
    method: 'post',
    data: parameter
  })
}

export function userBindMobile (parameter) {
  return request({
    url: userApi.SocialBindMobile,
    method: 'post',
    data: parameter
  })
}

export function userBindAccount (parameter) {
  return request({
    url: userApi.SocialBindAccount,
    method: 'post',
    data: parameter
  })
}

// 发送修改密码短信
export function smsPwdSend (parameter) {
  return request({
    url: userApi.PwdSmsSend,
    method: 'post',
    data: parameter
  })
}

// 执行修改密码操作
export function smsChangePassword (parameter) {
  return request({
    url: userApi.SmsChangePassword,
    method: 'post',
    data: parameter
  })
}
