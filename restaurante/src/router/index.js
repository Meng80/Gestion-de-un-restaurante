import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    component:() => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      { path: 'home', name: 'home', component:() => import('../views/Home.vue')},
      { path: 'user', name: 'user', component:() => import('../views/User.vue')},
      { path: 'role', name: 'role', component:() => import('../views/Role.vue')},
      { path: 'file', name: 'file', component:() => import('../views/File.vue')},
      { path: 'person', name: 'person', component:() => import('../views/Person.vue')},
      { path: 'vip', name: 'vip', component:() => import('../views/Vip.vue')},
      { path: 'password', name: 'password', component:() => import('../views/Password.vue')},
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
