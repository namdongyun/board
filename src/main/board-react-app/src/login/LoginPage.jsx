import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function LoginPage(props) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    // 경로 이동 함수
    const navigate = useNavigate();

    const login = async (event) => {
        event.preventDefault(); // 폼 제출 시 브라우저가 자동으로 페이지를 새로고침하는 것을 막습니다.

        // application/x-www-form-urlencoded 형식으로 바꿔주기 위함
        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        try {
            // Axios 라이브러리를 사용하여 서버의 /login 엔드포인트로 POST 요청을 보냅니다.
            // await 키워드는 해당 비동기 요청이 완료될 때까지 함수 실행을 일시 중지하고,
            // 요청이 성공적으로 완료되면 결과를 response 변수에 저장합니다.
            const response = await axios.post('/api/login', formData, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            console.log(`로그인 성공 : ${response.data}`);
            window.location.href = '/';
        } catch (error){
            console.log(`로그인 실패 : ${error}`);
        }
    };

    return (
        <div>
            <h1>로그인 페이지</h1>
            {/* 폼 제출시 함수 실행 */}
            <form onSubmit={login}>
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">Login</button>
            </form>
        </div>
    );
}

export default LoginPage;