import type { AppRouteModule, AppRouteRecordRaw } from '/@/router/types';
import type { Router, RouteRecordNormalized, RouteMeta } from 'vue-router';

import { isUrl } from '/@/utils/is';

import { getParentLayout, LAYOUT, EXCEPTION_COMPONENT } from '/@/router/constant';
import { cloneDeep, omit } from 'lodash-es';
import { warn } from '/@/utils/log';
import { createRouter, createWebHistory } from 'vue-router';

export type LayoutMapKey = 'LAYOUT';
const IFRAME = () => import('/@/views/sys/iframe/FrameBlank.vue');

const LayoutMap = new Map<string, () => Promise<typeof import('*.vue')>>();

LayoutMap.set('LAYOUT', LAYOUT);
LayoutMap.set('IFRAME', IFRAME);

let dynamicViewsModules: Record<string, () => Promise<Recordable>>;

// Dynamic introduction
function asyncImportRoute(routes: AppRouteRecordRaw[] | undefined) {
  dynamicViewsModules = dynamicViewsModules || import.meta.glob('../../views/**/*.{vue,tsx}');
  if (!routes) return;
  routes.forEach((item) => {
    if (!item.component && item.meta?.frameSrc) {
      item.component = 'IFRAME';
    }
    const { component, name } = item;
    const { children } = item;
    if (component) {
      const layoutFound = LayoutMap.get(component.toUpperCase());
      if (layoutFound) {
        item.component = layoutFound;
      } else {
        item.component = dynamicImport(dynamicViewsModules, component as string);
      }
    } else if (name) {
      item.component = getParentLayout(name);
    }
    children && asyncImportRoute(children);
  });
}

function dynamicImport(
  dynamicViewsModules: Record<string, () => Promise<Recordable>>,
  component: string,
) {
  const keys = Object.keys(dynamicViewsModules);
  const matchKeys = keys.filter((key) => {
    const k = key.replace('../../views', '');
    const startFlag = component.startsWith('/');
    const endFlag = component.endsWith('.vue') || component.endsWith('.tsx');
    const startIndex = startFlag ? 0 : 1;
    const lastIndex = endFlag ? k.length : k.lastIndexOf('.');
    return k.substring(startIndex, lastIndex) === component;
  });
  if (matchKeys?.length === 1) {
    const matchKey = matchKeys[0];
    return dynamicViewsModules[matchKey];
  } else if (matchKeys?.length > 1) {
    warn(
      'Please do not create `.vue` and `.TSX` files with the same file name in the same hierarchical directory under the views folder. This will cause dynamic introduction failure',
    );
    return;
  } else {
    warn('在src/views/下找不到`' + component + '.vue` 或 `' + component + '.tsx`, 请自行创建!');
    return EXCEPTION_COMPONENT;
  }
}

// Turn background objects into routing objects
// 将后台对象变成路由对象
export function transformObjToRoute<T = AppRouteModule>(routeList: AppRouteModule[]): T[] {
  routeList.forEach((route) => {
    const component = route.component as string;
    if (component) {
      if (component.toUpperCase() === 'LAYOUT') {
        route.component = LayoutMap.get(component.toUpperCase());
      } else {
        route.children = [cloneDeep(route)];
        route.component = LAYOUT;
        route.name = `${route.name}Parent`;
        route.path = '';
        const meta = route.meta || {};
        meta.single = true;
        meta.affix = false;
        route.meta = meta;
      }
    } else {
      warn('请正确配置路由：' + route?.name + '的component属性');
    }
    route.children && asyncImportRoute(route.children);
  });
  return routeList as unknown as T[];
}

/**
 * 动态组装菜单时，判断一级子菜单是否全部是隐藏的，如果全是隐藏的，
 * 那么此菜单hideChildrenInMenu设置为true
 *
 * 判断当前路由的跳转是否和其中一个一级子菜单一样，如果一样，那么这个菜单作为router-view父菜单使用，component设置为RouteView
 * @param resources 一级子菜单
 * @returns {map}
 */
function checkChildHiddenAndRedirect(resources) {
  let allHidden = false;
  resources.forEach((resource) => {
    // 只要有一个是展示的，那么就不算隐藏菜单
    allHidden = allHidden || resource.resourceShow;
  });
  return { menuChildHidden: !allHidden };
}

// 因后台数据返回格式和前端不一致，所以这里做一下转换
export function transformBackObj<T = AppRouteModule>(resources, parentPath = ''): T[] {
  const accessedRouters: AppRouteModule[] = [];
  resources.forEach((resource) => {
    const route = {} as AppRouteModule;
    route.path = resource.resourcePath;
    route.name = resource.resourceKey;

    route.meta = {} as RouteMeta;
    route.meta.title = resource.resourceName;
    // 可以读取自定义菜单图表
    route.meta.icon =
      resource?.resourceIcon?.indexOf(':') == -1
        ? resource?.resourceIcon + '|svg'
        : resource?.resourceIcon;
    route.meta.orderNo = resource.resourceLevel;
    route.meta.hideMenu = !resource.resourceShow;
    route.meta.ignoreKeepAlive = !resource.resourceCache;

    // 动态组装菜单时，判断一级子菜单是否全部是隐藏的，如果全是隐藏的，那么此菜单hideChildrenInMenu设置为true
    let nestedRoute = { menuChildHidden: false };
    if (resource.children && resource.children.length) {
      nestedRoute = checkChildHiddenAndRedirect(resource.children);
    }

    const redirectMenu = resource.resourceUrl.startsWith('redirect:');

    // 一级菜单
    if (resource.resourceUrl.toUpperCase() === 'LAYOUT') {
      route.component = 'LAYOUT';
      // 除了 layout 对应的 path 前面需要加 /，其余子路由都不要以/开头
      route.path = `/${route.path}`;
      // 菜单没有实际页面，这里配置redirect
      route.redirect = resource.resourcePageName;
    } else if (resource.resourceUrl.toUpperCase() === 'NESTED' || redirectMenu) {
      // 二级菜单
      route.name = resource.resourcePageName;
      // 菜单没有实际页面，这里配置redirect
      route.redirect = redirectMenu
        ? '/' + resource.resourceUrl.replace('redirect:', '')
        : resource.resourceUrl || resource.resourcePageName;
      route.meta.hideChildrenInMenu =
        resource.resourceUrl.toUpperCase() !== 'NESTED' && nestedRoute.menuChildHidden;
    } else {
      // 非LAYOUT和NESTED不允许有子菜单
      route.meta.hideChildrenInMenu = true;
      // 子菜单
      // 判断是内嵌外链接还是重新打开页面的外链接
      if (
        resource.resourceUrl?.startsWith('https://') ||
        resource.resourceUrl?.startsWith('http://')
      ) {
        // 如果path和url相同，那么直接跳出外部页面，如果不相同，则在内部加载
        if (resource.resourceUrl === resource.resourcePath) {
          route.component = 'LAYOUT';
        } else {
          // 内部加载
          route.meta.frameSrc = resource.resourceUrl;
        }
      } else {
        route.component = resource.resourceUrl;
      }

      // 当设置为不在菜单栏展示时，需要设置左侧选中菜单
      if (
        !resource.resourceShow &&
        resource.currentActivePath &&
        '' !== resource.currentActivePath
      ) {
        route.meta.currentActiveMenu = resource.currentActivePath;
      }
    }

    let fullPath = '';
    if (!(route.path.startsWith('/') || isUrl(route.path))) {
      // path doesn't start with /, nor is it a url, join parent path
      // 路径不以 / 开头，也不是 url，加入父路径
      fullPath = `${parentPath}/${route.path}`;
    }

    if (resource.children && resource.children.length) {
      route.children = transformBackObj(
        resource.children,
        route.meta?.hidePathForChildren ? parentPath : fullPath,
      );
    }
    accessedRouters.push(route);
  });
  return accessedRouters as unknown as T[];
}

/**
 * Convert multi-level routing to level 2 routing
 * 将多级路由转换为 2 级路由
 */
export function flatMultiLevelRoutes(routeModules: AppRouteModule[]) {
  const modules: AppRouteModule[] = cloneDeep(routeModules);

  for (let index = 0; index < modules.length; index++) {
    const routeModule = modules[index];
    // 判断级别是否 多级 路由
    if (!isMultipleRoute(routeModule)) {
      // 声明终止当前循环， 即跳过此次循环，进行下一轮
      continue;
    }
    // 路由等级提升
    promoteRouteLevel(routeModule);
  }
  return modules;
}

// Routing level upgrade
// 路由等级提升
function promoteRouteLevel(routeModule: AppRouteModule) {
  // Use vue-router to splice menus
  // 使用vue-router拼接菜单
  // createRouter 创建一个可以被 Vue 应用程序使用的路由实例
  let router: Router | null = createRouter({
    routes: [routeModule as unknown as RouteRecordNormalized],
    // history: createWebHashHistory(),
    // 使用history模式不使用hash模式
    history: createWebHistory(),
  });
  // getRoutes： 获取所有 路由记录的完整列表。
  const routes = router.getRoutes();
  // 将所有子路由添加到二级路由
  addToChildren(routes, routeModule.children || [], routeModule);
  router = null;

  // omit lodash的函数 对传入的item对象的children进行删除
  routeModule.children = routeModule.children?.map((item) => omit(item, 'children'));
}

// Add all sub-routes to the secondary route
// 将所有子路由添加到二级路由
function addToChildren(
  routes: RouteRecordNormalized[],
  children: AppRouteRecordRaw[],
  routeModule: AppRouteModule,
) {
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    const route = routes.find((item) => item.name === child.name);
    if (!route) {
      continue;
    }
    routeModule.children = routeModule.children || [];
    if (!routeModule.children.find((item) => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteModule);
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule);
    }
  }
}

// Determine whether the level exceeds 2 levels
// 判断级别是否超过2级
function isMultipleRoute(routeModule: AppRouteModule) {
  // Reflect.has 与 in 操作符 相同, 用于检查一个对象(包括它原型链上)是否拥有某个属性
  if (!routeModule || !Reflect.has(routeModule, 'children') || !routeModule.children?.length) {
    return false;
  }

  const children = routeModule.children;

  let flag = false;
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    if (child.children?.length) {
      flag = true;
      break;
    }
  }
  return flag;
}
