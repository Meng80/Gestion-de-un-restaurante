<template>
  <div class="workspace">
    <div class="page-header">
      <h2>Dashboard</h2>
      <p class="date">{{ currentDate }}</p>
    </div>

    <el-card class="filter-card" shadow="hover">
      <div class="filter-content">
        <span class="filter-label">Date Range:</span>
        <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="to"
            start-placeholder="Start Date"
            end-placeholder="End Date"
            value-format="yyyy-MM-dd"
            @change="loadData">
        </el-date-picker>
        <el-button type="primary" size="small" @click="loadData" style="margin-left: 10px">Query</el-button>
      </div>
    </el-card>

    <div class="section-title">
      <span>📊 Today's Business Data</span>
    </div>
    <div class="stats-grid">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon turnover">💰</div>
          <div class="stat-info">
            <div class="stat-label">Turnover</div>
            <div class="stat-value">€{{ businessData.turnover || 0 }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon orders">📋</div>
          <div class="stat-info">
            <div class="stat-label">Valid Orders</div>
            <div class="stat-value">{{ businessData.validOrderCount || 0 }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon rate">📊</div>
          <div class="stat-info">
            <div class="stat-label">Completion Rate</div>
            <div class="stat-value">{{ completionRate }}%</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon price">💵</div>
          <div class="stat-info">
            <div class="stat-label">Avg. Unit Price</div>
            <div class="stat-value">€{{ businessData.unitPrice || 0 }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <el-row :gutter="20">
      <el-col :span="14">
        <div class="section-title">
          <span>📋 Order Management</span>
        </div>
        <el-card class="section-card" shadow="hover">
          <div slot="header" class="section-header">
            <span>Today's Orders by Status</span>
            <el-button type="text" @click="goToOrders">View All →</el-button>
          </div>
          <div class="order-stats">
            <div class="order-item">
              <div class="order-label">Pending</div>
              <div class="order-count">{{ orderData.waitingOrders || 0 }}</div>
            </div>
            <div class="order-item">
              <div class="order-label">Preparing</div>
              <div class="order-count">{{ orderData.deliveredOrders || 0 }}</div>
            </div>
            <div class="order-item">
              <div class="order-label">Completed</div>
              <div class="order-count">{{ orderData.completedOrders || 0 }}</div>
            </div>
            <div class="order-item">
              <div class="order-label">Cancelled</div>
              <div class="order-count">{{ orderData.cancelledOrders || 0 }}</div>
            </div>
            <div class="order-item total">
              <div class="order-label">Total</div>
              <div class="order-count">{{ orderData.allOrders || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="10">
        <div class="section-title">
          <span>🍽️ Dish Overview</span>
        </div>
        <el-card class="section-card" shadow="hover">
          <div slot="header" class="section-header">
            <span>Dish Status</span>
            <el-button type="text" @click="goToDishes">Manage →</el-button>
          </div>
          <div class="dish-stats">
            <div class="dish-item" @click="goToDishes">
              <div class="dish-icon">🍽️</div>
              <div class="dish-info">
                <div class="dish-label">Available</div>
                <div class="dish-count">{{ dishData.sold || 0 }}</div>
              </div>
            </div>
            <div class="dish-item" @click="goToDishes">
              <div class="dish-icon">🚫</div>
              <div class="dish-info">
                <div class="dish-label">Discontinued</div>
                <div class="dish-count">{{ dishData.discontinued || 0 }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="section-title" style="margin-top: 10px">
      <span>📈 Overall Statistics (Selected Period)</span>
    </div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="overview-card">
          <div class="overview-item">
            <div class="overview-icon">📋</div>
            <div class="overview-info">
              <div class="overview-label">Total Orders</div>
              <div class="overview-value">{{ orderReport.totalOrderCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="overview-card">
          <div class="overview-item">
            <div class="overview-icon">✅</div>
            <div class="overview-info">
              <div class="overview-label">Completed Orders</div>
              <div class="overview-value">{{ orderReport.validOrderCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="overview-card">
          <div class="overview-item">
            <div class="overview-icon">📊</div>
            <div class="overview-info">
              <div class="overview-label">Completion Rate</div>
              <div class="overview-value">{{ formatCompletionRate(orderReport.orderCompletionRate) }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="section-title">
      <span>💰 Turnover Trend</span>
    </div>
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover">
          <div id="turnover-chart" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <div class="section-title">
      <span>📊 Order Statistics Trend</span>
    </div>
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover">
          <div id="order-chart" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <div class="section-title">
      <span>🏆 Top 10 Best Selling Dishes</span>
    </div>
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover">
          <el-table :data="topDishes" border stripe style="width: 100%">
            <el-table-column type="index" label="Rank" width="80" align="center">
              <template slot-scope="scope">
                <span :class="getRankClass(scope.$index + 1)">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="Dish Name" min-width="200"></el-table-column>
            <el-table-column prop="number" label="Sales Quantity" width="150" align="center">
              <template slot-scope="scope">
                <span style="font-weight: bold; color: #409EFF">{{ scope.row.number }}</span>
              </template>
            </el-table-column>
          </el-table>
          <div style="text-align: center; margin-top: 15px" v-if="topDishes.length === 0">
            <span style="color: #999">No sales data available</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Workspace",
  data() {
    return {
      currentDate: "",
      dateRange: [],
      businessData: {
        turnover: 0,
        validOrderCount: 0,
        orderCompletionRate: 0,
        unitPrice: 0
      },
      orderData: {
        waitingOrders: 0,
        deliveredOrders: 0,
        completedOrders: 0,
        cancelledOrders: 0,
        allOrders: 0
      },
      dishData: {
        sold: 0,
        discontinued: 0
      },
      topDishes: [],
      turnoverReport: {
        dateList: [],
        turnoverList: []
      },
      orderReport: {
        dateList: [],
        orderCountList: [],
        validOrderCountList: [],
        totalOrderCount: 0,
        validOrderCount: 0,
        orderCompletionRate: 0
      }
    };
  },
  computed: {
    completionRate() {
      const rate = this.businessData.orderCompletionRate || 0;
      return (rate * 100).toFixed(2);
    }
  },
  created() {
    this.setCurrentDate();
    this.initDateRange();
    this.loadData();
  },
  methods: {
    setCurrentDate() {
      const now = new Date();
      const options = { year: "numeric", month: "long", day: "numeric" };
      this.currentDate = now.toLocaleDateString("en-US", options);
    },

    formatCompletionRate(rate) {
      if (rate === undefined || rate === null) return '0.00';
      return (rate * 100).toFixed(2);
    },

    initDateRange() {
      const end = new Date();
      const start = new Date();
      start.setDate(start.getDate() - 7);
      this.dateRange = [this.formatDate(start), this.formatDate(end)];
    },

    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },

    loadData() {

      this.request.get("/admin/workspace/businessData").then(res => {
        if (res.code === "200") {
          this.businessData = res.data;
        }
      }).catch(error => {
        console.error("Failed to load business data:", error);
      });

      this.request.get("/admin/workspace/overviewOrders").then(res => {
        if (res.code === "200") {
          this.orderData = res.data;
        }
      }).catch(error => {
        console.error("Failed to load order overview:", error);
      });

      this.request.get("/admin/workspace/overviewDishes").then(res => {
        if (res.code === "200") {
          this.dishData = res.data;
        }
      }).catch(error => {
        console.error("Failed to load dish overview:", error);
      });

      if (this.dateRange && this.dateRange.length === 2) {
        const begin = this.dateRange[0];
        const end = this.dateRange[1];

        this.request.get("/admin/workspace/turnover", {
          params: { begin: begin, end: end }
        }).then(res => {
          if (res.code === "200") {
            this.turnoverReport = res.data;
            this.renderTurnoverChart();
          }
        }).catch(error => {
          console.error("Failed to load turnover report:", error);
        });

        this.request.get("/admin/workspace/orderStatistics", {
          params: { begin: begin, end: end }
        }).then(res => {
          if (res.code === "200") {
            this.orderReport = res.data;
            this.renderOrderChart();
          }
        }).catch(error => {
          console.error("Failed to load order report:", error);
        });

        this.request.get("/admin/workspace/top10", {
          params: { begin: begin, end: end }
        }).then(res => {
          if (res.code === "200" && res.data) {
            const nameList = res.data.nameList ? res.data.nameList.split(',') : [];
            const numberList = res.data.numberList ? res.data.numberList.split(',').map(Number) : [];
            this.topDishes = nameList.map((name, index) => ({
              name: name,
              number: numberList[index] || 0
            }));
          }
        }).catch(error => {
          console.error("Failed to load top10 dishes:", error);
        });
      }
    },

    renderTurnoverChart() {
      const chartDom = document.getElementById('turnover-chart');
      if (!chartDom) return;

      const myChart = echarts.init(chartDom);
      const dateList = this.turnoverReport.dateList ? this.turnoverReport.dateList.split(',') : [];
      const turnoverList = this.turnoverReport.turnoverList ? this.turnoverReport.turnoverList.split(',').map(Number) : [];

      const option = {
        tooltip: { trigger: 'axis', formatter: '{b}<br/>Turnover: €{c}' },
        xAxis: { type: 'category', data: dateList, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: 'Turnover (€)' },
        series: [{
          name: 'Turnover', type: 'line', data: turnoverList, smooth: true,
          lineStyle: { color: '#409EFF', width: 3 },
          areaStyle: { color: 'rgba(64, 158, 255, 0.1)' },
          symbol: 'circle', symbolSize: 8
        }]
      };
      myChart.setOption(option);
    },

    renderOrderChart() {
      const chartDom = document.getElementById('order-chart');
      if (!chartDom) return;

      const myChart = echarts.init(chartDom);
      const dateList = this.orderReport.dateList ? this.orderReport.dateList.split(',') : [];
      const orderCountList = this.orderReport.orderCountList ? this.orderReport.orderCountList.split(',').map(Number) : [];
      const validOrderCountList = this.orderReport.validOrderCountList ? this.orderReport.validOrderCountList.split(',').map(Number) : [];

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['Total Orders', 'Completed Orders'], top: 0 },
        xAxis: { type: 'category', data: dateList, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: 'Number of Orders' },
        series: [
          { name: 'Total Orders', type: 'bar', data: orderCountList, itemStyle: { color: '#409EFF' } },
          { name: 'Completed Orders', type: 'line', data: validOrderCountList, lineStyle: { color: '#67C23A', width: 3 } }
        ]
      };
      myChart.setOption(option);
    },

    goToOrders() {
      this.$router.push("/order");
    },

    goToDishes() {
      this.$router.push("/dish");
    },

    getRankClass(rank) {
      if (rank === 1) return 'rank-1';
      if (rank === 2) return 'rank-2';
      if (rank === 3) return 'rank-3';
      return '';
    }
  }
}
</script>

<style scoped>
.workspace {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #333;
}

.page-header .date {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.filter-card {
  margin-bottom: 24px;
}

.filter-content {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-label {
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

.section-title {
  margin: 20px 0 15px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  padding-left: 8px;
  border-left: 4px solid #409EFF;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 10px;
}

.stat-card {
  cursor: pointer;
}

/*.stat-card :deep(.el-card__body) {*/
/*  padding: 20px;*/
/*}*/

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.turnover { background: #e6f7ff; }
.stat-icon.orders { background: #e6fffa; }
.stat-icon.rate { background: #e8f5e9; }
.stat-icon.price { background: #fff3e0; }

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  color: #999;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.order-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.order-item {
  flex: 1;
  min-width: 80px;
  text-align: center;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.order-item.total {
  background: #1989fa;
}

.order-item.total .order-label,
.order-item.total .order-count {
  color: white;
}

.order-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.order-count {
  font-size: 22px;
  font-weight: bold;
  color: #333;
}

.dish-stats {
  display: flex;
  gap: 16px;
}

.dish-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.dish-item:hover {
  background: #eef2f6;
}

.dish-icon {
  font-size: 28px;
}

.dish-info {
  flex: 1;
}

.dish-label {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.dish-count {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.overview-card {
  cursor: pointer;
}

.overview-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px;
}

.overview-icon {
  width: 45px;
  height: 45px;
  background: #f0f2f5;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.overview-info {
  flex: 1;
}

.overview-label {
  font-size: 13px;
  color: #999;
  margin-bottom: 5px;
}

.overview-value {
  font-size: 22px;
  font-weight: bold;
  color: #333;
}

.rank-1 {
  background: linear-gradient(135deg, #ffd700, #ffb347);
  display: inline-block;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  color: white;
  font-weight: bold;
}

.rank-2 {
  background: linear-gradient(135deg, #c0c0c0, #a0a0a0);
  display: inline-block;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  color: white;
  font-weight: bold;
}

.rank-3 {
  background: linear-gradient(135deg, #cd7f32, #b87333);
  display: inline-block;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  color: white;
  font-weight: bold;
}
</style>