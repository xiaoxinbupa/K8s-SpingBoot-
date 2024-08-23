import { get, post } from '@/api/request.js'

// 登录
export const login = (data) => post(`/to_passport/login`, data)

// // 注册
// export const register = (data) => post(`/register`, data, {
//   baseURL: process.env.VUE_APP_TO_PASSPORT
// })

// 图片验证码
export const validCode = () => get(`/to_passport/sso/valid/code`)
