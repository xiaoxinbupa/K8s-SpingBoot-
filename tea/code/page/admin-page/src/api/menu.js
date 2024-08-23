import { get, post } from '@/api/request.js'

// 类别列表
export const getMenuList = (data) => post(`/api/menu/list`, data)

// 未绑定的类别一级列表
export const getBindList = (data) => post(`/api/category/list/bind`, data)

// 菜单详情
export const getMenuDetail = (data) => get(`/api/menu/detail`, data)

// 添加修改菜单
export const postMenuSave = (data) => post(`/api/menu/save`, data)

