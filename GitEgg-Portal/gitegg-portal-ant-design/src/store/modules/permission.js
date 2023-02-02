import { asyncRouterMap, constantRouterMap } from '@/config/router.config'
import * as viewComponent from '@/layouts'
import * as icons from '@/core/icons'

/**
 * 过滤账户是否拥有某一个权限，并将菜单从加载列表移除
 *
 * @param permission
 * @param route
 * @returns {boolean}
 */
function hasPermission (roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 单账户多角色时，使用该方法可过滤角色不存在的菜单
 *
 * @param roles
 * @param route
 * @returns {*}
 */
// eslint-disable-next-line
function hasRole(roles, route) {
  if (route.meta && route.meta.roles) {
    return route.meta.roles.includes(roles.id)
  } else {
    return true
  }
}

/**
 * 动态组装菜单时，判断一级子菜单是否全部是隐藏的，如果全是隐藏的，
 * 那么此菜单hideChildrenInMenu设置为true
 *
 * 判断当前路由的跳转是否和其中一个一级子菜单一样，如果一样，那么这个菜单作为router-view父菜单使用，component设置为RouteView
 * @param resources 一级子菜单
 * @returns {map}
 */
 function checkChildHiddenAndRedirect (resources) {
  let allHidden = false
  resources.forEach(resource => {
    // 只要有一个是展示的，那么就不算隐藏菜单
    allHidden = allHidden || resource.resourceShow
  })
  return { menuChildHidden: !allHidden }
}

function filterAsyncRouter (routerMap, roles) {
  const accessedRouters = routerMap.filter(route => {
    if (hasPermission(roles, route)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, roles)
      }
      return true
    }
    return false
  })
  return accessedRouters
}

/**
 * 递归组装路由表，返回符合用户角色权限的路由表（路由表后台配置时使用）
 * add by gitegg
 * @param resources
 */
function assembleAsyncRoutes (resources, parent) {
  const accessedRouters = []
  resources.forEach(resource => {
    var route = {}
    var resourceIcon = icons[resource.resourceIcon] ? icons[resource.resourceIcon] : resource.resourceIcon
    // 一级菜单配置为RouteView，二级菜单配置为PageView, PageView显示标签头。
    var resourceComponent = viewComponent[resource.resourcePageName] ? viewComponent[resource.resourcePageName] : viewComponent['RouteView']

    let nestedRoute = { menuChildHidden: false, redirectChild: false }
    if (resource.children && resource.children.length) {
      nestedRoute = checkChildHiddenAndRedirect(resource.children)
    }
    const redirectMenu = resource.resourceUrl.startsWith('redirect:')

    if (resource.resourceUrl.indexOf('Layout') >= 0) {
      route = {
        path: '/' + resource.resourcePath,
        component: resourceComponent,
        redirect: '/' + resource.resourceUrl,
        name: resource.resourcePageName,
        meta: {
          title: resource.resourceName,
          icon: resourceIcon
        }
      }
    } else if (resource.children && resource.children.length && (resource.resourceUrl.indexOf('nested') >= 0 || redirectMenu)) { // 包含子菜单的二级以下菜单
      const redirectPath = redirectMenu
        ? '/' + resource.resourceUrl.replace('redirect:', '')
        : `${parent && parent.path || ''}/${resource.resourcePath}/${resource?.children[0]?.resourcePath}`
      route = {
        path: `${parent && parent.path || ''}/${resource.resourcePath}`,
        component: resourceComponent,
        redirect: redirectPath,
        name: resource.resourcePageName,
        hideChildrenInMenu: resource.resourceUrl.indexOf('nested') === -1 && nestedRoute.menuChildHidden,
        meta: {
          title: resource.resourceName,
          keepAlive: resource.resourceCache,
          icon: resourceIcon
        },
        hidden: !resource.resourceShow
      }
    } else { // 最后一层菜单
      if (resource.resourcePath.indexOf('https://') === -1 && resource.resourcePath.indexOf('http://') === -1) {
        route = {
          // path: '/' + resource.resourcePath,
          path: `${parent && parent.path || ''}/${resource.resourcePath}`,
          component: () => import(`@/views/${resource.resourceUrl}`),
          name: resource.resourcePageName,
          hideChildrenInMenu: true,
          meta: {
            title: resource.resourceName,
            keepAlive: resource.resourceCache,
            icon: resourceIcon
          },
          hidden: !resource.resourceShow
        }
      } else {
        // path: item.path || `${parent && parent.path || ''}/${item.key}`,
        route = {
          // path: resource.resourcePath,
          path: `${parent && parent.path || ''}/${resource.resourcePath}`,
          component: () => import(`@/views/${resource.resourceUrl}`),
          name: resource.resourcePageName,
          hideChildrenInMenu: true,
          meta: {
            title: resource.resourceName,
            keepAlive: resource.resourceCache,
            target: '_blank',
            icon: resourceIcon
          },
          hidden: !resource.resourceShow
        }
      }
    }

    if (resource.children && resource.children.length) {
      route.children = assembleAsyncRoutes(resource.children, route)
    }
    accessedRouters.push(route)
  })
  return accessedRouters
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRouters ({ commit }, roles) {
      return new Promise(resolve => {
        const accessedRouters = filterAsyncRouter(asyncRouterMap, roles)
        commit('SET_ROUTERS', accessedRouters)
        resolve(accessedRouters)
      })
    },
    GenerateResourcesRouters ({ commit }, resources) { // add by GitEgg
      return new Promise(resolve => {
        let accessedRouters
        if (resources && resources.length > 0) {
          const basicMenus = asyncRouterMap.find(item => item.path === '/').children
          asyncRouterMap.find(item => item.path === '/').children = basicMenus.concat(assembleAsyncRoutes(resources))
          accessedRouters = asyncRouterMap
        } else {
          accessedRouters = filterAsyncRouter(asyncRouterMap, [])
        }
        accessedRouters.push({ path: '*', redirect: '/404', hidden: true })
        commit('SET_ROUTERS', accessedRouters)
        resolve(accessedRouters)
      })
    }
  }
}

export default permission
