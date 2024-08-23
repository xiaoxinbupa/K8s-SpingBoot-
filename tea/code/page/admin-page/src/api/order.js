import { get, post } from '@/api/request.js'

// 订单列表
export const getOrderList = (data) => get(`/api/order/page`, data)

// 订单详情
export const getOrderDetail = (data) => get(`/api/order/detail`, data)

// 取消订单
export const orderCancel = (id) => post(`/api/order/cancel?orderNo=${id}`)

