import Vue from 'vue'
import VueRouter from 'vue-router'

// 导入页面组件
import TableEntry from '@/views/TableEntry.vue'
import Menu from '@/views/Menu.vue'
import Cart from '@/views/ShoppingCart.vue'
import Orders from '@/views/Orders.vue'
import OrderDetail from '@/views/OrderDetail.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'TableEntry',
        component: TableEntry,
        meta: { title: '输入桌号' }
    },
    {
        path: '/menu',
        name: 'Menu',
        component: Menu,
        meta: { title: '点餐' }
    },
    {
        path: '/cart',
        name: 'Cart',
        component: Cart,
        meta: { title: '购物车' }
    },
    {
        path: '/orders',
        name: 'Orders',
        component: Orders,
        meta: { title: '我的订单' }
    },
    {
        path: '/order-detail/:id',
        name: 'OrderDetail',
        component: OrderDetail,
        meta: { title: '订单详情' }
    }
]

const router = new VueRouter({
    mode: 'history',  // 使用 history 模式，URL 不带 #
    base: process.env.BASE_URL,
    routes
})

// 路由守卫：设置页面标题
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title
    }
    next()
})

export default router