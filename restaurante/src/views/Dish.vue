<template>
  <div>
    <div style="padding: 10px 0">
      <el-input
          style="width: 200px"
          placeholder="Search by name"
          suffix-icon="el-icon-search"
          v-model="name">
      </el-input>

      <el-select
          v-model="categoryId"
          placeholder="Search by category"
          class="ml-5"
          style="width: 200px">
        <el-option
            v-for="item in categoryData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="status" placeholder="Select status">
        <el-option label="Disabled" :value="0"></el-option>
        <el-option label="Enabled" :value="1"></el-option>
      </el-select>

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

    <el-table
        :data="tableData"
        border
        stripe
        :header-cell-class-name="'headerBg'"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="100"></el-table-column>
      <el-table-column prop="name" label="DishName" width="100"></el-table-column>
      <el-table-column label="Image" width="150">
        <template slot-scope="scope">
          <img
              v-if="scope.row.image"
              :src="scope.row.image"
              style="width: 60px; height: 60px; object-fit: cover; cursor: pointer;"
              @click="previewImage(scope.row.image)">
          <span v-else style="color: #909399;">No Image</span>
        </template>
      </el-table-column>
      <el-table-column prop="categoryId" label="Category" width="100" :formatter="formatCategory"></el-table-column>
      <el-table-column prop="price" label="Price" width="100"></el-table-column>
      <el-table-column prop="status" label="Status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
            {{ scope.row.status === 1 ? 'Enabled' : 'Disabled' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="Description" width="200"></el-table-column>
      <el-table-column label="Operations" width="150">
        <template slot-scope="scope">
          <el-button type="success" size="small" @click="handleEdit(scope.row)">Edit</el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text="OK"
              cancel-button-text="No"
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete this dish?"
              @confirm="del(scope.row.id)">
            <el-button type="danger" size="small" slot="reference">Delete</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- Edit/Add dishDialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="500px">
      <el-form label-width="80px" size="small">
        <!-- upload image -->
        <el-form-item label="Image">
          <el-upload
              class="dish-uploader"
              :action="uploadUrl"
              :show-file-list="false"
              :on-success="handleDishSuccess"
              :before-upload="beforeDishUpload">
            <img v-if="form.image" :src="form.image" class="avatar">
            <i v-else class="el-icon-plus dish-uploader-icon"></i>
          </el-upload>
          <div style="font-size: 12px; color: #909399; margin-top: 5px;">
            Support JPG, PNG, WebP format, max 2MB
          </div>
        </el-form-item>

        <el-form-item label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Category">
          <el-select v-model="form.categoryId" placeholder="Select category" style="width: 100%">
            <el-option
                v-for="item in categoryData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Price">
          <el-input v-model="form.price" autocomplete="off" type="number"></el-input>
        </el-form-item>

        <el-form-item label="Status">
          <el-select v-model="form.status" placeholder="Select status" style="width: 100%">
            <el-option label="Enabled" :value="1"></el-option>
            <el-option label="Disabled" :value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Description">
          <el-input type="textarea" v-model="form.description" autocomplete="off" rows="3"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">OK</el-button>
      </div>
    </el-dialog>

    <!-- Image de vista preview -->
    <el-dialog title="Image Preview" :visible.sync="previewVisible" width="40%">
      <img :src="previewImageUrl" style="width: 100%;" />
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewVisible = false">Close</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {serverIp} from "../../public/Config";

export default {
  name: "Dish",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      price:0,
      categoryId: "",
      status: "",
      categoryData: [],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      uploadUrl: `http://${serverIp}:9090/file/upload`,
      previewVisible: false,
      previewImageUrl: ""
    };
  },
  computed: {
    dialogTitle() {
      return this.form.id ? "Edit Dish" : "Add Dish";
    }
  },
  created() {
    this.loadCategories();
    this.load();
  },
  methods: {
    load() {
      this.request.get("/admin/dish/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          categoryId: this.categoryId,
          status: this.status
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records || [];
          this.total = res.data.total || 0;
        } else {
          this.$message.error(res.msg || "Failed to load dishes");
        }
      }).catch(error => {
        console.error("Failed to load dishes:", error);
        this.$message.error("Failed to load dishes");
      });
    },

    // Load categories with status = 1
    loadCategories() {
      this.request.get("/admin/category/list", {
        params: {
          status: 1
        }
      }).then(res => {
        if(res.code === '200'){
          this.categoryData = res.data;
        }
      });
    },

    formatCategory(row) {
      let temp = this.categoryData.find(item => item.id == row.categoryId);
      return temp ? temp.name : "";
    },

    handleDishSuccess(res) {
      this.form.image = res;
      this.$message.success("Image uploaded successfully");
    },

    beforeDishUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('Image must be JPG, PNG or WebP format!');
      }
      if (!isLt2M) {
        this.$message.error('Image size cannot exceed 2MB!');
      }
      return isImage && isLt2M;
    },

    previewImage(url) {
      this.previewImageUrl = url;
      this.previewVisible = true;
    },

    save() {
      if (!this.form.name) {
        this.$message.warning("Please enter dish name");
        return;
      }
      if (!this.form.categoryId) {
        this.$message.warning("Please select category");
        return;
      }
      if (!this.form.price) {
        this.$message.warning("Please enter price");
        return;
      }

      console.log("Saving dish:", this.form);
      if(this.form.id){
        this.request.put("/admin/dish", this.form).then(res => {
          if(res.code === '200'){
            this.$message.success("Updated successfully");
            this.dialogFormVisible = false;
            this.load();
          } else {
            this.$message.error(res.msg || "Update failed");
          }
        }).catch(error => {
          console.error("Update failed:", error);
          this.$message.error("Update failed");
        });
      } else {
        this.request.post("/admin/dish", this.form).then(res => {
          if(res.code === '200'){
            this.$message.success("Added successfully");
            this.dialogFormVisible = false;
            this.load();
          } else {
            this.$message.error(res.msg || "Add failed");
          }
        }).catch(error => {
          console.error("Add failed:", error);
          this.$message.error("Add failed");
        });
      }
    },

    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {
        status: 1
      };
    },

    handleEdit(row) {
      this.form = Object.assign({}, row);
      this.dialogFormVisible = true;
    },

    del(id) {
      this.request.delete("/admin/dish/" + id).then(res => {
        if(res.code === '200'){
          this.$message.success("Deleted successfully");
          this.load();
        } else {
          this.$message.error(res.msg || "Delete failed");
        }
      }).catch(error => {
        console.error("Delete failed:", error);
        this.$message.error("Delete failed");
      });
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    delBatch() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("Please select dishes to delete");
        return;
      }
      let ids = this.multipleSelection.map(v => v.id);
      this.request.delete("/admin/dish/del/batch", { data: ids }).then(res => {
        if(res.code === '200'){
          this.$message.success("Successfully deleted");
          this.load();
        } else {
          this.$message.error(res.msg || "Batch delete failed");
        }
      }).catch(error => {
        console.error("Batch delete failed:", error);
        this.$message.error("Batch delete failed");
      });
    },

    reset() {
      this.name = "";
      this.categoryId = "";
      this.status = "";
      this.pageNum = 1;
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

<style scoped>
.headerBg {
  background: #eee !important;
}
.dish-uploader {
  text-align: left;
}
.dish-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
}
.dish-uploader .el-upload:hover {
  border-color: #409EFF;
}
.dish-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}
</style>