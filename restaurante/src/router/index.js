import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path:'/',
    component:() => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      { path: 'home', name: 'home', component:() => import('../views/Home.vue')},
      { path: 'workspace', name: 'workspace', component: () => import('../views/Workspace.vue') },
      { path: 'user', name: 'user', component:() => import('../views/User.vue')},
      { path: 'role', name: 'role', component:() => import('../views/Role.vue')},
      { path: 'file', name: 'file', component:() => import('../views/File.vue')},
      { path: 'person', name: 'person', component:() => import('../views/Person.vue')},
      { path: 'vip', name: 'vip', component:() => import('../views/Vip.vue')},
      { path: 'category', name: 'category', component:() => import('../views/Category.vue')},
      { path: 'product', name: 'product', component:() => import('../views/Product.vue')},
      { path: 'record', name: 'record', component:() => import('../views/Record.vue')},
      { path: 'password', name: 'password', component:() => import('../views/Password.vue')},
      { path: 'dishCategory', name: 'dishCategory', component:() => import('../views/DishCategory.vue')},
      { path: 'dish', name: 'dish', component:() => import('../views/Dish.vue')},
      { path: 'order', name: 'order', component:() => import('../views/Order.vue')},
    ]
  },
  {
    path:'/login',
    name:'Login',
    component:() => import('../views/Login.vue')
  },
  {
    path:'/register',
    name:'Register',
    component:() => import('../views/Register.vue')
  },
  {
    path:'/404',
    name:'404',
    component:() => import('../views/404.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// export const resetRouter = () => {
//   router.matcher = new VueRouter({
//     mode: 'history',
//     base: process.env.BASE_URL,
//     routes
//   })
//
// }

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)
  store.commit("setPath")
  next()
})
export default router
