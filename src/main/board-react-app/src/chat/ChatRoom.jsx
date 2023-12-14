import React, {useContext, useEffect, useRef, useState} from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from "sockjs-client";
import {AuthContext} from "../login/AuthContext";
import {useNavigate, useParams} from "react-router-dom";
import Box from "@mui/material/Box";
import {Button, CircularProgress, Paper, TextField, Typography} from "@mui/material";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import SendIcon from '@mui/icons-material/Send';
import axios from "axios";
import api from "../axiosInterceptor/api";

// 로딩 컴포넌트
const Loading = () => (
    <Box display="flex" justifyContent="center" alignItems="center" height="90vh">
        <CircularProgress />
    </Box>
);

export default function ChatRoom() {
    const accessToken = localStorage.getItem('accessToken');
    const { roomId } = useParams();     // 채팅방 id
    const navigate = useNavigate();

    const messagesEndRef = useRef(null); // 메시지 목록 컨테이너에 대한 ref 생성
    const stompClient = useRef(null);  // STOMP 클라이언트 인스턴스를 저장할 state 변수
    const [message, setMessage] = useState('');     // 사용자가 입력하는 메시지를 저장할 state 변수
    const [receivedMessages, setReceivedMessages] = useState([]); // 수신된 메시지들을 배열로 저장할 state 변수
    const [isLoading, setIsLoading] = useState(true); // state 변수로 로딩 상태를 추가합니다.

    // 페이지를 열었을 때 처음 STOMP 클라이언트에 연결하는 useEffect
    useEffect(() => {
        // 1. STOMP 클라이언트 인스턴스를 생성합니다. 이 인스턴스는 웹소켓 연결을 관리합니다
        stompClient.current = new Client({

            connectHeaders: {
                'Authorization': `Bearer ${accessToken}`, // 서버 측에서 이 헤더를 확인하여 인증 처리를 할 것입니다.
            },

            // STOMP 클라이언트가 연결할 웹소켓 서버의 URL을 설정합니다.
            // brokerURL: '/ws', // Spring Boot 서버의 WebSocket URL
            webSocketFactory: () => new SockJS('/ws'), // proxy를 통한 접속

            // 디버깅 정보를 콘솔에 출력하기 위한 함수를 설정합니다.
            debug: function (str){
                console.log(str);
            },
            reconnectDelay: 10000, // 연결이 끊어진 후 재연결을 시도하기 전 대기할 시간(밀리초)을 설정합니다.
            heartbeatIncoming: 4000,    // 서버로부터의 하트비트와
            heartbeatOutgoing: 4000     // 서버로 보내는 하트비트 간격을 밀리초 단위로 설정합니다.
        });

        // 2. 웹소켓에 성공적으로 연결되었을 때 실행될 콜백 함수를 정의합니다.
        stompClient.current.onConnect = (frame) => {
            try {
                // 2-1. 서버에서 보낸 메시지를 받기 위해 특정 주제(/room/1)를 구독합니다.
                stompClient.current.subscribe(`/room/${roomId}`,
                    // 이 함수는 서버로부터 메시지가 도착할 때마다 실행됩니다. (messageData 객체는 서버로부터 받은 메시지 정보)
                    (messageData) => {
                        const message = JSON.parse(messageData.body)

                        // setReceivedMessages의 콜백 함수는 prevMessages를 인자로 받아,
                        // 이전 메시지 배열에 새 메시지(message)를 추가한 새 배열을 만들어 반환합니다.

                        // prevMessages는 React의 useState 훅을 사용하여 현재 상태의 값을 제공합니다.
                        // 이러한 방식은 상태 업데이트가 비동기적으로 일어날 때 최신 상태를 안전하게 얻기 위해 사용됩니다.
                        setReceivedMessages((prevMessages) => {
                            console.log(message);
                            return [...prevMessages, message];
                        });
                    });

                // 2-2. 이전 대화 내역을 받기 위해 특정 주제(/room/1/history)를 구독합니다.
                stompClient.current.subscribe(`/room/${roomId}/history`, (messageData) => {
                    const message = JSON.parse(messageData.body)

                    // 서버로부터 받은 이전 메시지들을 상태에 저장합니다.
                    setReceivedMessages(message);
                });

                // 2-3. 서버에서 보낸 채팅방 입장 응답 메시지를 받기 위해 특정 주제(/roomJoin/{roomId}/joinMessage)를 구독합니다.
                stompClient.current.subscribe(`/roomJoin/${roomId}/joinMessage`, (messageData) => {
                    // 2-4. 채팅방 입장 성공 메시지를 받으면 이전 대화 내역을 요청합니다.
                    stompClient.current.publish({
                        destination: `/app/${roomId}/history`,
                    });
                });

                // 2-5. 연결 성공 후 로딩 상태를 변경합니다.
                setIsLoading(false);

            } catch (error){
                console.error('연결 중 오류 발생:', error);
            }
        };

        // 3. STOMP 클라이언트를 활성화하여 연결을 시작합니다.
        stompClient.current.activate();

        // 4. 컴포넌트가 언마운트될 때 클라이언트 연결을 해제합니다.
        return () => {
            if(stompClient.current){
                stompClient.current.deactivate();
            }
        };
    }, [roomId, accessToken]);

    // 메시지 목록이 업데이트될 때마다 스크롤을 맨 아래로 이동시키는 함수
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    // 메시지 목록 상태가 업데이트될 때마다 스크롤을 맨 아래로 이동
    useEffect(() => {
        scrollToBottom();
    }, [receivedMessages]);

    // 사용자가 버튼을 클릭하여 메시지를 보낼 때 호출될 handleSendMessage 함수를 정의합니다.
    const handleSendMessage = () => {
        // STOMP 클라이언트가 존재하는지 확인합니다.
        if (stompClient.current && message) {

            // 보낼 메시지 객체를 생성합니다. 여기에는 보내는 사람의 식별자, 메시지 텍스트, 그리고 현재 시간이 포함됩니다.
            const chatMessageDTO = {
                message: message, // 입력된 메시지 내용
            };

            // STOMP 클라이언트의 publish 메서드를 사용하여 메시지를 전송합니다.
            stompClient.current.publish({

                // 메시지를 보낼 목적지(엔드포인트)와 문자열로 변환된 메시지 객체를 지정합니다.
                destination: `/app/${roomId}/sendMessage`,
                body: JSON.stringify(chatMessageDTO),
            });


            // 메시지 전송 후 입력 필드를 비웁니다.
            setMessage('');
        }
    }

    // 메시지 입력을 처리하는 함수
    const handleMessageChange = (event) => {
        setMessage(event.target.value);
    };
    // shift 키와 함께 엔터를 누르면 메시지를 보내지 않습니다.
    const handleKeyPress = (event) => {
        // shift 키와 함께 엔터를 누르면 메시지를 보내지 않습니다.
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault(); // Enter 키의 기본 동작을 방지합니다.
            handleSendMessage();
        }
    };

    // 채팅방 삭제 핸들러
    const handleDeleteChatRoom = async () => {
        const isConfirmed = window.confirm('정말로 채팅방을 삭제하시겠습니까?');

        if (isConfirmed) {
            try {
                const response = await api.delete(`/api/chatrooms/delete/${roomId}`);
                if (response.status === 200) {
                    // 삭제 성공 시, 채팅방 목록으로 리다이렉트 혹은 사용자에게 알림
                    alert('채팅방이 삭제되었습니다.');

                    navigate('/chatRoomList');
                }
            } catch (error) {
                // 오류 처리
                alert(error.response.data.message);
            }
        }
    };

    return (
        isLoading ? (
            <Loading />
        ) : (
            <Box sx={{ p: 3, height: '90vh', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <List sx={{ overflowY: 'auto', mb: 2 }}>
                    {receivedMessages.map((msg) => (
                        <ListItem key={msg.id} alignItems="flex-start">
                            <Paper elevation={3}
                                   sx={{ padding: 2, backgroundColor: '#f5f5f5', borderRadius: 4, maxWidth: '70%' }}
                            >
                                <ListItemText
                                    primary={msg.senderNickname}
                                    secondary={
                                        <Typography
                                            sx={{display: 'inline'}}
                                            component="span"
                                            variant="body2"
                                            color="text.primary"
                                        >
                                            {msg.message}
                                        </Typography>
                                    }
                                />
                            </Paper>
                        </ListItem>
                    ))}
                    {/* 가상의 요소를 추가하여 ref를 부여하고, 이 요소로 스크롤 위치를 조정합니다. */}
                    <div ref={messagesEndRef} />
                </List>
                <Box component="form" sx={{ display: 'flex', alignItems: 'center' }}>
                    <TextField
                        fullWidth
                        sx={{ mr: 1 }}
                        placeholder="메시지를 입력하세요..."
                        value={message}
                        onChange={handleMessageChange}
                        onKeyPress={handleKeyPress}
                    />
                    <Button type="button" variant="contained" endIcon={<SendIcon />} onClick={handleSendMessage}>
                        보내기
                    </Button>
                </Box>
                <Button variant="outlined" color="secondary" onClick={handleDeleteChatRoom}>
                    채팅방 삭제
                </Button>
            </Box>
        )
    );
};