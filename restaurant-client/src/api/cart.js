import request from '@/utils/request'


export function addToCart(data) {
    return request({
        url: '/user/cart/add',
        method: 'post',
        data
    })
}


export function getCartList() {
    return request({
        url: '/user/cart/list',
        method: 'get'
    })
}


export function subCart(data) {
    return request({
        url: '/user/cart/sub',
        method: 'post',
        data
    })
}


export function updateCart(data) {
    return request({
        url: '/user/cart/update',
        method: 'put',
        data
    })
}


export function deleteCartItem(id) {
    return request({
        url: `/user/cart/${id}`,
        method: 'delete'
    })
}


export function clearCart() {
    return request({
        url: '/user/cart/clear',
        method: 'delete'
    })
}