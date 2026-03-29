<template>
  <div>
    <!--  Status Summary Card -->
    <div style="padding: 10px 0; display: flex; gap: 16px; flex-wrap: wrap;">
      <el-card
          v-for="status in statusList"
          :key="status.key"
          :class="['status-card', { 'active-card': activeStatus === status.key }]"
          @click.native="filterStatus(status.key)"
          style="flex: 1; cursor: pointer; text-align: center;">
        <div style="font-size: 14px; color: #909399;">{{ status.name }}</div>
        <div style="font-size: 28px; font-weight: bold; margin-top: 8px;">{{ statusCount[status.key] || 0 }}</div>
      </el-card>
    </div>

    <!-- Search Section -->
    <div style="padding: 10px 0">
      <el-input
          style="width: 200px"
          placeholder="Search by OrderNumber"
          suffix-icon="el-icon-search"
          v-model="orderNumber">
      </el-input>

      <el-input
          v-model="tableNumber"
          placeholder="Search by tableNumber"
          suffix-icon="el-icon-search"
          style="width: 200px; margin-left: 10px;">
      </el-input>

      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button class="ml-5" type="warning" @click="reset">Reset</el-button>
    </div>

    <!-- Order List Table -->
    <el-table
        :data="tableData"
        border
        stripe
        :header-cell-class-name="'headerBg'"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="numberOrder" label="Order Number" width="100"></el-table-column>
      <el-table-column prop="numberMesa" label="Table" width="80"></el-table-column>
      <el-table-column label="Dishes" min-width="180">
        <template slot-scope="scope">
          <div class="dish-list">
            <span v-for="(item, idx) in (scope.row.orderDetailList || scope.row.dishes || [])" :key="idx">
              {{ item.name }} * {{ item.number || item.count }}{{ idx < (scope.row.orderDetailList || scope.row.dishes).length - 1 ? '；' : '' }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="orderTime" label="Order Time" width="140"></el-table-column>
      <el-table-column prop="amount" label="Amount" width="80">
        <template slot-scope="scope">
          ¥{{ scope.row.amount }}
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="Remark" width="120" show-overflow-tooltip></el-table-column>
      <el-table-column label="Status" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Operations" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="viewDetail(scope.row)">View</el-button>

          <!-- Pending status (status=2): Show Confirm and Cancel buttons -->
          <template v-if="scope.row.status === 2">
            <el-button type="success" size="small" @click="confirmOrder(scope.row.id)">Confirm</el-button>
            <el-button type="danger" size="small" @click="cancelOrder(scope.row)">Cancel</el-button>
          </template>

          <!-- Delivering status (status=3): Show Deliver and Cancel buttons -->
          <template v-if="scope.row.status === 3">
            <el-button type="success" size="small" @click="deliverOrder(scope.row.id)">Deliver</el-button>
            <el-button type="danger" size="small" @click="cancelOrder(scope.row)">Cancel</el-button>
          </template>

          <!-- Completed status (status=4): Show Refund button -->
          <el-button v-if="scope.row.status === 4" type="warning" size="small" @click="refundOrder(scope.row)">Refund</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- Order Details Dialog -->
    <el-dialog title="Order Details" :visible.sync="dialogVisible" width="60%">
      <el-descriptions v-if="currentOrder" :column="2" border>
        <el-descriptions-item label="Order ID">{{ currentOrder.id }}</el-descriptions-item>
        <el-descriptions-item label="Order Number">{{ currentOrder.numberOrder }}</el-descriptions-item>
        <el-descriptions-item label="Table Number">{{ currentOrder.numberMesa }}</el-descriptions-item>
        <el-descriptions-item label="Order Time">{{ currentOrder.orderTime }}</el-descriptions-item>
        <el-descriptions-item label="Amount">¥{{ currentOrder.amount }}</el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="getStatusType(currentOrder.status)">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Remark" :span="2">{{ currentOrder.remark || 'No remarks' }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 20px">
        <h4>Order Items</h4>
        <el-table :data="currentOrderDetail" border stripe size="small">
          <el-table-column prop="name" label="Dish Name"></el-table-column>
          <el-table-column prop="number" label="Quantity" width="100"></el-table-column>
          <el-table-column prop="price" label="Price" width="100">
            <template slot-scope="scope">
              ¥{{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column label="Subtotal" width="120">
            <template slot-scope="scope">
              ¥{{ (scope.row.price * scope.row.number).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Close</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Order",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      orderNumber: "",
      tableNumber: "",
      multipleSelection: [],
      activeStatus: "all",
      statusList: [
        { key: "all", name: "All Orders", value: null },
        { key: "pending", name: "Pending", value: 2 },
        { key: "shipping", name: "Shipping", value: 3 },
        { key: "completed", name: "Completed", value: 4 },
        { key: "cancelled", name: "Cancelled", value: 5 }
      ],
      statusCount: {
        all: 0,
        pending: 0,
        shipping: 0,
        completed: 0,
        cancelled: 0
      },
      dialogVisible: false,
      currentOrder: null,
      currentOrderDetail: []
    };
  },
  created() {
    this.load();
    this.loadStatusCount();
  },
  methods: {
    load() {
      let params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };

      if (this.orderNumber && this.orderNumber.trim()) {
        params.numberOrder = this.orderNumber.trim();
      }

      if (this.tableNumber && this.tableNumber.trim()) {
        const tableNum = parseInt(this.tableNumber.trim());
        if (!isNaN(tableNum)) {
          params.numberMesa = tableNum;
        }
      }

      // Add status filter
      if (this.activeStatus !== "all") {
        const statusItem = this.statusList.find(s => s.key === this.activeStatus);
        if (statusItem) {
          params.status = statusItem.value;
        }
      }

      this.request.get("/admin/order/page", { params }).then(res => {
        console.log("return data:", res);
        if (res.code === '200') {
          this.tableData = res.data.records || [];
          this.total = res.data.total || 0;
        } else {
          this.$message.error(res.msg || "Failed to load orders");
        }
      }).catch(error => {
        console.error("Failed to load orders:", error);
        this.$message.error("Failed to load orders");
      });
    },

    loadStatusCount() {
      this.request.get("/admin/order/statistics").then(res => {
        if (res.code === '200') {
          const total = (res.data.toBeConfirmed || 0) +
              (res.data.confirmed || 0) +
              (res.data.completed || 0) +
              (res.data.cancelled || 0);

          this.statusCount = {
            all: total,
            pending: res.data.toBeConfirmed || 0,
            shipping: res.data.confirmed || 0,
            completed: res.data.completed || 0,
            cancelled: res.data.cancelled || 0
          };
        }
      }).catch(error => {
        console.error("Failed to load statistics:", error);
      });
    },

    filterStatus(status) {
      this.activeStatus = status;
      this.pageNum = 1;
      this.load();
    },

    reset() {
      this.orderNumber = "";
      this.tableNumber = "";
      this.activeStatus = "all";
      this.pageNum = 1;
      this.load();
    },

    viewDetail(row) {
      this.currentOrder = row;
      this.request.get(`/admin/order/details/${row.id}`).then(res => {
        if (res.code === '200') {
          this.currentOrderDetail = res.data.orderDetailList || [];
        }
      });
      this.dialogVisible = true;
    },

    confirmOrder(id) {
      this.request.put("/admin/order/confirm", { id: id }).then(res => {
        if (res.code === '200') {
          this.$message.success("Order confirmed successfully");
          this.load();
          this.loadStatusCount();
        } else {
          this.$message.error(res.msg || "Failed to confirm order");
        }
      }).catch(error => {
        console.error("Failed to confirm order:", error);
        this.$message.error("Failed to confirm order");
      });
    },

    deliverOrder(id) {
      this.request.put("/admin/order/deliver", { id: id }).then(res => {
        if (res.code === '200') {
          this.$message.success("Order delivered successfully");
          this.load();
          this.loadStatusCount();
        } else {
          this.$message.error(res.msg || "Failed to deliver order");
        }
      }).catch(error => {
        console.error("Failed to deliver order:", error);
        this.$message.error("Failed to deliver order");
      });
    },

    cancelOrder(row) {
      let statusText = row.status === 2 ? 'confirm' : 'deliver';
      this.$prompt('Please enter the reason for cancellation', 'Cancel Order', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputPlaceholder: 'Reason for cancellation'
      }).then(({ value }) => {
        this.request.put("/admin/order/cancel", {
          id: row.id,
          cancelReason: value || 'Cancelled by admin'
        }).then(res => {
          if (res.code === '200') {
            this.$message.success("Order cancelled successfully");
            this.load();
            this.loadStatusCount();
          } else {
            this.$message.error(res.msg || "Failed to cancel order");
          }
        }).catch(error => {
          console.error("Failed to cancel order:", error);
          this.$message.error("Failed to cancel order");
        });
      }).catch(() => {
        this.$message.info("Operation cancelled");
      });
    },

    refundOrder(row) {
      this.$prompt('Please enter the reason for refund', 'Refund Order', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputPlaceholder: 'Reason for refund',
        inputType: 'textarea'
      }).then(({ value }) => {
        this.request.put("/admin/order/refund", {
          id: row.id,
          cancelReason: value || 'Refund requested'
        }).then(res => {
          if (res.code === '200') {
            this.$message.success("Order refunded successfully");
            this.load();
            this.loadStatusCount();
          } else {
            this.$message.error(res.msg || "Failed to refund order");
          }
        }).catch(error => {
          console.error("Failed to refund order:", error);
          this.$message.error("Failed to refund order");
        });
      }).catch(() => {
        this.$message.info("Operation cancelled");
      });
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },

    getStatusText(status) {
      const statusMap = {
        1: "Pending Payment",
        2: "Pending",
        3: "Shipping",
        4: "Completed",
        5: "Cancelled"
      };
      return statusMap[status] || "Unknown";
    },

    getStatusType(status) {
      const typeMap = {
        1: "warning",
        2: "info",
        3: "primary",
        4: "success",
        5: "danger"
      };
      return typeMap[status] || "info";
    }
  }
};
</script>

<style scoped>
.headerBg {
  background: #eee !important;
}

.status-card {
  transition: all 0.3s ease;
  border-radius: 8px;
}

.status-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.active-card {
  border: 2px solid #409eff;
  background-color: #ecf5ff;
}

.dish-list {
  line-height: 1.5;
  font-size: 12px;
  color: #606266;
}

.ml-5 {
  margin-left: 5px;
}
</style>