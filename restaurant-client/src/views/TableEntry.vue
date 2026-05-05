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
          v-model="displayTableId"
          label="Table Number"
          placeholder="Select table number (1-20)"
          readonly
          clickable
          :right-icon="arrowDown"
          @click="showPicker = true"
      />

      <van-popup v-model="showPicker" position="bottom" round>
        <van-picker
            :columns="tableOptions"
            title="Choose your table"
            show-toolbar
            :confirm-button-text="'Confirm'"
            :cancel-button-text="'Cancel'"
            @confirm="onConfirm"
            @cancel="showPicker = false"
        />
      </van-popup>

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
      displayTableId: '',
      tableId: null,
      showPicker: false,
      tableOptions: Array.from({ length: 20 }, (_, i) => `Table ${i + 1}`)
    }
  },
  computed: {
    arrowDown() {
      return 'arrow-down'
    }
  },
  methods: {
    onConfirm(value) {
      this.displayTableId = value
      const match = value.match(/\d+/)
      this.tableId = match ? parseInt(match[0]) : null
      this.showPicker = false
    },
    enterRestaurant() {
      if (this.tableId) {
        this.$store.commit('SET_TABLE_ID', this.tableId)
        this.$router.push('/menu')
      }
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
  background: #fff;
  border-radius: 8px;
}

.van-button {
  margin-top: 20px;
}
</style>