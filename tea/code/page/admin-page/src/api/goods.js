import { get, post } from '@/api/request.js'

// 商品列表
export const getGoodsList = (data) => post(`/api/simple/goods/list`, data)

// 分类列表
export const getCategoryList = (data) => post(`/api/simple/category/list`, data)

// 商品删除
export const goodsDelete = (data) => post(`/api/simple/goods/delete`, data)

// 商品上架
export const goodsEnable = (data) => post(`/api/simple/goods/enable`, data)

// 商品下架
export const goodsDisable = (data) => post(`/api/simple/goods/disable`, data)

// 商品创建
export const goodsCreate = (data) => post(`/api/simple/goods/create`, data)

// 商品详情创建
export const goodsDetailCreate = (data) => post(`/api/simple/goods/detail/create`, data)

// 商品详情基本信息
export const getgoodsBasicDetail = (data) => get(`/api/simple/goods/detail`, data)

// 商品详情
export const getgoodsDetail = (data) => get(`/api/simple/goods/detail/detail`, data)

// 商品更新
export const goodsUpdate = (data) => post(`/api/simple/goods/update`, data)

// 关闭品类
export const categoryDisable = (data) => post(`/api/simple/category/disable`, data)

// 开启品类
export const categoryEnable = (data) => post(`/api/simple/category/enable`, data)

// 品类删除
export const categoryDelete = (data) => post(`/api/simple/category/delete`, data)

// 品类创建
export const categoryCreate = (data) => post(`/api/simple/category/create`, data)

// 品类详情
export const getCategoryDetail = (data) => get(`/api/simple/category/detail`, data)

// 品类更新
export const categoryUpdate = (data) => post(`/api/simple/category/update`, data)

