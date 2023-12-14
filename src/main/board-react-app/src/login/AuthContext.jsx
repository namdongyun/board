import React, {createContext, useEffect, useState} from "react";
import axios from "axios";

// 인증 상태를 전역적으로 공유 할 수 있게 해주는 Context 객체를 생성합니다.
export const AuthContext = createContext(null);

// AuthContext.Provider를 사용하는 컴포넌트로, 인증 상태를 관리하고 하위 컴포넌트에 전달하는 역할을 합니다.
// {children}은 자식 컴포넌트들을(여러개) 포함할 때 그 자식 컴포넌트들을 나타냅니다.
export const AuthProvider = ({children}) => {
    const [accessToken, setAccessToken] = useState(null);
    const [refreshToken, setRefreshToken] = useState(null);
    const [loading, setLoading] = useState(true); // 로딩 상태 추가

    // 컴포넌트가 처음 마운트될 때 로컬 스토리지에서 인증 정보를 가져와 상태에 설정하게 됩니다.
    useEffect(() => {
        const storedToken = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');
        if(storedToken) {
            setAccessToken(storedToken);
            setRefreshToken(refreshToken);
        }
        setLoading(false); // 로딩 상태 업데이트
    }, []);

    // 로그인 상태 업데이트 함수
    const login = (response) => {
        setAccessToken(response.data.accessToken);
        setRefreshToken(response.data.refreshToken);

        // accessToken과 refreshToken을 localStorage에 저장합니다.
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);

        console.log('저장된 accessToken : ', localStorage.getItem('accessToken'));
        console.log('저장된 refreshToken : ', localStorage.getItem('refreshToken'));
    };

    // 로그아웃 상태 업데이트 함수
    const logout = () => {
        setAccessToken(null);
        setRefreshToken(null);
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');

        console.log('저장된 accessToken : ', localStorage.getItem('accessToken'));
        console.log('저장된 refreshToken : ', localStorage.getItem('refreshToken'));
    };

    // 로그인 여부를 확인하는 함수입니다.
    const isAuthenticated = () => {
        return (accessToken !== null && refreshToken !== null);
    };


    return (
        // auth 상태를 하위 컴포넌트에게 전달합니다.
        <AuthContext.Provider value={{loading, login, logout, isAuthenticated}}>
            {children}
        </AuthContext.Provider>
    );
};