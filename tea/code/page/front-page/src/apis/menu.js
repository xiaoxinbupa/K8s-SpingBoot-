import { get } from '@/apis/request.js'

// 获取菜单列表
export const getMenuList = (data) => get(`/api/menu/list`, data)
