<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Search by name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-select v-model="category" placeholder="Search by category" class="ml-5" style="width: 200px">
        <el-option
            v-for="item in categoryData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-input style="width: 200px" placeholder="Search by mark" suffix-icon="el-icon-edit" class="ml-5" v-model="mark"></el-input>
      <el-input style="width: 200px" placeholder="Search by category" suffix-icon="el-icon-edit" class="ml-5" v-model="category"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button class="ml-5" type="warning" @click="reset">Reset</el-button>
      <el-button type="primary" @click="handleStockIn">StockIn <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" @click="handleStockOut">StockOut <i class="el-icon-remove-outline"></i></el-button>
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

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
    highlight-current-row @current-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="100"></el-table-column>
      <el-table-column prop="name" label="Name" width="150"></el-table-column>
      <el-table-column prop="category" label="Category" width="200" :formatter="formatCategory"></el-table-column>
      <el-table-column prop="count" label="Count" width="200"></el-table-column>
      <el-table-column prop="mark" label="Mark" width="200"></el-table-column>
      <el-table-column label="Operations" >
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text="OK"
              cancel-button-text="No"
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete this product?"
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

    <el-dialog title="Product Information" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Category">
          <el-select v-model="form.category" placeholder="Select category" style="width: 100%">
            <el-option
                v-for="item in categoryData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Count">
          <el-input v-model="form.count" autocomplete="off"></el-input>
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

    <el-dialog title="Stock" :visible.sync="stockInFormVisible" width="30%">
      <el-dialog width="75%" title="Choose user" :visible.sync="innerVisible" append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">Cancel</el-button>
          <el-button type="primary" @click="confirmUser">OK</el-button>
        </div>
      </el-dialog>
      <el-form label-width="80px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form1.productname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Operator">
          <el-input v-model="form1.username" autocomplete="off" @click.native="selectUser"></el-input>
        </el-form-item>
        <el-form-item label="Count">
          <el-input v-model="form1.count" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Mark">
          <el-input v-model="form1.mark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stockInFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveIn">OK</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "@/views/SelectUser";
export default {
  name: "Product",
  components: {
    SelectUser,
  },
  data() {
    let checkCount = (rule, value, callback) => {
      if (!/^[1-9]\d*$/.test(value)) {
        callback(new Error('Count must be a positive integer'));
      } else if (parseInt(value) > 9999) {
        callback(new Error('Count cannot exceed 9999'));
      } else {
        callback();
      }
    };
    return {
      tempUser:{},
      user: JSON.parse(sessionStorage.getItem('currentUser')),
      categoryData: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      mark: "",
      count:0,
      category: "",
      form: {},
      form1: {
        product:'',
        productname: '',
        count:'',
        username:'',
        userId:'',
        adminId:'',
        mark:'',
        action: '1'
      },
      dialogFormVisible: false,
      stockInFormVisible: false,
      innerVisible: false,
      multipleSelection: [],
      rules: {
        name: [
          {required: true, message: 'Please enter un product name', trigger: 'blur'}
        ],
        count: [
          {required: true, message: 'Please enter un count', trigger: 'blur'},
          { pattern: /^[1-9]\d*$/, message: 'Count must be a positive integer', trigger: 'blur' },
          { validator: checkCount, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.loadCategories();
    this.load();
  },
  methods: {
    load() {
      this.request.get("/product/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          mark: this.mark,
          category: this.category,
          count: this.count
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
        console.log(res)
        if(res.code === '200'){
          this.categoryData = res.data
        }else {
          this.$message.error("Save failed");
        }
      });

    },
    save() {
      this.request.put("/product", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Saved successfully");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save failed");
        }
      });
    },
    saveIn() {
      this.request.put("/record", this.form1).then(res => {
        if (res.code === '200') {
          this.$message.success("Saved successfully");
          this.stockInFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save failed");
        }
      });
    },
    formatCategory(row) {
      let temp = this.categoryData.find(item=> {
        return item.id == row.category
      })
      return temp && temp.name
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    handleStockIn() {
      if(!this.multipleSelection.id){
        alert("Please chose record")
        return;
      }
      this.stockInFormVisible = true;
      this.form1.productname = this.multipleSelection.name
      this.form1.product = this.multipleSelection.id
      this.form1.userId = this.user.id
      this.form1.action = '1'
    },
    handleStockOut() {
      if(!this.multipleSelection.id){
        alert("Please chose record")
        return;
      }
      this.stockInFormVisible = true;
      this.form1.productname = this.multipleSelection.name
      this.form1.product = this.multipleSelection.id
      this.form1.userId = this.user.id
      this.form1.action = "2"
    },
    selectUser() {
      this.innerVisible = true;
    },
    doSelectUser(val) {
      console.log(val)
      this.tempUser = val
    },
    confirmUser() {
      this.form1.username = this.tempUser.username
      this.form1.userId = this.tempUser.id
      this.innerVisible = false
    },
    handleEdit(row) {
      this.form = Object.assign({}, row);
      this.dialogFormVisible = true;
    },
    del(id) {
      this.request.delete("/product/" + id).then(res => {
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
      this.request.delete("/product/del/batch", { data: ids }).then(res => {
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
      this.category = "";
      this.count = 0;
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