import request from '@/utils/request'

/**
 * Submit order
 * @param {Object} data - { tableId, totalAmount, items }
 * @returns {Promise}
 */
export function submitOrder(data) {
    return request({
        url: '/user/order/submit',
        method: 'post',
        data
    })
}

export function searchOrderByNumber(numberOrder) {
    return request({
        url: '/user/order/search',
        method: 'get',
        params: { numberOrder }
    })
}

/**
 * Get orders by table id
 * @param {Object} params - { tableId }
 * @returns {Promise}
 */
export function getOrderList(params) {
    return request({
        url: '/user/order/list',
        method: 'get',
        params
    })
}

/**
 * Get order detail
 * @param {Number} tableId
 * @returns {Promise}
 */
export function getOrderDetail(tableId) {
    return request({
        url: `/user/order/detail/${tableId}`,
        method: 'get'
    })
}


export function cancelOrder(id) {
    return request({
        url: `/user/order/cancel/${id}`,
        method: 'put'
    })
}