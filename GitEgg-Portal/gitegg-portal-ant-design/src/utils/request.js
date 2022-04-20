import axios from 'axios'
import Vue from 'vue'
import createAuthRefreshInterceptor from 'axios-auth-refresh'
import store from '@/store'
import storage from 'store'
import { serialize } from '@/utils/util'
import notification from 'ant-design-vue/es/notification'
import modal from 'ant-design-vue/es/modal'
import { VueAxios } from './axios'
import { ACCESS_TOKEN, REFRESH_ACCESS_TOKEN } from '@/store/mutation-types'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 30000 // 请求超时时间
})

// 当token失效时，需要调用的刷新token的方法
const refreshAuthLogic = failedRequest =>
  axios.post(process.env.VUE_APP_API_BASE_URL + '/gitegg-oauth/oauth/token',
  serialize({
      grant_type: 'refresh_token',
      refresh_token: storage.get(REFRESH_ACCESS_TOKEN)
    }),
    {
      headers: { 'TenantId': process.env.VUE_APP_TENANT_ID, 'Content-Type': 'application/x-www-form-urlencoded', 'Authorization': `Basic ${process.env.VUE_APP_CLIENT_SECRET}` },
      skipAuthRefresh: true // 刷新token请求过期，不再进行刷新
    }
    ).then(tokenRefreshResponse => {
      if (tokenRefreshResponse.status === 200 && tokenRefreshResponse.data && tokenRefreshResponse.data.success) {
        const result = tokenRefreshResponse.data.data
        storage.set(ACCESS_TOKEN, result.tokenHead + result.token, result.expiresIn * 1000)
        storage.set(REFRESH_ACCESS_TOKEN, result.refreshToken, result.refreshExpiresIn * 1000)
        failedRequest.response.config.headers['Authorization'] = result.tokenHead + result.token
      } else if (tokenRefreshResponse.status === 200 && tokenRefreshResponse.data &&
        !tokenRefreshResponse.data.success && tokenRefreshResponse.data.code === 401) {
          store.dispatch('Timeout').then(async () => {
            window.location.reload()
        })
      }
      return Promise.resolve()
})

// 初始化刷新token拦截器
createAuthRefreshInterceptor(request, refreshAuthLogic, {
  pauseInstanceWhileRefreshing: true // 当刷新token执行时，暂停其他请求
})

// 异常拦截处理器
const errorHandler = (error) => {
  if (error.response) {
    const data = error.response.data
    if (error.response.status === 403) {
      notification.error({
        message: '禁止访问',
        description: data.message
      })
    } else if (error.response.status === 401 && !(data.result && data.result.isLogin)) {
       // 当刷新token超时，则调到登录页面
       modal.warn({
        title: '登录超时',
        content: '由于您长时间未操作， 为确保安全， 请重新登录系统进行后续操作 ！',
        okText: '重新登录',
        onOk () {
            store.dispatch('Timeout').then(() => {
                window.location.reload()
            })
         }
      })
    }
  }
  return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token && config.authenticationScheme !== 'Basic') {
    config.headers['Authorization'] = token
  }
  config.headers['TenantId'] = process.env.VUE_APP_TENANT_ID
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  const res = response.data
  if (res.code) {
    if (res.code !== 200) {
      notification.error({
        message: '操作失败',
        description: res.msg
      })
      Vue.prototype.$loading.hide()
      return Promise.reject(res || 'Error')
    } else {
      return response.data
    }
  } else {
    return response
  }
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export {
  installer as VueAxios,
  request as axios
}
