import { get, post } from '@/api/request.js'

// 查看文章列表
export const getContentList = (data) => post(`/api/content/list`, data)

// 文章详情
export const getContentDetail = (data) => get(`/api/content/detail`, data)

// 发布内容
export const contentSave = (data) => post(`/api/content/save`, data)

// 内容的批量上下架
export const contentEnable = (data) => post(`/api/content/enable`, data)

// 内容审核[auditStatus 0:待审核;1:审核通过;2:审核拒绝]
export const contentAudit = (data) => post(`/api/content/content/audit`, data)
