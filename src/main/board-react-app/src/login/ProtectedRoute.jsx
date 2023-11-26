import React, {useContext} from "react";
import {AuthContext} from "./AuthContext";
import {Navigate, useLocation} from "react-router-dom";

// 특정 사용자 역할에 따라 접근을 제한하는 라우팅 경로를 설정하는 데 사용됩니다.
const ProtectedRoute = ({ children, roles }) => {
    const {role} = useContext(AuthContext); // 현재 로그인한 사용자의 역할(role)을 가져옵니다.

    // useLocation() Hook 을 사용하여 현재 브라우저의 위치(경로) 정보를 location 변수에 저장
    const location = useLocation();


    return (
        roles.includes(role) ? (    // roles(props)가 role(context)를 포함하는지 확인합니다.
            children    // 포함하면 하위(자식) 컴포넌트를 렌더링 합니다.
        ) : (
            <Navigate to='/' state={{from: location}} replace/> // 포함하지 않으면 '/'경로로 이동
        )
    );
};

export default ProtectedRoute;