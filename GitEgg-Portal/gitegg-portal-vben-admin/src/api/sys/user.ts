import { defHttp } from '/@/utils/http/axios';
import { LoginParams, LogoutParams, LoginResultModel, GetUserInfoModel } from './model/userModel';
import type { CreateAxiosOptions } from '/@/utils/http/axios/axiosTransform';
import { ContentTypeEnum, AuthorizationTypeEnum } from '/@/enums/httpEnum';
import { AxiosAuthRefreshRequestConfig } from 'axios-auth-refresh';

import { ErrorMessageMode } from '/#/axios';

enum Api {
  Login = '/gitegg-oauth/oauth/token',
  Logout = '/gitegg-oauth/oauth/logout',
  GetUserInfo = '/gitegg-service-system/account/user/info',
  UpdateUserInfo = '/gitegg-service-system/account/info/update',
  UpdateUserAvatar = '/gitegg-service-system/account/avatar/update',
  GetPermCode = '/gitegg-service-system/resource/user/resource',
  TestRetry = '/testRetry',
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginResultModel>(
    {
      url: Api.Login,
      authenticationScheme: AuthorizationTypeEnum.BASIC,
      headers: {
        'Content-Type': ContentTypeEnum.FORM_URLENCODED,
      },
      params: params,
    } as CreateAxiosOptions,
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

/**
 * @description: updateUserAvatar
 */
export function updateUserAvatar(data) {
  return defHttp.post({
    url: Api.UpdateUserAvatar,
    data,
  });
}

/**
 * @description: updateUserInfo
 */
export function updateUserInfo(data) {
  return defHttp.post({
    url: Api.UpdateUserInfo,
    data,
  });
}

export function getPermCode() {
  return defHttp.get<[]>({ url: Api.GetPermCode });
}

export function doLogout(params: LogoutParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post(
    {
      url: Api.Logout,
      skipAuthRefresh: true,
      data: params,
    } as AxiosAuthRefreshRequestConfig,
    {
      errorMessageMode: mode,
    },
  );
}

export function testRetry() {
  return defHttp.get(
    { url: Api.TestRetry },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}
