import { post } from '@/api/request'

// 上传图片
export const fileUpload = (data) => post(`/api/attach/upload`, data)

