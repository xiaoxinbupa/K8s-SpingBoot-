import { get, post } from '@/apis/request.js'

// 获取所有一级品类
export const getGoodTtopNodes = (data) => get(`/api/simple/goods/top-nodes`, data)
// 获取商品列表
export const getGoodList = (data) => post(`/api/simple/goods/page`, data, { returnAllResponse: true })
// 获取商品详情
export const getGoodDetail = (data) => get(`/api/simple/goods/info/id`, data)
// 获取商品详情富文本
export const getGoodDetailTxt = (data) => get(`/api/simple/goods/txt/id`, data)
// 商品加入购物车
export const addCart = (data) => post(`/api/cart/add`, data)

// 购物车首页列表
export const getCarList = (data) => get(`/api/cart/index`, data)
// 向购物车中添加购物项
export const addCartItem = (data) => post(`/api/cart/add`, data)

// 选中某一个购物项
export const selectCartItem = (data) => post(`/api/cart/selected`, data)

// 取消选中某一个购物项
export const unSelectCartItem = (data) => post(`/api/cart/unselected`, data)

// 全部选中购物项
export const selectAllCartItem = () => post(`/api/cart/selected-all`, {})

// 取消全选购物项
export const unSelectAllCartItem = () => post(`/api/cart/unselected-all`, {})

// 删除某一个购物项
export const removeCartItem = (data) => post(`/api/cart/remove`, data)

// 删除所有选中的购物项
export const removeAllCartItem = () => post(`/api/cart/remove-selected`, {})

// 修改数量
export const modifyNum = (data) => post(`/api/cart/modify`, data)

// 创建订单
export const orderCreate = (data) => post('/api/order/create', data)

// 获取收银台地址
export const orderCashDesk = (data) => get('/api/order/cashDesk', data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  returnAllResponse: true,
  hideGlobalMsg: true
})

// 订单列表
export const orderPage = (data) => get('/api/order/page', data)

//  订单列表-上方统计
export const orderStatistics = () => get('/api/order/statistics', {})

// 订单详情
export const orderDetail = (data) => get('/api/order/detail', data)

// 购物车首页
export const cartIndex = (data) => get('/api/cart/index', data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

// 订单状态 orderNo
export const orderStatus = (data) => get('/api/order/status', data)

// 地址新建
export const createAddress = (data) => post('/api/user/address/create', data)

// 地址删除
export const deleteAddress = (data) => post('/api/user/address/delete', data, {
  headers: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

// 根据地址id获取地址详情
export const getAddressById = (data) => get('/api/user/address/detail/id', data)

// 根据用户id获取地址列表
export const getAddressByUserId = (data) => get('/api/user/address/list/user_id', data)

// 地址更新
export const addressUpdate = (data) => post('/api/user/address/update', data)

// 获取城市树
export const getCityTree = (data) => get('/api/city/tree', data)

