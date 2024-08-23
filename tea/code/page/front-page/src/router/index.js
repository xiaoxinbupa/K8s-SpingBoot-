import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/information/HomeView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: HomeView,
    redirect: '/mall',
    meta: {
      title: '首页', needAuth: true
    },
    children: [
      {
        path: '/information/:id',
        component: () => import('../views/information/Information.vue'),
        meta: {
          title: '资讯', needAuth: true
        }
      },
      {
        path: '/baike',
        component: () => import('../views/information/BaiKeView.vue'),
        meta: {
          title: '百科', needAuth: true
        }
      },
      {
        path: '/typelist',
        component: () => import('../views/information/TypeList.vue'),
        meta: {
          title: '列表', needAuth: true
        }
      },
      {
        path: '/article/details',
        component: () => import('../views/information/ArticleDetails.vue'),
        meta: {
          title: '详情', needAuth: true
        }
      },
      {
        path: '/mall',
        component: () => import('../views/mall/home/MallView.vue'),
        meta: {
          title: '商城', needAuth: true
        }
      },
      {
        path: '/malllist',
        component: () => import('../views/mall/list/MallList.vue'),
        meta: {
          title: '商城列表', needAuth: true
        }
      },
      {
        path: '/malldetail',
        component: () => import('../views/mall/detail/MallDetail.vue'),
        meta: {
          title: '商城详情', needAuth: true
        }
      },
      {
        path: '/carpage',
        component: () => import('../views/mall/car/CarPage.vue'),
        meta: {
          title: '购物车', needAuth: true
        }
      },
      {
        path: '/orderpage',
        component: () => import('../views/mall/order/OrderPage.vue'),
        meta: {
          title: '订单中心', needAuth: true
        }
      },
      {
        path: '/myorder',
        component: () => import('../views/mall/order/MyOrder.vue'),
        meta: {
          title: '订单管理', needAuth: true
        }
      },
      {
        path: '/orderinfor',
        component: () => import('../views/mall/order/OrderInfor.vue'),
        meta: {
          title: '订单详情', needAuth: true
        }
      }
    ]
  },
  {
    path: '/login',
    component: () => import('../views/sso/Login.vue'),
    meta: {
      title: '登录'
    }
  },
  {
    path: '/register',
    component: () => import('../views/sso/Register.vue'),
    meta: {
      title: '注册'
    }
  }
]

const router = new VueRouter({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes
})

export default router
