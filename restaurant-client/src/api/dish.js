import request from '@/utils/request'

/**
 * Get category list
 */
export function getCategoryList() {
    return request({
        url: '/user/category/list',
        method: 'get',

    })
}

/**
 * Get dish list by category id
 * @param {Object} params - { categoryId }
 */
export function getDishList(params) {
    return request({
        url: '/user/dish/list',
        method: 'get',
        params
    })
}


export const searchDishes = (keyword) => {
    return request({
        url: 'user/dish/search',
        method: 'get',
        params: {
            keyword: keyword,
        }
    })
}