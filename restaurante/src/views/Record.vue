<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Search by Product" suffix-icon="el-icon-goods" class="ml-5" v-model="product"></el-input>
      <el-select v-model="category" placeholder="Search by Category" class="ml-5" style="width: 200px">
        <el-option
            v-for="item in categoryData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button class="ml-5" type="warning" @click="reset">Reset</el-button>
    </div>

<!--    <div style="padding: 10px 0">-->
<!--      <el-button type="primary" @click="handleAdd">Add <i class="el-icon-circle-plus-outline"></i></el-button>-->
<!--      <el-popconfirm-->
<!--          class="ml-5"-->
<!--          confirm-button-text="OK"-->
<!--          cancel-button-text="No"-->
<!--          icon="el-icon-info"-->
<!--          icon-color="red"-->
<!--          title="Are you sure you want to delete the selected items?"-->
<!--          @confirm="delBatch">-->
<!--        <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>-->
<!--      </el-popconfirm>-->
<!--    </div>-->

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="productName" label="Product" width="150"></el-table-column>
      <el-table-column prop="categoryName" label="Category" width="150"></el-table-column>
      <el-table-column prop="count" label="Count" width="150"></el-table-column>
      <el-table-column prop="username" label="UserName" width="170"></el-table-column>
      <el-table-column prop="createTime" label="Create Time" width="180"></el-table-column>
      <el-table-column prop="mark" label="Mark" width="245"></el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          background
          layout="prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Record Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="Product">
          <el-input v-model="form.product" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Count">
          <el-input v-model="form.count" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="User ID">
          <el-input v-model="form.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Mark">
          <el-input v-model="form.mark" autocomplete="off"></el-input>
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
  name: "Record",
  data() {
    return {
      categoryData: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      userId: "",
      product: "",
      category: "",
      mark: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: []
    };
  },
  created() {
    this.loadCategories();
    this.load();
  },
  methods: {
    load() {
      this.request.get("/record/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.userId,
          product: this.product,
          category: this.category,
          mark: this.mark
        }
      }).then(res => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      }).catch(error => {
        console.error("Error loading data:", error);
        this.$message.error("Failed to load data: " + error.message);
      });
    },
    loadCategories() {
      this.request.get("/category/list").then(res => {
        if (res.code === '200') {
          this.categoryData = res.data;
        } else {
          this.$message.error("Failed to load categories");
        }
      });
    },
    save() {
      this.request.put("/record", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Saved successfully");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save failed");
        }
      });
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    handleEdit(row) {
      this.form = Object.assign({}, row);
      this.dialogFormVisible = true;
    },
    del(id) {
      this.request.delete("/record/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("Deleted successfully");
          this.load();
        } else {
          this.$message.error("Delete failed");
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id);
      this.request.delete("/record/del/batch", { data: ids }).then(res => {
        if (res.code === '200') {
          this.$message.success("Successfully deleted");
          this.load();
        } else {
          this.$message.error("Failed to delete");
        }
      });
    },
    reset() {
      this.userId = "";
      this.product = "";
      this.category = "";
      this.mark = "";
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    }
  }
};
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>
