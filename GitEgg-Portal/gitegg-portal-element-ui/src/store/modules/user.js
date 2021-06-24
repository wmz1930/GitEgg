import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, setRefreshToken, getRefreshToken, removeToken } from '@/utils/auth'
import { serialize } from '@/utils'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_REFRESH_TOKEN: (state, refreshToken) => {
    state.refreshToken = refreshToken
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  }
}

const actions = {
  // user login
  loginByPassword({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(serialize(userInfo)).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.tokenHead + data.token)
        commit('SET_REFRESH_TOKEN', data.tokenHead + data.refreshToken)
        setToken(data.tokenHead + data.token)
        setRefreshToken(data.tokenHead + data.refreshToken)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user login
  refreshToken({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login({
        grant_type: 'refresh_token',
        refresh_token: getRefreshToken()
      }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.tokenHead + data.token)
        commit('SET_REFRESH_TOKEN', data.tokenHead + data.refreshToken)
        setToken(data.tokenHead + data.token)
        setRefreshToken(data.tokenHead + data.refreshToken)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response
        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { roleKeyList, userName, avatar, introduction } = data

        // roles must be a non-empty array
        if (roleKeyList && roleKeyList.length > 0) {
          commit('SET_ROLES', roleKeyList)
          // commit('SET_PERMISSIONS', stringResources)
        } else {
          reject('getInfo: roles must be a non-null array !')
        }
        commit('SET_NAME', userName)
        commit('SET_AVATAR', avatar)
        commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        removeToken()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_PERMISSIONS', [])
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
