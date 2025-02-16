<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Search by name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="Search by mark" suffix-icon="el-icon-edit" class="ml-5" v-model="mark"></el-input>
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
      <el-table-column prop="mark" label="Mark" width="500"></el-table-column>
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
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          background
          layout="prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Category Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
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
  name: "Category",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      mark: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: []
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request.get("/category/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
    save() {
      this.request.put("/category", this.form).then(res => {
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
      this.request.delete("/category/" + id).then(res => {
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
      this.request.delete("/category/del/batch", { data: ids }).then(res => {
        if (res.code === '200') {
          this.$message.success("Successfully deleted");
          this.load();
        } else {
          this.$message.error("Failed to delete");
        }
      });
    },
    reset() {
      this.name = "";
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
