import router from './router'
import store from './store'
import {
  Message
} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({
  showSpinner: false
}) // NProgress Configuration

// const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // set page title
  document.title = (to.meta.title || '') + ' 学茶资讯后台'
  const token = store.getters.token
  if (token) {
    /* if (to.path === '/login') {
      next({ path: '/' })
    } else {
      next()
    } */
    next()
  } else {
    if (!to.meta.needAuth) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
