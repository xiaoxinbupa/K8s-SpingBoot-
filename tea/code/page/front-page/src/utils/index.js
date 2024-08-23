import {
  MessageBox,
  Message
} from 'element-ui'
import {
  orderStatus
} from '@/apis/mall.js'

export function toCheckPayFn(orderNo, win, callback) {
  MessageBox.confirm('请在新打开的窗口中完成支付！' + (!win ? '<p style="color:#fe6c6f; padding:10px 0;">(Tips：窗口可能被拦截，请设置信任该弹窗！)</p>' : ''), '支付', {
    confirmButtonText: '支付成功',
    cancelButtonText: '支付遇到问题',
    dangerouslyUseHTMLString: true,
    type: 'info',
    center: true,
    closeOnClickModal: false,
    closeOnPressEscape: false,
    distinguishCancelAndClose: true,
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        orderStatus({
          orderNo
        }).then(res => {
          done()
          if (res.payStatus === 2) {
            Message.success('支付成功！')
            callback && callback()
          } else {
            Message.error('支付失败，请重新发起！')
          }
        })
      } else if (action === 'cancel') {
        Message.error('支付失败，请重新发起！')
        done()
      } else {
        done()
      }
    },
    callback: (action, instance) => {

    }
  })
}

export function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (const k in o) {
    if (new RegExp(`(${k})`).test(fmt)) {
      const str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}
// 给评论添加显示回复框隐藏显示属性
export function disabledTree(arr) {
  if (!arr) return []
  const deepCloneArr = JSON.parse(JSON.stringify(arr))
  if (deepCloneArr && deepCloneArr.length > 0) {
    deepCloneArr.forEach((el) => {
      el.showReplay = false
      el.children && disabledTree(el.reCommentList || [])
    })
    return deepCloneArr
  }
}
