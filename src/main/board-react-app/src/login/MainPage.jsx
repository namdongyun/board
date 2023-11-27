import React, {useContext} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import styled from "styled-components";
import {AuthContext} from "./AuthContext";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f4f7f6;
`;

const Button = styled.button`
  background-color: #4CAF50; /* Green */
  border: none;
  color: white;
  padding: 15px 32px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 4px;
  font-size: 16px;
  
  &:hover {
    background-color: #45a049;
  }
`;

function MainPage(props) {
    const navigate = useNavigate();

    const {logout} = useContext(AuthContext); // AuthContext에서 logout 함수를 가져옵니다.

    const handleLogout = () => {
        axios.post('/api/logout', {}, {
            withCredentials: true // 쿠키를 포함시키기 위해 withCredentials 옵션을 true 설정
        })
            .then(response => {
                // 로그아웃 성공 시 처리
                console.log("로그아웃 성공");
                // 메인 페이지로 이동
                // window.location.href = '/';
                navigate('/'); // 홈페이지로 이동
                logout();
            })
            .catch(error => {
                // 로그아웃 실패 시
                console.error(`로그아웃 실패 ${error}`);
            })
    }

    return (
        <Container>
            <h1>로그인 해야지 게시판 들어갈 수 있음요</h1>
            <h1>회원가입 하세요</h1>
            <Button onClick={() => navigate('/board/list')}>
                게시판 리스트 페이지 이동
            </Button>
            <Button onClick={() => navigate('/register')}>
                회원가입
            </Button>
            <Button onClick={() => navigate('/login')}>
                로그인
            </Button>
            <Button onClick={handleLogout}>
                로그아웃
            </Button>
            <div id="root"></div>
        </Container>
    );
}

export default MainPage;