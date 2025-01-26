<template>
  <el-card style="width: 500px;">
    <el-form label-width="120px" size="small" :model="form" :rules="rules" ref="pass">

      <el-form-item label="password" prop="password">
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="newPassword" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="confirmPassword" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">ok</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>

export default {
  name: "Password",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        password: {
          Password: [
            { required: true, message: 'please enter your password', trigger: 'blur' },
            { min: 3, max: 10, message: 'Password must be between 3 and 10 characters', trigger: 'blur' }
          ],
          newPassword: [
            { required: true, message: 'Please enter new password', trigger: 'blur' },
            { min: 3, message: 'New password must be at least 3 characters long', trigger: 'blur' },
          ],
          confirmPassword: [
            { required: true, message: 'Please confirm your password', trigger: 'blur' },
            { min: 3, max: 10, message: 'Password must be between 3 and 10 characters', trigger: 'blur' },
          ],
        }
      }
    }
  },
  created() {
    this.form.username = this.user.username
  },
  methods: {
    save() {
      this.$refs.pass.validate((valid)=> {
        if (valid) {
          if (this.form.newPassword != this.form.confirmPassword) {
            this.$message.error("The password you entered twice is different")
            return false
          }
          this.request.put("/user/password", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("Modification successful")
              this.$store.commit("logout")
        } else {
              this.$message.error(res.msg)
            }
         })
        }
      })
    },
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  border-radius: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>