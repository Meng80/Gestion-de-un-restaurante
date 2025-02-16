<template>
  <div class="wrapper">
    <div style="margin: 150px auto; background-color: #fff; width: 350px; height: 350px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="Please input Username" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="Please enter password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="Please confirm your password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input placeholder="Please enter your email" size="medium" prefix-icon="el-icon-message" show-password v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input placeholder="Enter your code" size="medium" prefix-icon="el-icon-lock" style="width: 195px" show-password v-model="user.code"></el-input>
          <el-button type="primary" size="small" class="ml-5" @click="getEmailCode" >Get your Code</el-button>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="primary" size="small"  autocomplete="off" @click="register">register</el-button>
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">Return</el-button>
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
          { required: true, message: 'please enter user name', trigger: 'blur' },
          { min: 3, max: 10, message: 'Username must be between 3 and 10 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 8, message: 'Password must be at least 8 characters long', trigger: 'blur' },
          { validator: this.validatePassword, trigger: 'blur'}
        ],
        confirmPassword: [
          { required: true, message: 'Please confirm your password', trigger: 'blur' },
          { min: 1, max: 20, message: '1 to 20 characters in length', trigger: 'blur' },
          {validator: this.validateConfirmPassword, trigger: 'blur'}
        ],
        email: [
          { required: true, message: 'Please enter your email', trigger: 'blur' },
          { min: 1, max: 20, message: '1 to 20 characters in length', trigger: 'blur' },
        ],
        code: [
          { required: true, message: 'Please enter your code', trigger: 'blur' },
          { min: 1, max: 4, message: '4 characters in length', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("The passwords entered twice are inconsistent")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("registration success")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },

    validatePassword(rule, value, callback){
      if (!value) {
        return callback(new Error('Password cannot be empty'));
      }
      if (value.length < 6) {
        callback(new Error('Password must be at least 6 characters long'));
      } else {
        callback();
      }
    },

    validateConfirmPassword(rule, value, callback) {
      if(value != this.user.password) {
        callback(new Error ('Password do not match'));
      } else {
        callback ();
      }
    },

    getEmailCode(){
      if(!this.user.email){
        this.$message.warning("Please enter your email");
      }
      if(!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.user.email)){
        this.$message.warning("Please enter email right");
      }
      this.request.post("/user/sendCode/" + this.user.email).then(res => {
        if(res.code === '200') {
          this.$message.success("send success");
        }else {
          this.$message.error(res.msg);
        }

      })

    },
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B , #3F5EFB);
  overflow: hidden;
}

.el-form-item__error {
  color: #f56c6c;
  font-size: 12px;
}
</style>