import React, {useContext} from "react";
import {AuthContext} from "../login/AuthContext";
import {Navigate, useLocation} from "react-router-dom";

// 특정 사용자 역할에 따라 접근을 제한하는 라우팅 경로를 설정하는 데 사용됩니다.
const ProtectedRoute = ({ children, ...rest }) => {
    const {token, loading } = useContext(AuthContext); // 현재 로그인 한 사용자의 auth(인증 상태), loading(로딩 상태)를 가져옵니다.

    // useLocation() Hook 을 사용하여 현재 브라우저의 위치(경로) 정보를 location 변수에 저장
    const location = useLocation();

    if (loading) {
        // 로딩 중일 때 처리, 예를 들어 로딩 인디케이터를 반환할 수 있습니다.
        return <div>로딩 중...</div>;
    }

    if (!token) {
        alert('로그인이 필요한 서비스입니다. 로그인 페이지로 이동합니다.');
        // auth 객체가 없거나 로그인하지 않은 상태면 로그인 페이지로 리다이렉트합니다.
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return React.cloneElement(children, {...rest}); // 하위(자식) 컴포넌트를 렌더링 합니다.
};

export default ProtectedRoute;