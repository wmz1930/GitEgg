import { defHttp } from '/@/utils/http/axios';
// import { getMenuListResultModel } from './model/menuModel';

enum Api {
  GetMenuList = '/gitegg-service-system/resource/user/menu',
}

/**
 * @description: Get user menu based on id
 */

// export const getMenuList = () => {
//   return defHttp.get<getMenuListResultModel>({ url: Api.GetMenuList });
// };

/**
 * @description: Get user menu based on id
 */

export const getMenuList = () => {
  return defHttp.get<any>({ url: Api.GetMenuList });
};
