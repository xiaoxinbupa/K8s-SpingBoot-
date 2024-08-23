import { post } from '@/api/request.js'

// 库存列表页
export const getStockList = (data) => post(`/api/stock/query/page`, data)

// 初始化库存
export const initSkuStock = (data) => post(`/api/stock/initSkuStock`, data)

// 批量初始化库存
export const batchInitSkuStock = (data) => post(`/api/stock/batchInitSkuStock`, data)

// 人工补货
export const addSkuStock = (data) => post(`/api/stock/addSkuStock`, data)

