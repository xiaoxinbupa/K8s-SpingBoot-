import { get, post } from '@/api/request.js'

// 类别列表
export const getCategoryTree = (data) => get(`/api/category/tree`, data)

// 获取类别详情
export const getCategoryDetail = (data) => get(`/api/category/detail`, data)

// 添加更新类别
export const categorySave = (data) => post(`/api/category/save`, data)
