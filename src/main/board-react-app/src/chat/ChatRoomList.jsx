import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import axios from "axios";
import {AuthContext} from "../login/AuthContext";
import {Button} from "@mui/material";
import CreateChatRoomModal from "./CreateChatRoomModal";

export default function ChatRoomList() {
    const navigate = useNavigate();
    const [chatRooms, setChatRooms] = useState([]); // 상태로 채팅방 목록을 관리합니다.
    const [isModalOpen, setIsModalOpen] = useState(false); // 모달 창 상태

    useEffect(() => {
        // 서버에서 채팅방 목록을 가져오는 함수
        const fetchChatRooms = async () => {
            try {
                const response = await axios.get('/api/chatrooms/list'); // Spring 서버의 채팅방 목록 API 엔드포인트
                setChatRooms(response.data); // 응답 데이터로 상태 업데이트

                console.log('채팅방 리스트 불러오기 성공: ', response.data);
            } catch (error) {
                console.error('채팅방 리스트 불러오는 중 오류 발생:', error.response || error);
            }
        };

        fetchChatRooms(); // 함수 호출
    }, []); // 의존성 배열이 빈 배열이므로 컴포넌트 마운트 시 한 번만 호출됩니다.



    // 채팅방을 클릭했을 때의 이벤트 핸들러
    const handleListItemClick = (roomId) => {
        navigate(`/chat/${roomId}`); // 여기서는 react-router-dom의 navigate 함수를 사용하여 해당 채팅방으로 이동합니다.
    };

    // 모달 창을 여는 함수
    const handleOpenModal = () => {
        setIsModalOpen(true);
    };

    // 모달 창을 닫는 함수
    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    return (
        <>
            <Button variant="contained" onClick={handleOpenModal}>
                채팅방 생성
            </Button>
            <List component="nav" aria-label="main mailbox folders">
                {chatRooms.map((room) => (
                    <React.Fragment key={room.id}>
                        <ListItem button onClick={() => handleListItemClick(room.id)}>
                            <ListItemText primary={room.chatRoomName} />
                        </ListItem>
                        <Divider />
                    </React.Fragment>
                ))}
            </List>
            <CreateChatRoomModal isOpen={isModalOpen} onClose={handleCloseModal} />
        </>
    );
}