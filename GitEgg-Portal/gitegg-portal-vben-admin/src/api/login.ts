import { defHttp } from '/@/utils/http/axios';
import { LoginParams, LogoutParams, LoginResultModel } from './sys/model/userModel';
import { AxiosAuthRefreshRequestConfig } from 'axios-auth-refresh';
import { ContentTypeEnum } from '/@/enums/httpEnum';

import { ErrorMessageMode } from '/#/axios';

export enum LoginApiEnum {
  Login = '/gitegg-oauth/oauth/token',
  Logout = '/gitegg-oauth/oauth/logout',
  CaptchaType = '/gitegg-oauth/oauth/captcha/type',
  ImageCaptcha = '/gitegg-oauth/oauth/captcha/image',
  ForgePassword = '/auth/forge-password',
  twoStepCode = '/auth/2step-code',
  SendSms = '/gitegg-oauth/oauth/sms/captcha/send',
  SendSmsErr = '/account/sms_err',
  UserInfo = '/gitegg-service-system/auth/user/info',
  UserMenu = '/user/nav',
  // Social login
  SocialLoginUrl = '/gitegg-oauth/social/login/',
  SocialLoginCallback = '/gitegg-oauth/social/',
  // Social bind
  SocialBindMobile = '/gitegg-oauth/social/bind/mobile',
  SocialBindAccount = '/gitegg-oauth/social/bind/account',
  // sms
  SmsSend = '/gitegg-service-extension/extension/sms/code/send',
  SmsCheckPre = '/gitegg-service-extension/extension/sms/check/code/pre',
  SmsCheck = '/gitegg-service-extension/extension/sms/check/code',
  // account
  SmsRegisterSend = '/gitegg-service-system/account/register/sms/send',
  Register = '/gitegg-service-system/account/register',
  CheckUserExist = '/gitegg-service-system/account/register/check',
  // change password
  PwdSmsSend = '/gitegg-service-system/account/pwd/sms/send',
  SmsChangePassword = '/gitegg-service-system/account/pwd/update',
}

/**
 * @description: user login api
 */
export function login(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginResultModel>(
    {
      url: LoginApiEnum.Login,
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * @description: user login api
 */
export function logout(params: LogoutParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post(
    {
      url: LoginApiEnum.Logout,
      skipAuthRefresh: true,
      params,
    } as AxiosAuthRefreshRequestConfig,
    {
      errorMessageMode: mode,
    },
  );
}

export function getCaptchaType() {
  return defHttp.get({
    url: LoginApiEnum.CaptchaType,
    method: 'get',
  });
}

export function getImageCaptcha() {
  return defHttp.get({
    url: LoginApiEnum.ImageCaptcha,
  });
}

export function getSmsCaptcha(parameter) {
  return defHttp.post({
    url: LoginApiEnum.SendSms,
    data: parameter,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_URLENCODED,
    },
  });
}

export function getInfo() {
  return defHttp.get({
    url: LoginApiEnum.UserInfo,
  });
}

export function getSocialLoginUrl(socialType) {
  return defHttp.get({
    url: LoginApiEnum.SocialLoginUrl + socialType,
  });
}

export function socialLoginCallback(socialType, parameter) {
  return defHttp.get({
    url: LoginApiEnum.SocialLoginCallback + socialType + '/callback',
    params: parameter,
  });
}

export function sendSmsCode(parameter) {
  return defHttp.post({
    url: LoginApiEnum.SmsSend,
    data: parameter,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_URLENCODED,
    },
  });
}

export function checkSmsCode(parameter) {
  return defHttp.get({
    url: LoginApiEnum.SmsCheckPre,
    params: parameter,
  });
}

export function smsRegisterSend(data) {
  return defHttp.post({
    url: LoginApiEnum.SmsRegisterSend,
    data: data,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_URLENCODED,
    },
  });
}

export function checkUserExist(parameter) {
  return defHttp.post({
    url: LoginApiEnum.CheckUserExist,
    data: parameter,
  });
}

export function userRegister(parameter) {
  return defHttp.post({
    url: LoginApiEnum.Register,
    data: parameter,
  });
}

export function userBindMobile(parameter) {
  return defHttp.post({
    url: LoginApiEnum.SocialBindMobile,
    data: parameter,
  });
}

export function userBindAccount(parameter) {
  return defHttp.post({
    url: LoginApiEnum.SocialBindAccount,
    data: parameter,
  });
}

// 发送修改密码短信
export function smsPwdSend(data) {
  return defHttp.post({
    url: LoginApiEnum.PwdSmsSend,
    data: data,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_URLENCODED,
    },
  });
}

// 执行修改密码操作
export function smsChangePassword(data) {
  return defHttp.post({
    url: LoginApiEnum.SmsChangePassword,
    data: data,
  });
}
