<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="search the name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="search the message" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="search the position" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
      <el-button class="ml-5" type="primary" @click="load"> search </el-button>
      <el-button class="ml-5" type="warning" @click="reset"> reset </el-button>
      <el-button type="primary" @click="loadRecentVisitors('lastMonth')">lastMonth</el-button>
      <el-button type="primary" @click="loadRecentVisitors('longTimeNoVisit')">longTimeNoVisit</el-button>
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
      <el-upload action="http://localhost:9090/vip/import" :show-file-list="false" accept=".xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5"> import <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5"> export <i class="el-icon-top"></i></el-button>
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="id" width="50"></el-table-column>
      <el-table-column prop="name" label="name" width="90"></el-table-column>
      <el-table-column prop="email" label="email" width="140"></el-table-column>
      <el-table-column prop="phone" label="phone" width="100"></el-table-column>
      <el-table-column prop="address" label="address"></el-table-column>
      <el-table-column prop="createTime" label="Create Time" sortable width="180" column-key="createTime" :formatter="dateFormatter"></el-table-column>
      <el-table-column prop="lastVisit" label="Last Visit" ></el-table-column>
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

    <el-dialog title="vip information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="name" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
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
        <el-form-item label="last_Visit" >
          <el-input v-model="form.lastVisit" autocomplete="off"></el-input>
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
  name: "Vip",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      sort: {
        prop: "createTime",
        order: "desc"
      }

    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/vip/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          email: this.email,
          address: this.address,
          sort: this.sort.prop,
          order: this.sort.order
        }
      }).then(res => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      }).catch(error => {
        console.error("Error loading data:", error);
        this.$message.error("Failed to load data: " + error.message);
      });
    },
    save() {
      this.request.post("/vip", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Saved successfully")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("save failed")
        }
      });
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = Object.assign({}, row)
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/vip/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("delete successfully")
          this.load()
        } else {
          this.$message.error("delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/vip/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("successfully deleted")
          this.load()
        } else {
          this.$message.error("failed to delete")
        }
      })
    },
    reset() {
      this.name = ""
      this.email = ""
      this.address = ""
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
    exp() {
      window.open("http://localhost:9090/vip/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("imported successfully")
      this.load()
    },
    handleSortChange({prop, order}) {
      this.sort.prop = prop
      this.sort.order = order === 'ascending' ? 'asc' : 'desc'
      this.load()
    },
    dateFormatter(row, column) {
      const date = new Date(row.createTime);
      const year = date.getFullYear();
      const month = ('0' + (date.getMonth() + 1)).slice(-2);
      const day = ('0' + date.getDate()).slice(-2);
      const hours = ('0' + date.getHours()).slice(-2);
      const minutes = ('0' + date.getMinutes()).slice(-2);
      const seconds = ('0' + date.getSeconds()).slice(-2);
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    loadRecentVisitors(period) {
      console.log("Loading recent visitors with period:", period);
      this.request.get("/vip/recent", {
        params: {
          period: period
        }
      }).then(res => {
        this.tableData = res.data;
      }).catch(error => {
        console.error("Error loading recent visitors:", error);
        this.$message.error("Failed to load recent visitors: " + error.message);
      });
    }
  }
};
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>