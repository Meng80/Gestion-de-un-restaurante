<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Search by name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button class="ml-5" type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="handleAdd">Add <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text="OK"
          cancel-button-text="No"
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure you want to delete the selected items?"
          @confirm="delBatch">
        <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="100"></el-table-column>
      <el-table-column prop="name" label="Name" width="300"></el-table-column>
      <el-table-column prop="sort" label="Sort" width="100"></el-table-column>
      <el-table-column label="Status" width="120">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ccc"
              @change="changeStatus(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="Update Time" width="300"></el-table-column>
      <el-table-column label="Operations" >
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text="OK"
              cancel-button-text="No"
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete this category?"
              @confirm="del(scope.row.id)">
            <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          :current-page="pageNum"
          background
          layout="prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="DishCategory Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" size="small">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Sort">
          <el-input v-model.number="form.sort" type="number" min="0" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">OK</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DishCategory",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      rules: {
        name: [
          { required: true, message: 'Please enter category name', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request.get("/admin/category/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
      if (this.form.id) {
        this.request.put("/admin/category", this.form).then(res => {
          if (res.code === '200') {
            this.$message.success("Updated successfully");
            this.dialogFormVisible = false;
            this.load();
          } else {
            this.$message.error(res.msg || "Update failed");
          }
        });
      } else {
        this.request.post("/admin/category", this.form).then(res => {
          if (res.code === '200') {
            this.$message.success("Added successfully");
            this.dialogFormVisible = false;
            this.load();
          } else {
            this.$message.error(res.msg || "Add failed");
          }
        });
      }
    },

    handleAdd() {
      this.dialogFormVisible = true;
      this.form = { sort: 0 };
    },

    handleEdit(row) {
      this.form = Object.assign({}, row);
      this.dialogFormVisible = true;
    },

    del(id) {
      this.request.delete("/admin/category/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("Deleted successfully");
          this.load();
        } else {
          this.$message.error(res.msg || "Delete failed");
        }
      }).catch(error => {
        if (error.response && error.response.data && error.response.data.msg) {
          this.$message.error(error.response.data.msg);
        } else if (error.response && error.response.data && error.response.data.message) {
          this.$message.error(error.response.data.message);
        } else {
          this.$message.error("Cannot delete, this category may have dishes");
        }
      });
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    delBatch() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("Please select categories to delete");
        return;
      }
      let ids = this.multipleSelection.map(v => v.id);
      this.request.delete("/admin/category/del/batch", {data: ids}).then(res => {
        if (res.code === '200') {
          this.$message.success("Successfully deleted");
          this.load();
        } else {
          this.$message.error(res.msg || "Failed to delete");
        }
      }).catch(error => {
        if (error.response && error.response.data && error.response.data.msg) {
          this.$message.error(error.response.data.msg);
        } else if (error.response && error.response.data && error.response.data.message) {
          this.$message.error(error.response.data.message);
        } else {
          this.$message.error("Cannot delete, some categories may have dishes");
        }
      });
    },

    changeStatus(row) {
      console.log("Sending row:", row);
      this.request.put("/admin/category", {
        id: row.id,
        status: row.status
      }).then(res => {
        if (res.code === '200') {
          this.$message.success("Status updated");
        } else {
          this.$message.error("Update failed");
        }
      });
    },

    reset() {
      this.name = "";
      this.load();
    },

    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
  }
};
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>