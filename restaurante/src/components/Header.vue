<template>
  <div style="font-size: 12px; line-height: 60px; display: flex">
    <div style="flex: 1; font-size: 20px">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <el-breadcrumb-item :to="'/'">Main Page</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: auto; cursor: pointer; text-align: right">
      <div style="display: inline-block">
        <img :src="user.avatarUrl" alt="" referrerpolicy="no-referrer"
             style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <span>{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/password" style="text-emphasis:none; text-decoration:none; color: #686868 ">EditPassword</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/person" style="text-emphasis:none; text-decoration:none; color: #686868 ">InfoPerson</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">exit</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>

</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse:'',
    user: Object
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　
    }
  },
  data(){
    return{

    }
  },
  methods: {
    collapse() {
      this.$emit("asideCollapse")
    },
    logout(){
      this.$store.commit("logout")
      this.$message.success("exit success")
    }
  }
}
</script>

<style scoped>

</style>