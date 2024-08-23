import { getContentList, getHotContentList } from '@/apis/article.js'

const state = {
  menuId: null,
  categoryId: null,
  activeNum: 0
}

const mutations = {
  SET_MENU_ID: (state, data) => {
    state.menuId = data
  },
  SET_CATEGORY_ID: (state, data) => {
    state.categoryId = data
  },
  SET_ACTIVE_NUM: (state, data) => {
    state.activeNum = data
  }
}
const actions = {
  // 获取菜单下的文章列表
  getContentListData({ commit }, data) {
    return new Promise((resolve, reject) => {
      getContentList({ ...data }).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
  // 查看热门文章列表
  getHotContentList({ commit }, data) {
    return new Promise((resolve, reject) => {
      getHotContentList({ ...data }).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },

  setMenuId({ commit }, data) {
    commit('SET_MENU_ID', data.menuId)
  },
  setCategoryId({ commit }, data) {
    commit('SET_CATEGORY_ID', data.categoryId)
  },
  setActiveNum({ commit }, data) {
    commit('SET_ACTIVE_NUM', data.activeIndex)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
