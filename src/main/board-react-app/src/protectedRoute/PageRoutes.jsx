import React from "react";
import {Navigate, Route, Routes} from "react-router-dom";
import MainPage from "../login/MainPage";
import LoginPage from "../login/LoginPage";
import RegisterPage from "../login/RegisterPage";
import BoardList from "../board/BoardList";
import BoardView from "../board/BoardView";
import BoardEdit from "../board/BoardEdit";
import BoardWrite from "../board/BoardWrite";
import ProtectedRoute from "./ProtectedRoute";
import MyPage from "../mypage/MyPage";
import ChatRoomList from "../chat/ChatRoomList";
import ChatRoom from "../chat/ChatRoom";

export default function PageRoutes() {

    // 경로 권한 설정
    const renderProtected = (Component) => (
        <ProtectedRoute roles={['ROLE_USER', 'ROLE_ADMIN']}>
            <Component />
        </ProtectedRoute>
    );

    return (
        <Routes>
            <Route path="/" element={<MainPage/>} />
            <Route path="/login" element={<LoginPage/>} />
            <Route path="/register" element={<RegisterPage/>} />
            <Route path="/board/list" element={<BoardList/>} />
            <Route path="/board/view/:id" element={<BoardView/>} />
            <Route path="/board/editPage/:id" element={renderProtected(BoardEdit)} />
            <Route path="/board/write" element={renderProtected(BoardWrite)} />
            <Route path="/mypage" element={renderProtected(MyPage)} />
            <Route path="/chatRoomList" element={<ChatRoomList/>} />
            <Route path="/chat/:roomId" element={renderProtected(ChatRoom)} />


            <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    );
}