import { get, post } from '@/api/request.js'
// 查看审核列表
export const getTagList = (data) => get(`/api/tag/type/list`, data)

// 标签的启用
export const tagEnable = (data) => post(`/api/tag/enable`, data)

// 标签的启用
export const saveTag = (data) => post(`/api/tag/save`, data)

// 查看标签类型列表
export const tagList = (data) => post(`/api/tag/list`, data)

// 查看标签树
export const tagTypeTree = (data) => get(`/api/tag/type/tree`, data)

