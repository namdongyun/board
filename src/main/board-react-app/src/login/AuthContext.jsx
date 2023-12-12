import React, {createContext, useEffect, useState} from "react";
import axios from "axios";

// 인증 상태를 전역적으로 공유 할 수 있게 해주는 Context 객체를 생성합니다.
export const AuthContext = createContext(null);

// AuthContext.Provider를 사용하는 컴포넌트로, 인증 상태를 관리하고 하위 컴포넌트에 전달하는 역할을 합니다.
// {children}은 자식 컴포넌트들을(여러개) 포함할 때 그 자식 컴포넌트들을 나타냅니다.
export const AuthProvider = ({children}) => {
    // auth를 state로 관리합니다. 사용자 아이디(username)와 역할(role)을 담고 있습니다
    const [token, setToken] = useState(null);
    const [loading, setLoading] = useState(true); // 로딩 상태 추가

    // 컴포넌트가 처음 마운트될 때 로컬 스토리지에서 인증 정보를 가져와 상태에 설정하게 됩니다.
    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        if(storedToken) {
            setToken(storedToken);
        }
        setLoading(false); // 로딩 상태 업데이트
    }, []);

    // 로그인 상태 업데이트 함수
    const login = (newToken) => {
        setToken(newToken);

        // newToken을 로컬 스토리지에 저장합니다.
        localStorage.setItem('token', newToken);

        console.log('localStorage 저장된 JWT 값 : ', localStorage.getItem('token'));
    };

    // 로그아웃 상태 업데이트 함수
    const logout = () => {
        setToken(null);
        localStorage.removeItem('token');
    };

    // 로그인 여부를 확인하는 함수입니다.
    const isAuthenticated = () => {
        return token !== null;
    };


    return (
        // auth 상태를 하위 컴포넌트에게 전달합니다.
        <AuthContext.Provider value={{token, login, logout, loading, isAuthenticated}}>
            {children}
        </AuthContext.Provider>
    );
};