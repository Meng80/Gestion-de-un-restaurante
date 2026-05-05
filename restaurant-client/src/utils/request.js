import axios from 'axios'
import { Toast } from 'vant'

const request = axios.create({
    baseURL: process.env.VUE_APP_BASE_API || 'http://localhost:9090',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

request.interceptors.request.use(
    config => {
        const tableId = localStorage.getItem('tableId')
        console.log('url:', config.url, 'tableId:', tableId)

        if (tableId) {
            config.headers['X-Table-Id'] = tableId
            if (config.method === 'get') {
                config.params = config.params || {}
                config.params.tableId = tableId
            }
        }

        return config
    },
    error => {
        return Promise.reject(error)
    }
)

request.interceptors.response.use(
    response => {
        const res = response.data
        if (res.code === '200') {
            return res
        } else {
            Toast.fail(res.msg || 'Fail')
            return Promise.reject(new Error(res.msg || 'Error'))
        }
    },
    error => {
        console.error('Response error:', error)
        Toast.fail(error.message || '网络错误')
        return Promise.reject(error)
    }
)

export default request