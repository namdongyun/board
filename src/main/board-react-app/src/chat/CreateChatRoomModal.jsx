import React, {useContext, useState} from "react";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import axios from "axios";
import {AuthContext} from "../login/AuthContext";
import {useNavigate} from "react-router-dom";

function CreateChatRoomModal({ isOpen, onClose }) {
    const {accessToken} = useContext(AuthContext); // 현재 로그인 한 사용자의 auth(인증 상태)를 가져옵니다.
    const navigate = useNavigate();
    const [chatRoomName, setChatRoomName] = useState("");

    // 채팅방 이름 입력 처리 함수
    const handleRoomNameChange = (event) => {
        setChatRoomName(event.target.value);
    };

    // 채팅방 생성 처리 함수
    const handleCreateRoom = async (e) => {
        e.preventDefault();
        const chatRoomData = {chatRoomName};

        try {
            const response = await axios.post('/api/chatrooms/create', chatRoomData, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            });

            console.log('채팅방 생성 성공', response);

            const chatRoomId = response.data.id;

            navigate(`/chat/${chatRoomId}`);

        } catch (error) {
            console.error('요청 처리 중 오류 발생', error.response || error);
        }
        handleClose(); // 모달 닫기
    };

    // 모달을 닫는 함수
    const handleClose = () => {
        setChatRoomName(''); // 채팅방 이름을 빈 문자열로 초기화
        onClose(); // 부모 컴포넌트에서 전달받은 onClose 함수 호출
    };

    return (
        <Dialog open={isOpen} onClose={handleClose}>
            <DialogTitle>새 채팅방 생성</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    id="room-name"
                    label="채팅방 이름"
                    type="text"
                    fullWidth
                    variant="standard"
                    value={chatRoomName}
                    onChange={handleRoomNameChange}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>취소</Button>
                <Button onClick={handleCreateRoom}>생성</Button>
            </DialogActions>
        </Dialog>
    );
}

export default CreateChatRoomModal;
