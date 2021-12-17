import lacksPerms from './lacksPerms'

const install = function (Vue) {
  Vue.directive('lacksPerms', lacksPerms)
}

if (window.Vue) {
  window['lacksPerms'] = lacksPerms
  Vue.use(install); // eslint-disable-line
}

lacksPerms.install = install
export default lacksPerms
