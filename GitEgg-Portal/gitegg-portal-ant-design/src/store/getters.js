const getters = {
  isMobile: state => state.app.isMobile,
  lang: state => state.app.lang,
  theme: state => state.app.theme,
  color: state => state.app.color,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  nickname: state => state.user.nickname,
  welcome: state => state.user.welcome,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  userInfo: state => state.user.info,
  addRouters: state => state.permission.addRouters,
  multiTab: state => state.app.multiTab
}

export default getters
