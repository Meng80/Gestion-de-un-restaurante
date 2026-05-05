import Vue from 'vue'
import VueRouter from 'vue-router'
import TableEntry from '@/views/TableEntry.vue'
import Menu from '@/views/Menu.vue'
import Cart from '@/views/ShoppingCart.vue'
import Orders from '@/views/Orders.vue'
import HistoryOrder from '@/views/HistoryOrder.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'TableEntry',
        component: TableEntry,
        meta: { title: 'Table Number Entry' }
    },
    {
        path: '/menu',
        name: 'Menu',
        component: Menu,
        meta: { title: 'Menu' }
    },
    {
        path: '/cart',
        name: 'Cart',
        component: Cart,
        meta: { title: 'Shopping Cart' }
    },
    {
        path: '/orders',
        name: 'Orders',
        component: Orders,
        meta: { title: 'My Orders' }
    },
    {
        path: '/history-order',
        name: 'HistoryOrder',
        component: HistoryOrder,
        meta: { title: 'History Order' }
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title
    }
    next()
})

export default router