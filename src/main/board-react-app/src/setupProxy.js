const {createProxyMiddleware} = require('http-proxy-middleware');

module.exports = (app) => {
    // WebSocket 연결을 위한 프록시 설정
    app.use(
        "/ws",
        createProxyMiddleware({
            target: "http://localhost:8080",
            ws: false })
    );

    // HTTP 요청을 위한 프록시 설정
    app.use(
        '/api', // 이 경로로 시작하는 HTTP 요청을 잡아서

        createProxyMiddleware({
            target: 'http://localhost:8080', // 여기로 전달
            changeOrigin: true,
        })
    );
};