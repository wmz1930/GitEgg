// token key
export const TOKEN_KEY = 'TOKEN__';

export const REFRESH_TOKEN_KEY = 'REFRESH_TOKEN__';

export const LOCALE_KEY = 'LOCALE__';

// user info key
export const USER_INFO_KEY = 'USER__INFO__';

// role info key
export const ROLES_KEY = 'ROLES__KEY__';

// project config key
export const PROJ_CFG_KEY = 'PROJ__CFG__KEY__';

// lock info
export const LOCK_INFO_KEY = 'LOCK__INFO__KEY__';

export const MULTIPLE_TABS_KEY = 'MULTIPLE_TABS__KEY__';

export const APP_DARK_MODE_KEY_ = '__APP__DARK__MODE__';

// base global local key
export const APP_LOCAL_CACHE_KEY = 'COMMON__LOCAL__KEY__';

// base global session key
export const APP_SESSION_CACHE_KEY = 'COMMON__SESSION__KEY__';

// system dict cache key
export const DICT_SYSTEM_CACHE_KEY = 'DICT_SYSTEM_CACHE_KEY__';

// business dict cache key
export const DICT_BUSSINESS_CACHE_KEY = 'DICT_BUSSINESS_CACHE_KEY__';

// no clear  退出登录时，不会被清空的缓存，比如登录页的记住密码，都是加密存储
export const NO_CLEAR_START_KEY = 'NO_CLEAR_START_KEY__';

// remember account key
export const REMEMBER_ACCOUNT_KEY = 'ACCOUNT_KEY__';

// remember mobile key
export const REMEMBER_MOBILE_KEY = 'MOBILE_KEY__';

export enum CacheTypeEnum {
  SESSION,
  LOCAL,
}
