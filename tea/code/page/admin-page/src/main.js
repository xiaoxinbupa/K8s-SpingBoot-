import Vue from 'vue'
import axios from 'axios'
Vue.prototype.axios = axios

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

ElementUI.Dialog.props.closeOnClickModal.default = false // 全局禁止通过点击 modal 关闭 Dialog
ElementUI.Dialog.props.closeOnPressEscape = false // 全局禁止通过按下 ESC 关闭 Dialog

Vue.use(ElementUI, { size: 'medium' })

Vue.config.productionTip = false

/* 无权限提示，非业务功能 */
Vue.prototype.$showNoAuthMsg = () => {
  ElementUI.Message.warning('无操作权限！')
}

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
