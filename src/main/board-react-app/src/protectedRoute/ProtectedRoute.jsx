import React, {useContext} from "react";
import {AuthContext} from "../login/AuthContext";
import {Navigate, useLocation} from "react-router-dom";

// 특정 사용자 역할에 따라 접근을 제한하는 라우팅 경로를 설정하는 데 사용됩니다.
const ProtectedRoute = ({ children, roles }) => {
    const {auth, loading } = useContext(AuthContext); // 현재 로그인 한 사용자의 auth(인증 상태), loading(로딩 상태)를 가져옵니다.

    // useLocation() Hook 을 사용하여 현재 브라우저의 위치(경로) 정보를 location 변수에 저장
    const location = useLocation();

    if (loading) {
        // 로딩 중일 때 처리, 예를 들어 로딩 인디케이터를 반환할 수 있습니다.
        return <div>로딩 중...</div>;
    }

    if (!auth) {
        // auth 객체가 없거나 로그인하지 않은 상태면 로그인 페이지로 리다이렉트합니다.
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return (
        roles.includes(auth.role) ? (    // roles(props)가 role(context)를 포함하는지 확인합니다.
            children    // 포함하면 하위(자식) 컴포넌트를 렌더링 합니다.
        ) : (
            <Navigate to='/' state={{from: location}} replace/> // 포함하지 않으면 '/'경로로 이동
        )
    );
};

export default ProtectedRoute;