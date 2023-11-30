import React, {createContext, useEffect, useState} from "react";
import axios from "axios";

// 인증 상태를 전역적으로 공유 할 수 있게 해주는 Context 객체를 생성합니다.
export const AuthContext = createContext(null);

// AuthContext.Provider를 사용하는 컴포넌트로, 인증 상태를 관리하고 하위 컴포넌트에 전달하는 역할을 합니다.
// {children}은 자식 컴포넌트들을(여러개) 포함할 때 그 자식 컴포넌트들을 나타냅니다.
export const AuthProvider = ({children}) => {
    // auth를 state로 관리합니다. 사용자 아이디(username)와 역할(role)을 담고 있습니다
    const [auth, setAuth] = useState({username: null, role: null});
    const [loading, setLoading] = useState(true); // 로딩 상태 추가

    // 컴포넌트가 마운트될 때 서버로부터 사용자 프로필을 가져오는 side effect을 처리합니다.

    // withCredentials를 true로 설정하면,
    // 서버로 요청을 보낼 때 현재 가지고 있는 자격 증명 데이터를 포함해서 보냅니다.
    // useEffect(() => {
    //
    //     axios.get('/account/profile', {withCredentials: true})
    //
    //         // 요청이 성공하면 응답으로부터 사용자 이름과 역할을 받아와서 auth 상태를 업데이트합니다.
    //         .then(response => {
    //             setAuth({
    //                 username: response.data.username,
    //                 role: response.data.role,
    //             })
    //         })
    //         .catch(error => {
    //             console.log('프로필 요청 중 오류가 발생했습니다.', error);
    //         });
    // }, []);

    // 컴포넌트가 처음 마운트될 때 로컬 스토리지에서 인증 정보를 가져와 상태에 설정하게 됩니다.
    useEffect(() => {
        const storedAuth = localStorage.getItem('auth');
        if(storedAuth) {
            // 문자열로 저장된 인증 정보를 JavaScript 객체로 변환합니다.
            const authData  = JSON.parse(storedAuth);

            setAuth({
                username: authData.username,
                role: authData.role
            });
        }
        setLoading(false); // 로딩 상태 업데이트
    }, []);

    // 로그인 상태 업데이트 함수
    const login = (username, role) => {
        setAuth({ username, role });

        // JavaScript 객체나 값(value)을 JSON 형식의 문자열로 변환하여 로컬 스토리지에 저장합니다.
        localStorage.setItem('auth', JSON.stringify({ username, role }));

        console.log('localStorage 저장된 값 : ', localStorage.getItem('auth'));
    };

    // 로그아웃 상태 업데이트 함수
    const logout = () => {
        setAuth({ username: null, role: null });
        localStorage.removeItem('auth');
    };

    return (
        // auth 상태를 하위 컴포넌트에게 전달합니다.
        <AuthContext.Provider value={{auth, login, logout, loading}}>
            {children}
        </AuthContext.Provider>
    );
};