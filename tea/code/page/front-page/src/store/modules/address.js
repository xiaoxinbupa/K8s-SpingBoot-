import { getCityTree } from '@/apis/mall'
const state = {
  cityTree: []
}

const mutations = {
  SET_CITY_TREE: (state, data) => {
    state.cityTree = data
  }
}

const actions = {
  async getCityTree({ commit }, formData) {
    const res = await getCityTree()
    commit('SET_CITY_TREE', res)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
