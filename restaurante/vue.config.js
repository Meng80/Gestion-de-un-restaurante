module.exports = {
    publicPath: '/',
    devServer: {
        port: 8081,
        proxy: {
            '/api': {
                target: 'http://localhost:9090',
                changeOrigin: true
            }
        }
    }
}