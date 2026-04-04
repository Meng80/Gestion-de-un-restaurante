<template>
  <div class="cart-page">
    <van-nav-bar title="Shopping Cart" left-arrow @click-left="$router.back()">
      <template #right>
        <van-button type="danger" plain size="small" @click="clearAll">ClearAll</van-button>
      </template>
    </van-nav-bar>

    <div v-if="cartList.length > 0">
      <van-card
          v-for="item in cartList"
          :key="item.id"
          :title="item.name"
          :price="item.amount"
          :num="item.number"
          :thumb="item.image || '/default-dish.png'"
      >
        <template #tags>
          <van-tag type="primary">€{{ (item.amount / item.number).toFixed(2) }} / each</van-tag>
        </template>
        <template #footer>
          <van-stepper
              v-model="item.number"
              min="0"
              :max="99"
              @plus="increaseItem(item)"
              @minus="decreaseItem(item)"
          />
        </template>
      </van-card>

      <div class="total">
        <div class="total-info">
          <span>Total：</span>
          <span class="total-price">€{{ totalPrice }}</span>
        </div>
        <van-button type="primary" block @click="submitOrder">Place Order</van-button>
      </div>
    </div>

    <van-empty v-else description="Your cart is empty">
      <van-button type="primary" @click="goToMenu">goToMenu</van-button>
    </van-empty>
  </div>
</template>

<script>

import { submitOrder } from '@/api/order'
import { getCartList, deleteCartItem, clearCart, addToCart, subCart } from '@/api/cart'

export default {
  name: 'ShoppingCart',
  data() {
    return {
      cartList: [],
      tableId: localStorage.getItem('tableId') || ''
    }
  },
  computed: {
    totalPrice() {
      return this.cartList.reduce((sum, item) => sum + (item.amount || 0), 0).toFixed(2)
    }
  },
  mounted() {
    this.loadCartList()
  },
  methods: {
    async loadCartList() {
      try {
        const res = await getCartList()
        if (res.code === '200') {
          this.cartList = res.data || []
          this.syncToVuex()
        }
      } catch (error) {
        console.error('Failed to load cart:', error)
        this.$toast.fail('Failed to load cart')
      }
    },


    syncToVuex() {
      const vuexCart = this.cartList.map(item => ({
        dishId: item.dishId,
        name: item.name,
        price: item.amount / item.number,
        quantity: item.number,
        image: item.image
      }))
      this.$store.commit('SET_CART', vuexCart)
    },

    async updateQuantity(item) {

      const oldItem = this.cartList.find(i => i.id === item.id)
      const oldNumber = oldItem ? oldItem.number : item.number
      const newNumber = item.number

      try {
        if (newNumber <= 0) {
          await deleteCartItem(item.id)
          this.$toast.success('Delete')
        } else if (newNumber > oldNumber) {
          const diff = newNumber - oldNumber
          await addToCart({ dishId: item.dishId, number: diff })
        } else if (newNumber < oldNumber) {
          const diff = oldNumber - newNumber
          await subCart({ dishId: item.dishId, number: diff })
        }
        await this.loadCartList()
      } catch (error) {
        console.error('Update Error:', error)
        this.$toast.fail('Operation Error')
        await this.loadCartList()
      }
    },

    async increaseItem(item) {
      console.log('➕ Add:', item.name)
      try {
        await addToCart({
          dishId: item.dishId,
          number: 1
        })
        await this.loadCartList()
      } catch (error) {
        console.error('Add Error:', error)
        this.$toast.fail('Operation Error')
      }
    },

    async decreaseItem(item) {
      console.log('➖ Decrease:', item.name, 'Count actual:', item.number)
      try {
        if (item.number <= 1) {
          await deleteCartItem(item.id)
          this.$toast.success('Delete Success')
        } else {
          await subCart({
            dishId: item.dishId,
            number: 1
          })
        }
        await this.loadCartList()
      } catch (error) {
        console.error('Decrease Error:', error)
        this.$toast.fail('Operation Error ')
      }
    },

    async clearAll() {
      try {
        await clearCart()
        this.$toast.success('Clear success')
        this.cartList = []
        this.$store.commit('CLEAR_CART')
      } catch (error) {
        console.error('Clear Error:', error)
        this.$toast.fail('Clear Error')
      }
    },

    async submitOrder() {
      if (!this.tableId) {
        this.$toast('Please chose un table number')
        this.$router.push('/')
        return
      }

      if (this.cartList.length === 0) {
        this.$toast('Your cart is empty')
        return
      }

      const orderData = {
        tableId: parseInt(this.tableId),
        totalAmount: this.totalPrice,
        items: this.cartList.map(item => ({
          dishId: item.dishId,
          name: item.name,
          price: item.amount / item.number,
          quantity: item.number
        }))
      }

      try {
        const res = await submitOrder(orderData)
        if (res.code === '200') {
          this.$toast.success('Submit Order Success')
          await clearCart()
          this.cartList = []
          this.$store.commit('CLEAR_CART')
          this.$router.push(`/order-detail/${res.data}`)
        }
      } catch (error) {
        console.error('Submit Order Error:', error)
        this.$toast.fail('Submit Order Error')
      }
    },

    goToMenu() {
      this.$router.push('/menu')
    }
  }
}
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 80px;
}

.van-card {
  margin-bottom: 8px;
}

.total {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0,0,0,0.05);
}

.total-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 16px;
}

.total-price {
  color: #f44;
  font-weight: bold;
  font-size: 20px;
}
</style>