import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)


const loadCart = () => {
    const cart = localStorage.getItem('shoppingCart')
    return cart ? JSON.parse(cart) : []
}


const saveCart = (cart) => {
    localStorage.setItem('shoppingCart', JSON.stringify(cart))
}

export default new Vuex.Store({
    state: {
        cart: loadCart(),
        tableId: localStorage.getItem('tableId') || '',
        userInfo: null
    },

    getters: {
        cartTotalCount: state => {
            return state.cart.reduce((sum, item) => sum + item.quantity, 0)
        },
        cartTotalPrice: state => {
            return state.cart.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2)
        },
        cartHasItems: state => {
            return state.cart.length > 0 && state.cart.some(item => item.quantity > 0)
        }
    },

    mutations: {
        SET_TABLE_ID(state, tableId) {
            state.tableId = tableId
            localStorage.setItem('tableId', tableId)
        },

        SET_CART(state, cart) {
            state.cart = cart
            localStorage.setItem('shoppingCart', JSON.stringify(cart))
        },

        ADD_TO_CART(state, item) {
            const existing = state.cart.find(i => i.dishId === item.dishId && i.flavor === item.flavor)
            if (existing) {
                existing.quantity += item.quantity
            } else {
                state.cart.push({ ...item })
            }
            saveCart(state.cart)
        },

        UPDATE_CART_ITEM(state, item) {
            const index = state.cart.findIndex(i => i.dishId === item.dishId && i.flavor === item.flavor)
            if (index !== -1) {
                if (item.quantity <= 0) {
                    state.cart.splice(index, 1)
                } else {
                    state.cart[index] = { ...item }
                }
            } else if (item.quantity > 0) {
                state.cart.push({ ...item })
            }
            saveCart(state.cart)
        },

        REMOVE_CART_ITEM(state, item) {
            const index = state.cart.findIndex(i => i.dishId === item.dishId && i.flavor === item.flavor)
            if (index !== -1) {
                state.cart.splice(index, 1)
                saveCart(state.cart)
            }
        },

        CLEAR_CART(state) {
            state.cart = []
            localStorage.removeItem('shoppingCart')
        },

        SET_USER_INFO(state, userInfo) {
            state.userInfo = userInfo
        }
    },

    actions: {
        addToCart({ commit }, item) {
            commit('ADD_TO_CART', item)
        },
        updateCartItem({ commit }, item) {
            commit('UPDATE_CART_ITEM', item)
        },
        removeCartItem({ commit }, item) {
            commit('REMOVE_CART_ITEM', item)
        },
        clearCart({ commit }) {
            commit('CLEAR_CART')
        },
        setTableId({ commit }, tableId) {
            commit('SET_TABLE_ID', tableId)
        }
    }
})