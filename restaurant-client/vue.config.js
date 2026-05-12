module.exports = {
    publicPath: '/user',
    devServer: {
        port: 8082,
        proxy: {
            '/api': {
                target: 'http://localhost:9090',
                changeOrigin: true
            }
        }
    }
}