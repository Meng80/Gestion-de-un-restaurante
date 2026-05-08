<template>
  <div class="orders-page">
    <van-nav-bar title="Order Query" left-arrow @click-left="$router.back()">
      <template #right>
        <van-icon name="shop-o" @click="goToMenu" />
      </template>
    </van-nav-bar>

    <div v-if="latestOrder" class="latest-order-card">
      <div class="card-header">
        <van-icon name="checked" class="check-icon" />
        <span class="title">Your Latest Order</span>
      </div>
      <div class="order-info">
        <div class="info-item">
          <span class="label">Table Number:</span>
          <span class="value">{{ latestOrder.tableId }}</span>
        </div>
        <div class="info-item">
          <span class="label">Order Number:</span>
          <span class="value order-number">{{ latestOrder.orderNumber }}</span>
        </div>
        <div class="info-item">
          <span class="label">Order Time:</span>
          <span class="value">{{ latestOrder.orderTime }}</span>
        </div>
      </div>
      <div class="card-footer">
        <van-button size="small" type="primary" @click="queryLatestOrder">
          Query This Order
        </van-button>
        <van-button size="small" plain @click="dismissLatestOrder">
          Dismiss
        </van-button>
      </div>
    </div>


    <div class="search-box" :class="{ 'no-margin': latestOrder }">
      <van-field
          v-model="orderNumber"
          label="Order Number"
          placeholder="Please enter order number"
          clearable
      >
        <template #button>
          <van-button size="small" type="primary" @click="handleSearch" :loading="loading">
            Query
          </van-button>
        </template>
      </van-field>
    </div>


    <div v-if="order" class="order-card">
      <div class="status-bar">
        <van-tag :type="getStatusType(order.status)" size="large">
          {{ getStatusText(order.status) }}
        </van-tag>
        <van-tag
            :type="getPayStatusType(order.payStatus)"
            size="large"
            plain
        >
          {{ getPayStatusText(order.payStatus) }}
        </van-tag>
      </div>

      <van-cell-group title="Order Info">
        <van-cell title="Table Number" :value="order.numberMesa" />
        <van-cell title="Order Number" :value="order.orderNumber || order.numberOrder" />
        <van-cell title="Order Time" :value="formatDateTime(order.orderTime)" />
        <van-cell title="Total Amount" :value="'€' + order.amount" />
        <van-cell v-if="order.remark" title="Remark" :value="order.remark" />
      </van-cell-group>

      <van-cell-group title="Order Items">
        <div class="dish-list">
          <div v-for="(item, index) in order.orderDetailList" :key="index" class="dish-item">
            <div class="dish-image">
              <img :src="item.image || '/default-dish.png'" :alt="item.name" @error="handleImageError" />
            </div>
            <div class="dish-info">
              <div class="dish-name">{{ item.name }}</div>
              <div class="dish-price">€{{ (item.amount / item.number).toFixed(2) }}</div>
            </div>
            <div class="dish-quantity">
              × {{ item.number }}
            </div>
          </div>
        </div>
      </van-cell-group>

      <div class="total-section">
        <div class="total-row">
          <span>Total</span>
          <span class="total-amount">€{{ order.amount }}</span>
        </div>
      </div>

      <div class="action-buttons" v-if="canCancel">
        <van-button type="danger" block @click="handleCancel">Cancel Order</van-button>
      </div>
    </div>

    <div class="action-buttons" v-if="canReminder">
      <van-button type="warning" block @click="handleReminder" :loading="reminderLoading">
        🔔 Reminder (Hurry Up)
      </van-button>
    </div>

    <van-empty v-else-if="searched && !order" description="Order not found" />

    <div class="tip" v-else-if="!latestOrder && !searched">
      <van-icon name="info-o" />
      <span>Please enter order number to query</span>
    </div>
  </div>
</template>

<script>
import { searchOrderByNumber, cancelOrder, reminderOrder  } from '@/api/order'
import { Dialog, Toast } from 'vant'

export default {
  name: 'Orders',
  data() {
    return {
      orderNumber: '',
      order: null,
      loading: false,
      searched: false,
      latestOrder: null,
      reminderLoading: false
    }
  },
  computed: {
    canCancel() {
      return this.order && (this.order.status === 1 || this.order.status === 2)
    },
    canReminder() {
      return this.order && (this.order.status === 2 || this.order.status === 3)
    }
  },
  mounted() {
    const savedOrder = localStorage.getItem('latestOrder')
    if (savedOrder) {
      this.latestOrder = JSON.parse(savedOrder)
    }
  },
  methods: {

    goToMenu() {
      this.$router.push('/menu')
    },

    formatDateTime(dateTime) {
      if (!dateTime) return ''
      if (Array.isArray(dateTime)) {
        const [year, month, day, hour, minute, second] = dateTime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`
      }
      return dateTime
    },

    async queryLatestOrder() {
      if (this.latestOrder) {
        this.orderNumber = this.latestOrder.orderNumber
        await this.handleSearch()
      }
    },

    dismissLatestOrder() {
      this.latestOrder = null
      localStorage.removeItem('latestOrder')
    },

    async handleSearch() {
      if (!this.orderNumber.trim()) {
        Toast('Please enter order number')
        return
      }

      this.loading = true
      this.searched = true

      try {
        const res = await searchOrderByNumber(this.orderNumber.trim())
        if (res.code === '200' && res.data) {
          this.order = res.data
          Toast.success('Order found')
        } else {
          this.order = null
          Toast.fail('Order not found')
        }
      } catch (error) {
        console.error('Search error:', error)
        this.order = null
        Toast.fail('Order not found')
      } finally {
        this.loading = false
      }
    },

    async handleCancel() {
      Dialog.confirm({
        title: 'Cancel Order',
        message: 'Are you sure you want to cancel this order?'
      }).then(async () => {
        try {
          await cancelOrder(this.order.id)
          Toast.success('Order cancelled')
          this.handleSearch()
        } catch (error) {
          Toast.fail('Cancel failed')
        }
      }).catch(() => {})
    },

    async handleReminder() {
      this.reminderLoading = true
      try {
        const res = await reminderOrder(this.order.id)
        if (res.code === '200') {
          Toast.success(res.msg || 'Reminder sent, restaurant notified')
        } else {
          Toast.fail(res.msg || 'Reminder failed')
        }
      } catch (error) {
        console.error('Reminder error:', error)
        Toast.fail('Reminder failed, please try again')
      } finally {
        this.reminderLoading = false
      }
    },


    getStatusText(status) {
      const map = { 1: 'To Pay', 2: 'Pending', 3: 'Preparing', 4: 'Completed', 5: 'Cancelled' }
      return map[status] || 'Unknown'
    },

    getStatusType(status) {
      const map = { 1: 'warning', 2: 'primary', 3: 'success', 4: 'success', 5: 'danger' }
      return map[status] || 'default'
    },

    getPayStatusText(payStatus) {
      const map = { 0: 'Unpaid', 1: 'Paid', 2: 'Refunded' }
      return map[payStatus] || 'Unknown'
    },

    getPayStatusType(payStatus) {
      const map = { 0: 'warning', 1: 'success', 2: 'default' }
      return map[payStatus] || 'default'
    },

    handleImageError(e) {
      e.target.src = '/default-dish.png'
    }
  }
}
</script>

<style scoped>
.orders-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.latest-order-card {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
  margin: 10px;
  padding: 16px;
  border-radius: 12px;
  color: white;
  animation: slideDown 0.5s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: bold;
}

.check-icon {
  font-size: 20px;
}

.order-info {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-item .label {
  opacity: 0.85;
  font-size: 13px;
}

.info-item .value {
  font-weight: bold;
  font-size: 14px;
}

.order-number {
  letter-spacing: 1px;
}

.card-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.search-box {
  background: #fff;
  padding: 16px;
  margin-bottom: 10px;
}

.order-card {
  background: #fff;
  margin: 10px;
  border-radius: 8px;
  overflow: hidden;
}

.status-bar {
  padding: 20px;
  text-align: center;
  background: #fff;
  border-bottom: 1px solid #ebedf0;
  display: flex;
  justify-content: center;
  gap: 10px;
}

.action-buttons {
  margin: 20px 16px;
}

.tip {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.tip i {
  font-size: 48px;
  margin-bottom: 10px;
  display: block;
}

.dish-list {
  padding: 0 16px;
}

.dish-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.dish-item:last-child {
  border-bottom: none;
}

.dish-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  background: #f5f5f5;
  flex-shrink: 0;
}

.dish-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dish-info {
  flex: 1;
  margin-left: 12px;
}

.dish-name {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.dish-price {
  font-size: 13px;
  color: #999;
}

.dish-quantity {
  font-size: 15px;
  color: #666;
  flex-shrink: 0;
  margin-left: 12px;
}

.total-section {
  padding: 16px;
  border-top: 1px solid #ebedf0;
  background: #fff;
}

.total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.total-amount {
  color: #f44;
  font-size: 18px;
  font-weight: bold;
}

</style>