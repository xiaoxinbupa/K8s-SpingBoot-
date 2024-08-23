import {
  get,
  post
} from '@/apis/request.js'

// 登录
export const login = (data) => post(`passport-api/login`, data)

// 注册
export const register = (data) => post(`passport-api/register`, data)

// 图片验证码
export const validCode = () => get(`/sso/valid/code`)
