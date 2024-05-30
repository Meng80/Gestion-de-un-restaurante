<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="search the name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load"> search </el-button>
      <el-button class="ml-5" type="warning" @click="reset"> reset </el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click = "handleAdd()"> add <i class="el-icon-circle-plus-outline"></i></el-button>
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
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              row-key="id" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="id" width="80"></el-table-column>
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column prop="path" label="path"></el-table-column>
      <el-table-column prop="pagePath" label="pagePath"></el-table-column>
      <el-table-column label="icon"  align="center" >
        <template slot-scope="scope">
          <i :class="scope.row.icon" style="font-size:25px"></i>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="description"></el-table-column>
      <el-table-column label="operate" width="350" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path"> add child menu <i class="el-icon-plus"></i></el-button>
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


    <el-dialog title="menu information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="name" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="path" >
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="pagePath" >
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="icon" >
          <template slot-scope="scope">
            <el-select clearable v-model="form.icon" placeholder="please optional" style="width: 100%">
              <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value"></el-option>

            </el-select>
          </template>
        </el-form-item>
        <el-form-item label="description" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "Menu",
  data(){
    return {
      tableData: [],
      total: 0,
      pageNum:1,
      pageSize:10,
      name:"",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      options: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/menu", {
        params:{
          name: this.name,
        }
      }).then(res =>{
        this.tableData = res.data
      })
    },
    save(){
      this.request.post("/menu", this.form).then(res => {
        if(res.code === '200'){
          this.$message.success("Saved successfully")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("save failed")
        }
      })
    },
    handleAdd(pid){
      this.dialogFormVisible = true
      this.form = {}
      if(pid){
        this.form.pid = pid
      }
    },
    handleEdit(row){
      this.form = Object.assign({},row)
      this.dialogFormVisible = true

      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })

    },
    del(id){
      this.request.delete("/menu/" + id).then(res =>{
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
      this.request.post("/menu/del/batch", ids).then(res =>{
        if(res.code === '200'){
          this.$message.success("successfully deleted")
          this.load()
        } else {
          this.$message.error("failed to delete")
        }
      })
    },
    reset() {
      this.name = ""
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