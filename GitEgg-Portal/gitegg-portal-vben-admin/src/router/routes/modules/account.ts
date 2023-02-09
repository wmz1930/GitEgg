import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const account: AppRouteModule = {
  path: '/account',
  name: 'AccountPage',
  component: LAYOUT,
  redirect: '/account/settings',
  meta: {
    hideChildrenInMenu: true,
    title: t('routes.account.account'),
    hideMenu: true,
  },
  children: [
    {
      path: 'center',
      name: 'AccountCenterPage',
      component: () => import('/@/views/system/account/center/index.vue'),
      meta: {
        title: t('routes.account.center'),
        hideMenu: true,
      },
    },
    {
      path: 'settings',
      name: 'AccountSettingsPage',
      component: () => import('/@/views/system/account/settings/index.vue'),
      meta: {
        title: t('routes.account.settings'),
        hideMenu: true,
      },
    },
  ],
};

export default account;
