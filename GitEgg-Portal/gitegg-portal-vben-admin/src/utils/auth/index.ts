import { Persistent, BasicKeys } from '/@/utils/cache/persistent';
import { CacheTypeEnum, TOKEN_KEY, REFRESH_TOKEN_KEY } from '/@/enums/cacheEnum';
import projectSetting from '/@/settings/projectSetting';

const { permissionCacheType } = projectSetting;
const isLocal = permissionCacheType === CacheTypeEnum.LOCAL;

export function getToken() {
  return getAuthCache(TOKEN_KEY);
}

export function getRrefreshToken() {
  return getAuthCache(REFRESH_TOKEN_KEY);
}

export function getAuthCache<T>(key: BasicKeys) {
  const fn = isLocal ? Persistent.getLocal : Persistent.getSession;
  return fn(key) as T;
}

export function getAuthCacheWithTimeOut<T>(key: string) {
  return Persistent.getLocalKeyString(key) as T;
}

export function setAuthCache(key: BasicKeys, value) {
  const fn = isLocal ? Persistent.setLocal : Persistent.setSession;
  return fn(key, value, true);
}

// 增加带超时的缓存
export function setAuthCacheWithTimeOut(key: string, value, timeout) {
  return Persistent.setLocalTimeOut(key, value, true, timeout);
}

export function removeAuthCacheWithTimeOut(key: string) {
  return Persistent.removeLocalTimeOut(key);
}

export function clearAuthCache(immediate = true) {
  const fn = isLocal ? Persistent.clearLocal : Persistent.clearSession;
  return fn(immediate);
}
