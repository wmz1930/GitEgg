/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username?: string;
  password?: string;
  grant_type: string;
  captcha_type?: string;
  captcha_key?: string;
  captcha_code?: string;
  sliding_type?: string;
  captcha_verification?: string;
  // mobile login
  phoneNumber?: string;
  code?: string;
  captcha?: string;
  smsCode?: string;
  // social login
  social_key?: string;
}

/**
 * @description: Logout interface parameters
 */
export interface LogoutParams {
  refreshToken: string;
}

export interface RoleInfo {
  roleName: string;
  value: string;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  token: string;
  refreshToken: string;
  tokenHead: string;
  expiresIn: string;
  exp: string;
  refreshExpiresIn: string;
  refreshExp: string;
}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  roles: RoleInfo[];
  // 用户id
  id: string | number;
  // 用户名
  account: string;
  // 用户名
  nickname: string;
  // 真实名字
  realName: string;
  // 头像
  avatar: string;
  // 介绍
  desc?: string;
  // 机构id
  organizationId: string;
  // 机构名称
  organizationName: string;
  // 角色id列表
  roleIdList: string[];
  // 角色列表
  roleKeyList: string[];
  // 角色名称列表
  roleNameList: string[];
}
