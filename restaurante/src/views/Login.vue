<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>login in</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/register')">register</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>


export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          { required: true, message: 'Introduce your name', trigger: 'blur' },
          { min: 3, max: 10, message: '3 to 10 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Introduce password', trigger: 'blur' },
          { min: 1, max: 20, message: '1 to 20 characters', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    login: function () {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.post("/user/login", this.user).then(res => {
            if (res.code == '200') {
              localStorage.setItem("user", JSON.stringify(res.data))
              localStorage.setItem("menus", JSON.stringify(res.data.menus))

              this.$router.push("/home")
              this.$message.success("login success")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B , #3F5EFB);
  overflow: hidden;
}
</style>