<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="search the name" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="search the message" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="search the position" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
      <el-button class="ml-5" type="primary" @click="load"> search </el-button>
      <el-button class="ml-5" type="warning" @click="reset"> reset </el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click = "handleAdd"> add <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='ok'
          cancel-button-text='no'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure you want to delete the content?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" > delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept=".xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5"> import <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5"> export <i class="el-icon-top"></i></el-button>
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="id" width="80"></el-table-column>
      <el-table-column prop="username" label="name" width="120"></el-table-column>
      <el-table-column prop="role" label="role" width="120"></el-table-column>
      <el-table-column prop="nickname" label="nickname" width="100"></el-table-column>
      <el-table-column prop="email" label="email" widtg="120"></el-table-column>
      <el-table-column prop="phone" label="phone" widtg="120"></el-table-column>
      <el-table-column prop="address" label="address"></el-table-column>
      <el-table-column label="operate">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)"> edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='ok'
              cancel-button-text='no'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete user?"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference"> delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = "pageNum"
          background
          layout="prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="user information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="username" >
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="role" >
          <el-select clearable v-model="form.role" placeholder="Please select a role" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.mark"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="nickname" >
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="email" >
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="phone" >
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="address" >
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false"> cancel </el-button>
        <el-button type="primary" @click="save"> ok </el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>

export default {
  name: "User",
  data(){
    return {
      tableData: [],
      total: 0,
      pageNum:1,
      pageSize:10,
      username:"",
      email:"",
      address:"",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: []

    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })


      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save(){
      this.request.post("/user", this.form).then(res => {
        if(res.code === '200'){
          this.$message.success("Saved successfully")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("save failed")
        }
      })
    },
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = Object.assign({},row)
      this.dialogFormVisible = true
    },
    del(id){
      this.request.delete("/user/" + id).then(res =>{
        if(res.code === '200'){
          this.$message.success("delete successfully")
          this.load()
        } else {
          this.$message.error("delete failed")
        }
      })
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{}, {}, {}]=>[1,2,3]
      this.request.post("/user/del/batch", ids).then(res =>{
        if(res.code === '200'){
          this.$message.success("successfully deleted")
          this.load()
        } else {
          this.$message.error("failed to delete")
        }
      })
    },
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp(){
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("imported successfully")
      this.load()
    }
  }

}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>