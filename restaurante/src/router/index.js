import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
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

export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })

}

export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus){

    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      const manageRoute = { path: '/', name:'Manage', component:  () => import('../views/Manage.vue'), redirect:"/home", children:[
          { path: 'person', name: 'information person', component: () => import('../views/Person.vue')}
        ] }
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if(item.path){
          let itemMenu = { path: item.path.replace("/", ""), name:item.name , component:  () => import('../views/' + item.pagePath + '.vue') }
          manageRoute.children.push(itemMenu)
        }else if(item.children.length){
          item.children.forEach(item => {
            if(item.path){
              let itemMenu = { path: item.path.replace("/", ""), name:item.name , component:  () => import('../views/' + item.pagePath + '.vue') }
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      //动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

//重置再set一次路由
setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新

  //未找到路由的情况
  if(!to.matched.length){
    const storeMenus = localStorage.getItem("menus")
    if(storeMenus) {
      next("/404")
    }else {
      next("/login")//跳回登录页面
    }
  }
  next()
})

export default router
