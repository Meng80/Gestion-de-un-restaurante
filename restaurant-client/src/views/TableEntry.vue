<template>
  <div class="table-entry">
    <van-nav-bar title="SmartOrder" fixed placeholder />

    <div class="content">
      <div class="logo">
        <img src="../assets/logo.png" alt="logo" />
        <h2>Welcome</h2>
        <p>Please enter your table number</p>
      </div>

      <van-field
          v-model="tableId"
          label="Table Number"
          placeholder="Enter your table number"
          type="number"
          clearable
      />

      <van-button
          type="primary"
          block
          :disabled="!tableId"
          @click="enterRestaurant"
      >
       Order Food
      </van-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TableEntry',
  data() {
    return {
      tableId: ''
    }
  },
  methods: {
    enterRestaurant() {
      // 保存桌号到 Vuex 和 localStorage
      this.$store.commit('SET_TABLE_ID', this.tableId)
      // 跳转到点餐页面
      this.$router.push('/menu')
    }
  }
}
</script>

<style scoped>
.table-entry {
  min-height: 100vh;
  background: #f5f5f5;
}

.content {
  padding: 60px 20px;
  text-align: center;
}

.logo {
  margin-bottom: 40px;
}

.logo img {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
}

.logo h2 {
  margin: 10px 0;
  color: #333;
}

.logo p {
  color: #666;
  font-size: 14px;
}

.van-field {
  margin: 20px 0;
}

.van-button {
  margin-top: 20px;
}
</style>