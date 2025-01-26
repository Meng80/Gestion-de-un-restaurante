<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="search the name" suffix-icon="el-icon-search" v-model="name"></el-input>
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
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="id" width="80"></el-table-column>
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column prop="description" label="description"></el-table-column>
      <el-table-column prop="mark" label="mark"></el-table-column>
      <el-table-column label="operate" width="350" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)"> assignment menu <i class="el-icon-menu"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)"> edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='ok'
              cancel-button-text='no'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete user?"
              @confirm="del(scope.row.id)">
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

    <el-dialog title="role information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="name" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="description" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="mark" >
          <el-input v-model="form.mark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false"> cancel </el-button>
        <el-button type="primary" @click="save"> ok </el-button>
      </div>
    </el-dialog>

    <el-dialog title="menu assignment" :visible.sync="menuDialogVis" width="30%" >
      <el-tree
          :check-strictly="true"
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span><i :class="data.icon"></i> {{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false"> cancel </el-button>
        <el-button type="primary" @click="saveRoleMenu"> ok </el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>

export default {
  name: "Role",
  data(){
    return {
      tableData: [],
      total: 0,
      pageNum:1,
      pageSize:10,
      name:"",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: '',
      ids: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/role/page", {
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res =>{
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/menu", {
        params: {
          name: "",
        }
      }).then(res => {
        this.menuData = res.data
      })
      this.request.get("/menu/ids").then(r => {
        this.ids = r.data
      })
    },
    save(){
      this.request.put("/role", this.form).then(res => {
        if(res.code === '200'){
          this.$message.success("Saved successfully")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("save failed")
        }
      })
    },
    saveRoleMenu(){
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if(res.code === '200') {
          this.$message.success("bind successfully")
          this.menuDialogVis = false

          if(this.roleFlag === 'ROLE_ADMIN') {
            this.$store.commit("logout")
          }
        }else {
          this.$message.error(res.msg)
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
      this.request.delete("/role/" + id).then(res =>{
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
      this.request.delete("/role/del/batch",  { data: ids }).then(res =>{
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
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    async selectMenu(role) {
      this.roleId = role.id
      this.roleFlag = role.mark

      this.request.get("/menu").then(res => {
        this.menuData = res.data
        this.expends = this.menuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/" + this.roleId).then( res => {
        this.checks = res.data
        this.ids.forEach(id => {
          if(!this.checks.includes(id)) {
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, false)
            })

          }
        })
        this.menuDialogVis = true
      })
    },
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>