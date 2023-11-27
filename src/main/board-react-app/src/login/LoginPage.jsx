import React, {useContext, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import styled from "styled-components";
import {AuthContext} from "./AuthContext";

// 스타일이 적용된 컨테이너
const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f7f6;
`;

// 스타일이 적용된 로그인 박스
const LoginBox = styled.div`
  padding: 40px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  max-width: 400px;
  width: 100%;
`;

// 스타일이 적용된 제목
const Title = styled.h1`
  font-size: 2em;
  color: #333333;
  margin-bottom: 20px;
  text-align: center;
`;

// 스타일이 적용된 입력 필드
const Input = styled.input`
  background-color: #e8eeef;
  border: none;
  padding: 15px;
  margin-bottom: 20px;
  width: calc(100% - 30px);
  border-radius: 4px;
  
  &:focus {
    outline: none;
    box-shadow: inset 0 1px 3px rgba(0,0,0,0.2);
  }
`;

// 스타일이 적용된 버튼
const Button = styled.button`
  background-color: #27ae60;
  color: white;
  border: none;
  padding: 15px;
  width: 100%;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #219653;
  }
`;

function LoginPage(props) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const {login} = useContext(AuthContext); // AuthContext에서 login 함수를 가져옵니다.

    // 경로 이동 함수
    const navigate = useNavigate();

    const handleLogin = async (event) => {
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
            console.log('response 값 : ', response);

            // 서버로부터 받은 role 값에서 대괄호를 제거합니다.
            const roleWithoutBrackets = response.data.role.replace(/\[|\]/g, '');

            // 로그인 성공 후, AuthProvider의 login 함수를 호출하여 상태를 업데이트합니다.
            login(response.data.username, roleWithoutBrackets);

            console.log('로그인 성공 : ', response.data);
            navigate('/'); // 홈페이지로 이동
        } catch (error){
            console.log(`로그인 실패 : ${error}`);
        }
    };

    return (
        <Container>
            <LoginBox>
                <Title>로그인</Title>
                <form onSubmit={handleLogin}>
                    <Input
                        type="text"
                        name="username"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <Input
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <Button type="submit">Login</Button>
                </form>
            </LoginBox>
        </Container>
    );
}

export default LoginPage;