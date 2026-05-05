<template>
  <div class="history-order-page">
    <van-nav-bar title="Order History" left-arrow @click-left="$router.back()" />

    <div class="table-info">
      <van-icon name="shop-o" />
      <span>Table {{ tableId }}</span>
    </div>

    <div v-if="loading" class="loading">
      <van-loading />
    </div>

    <div v-else-if="orderList.length > 0" class="order-list">
      <div
          v-for="order in orderList"
          :key="order.id"
          class="order-card"
          @click="viewOrderDetail(order)"
      >
        <div class="order-header">
          <div class="order-number">Order #{{ order.numberOrder || order.orderNumber }}</div>
          <van-tag :type="getStatusType(order.status)">
            {{ getStatusText(order.status) }}
          </van-tag>
        </div>
        <div class="order-info">
          <div class="info-item">
            <span class="label">Order Time:</span>
            <span class="value">{{ formatDateTime(order.orderTime) }}</span>
          </div>
          <div class="info-item">
            <span class="label">Total Amount:</span>
            <span class="value amount">€{{ order.amount }}</span>
          </div>
          <div class="info-item" v-if="order.remark">
            <span class="label">Remark:</span>
            <span class="value remark">{{ order.remark }}</span>
          </div>
        </div>
        <div class="order-footer">
          <span class="dish-preview">
            {{ getDishNames(order.orderDetailList) }}
          </span>
          <van-icon name="arrow" />
        </div>
      </div>
    </div>

    <van-empty v-else description="No order history" />
  </div>
</template>

<script>
import { getOrderList } from '@/api/order'
import { Toast } from 'vant'

export default {
  name: 'HistoryOrder',
  data() {
    return {
      tableId: '',
      orderList: [],
      loading: true
    }
  },
  async mounted() {

    this.tableId = this.$route.query.tableId || this.$store.state.tableId

    if (!this.tableId) {
      Toast('No table number found')
      this.$router.back()
      return
    }

    await this.loadHistoryOrders()
  },
  methods: {
    async loadHistoryOrders() {
      this.loading = true

      try {
        console.log('查询桌号:', this.tableId)

        const res = await getOrderList({ tableId: this.tableId })
        console.log('返回数据:', res)

        if (res.code === '200') {
          this.orderList = res.data.records || res.data || []
          console.log('订单列表:', this.orderList)
        } else {
          Toast.fail(res.msg || 'Failed to load orders')
        }
      } catch (error) {
        console.error('Load history error:', error)
        Toast.fail('Failed to load order history')
      } finally {
        this.loading = false
      }
    },

    viewOrderDetail(order) {
      const orderNumber = order.numberOrder || order.orderNumber
      this.$router.push(`/orders?orderNumber=${orderNumber}`)
    },

    getDishNames(detailList) {
      if (!detailList || detailList.length === 0) return 'No dishes'
      const names = detailList.slice(0, 3).map(item => item.name)
      if (detailList.length > 3) {
        return names.join('、') + ` +${detailList.length - 3}`
      }
      return names.join('、')
    },

    formatDateTime(dateTime) {
      if (!dateTime) return ''
      if (Array.isArray(dateTime)) {
        const [year, month, day, hour, minute] = dateTime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
      }
      return dateTime
    },

    getStatusText(status) {
      const map = { 1: 'To Pay', 2: 'Pending', 3: 'Preparing', 4: 'Completed', 5: 'Cancelled' }
      return map[status] || 'Unknown'
    },

    getStatusType(status) {
      const map = { 1: 'warning', 2: 'primary', 3: 'success', 4: 'success', 5: 'danger' }
      return map[status] || 'default'
    }
  }
}
</script>

<style scoped>
.history-order-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.table-info {
  background: #fff;
  padding: 12px 16px;
  margin: 10px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.loading {
  display: flex;
  justify-content: center;
  margin-top: 50px;
}

.order-list {
  padding: 0 10px;
}

.order-card {
  background: #fff;
  border-radius: 12px;
  margin-bottom: 12px;
  padding: 14px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.order-card:active {
  transform: scale(0.98);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.order-number {
  font-weight: bold;
  font-size: 14px;
  color: #333;
}

.order-info {
  margin-bottom: 10px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
  font-size: 13px;
}

.info-item .label {
  color: #999;
}

.info-item .value {
  color: #666;
}

.info-item .value.amount {
  color: #f44;
  font-weight: bold;
}

.info-item .value.remark {
  color: #1989fa;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
}

.dish-preview {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}
</style>


