import storage from 'store'
import { login, getInfo, logout } from '@/api/login'
import { ACCESS_TOKEN, REFRESH_ACCESS_TOKEN } from '@/store/mutation-types'
import { welcome, serialize } from '@/utils/util'

const user = {
  state: {
    token: '',
    refreshToken: '',
    name: '',
    nickname: '',
    welcome: '',
    avatar: '',
    roles: [],
    permissions: [],
    menuTree: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_REFRESH_TOKEN: (state, refreshToken) => {
      state.refreshToken = refreshToken
    },
    SET_NAME: (state, { name, nickname, welcome }) => {
      state.name = name
      state.nickname = nickname
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_MENU_TREE: (state, menuTree) => {
      state.menuTree = menuTree
    },
    SET_INFO: (state, info) => {
      state.info = info
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(serialize(userInfo)).then(response => {
          const result = response.data
          storage.set(ACCESS_TOKEN, result.tokenHead + result.token, result.expiresIn * 1000)
          storage.set(REFRESH_ACCESS_TOKEN, result.refreshToken, result.refreshExpiresIn * 1000)
          commit('SET_TOKEN', result.tokenHead + result.token)
          commit('SET_REFRESH_TOKEN', result.refreshToken)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const result = response.data
          if (result.roleIdList && result.roleIdList.length > 0 && result.resourceKeyList && result.resourceKeyList.length > 0 && result.menuTree && result.menuTree.length > 0) {
            const roleIdList = result.roleIdList
            const resourceKeyList = result.resourceKeyList
            const menuTree = result.menuTree
            commit('SET_ROLES', roleIdList)
            commit('SET_PERMISSIONS', resourceKeyList)
            commit('SET_MENU_TREE', menuTree)
            commit('SET_INFO', result)
          } else {
            reject(new Error('getInfo: roles must be a non-null array !'))
          }
          commit('SET_NAME', { name: result.realName, nickname: result.nickname, welcome: welcome() })
          commit('SET_AVATAR', result.avatar)

          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 手动点击退出登录，需要到后台把token和refreshToken加入黑名单，token从头信息中取，refreshToken从参数中取
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        commit('SET_TOKEN', '')
        commit('SET_REFRESH_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        commit('SET_MENU_TREE', [])
        commit('SET_INFO', [])
        commit('SET_NAME', '')
        commit('SET_AVATAR', '')
        storage.remove(ACCESS_TOKEN)
        storage.remove(REFRESH_ACCESS_TOKEN)
        logout(serialize({ RefreshToken: storage.get(REFRESH_ACCESS_TOKEN) })).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          resolve()
        })
      })
    },

    // 超时的退出登录，token已失效，不需要去后台注销
    Timeout ({ commit }) {
      commit('SET_TOKEN', '')
      commit('SET_REFRESH_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_PERMISSIONS', [])
      commit('SET_MENU_TREE', [])
      commit('SET_INFO', [])
      commit('SET_NAME', '')
      commit('SET_AVATAR', '')
      storage.remove(ACCESS_TOKEN)
      storage.remove(REFRESH_ACCESS_TOKEN)
      return new Promise((resolve) => {
          resolve()
      })
    }

  }
}

export default user
