import axios from "axios";
import {jwtDecode} from "jwt-decode";

const api = axios.create({
    timeout: 1000,
})
// 인터셉터 설정
api.interceptors.request.use(
    async (config) => {
        const now = Date.now();
        const accessToken = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');

        // accessToken의 만료 시간을 디코드하여 확인하는 로직이 필요합니다.
        // 예를 들어, JWT 토큰의 경우 jwt-decode 라이브러리 등을 사용할 수 있습니다.
        const decodedToken = jwtDecode(accessToken);
        if (decodedToken.exp * 1000 < now) {

            // 토큰이 만료되었다면 refreshToken으로 새 accessToken 요청
            const response = await api.post('api/refresh-token', { refreshToken });
            const newAccessToken = response.data.accessToken;

            // 새로 발급받은 accessToken 저장
            localStorage.setItem('accessToken', newAccessToken);

            // 현재 요청에 새로운 accessToken 적용
            config.headers['Authorization'] = `Bearer ${newAccessToken}`;
        } else {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;