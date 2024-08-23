// 订单步骤
export const stepArray = [
  { name: '提交订单', value: 1 },
  { name: '支付订单', value: 2 },
  { name: '平台发货', value: 3 },
  { name: '确认收货', value: 4 },
  { name: '完成评价', value: 5 }
]

// 订单渠道
export const orderChannelOpts = [
  {
    label: '全部',
    value: null
  },
  {
    label: 'PC',
    value: 1
  },
  {
    label: 'APP',
    value: 2
  }
]

// 订单状态
export const orderStatusOpts = [
  {
    label: '全部',
    value: null
  },
  {
    label: '待付款',
    value: 10
  },
  {
    label: '待发货',
    value: 20
  },
  {
    label: '待收货',
    value: 30
  },
  {
    label: '交易成功',
    value: 40
  },
  {
    label: '取消',
    value: -1
  }
]
// 支付渠道
export const payChannelOpts = [
  {
    label: '全部',
    value: null
  },
  {
    label: '支付宝',
    value: 1
  },
  {
    label: '微信',
    value: 2
  }
]
// 支付方式
export const payMethodOpts = [
  {
    label: '全部',
    value: null
  },
  {
    label: '在线支付',
    value: 1
  },
  {
    label: '货到付款',
    value: 2
  }
]

// 订单类型
export const orderTypeOpts = [
  {
    label: '全部',
    value: null
  },
  {
    label: '普通订单',
    value: 1
  },
  {
    label: '秒杀订单',
    value: 2
  }
]
// 待支付: 10, 已支付:20, 订单取消: -10,-20,-30
export const afterStatusObj = {
  '10': '待支付',
  '20': '待发货',
  '30': '待收货',
  '40': '交易成功',
  '-10': '订单取消',
  '-20': '订单取消',
  '-30': '订单取消'
}
