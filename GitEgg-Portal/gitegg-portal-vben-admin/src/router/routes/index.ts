import type { AppRouteRecordRaw, AppRouteModule } from '/@/router/types';

import { PAGE_NOT_FOUND_ROUTE, REDIRECT_ROUTE } from '/@/router/routes/basic';

import { PageEnum } from '/@/enums/pageEnum';
import { t } from '/@/hooks/web/useI18n';

// import.meta.globEager() 直接引入所有的模块 Vite 独有的功能
const modules = import.meta.globEager('./modules/**/*.ts');
const routeModuleList: AppRouteModule[] = [];

// 加入到路由集合中
Object.keys(modules).forEach((key) => {
  const mod = modules[key].default || {};
  const modList = Array.isArray(mod) ? [...mod] : [mod];
  routeModuleList.push(...modList);
});

export const asyncRoutes = [PAGE_NOT_FOUND_ROUTE, ...routeModuleList];

// 根路由
export const RootRoute: AppRouteRecordRaw = {
  path: '/',
  name: 'Root',
  redirect: PageEnum.BASE_HOME,
  meta: {
    title: 'Root',
  },
};

export const LoginRoute: AppRouteRecordRaw = {
  path: '/login',
  name: 'Login',
  component: () => import('/@/views/sys/login/Login.vue'),
  meta: {
    title: t('routes.basic.login'),
  },
};

export const RecoverRoute: AppRouteRecordRaw = {
  path: '/recover',
  name: 'Recover',
  component: () => import('/@/views/sys/recover/Recover.vue'),
  meta: {
    title: '找回密码',
  },
};

export const RegisterRoute: AppRouteRecordRaw = {
  path: '/register',
  name: 'Register',
  component: () => import('/@/views/sys/register/Register.vue'),
  meta: {
    title: t('routes.basic.register'),
  },
};

export const SocialCallbackRoute: AppRouteRecordRaw = {
  path: '/social/:socialType/callback',
  name: 'SocialCallback',
  component: () => import('/@/views/sys/social/SocialCallback.vue'),
  meta: {
    title: '第三方联合登录',
  },
};

export const SocialBindRoute: AppRouteRecordRaw = {
  path: '/social/bind',
  name: 'SocialBind',
  component: () => import('/@/views/sys/social/SocialBind/SocialBind.vue'),
  meta: {
    title: '第三方联合登录',
  },
};

// Basic routing without permission
// 未经许可的基本路由
export const basicRoutes = [
  LoginRoute,
  RecoverRoute,
  RegisterRoute,
  SocialCallbackRoute,
  SocialBindRoute,
  RootRoute,
  REDIRECT_ROUTE,
  PAGE_NOT_FOUND_ROUTE,
];
