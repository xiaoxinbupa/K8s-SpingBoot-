import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import qs from 'qs'
import '@/styles/index.scss' // global css
import '@/assets/iconfont/iconfont.css'
import '@/permission' // permission control
Vue.prototype.qs = qs
Vue.prototype.axios = axios
Vue.use(ElementUI, { size: 'small' })
Vue.config.productionTip = false
Vue.prototype.$EventBus = new Vue()
/* 无权限提示，非业务功能 */
Vue.prototype.$showNoAuthMsg = () => {
  Vue.prototype.$message.warning('请联系老师获得高级账号体验项目完整功能获得全套学习资料')
}
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
