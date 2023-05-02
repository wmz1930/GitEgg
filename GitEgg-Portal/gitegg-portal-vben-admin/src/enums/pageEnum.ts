export enum PageEnum {
  // basic login path
  BASE_LOGIN = '/login',
  // basic home path
  BASE_HOME = '/dashboard',
  // error page path
  ERROR_PAGE = '/exception',
  // error log page path
  ERROR_LOG_PAGE = '/error-log/list',
  // basic register path
  BASE_REGISTER = '/register',
  // basic social callback path
  BASE_SOCIAL_CALLBACK = '/social/:socialType/callback',
  // basic social bind path
  BASE_SOCIAL_BIND = '/social/bind',
}

// 使用page name判断黑白名单，使用path时，如果有路径参数，那么有问题
export enum PageNameEnum {
  // basic login name
  BASE_LOGIN = 'Login',
  // basic recover password
  BASE_RECOVER = 'Recover',
  // basic register name
  BASE_REGISTER = 'Register',
  // basic social callback name
  BASE_SOCIAL_CALLBACK = 'SocialCallback',
  // basic social bind name
  BASE_SOCIAL_BIND = 'SocialBind',
}
