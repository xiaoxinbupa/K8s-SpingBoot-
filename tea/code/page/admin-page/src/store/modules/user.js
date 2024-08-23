import { login } from '@/api/passport.js'

const getDefaultState = () => {
  return {
    token: '',
    name: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}
const actions = {
  // user login
  login({ commit }, formData) {
    return new Promise((resolve, reject) => {
      login(formData).then(res => {
        commit('SET_TOKEN', `${res.jwt}`)
        commit('SET_NAME', `${res.nickname}`)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_NAME', '')
      resolve()
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_NAME', '')
      resolve()
    })
  },
  // get user info
  getInfo({ commit, state }) {
    /* return new Promise((resolve, reject) => {

    }) */
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
