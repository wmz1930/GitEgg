import { getAuthCache, setAuthCache } from '/@/utils/auth';
import { DICT_SYSTEM_CACHE_KEY, DICT_BUSSINESS_CACHE_KEY } from '/@/enums/cacheEnum';
import { listDict, batchListDict } from '/@/api/system/base/dict';
import { listDictBusiness, batchListDictBusiness } from '/@/api/system/base/dictBusiness';

// System default cache time
export const DICT_CACHE_TIME = 60 * 60 * 2 * 1000;

// Dict
export interface Dict {
  // dictCode
  dictCode: string;
  // dictList
  dictList?: [];
  // filterMap
  filterMap?: {};
}

// DictMap
export interface DictMap {
  // dictList
  dictMap: {};
}

export function getDictCacheOnly(dict: Dict) {
  let dictMap = getAuthCache(DICT_SYSTEM_CACHE_KEY) as any;
  if (!dictMap) {
    dictMap = {};
  }
  if (dictMap[dict.dictCode]) {
    return dictMap[dict.dictCode] as Dict;
  } else {
    getDict(dict).then(function (dictReturn) {
      dictMap[dict.dictCode] = dictReturn;
      // 数据字典默认缓存2小时，重新登陆后失效
      setAuthCache(DICT_SYSTEM_CACHE_KEY, dictMap);
    });
    return dict;
  }
}

export function getDictBusinessCacheOnly(dict: Dict) {
  let dictBusinessMap = getAuthCache(DICT_BUSSINESS_CACHE_KEY) as any;
  if (!dictBusinessMap) {
    dictBusinessMap = {};
  }
  if (dictBusinessMap[dict.dictCode]) {
    return dictBusinessMap[dict.dictCode] as Dict;
  } else {
    getDictBusiness(dict).then(function (dictReturn) {
      dictBusinessMap[dict.dictCode] = dictReturn;
      // 数据字典默认缓存2小时，重新登陆后失效
      setAuthCache(DICT_BUSSINESS_CACHE_KEY, dictBusinessMap);
    });
    return dict;
  }
}

export async function getDictCache(dict: Dict) {
  let dictMap = getAuthCache(DICT_SYSTEM_CACHE_KEY) as any;
  if (!dictMap) {
    dictMap = {};
  }
  if (dictMap[dict.dictCode]) {
    return dictMap[dict.dictCode] as Dict;
  } else {
    const dictReturn = await getDict(dict);
    dictMap[dict.dictCode] = dictReturn;
    // 数据字典默认缓存2小时，重新登陆后失效
    setAuthCache(DICT_SYSTEM_CACHE_KEY, dictMap);
    return dictReturn;
  }
}

export async function getDictBusinessCache(dict: Dict) {
  let dictBusinessMap = getAuthCache(DICT_BUSSINESS_CACHE_KEY) as any;
  if (!dictBusinessMap) {
    dictBusinessMap = {};
  }
  if (dictBusinessMap[dict.dictCode]) {
    return dictBusinessMap[dict.dictCode] as Dict;
  } else {
    const dictReturn = await getDictBusiness(dict);
    dictBusinessMap[dict.dictCode] = dictReturn;
    // 数据字典默认缓存2小时，重新登陆后失效
    setAuthCache(DICT_BUSSINESS_CACHE_KEY, dictBusinessMap);
    return dictReturn;
  }
}

// 批量初始化系统字典
export async function initDictCache(dictCodeList: string[]) {
  let dictMap = getAuthCache(DICT_SYSTEM_CACHE_KEY) as any;
  if (!dictMap) {
    dictMap = {};
  }
  const dictResultMap = await batchListDict(dictCodeList);
  if (dictResultMap) {
    dictCodeList.forEach(function (dictCode) {
      if (dictResultMap[dictCode]) {
        const dict = {} as Dict;
        dict.dictList = dictResultMap[dictCode];
        dict.filterMap = {};
        dict.dictList.forEach((item) => {
          const itemDict = item as any;
          dict.filterMap[itemDict.dictCode] = itemDict.dictName;
        });
        dictMap[dictCode] = dict;
      }
    });
    // 数据字典默认缓存2小时，重新登陆后失效
    setAuthCache(DICT_SYSTEM_CACHE_KEY, dictMap);
  }
}

// 批量初始化业务字典
export async function initDictBusinessCache(dictCodeList: string[]) {
  let dictMap = getAuthCache(DICT_BUSSINESS_CACHE_KEY) as any;
  if (!dictMap) {
    dictMap = {};
  }
  const dictResultMap = await batchListDictBusiness(dictCodeList);
  if (dictResultMap) {
    dictCodeList.forEach(function (dictCode) {
      if (dictResultMap[dictCode]) {
        const dict = {} as Dict;
        dict.dictList = dictResultMap[dictCode];
        dict.filterMap = {};
        dict.dictList.forEach((item) => {
          const itemDict = item as any;
          dict.filterMap[itemDict.dictCode] = itemDict.dictName;
        });
        dictMap[dictCode] = dict;
      }
    });
    // 数据字典默认缓存2小时，重新登陆后失效
    setAuthCache(DICT_BUSSINESS_CACHE_KEY, dictMap);
  }
}

export async function getDict(dict: Dict) {
  const dictList = await listDict(dict.dictCode);
  if (dictList && dictList.length > 0) {
    dict.dictList = dictList;
    dict.filterMap = {};
    dictList.forEach((item) => {
      dict.filterMap[item.dictCode] = item.dictName;
    });
  }
  return dict;
}

export async function getDictBusiness(dict: Dict) {
  const dictBusinessList = await listDictBusiness(dict.dictCode);
  if (dictBusinessList && dictBusinessList.length > 0) {
    dict.dictList = dictBusinessList;
    dict.filterMap = {};
    dictBusinessList.forEach((item) => {
      dict.filterMap[item.dictCode] = item.dictName;
    });
  }
  return dict;
}
