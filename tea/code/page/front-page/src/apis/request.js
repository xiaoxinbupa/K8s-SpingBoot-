import axios from 'axios'
import store from '@/store'

import {
  Message
} from 'element-ui'
import { blobToJson } from '@/utils/download-file.js'

const defConfig = {
  headers: {
    'content-type': 'application/json'
  },
  timeout: 60000,
  withCredentials: true,
  nonConcurrent: true
}
const instance = axios.create(defConfig)

instance.interceptors.request.use(config => {
  // 控制仅开发环境可启用 mock-server
  /* if (process.env.NODE_ENV === 'development' && config.useMockServer === true) {
    config.baseURL = process.env.VUE_APP_MOCK_SERVER
  } */
  // 暂时先写死的Authorization
  // config.headers.Authorization = '{"userName":"zhangmin46","userId":5003}'
  config.headers.Authorization = store.state.user.token || ''
  return config
}, error => {
  return Promise.reject(error)
})

instance.interceptors.response.use(response => {
  /**
   * 接口业务处理成功，只返回业报文中的 data;
   * 处理失败，返回整个报文，在catch 中捕获，通过 ‘serverError’ 是否为 true 区分是否是 http请求错误；
   * @example  response.data = {"code": 0,"data": 0,"message": ""}
   */

  // eslint-disable-next-line
  if (response.data.code == '0') { // 业务响应成功，不确认各个系统返回的类型是 string 还是 int
    if (response.config.returnAllResponse) { // 是否返回全部报文
      return response
    } else {
      return response.data.data
    }
  } else {
    if (!response.config.hideGlobalMsg) { // 是否隐藏全局默认错误提示信息
      Message.warning(response.data.message)
    }
    // eslint-disable-next-line
    if(response.data.code == '1000'){
      store.dispatch('user/resetToken').then(() => {
        location.reload()
      })
    }
    if (response.config.returnAllResponse) { // 是否返回全部报文
      return Promise.reject(response)
    } else {
      return Promise.reject(response.data)
    }
  }
}, async error => {
  if (error.response) {
    if (!error.response.config.hideGlobalMsg) { // 是否隐藏全局默认错误提示信息
      let msg_ = ''
      if (error.response.status === 500 && error.response.config.responseType === 'blob') {
        // 处理二进制文件流模式，一般用于文件下载失败的错误提示
        msg_ = (await blobToJson(error.response.data)).tip
      } else {
        msg_ = error.response.data.message
      }
      Message.warning(msg_)
    }
  }
  const _reject = {
    code: -10000,
    data: '',
    message: error.message,
    serverError: true
  }
  return Promise.reject(_reject)
})

export const post = (url, data, config = {}) => instance.post(url, data, config)
export const get = (url, params, config = {}) => instance.get(url, {
  params,
  ...config
})
export const del = (url, data, config = {}) => instance.delete(url, {
  data,
  ...config
})
export const put = (url, params, config = {}) => instance.put(url, params, config)

export default instance
