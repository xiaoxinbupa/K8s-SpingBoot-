import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

/* Layout */
import Layout from '@/layout'
export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'dashboard', affix: true, noCache: true }
      }
    ]
  },
  {
    path: '/goods',
    meta: { title: '商品管理', icon: 'el-icon-s-goods' },
    name: 'Goods',
    redirect: '/goods',
    component: Layout,
    children: [
      {
        path: 'management/list',
        component: () => import('@/views/goods/index.vue'),
        meta: { title: '商品列表', icon: 'el-icon-goods', needAuth: true },
        name: 'goodsManagementList'
      },
      {
        path: 'publish/product',
        component: () => import('@/views/goods/publishProduct.vue'),
        meta: { title: '发布商品', icon: 'el-icon-circle-plus', needAuth: true },
        name: 'publishProduct'
      },
      {
        path: 'product/category',
        component: () => import('@/views/goods/productCategory.vue'),
        meta: { title: '商品类别', icon: 'el-icon-s-flag', needAuth: true },
        name: 'productCategory'
      }
    ]
  },
  {
    path: '/stock',
    meta: { title: '库存管理', icon: 'el-icon-s-ticket' },
    name: 'Stock',
    redirect: '/stock',
    component: Layout,
    children: [
      {
        path: '/stock/list',
        component: () => import('@/views/stock/index.vue'),
        meta: { title: '库存管理', needAuth: true },
        name: 'stockList'
      }
    ]
  },
  {
    path: '/order',
    meta: { title: '订单管理', icon: 'el-icon-s-order' },
    name: 'Order',
    redirect: '/order',
    component: Layout,
    children: [
      {
        path: '/order/list',
        component: () => import('@/views/order/index.vue'),
        meta: { title: '订单管理', needAuth: true },
        name: 'orderList'
      },
      {
        path: '/order/detail',
        component: () => import('@/views/order/orderDetail.vue'),
        meta: { title: '订单详情', needAuth: true },
        name: 'orderDetail',
        hidden: true
      }
    ]
  },
  {
    path: '/category',
    meta: { title: '类目管理', icon: 'el-icon-s-comment' },
    name: 'Category',
    redirect: '/category',
    component: Layout,
    children: [
      {
        path: 'management/list',
        component: () => import('@/views/categoryManagement/index.vue'),
        meta: { title: '类目管理', needAuth: true },
        name: 'categoryList'
      }
    ]
  },
  {
    path: '/menu',
    meta: { title: '菜单管理', icon: 'el-icon-menu' },
    name: 'Menu',
    redirect: '/menu',
    component: Layout,
    children: [
      {
        path: 'menu/list',
        component: () => import('@/views/menu/index.vue'),
        meta: { title: '菜单管理', needAuth: true },
        name: 'menuList'
      }
    ]
  },
  {
    path: '/content',
    meta: { title: '内容管理', icon: 'el-icon-s-data' },
    name: 'Content',
    redirect: '/content',
    component: Layout,
    children: [
      {
        path: 'management/list',
        component: () => import('@/views/content/index.vue'),
        meta: { title: '内容管理', needAuth: true },
        name: 'managementList'
      },
      {
        path: 'build/management',
        component: () => import('@/views/content/builtOrEdit.vue'),
        meta: { title: '新建/编辑文章', needAuth: true },
        name: 'buildManagement',
        hidden: true
      },
      {
        path: 'management/detail',
        component: () => import('@/views/content/contentDetail.vue'),
        meta: { title: '文章详情', needAuth: true },
        name: 'contentDetail',
        hidden: true
      }
    ]
  },
  // {
  //   path: '/comment',
  //   meta: { title: '评论管理', icon: 'el-icon-s-promotion' },
  //   name: 'Comment',
  //   redirect: '/comment',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'management/list',
  //       component: () => import('@/views/comment/index.vue'),
  //       meta: { title: '评论列表', needAuth: true },
  //       name: 'CommentManagementList'
  //     }
  //   ]
  // },
  {
    path: '/tag',
    meta: { title: '标签管理', icon: 'el-icon-s-promotion' },
    name: 'Tag',
    redirect: '/tag',
    component: Layout,
    children: [
      {
        path: 'tag/list',
        component: () => import('@/views/tag/index.vue'),
        meta: { title: '标签管理', needAuth: true },
        name: 'TagList'
      },
      {
        path: 'tag/type/list',
        component: () => import('@/views/tag/typeList.vue'),
        meta: { title: '标签类型列表', needAuth: true },
        name: 'TagTypeList',
        hidden: true
      }
    ]
  },
  {
    path: '/examine',
    meta: { title: '审批', icon: 'el-icon-message-solid' },
    name: 'Examine',
    redirect: '/examine',
    component: Layout,
    children: [
      {
        path: 'examine/list',
        component: () => import('@/views/examine/index.vue'),
        meta: { title: '审批', needAuth: true },
        name: 'examineList'
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
