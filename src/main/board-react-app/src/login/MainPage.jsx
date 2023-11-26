import React from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function MainPage(props) {
    const navigate = useNavigate();

    const logout = () => {
        axios.post('/api/logout', {}, {
            withCredentials: true // 쿠키를 포함시키기 위해 withCredentials 옵션을 true 설정
        })
            .then(response => {
                // 로그아웃 성공 시 처리
                console.log("로그아웃 성공");
                // 메인 페이지로 이동
                window.location.href = '/';
            })
            .catch(error => {
                // 로그아웃 실패 시
                console.error(`로그아웃 실패 ${error}`);
            })
    }

    return (
        <div>
            <button onClick={() => navigate('/board/list')}>
                게시판 리스트 페이지 이동
            </button>
            <button onClick={() => navigate('/register')}>
                회원가입
            </button>
            <button onClick={() => navigate('/login')}>
                로그인
            </button>
            <button onClick={logout}>
                로그아웃
            </button>

            <h1>안녕하세요</h1>

            <div id="root"></div>
        </div>
    );
}

export default MainPage;