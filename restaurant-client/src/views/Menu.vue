<template>
  <div class="menu-page">
    <van-nav-bar title="Restaurant" fixed placeholder>
      <template #right>
        <van-icon name="cart-o" :badge="cartTotalCount" @click="goToCart" />
      </template>
    </van-nav-bar>

    <div class="menu-container">
      <van-sidebar v-model="activeCategory" @change="onCategoryChange">
        <van-sidebar-item
            v-for="cat in categories"
            :key="cat.id"
            :title="cat.name"
        />
      </van-sidebar>

      <div class="dish-list">
        <div v-for="dish in dishes" :key="dish.id" class="dish-card">
          <div class="dish-image">
            <img :src="dish.image || '/default-dish.png'" :alt="dish.name" />
          </div>
          <div class="dish-info">
            <h4>{{ dish.name }}</h4>
            <p class="description">{{ dish.description || 'Este plato aún no tiene descripción ' }}</p>
            <div class="dish-footer">
              <span class="price">€{{ dish.price }}</span>
              <div class="quantity-control">
                <van-stepper
                    v-model="dish.quantity"
                    min="0"
                    :max="99"
                    @change="updateCart(dish)"
                />
              </div>
            </div>
          </div>
        </div>

        <van-empty v-if="dishes.length === 0" description="No hay platos disponibles" />
      </div>
    </div>

    <div class="cart-bar" v-if="cartTotalCount > 0" @click="goToCart">
      <div class="cart-info">
        <van-icon name="cart-o" />
        <span>ShoppingCart ({{ cartTotalCount }})</span>
        <span class="total-price">¥{{ cartTotalPrice }}</span>
      </div>
      <van-button type="primary" size="small">去结算</van-button>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters, mapMutations } from 'vuex'
import { getCategoryList, getDishList } from '@/api/dish'
import { addToCart, getCartList } from '@/api/cart'

export default {
  name: 'Menu',
  data() {
    return {
      categories: [],
      dishes: [],
      activeCategory: 0
    }
  },
  computed: {
    ...mapState(['cart']),
    ...mapGetters(['cartTotalCount', 'cartTotalPrice']),
  },
  mounted() {
    this.loadCategories()

  },
  methods: {
    ...mapMutations(['UPDATE_CART_ITEM', 'SET_CART']),

    async updateCart(dish) {
      try {
        await addToCart({
          dishId: dish.id,
          number: dish.quantity
        })

        const res = await getCartList()
        if (res.code === '200' && res.data) {
          const vuexCart = res.data.map(item => ({
            dishId: item.dishId,
            name: item.name,
            price: item.amount / item.number,
            quantity: item.number,
            image: item.image
          }))
          this.SET_CART(vuexCart)
        }

      } catch (error) {
        console.error('Add shopping cart error:', error)
        this.$toast.fail('add fail')
      }
    },

    async loadCategories() {
      try {
        const res = await getCategoryList()
        if (res.code === '200') {
          this.categories = res.data
          if (this.categories.length > 0) {
            this.loadDishes(this.categories[0].id)
          }
        } else {
          this.$toast.fail(res.msg || 'Add category fail')
        }
      } catch (error) {
        console.error('Add category fail:', error)
        this.$toast.fail('Load dishes fail')
      }
    },

    async loadDishes(categoryId) {
      try {
        const res = await getDishList({ categoryId })
        if (res.code === '200') {
          this.dishes = res.data.map(dish => ({
            ...dish,
            quantity: this.getCartQuantity(dish.id)
          }))
        } else {
          this.$toast.fail(res.msg || 'Load dishes fail')
        }
      } catch (error) {
        console.error('Load dishes fail:', error)
        this.$toast.fail('Load dishes fail')
      }
    },

    onCategoryChange(index) {
      this.loadDishes(this.categories[index].id)
    },

    getCartQuantity(dishId) {
      const item = this.cart.find(i => i.dishId === dishId)
      return item ? item.quantity : 0
    },

    goToCart() {
      this.$router.push('/cart')
    }
  }
}
</script>

<style scoped>
.menu-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 60px;
}

.menu-container {
  display: flex;
  margin-top: 46px;
}

.van-sidebar {
  width: 100px;
  flex-shrink: 0;
}

.dish-list {
  flex: 1;
  padding: 10px;
}

.dish-card {
  display: flex;
  background: #fff;
  border-radius: 12px;
  margin-bottom: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.dish-image {
  width: 100px;
  height: 100px;
  flex-shrink: 0;
}

.dish-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dish-info {
  flex: 1;
  padding: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.dish-info h4 {
  margin: 0 0 5px;
  font-size: 16px;
  color: #333;
}

.description {
  font-size: 12px;
  color: #999;
  margin: 0 0 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.dish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f44;
  font-weight: bold;
  font-size: 16px;
}

.cart-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 16px;
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0,0,0,0.1);
  cursor: pointer;
}

.cart-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cart-info .van-icon {
  font-size: 24px;
  color: #1989fa;
}

.total-price {
  color: #f44;
  font-weight: bold;
  margin-left: 10px;
}
</style>