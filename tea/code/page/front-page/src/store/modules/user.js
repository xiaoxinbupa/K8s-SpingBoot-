import { login } from '@/apis/passport.js'

const state = {
  token: '',
  name: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
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
      resolve()
      /* logout(state.token).then(() => {

      }).catch(error => {
        reject(error)
      }) */
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_NAME', '')
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
