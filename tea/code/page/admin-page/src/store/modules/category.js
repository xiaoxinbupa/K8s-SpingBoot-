import { getCategoryList } from '@/api/goods'
const state = {
  categoryList: [] // 商品品类列表
}

const mutations = {
  SET_CATEGORY_LIST: (state, data) => {
    state.categoryList = data
  }
}

const actions = {
  async getCategoryList({ commit }) {
    try {
      const res = await getCategoryList()
      commit('SET_CATEGORY_LIST', res)
    } catch (error) {
      console.log(error)
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

