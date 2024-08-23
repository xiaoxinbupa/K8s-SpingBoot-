import { post } from '@/api/request.js'
// 查看审核列表
export const getContentAuditList = (data) => post(`/api/content/content/audit`, data)

