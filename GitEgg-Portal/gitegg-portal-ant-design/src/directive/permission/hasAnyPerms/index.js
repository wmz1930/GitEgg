import hasAnyPerms from './hasAnyPerms'

const install = function (Vue) {
  Vue.directive('hasAnyPerms', hasAnyPerms)
}

if (window.Vue) {
  window['hasAnyPerms'] = hasAnyPerms
  Vue.use(install); // eslint-disable-line
}

hasAnyPerms.install = install
export default hasAnyPerms
