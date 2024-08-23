import { get, post } from '@/apis/request.js'

// 查看菜单下的文章列表
export const getContentList = (data) => post(`/api/content/menu/list`, data)

// 获取热门文章列表
export const getHotContentList = (data) => post(`/api/content/hot`, data)
// 获取文章详情
export const getContentDetail = (data) => post(`/api/content/detail`, data)

// 获取内容点击
export const contentClick = (data) => post(`/api/content/click`, data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  returnAllResponse: true
})

// 内容顶
export const contentUp = (data) => post(`/api/content/up`, data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  returnAllResponse: true
})

// 内容踩
export const contentDown = (data) => post(`/api/content/down`, data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  returnAllResponse: true
})

// 类别列表
export const getCategoryList = (data) => get(`/api/category/list?menuId=${data}`, {

})

// 发表评论
export const commentAdd = (data) => post(`/api/comment/add`, data, {
  returnAllResponse: true
})

// 评论顶
export const commentUp = (data) => post(`/api/comment/up`, data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  returnAllResponse: true
})

// 评论踩
export const commentDown = (data) => post(`/api/comment/down`, data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  returnAllResponse: true
})

// 用户评论列表
export const getCommentList = (data) => post(`/api/comment/list`, data)

