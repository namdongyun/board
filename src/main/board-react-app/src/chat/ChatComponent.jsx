import React, {useEffect, useRef, useState} from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from "sockjs-client";
import styled from "styled-components";
import {Link} from "react-router-dom";

const ChatContainer = styled.div`
  padding: 20px;
  height: 90vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const MessageList = styled.ul`
  list-style-type: none;
  padding: 0;
  height: 75%; // 메시지 리스트 높이를 조금 줄였습니다.
  overflow-y: auto;
  font-weight: bold;
`;

const MessageItem = styled.li`
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 10px;
  background-color: blanchedalmond;
  &:last-child {
    margin-bottom: 0;
  }
`;

const InputContainer = styled.div`
  display: flex;
`;

const MessageInput = styled.input`
  flex-grow: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
`;

const SendButton = styled.button`
  padding: 10px 20px;
  margin-left: 10px;
  border-radius: 5px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
`;

const BackButton = styled(Link)`
  padding: 10px 15px;
  margin-top: 20px;
  border-radius: 5px;
  border: none;
  background-color: #666;
  color: white;
  cursor: pointer;
  &:hover {
    background-color: #555;
  }
`;

// 로딩 컴포넌트 스타일
const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
`;

// 로딩 컴포넌트
const Loading = () => (
    <LoadingContainer>
        <p>로딩중...</p>
    </LoadingContainer>
);

const ChatComponent = () => {
    const messagesEndRef = useRef(null); // 메시지 목록 컨테이너에 대한 ref 생성
    const stompClient = useRef(null);  // STOMP 클라이언트 인스턴스를 저장할 state 변수
    const [message, setMessage] = useState('');     // 사용자가 입력하는 메시지를 저장할 state 변수
    const [receivedMessages, setReceivedMessages] = useState([]); // 수신된 메시지들을 배열로 저장할 state 변수

    const [isLoading, setIsLoading] = useState(true); // state 변수로 로딩 상태를 추가합니다.

    // 메시지 목록이 업데이트될 때마다 스크롤을 맨 아래로 이동시키는 함수
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        // STOMP 클라이언트 인스턴스를 생성합니다. 이 인스턴스는 웹소켓 연결을 관리합니다
        stompClient.current = new Client({

            // STOMP 클라이언트가 연결할 웹소켓 서버의 URL을 설정합니다.
            // brokerURL: '/ws', // Spring Boot 서버의 WebSocket URL

            webSocketFactory: () => new SockJS('/ws'), // proxy를 통한 접속

            // 웹소켓 연결 시 사용할 헤더 정보(로그인과 패스코드)를 설정합니다.
            // connectHeaders: {
            //     "auth-token": "spring-chat-auth-token",
            // },

            // 디버깅 정보를 콘솔에 출력하기 위한 함수를 설정합니다.
            debug: function (str){
                console.log(str);
            },
            reconnectDelay: 5000, // 연결이 끊어진 후 재연결을 시도하기 전 대기할 시간(밀리초)을 설정합니다.
            heartbeatIncoming: 4000,    // 서버로부터의 하트비트와
            heartbeatOutgoing: 4000     // 서버로 보내는 하트비트 간격을 밀리초 단위로 설정합니다.
        });

        // 웹소켓에 성공적으로 연결되었을 때 실행될 콜백 함수를 정의합니다.
        stompClient.current.onConnect = (frame) => {

            // 서버에서 보낸 메시지를 받기 위해 특정 주제(/topic/public)를 구독합니다.
            stompClient.current.subscribe('/topic/public', (message) => {

                // setReceivedMessages의 콜백 함수는 prevMessages를 인자로 받아,
                // 이전 메시지 배열에 새 메시지(JSON.parse(message.body))를 추가한 새 배열을 만들어 반환합니다.

                // setReceivedMessages 함수에 함수를 인자로 전달하는 패턴을 사용하면,
                // React는 자동으로 현재 상태의 값을 prevMessages라는 이름의 매개변수로 해당 함수에 제공합니다.

                // 이러한 방식은 상태 업데이트가 비동기적으로 일어날 때 최신 상태를 안전하게 얻기 위해 사용됩니다.
                setReceivedMessages((prevMessages) => {
                    console.log(JSON.parse(message.body));

                    return [...prevMessages, JSON.parse(message.body)];

                    // 화살표 함수에서 중괄호 {}를 사용하지 않고 바로 값을 반환하는 경우에는 return 키워드를 생략할 수 있습니다.
                });
            });

            // 이전 대화 내역을 받기 위해 특정 주제(/topic/public)를 구독합니다.
            stompClient.current.subscribe('/topic/history', (message) => {
                // 서버로부터 받은 이전 메시지들을 상태에 저장합니다.
                setReceivedMessages(JSON.parse(message.body));
            });

            // 이전 대화 내역을 요청하는 메시지를 보냅니다.
            stompClient.current.publish({
                destination: '/app/chat.history',
                body: JSON.stringify({ chatRoomId: 'room1' }),
            });

            // 연결 성공 후 로딩 상태를 변경합니다.
            setIsLoading(false);
        };

        // STOMP 클라이언트를 활성화하여 연결을 시작합니다.
        stompClient.current.activate();

        // 컴포넌트가 언마운트될 때 클라이언트 연결을 해제합니다.
        return () => {
            if(stompClient.current){
                stompClient.current.deactivate();
            }
        };
    }, []);

    // 메시지 목록 상태가 업데이트될 때마다 스크롤을 맨 아래로 이동
    useEffect(() => {
        scrollToBottom();
    }, [receivedMessages]);

    // 사용자가 버튼을 클릭하여 메시지를 보낼 때 호출될 handleSendMessage 함수를 정의합니다.
    const handleSendMessage = () => {
        // STOMP 클라이언트가 존재하는지 확인합니다.
        if (stompClient.current && message) {

            const auth = JSON.parse(localStorage.getItem('auth'));
            const senderUsername = auth ? auth.username : null; // 사용자 username이 없으면 null을 할당합니다.

            // 보낼 메시지 객체를 생성합니다. 여기에는 보내는 사람의 식별자, 메시지 텍스트, 그리고 현재 시간이 포함됩니다.
            const chatMessageDTO = {
                chatRoomId: 'room1',
                message: message, // 입력된 메시지 내용
                // timestamp: new Date(), // 현재 시간
                senderUsername: senderUsername // 가정한 보내는 사람의 ID
            };

            // STOMP 클라이언트의 publish 메서드를 사용하여 메시지를 전송합니다.
            stompClient.current.publish({

                // 메시지를 보낼 목적지(엔드포인트)와 문자열로 변환된 메시지 객체를 지정합니다.
                destination: '/app/chat.sendMessage',
                body: JSON.stringify(chatMessageDTO)
            });


            // 메시지 전송 후 입력 필드를 비웁니다.
            setMessage('');
        }
    }

    // 메시지 입력을 처리하는 함수
    const handleMessageChange = (event) => {
        setMessage(event.target.value);
    };

    // 엔터 키 이벤트를 처리하는 함수
    const handleKeyPress = (event) => {
        // 엔터 키가 눌렸을 경우
        if(event.key === 'Enter') {
            handleSendMessage();
        }
    };

    // 간단한 해시 함수로 문자열을 기반으로 색상 코드 생성
    const stringToColor = (string) => {
        let hash = 0;
        for (let i = 0; i < string.length; i++) {
            hash = string.charCodeAt(i) + ((hash << 5) - hash);
        }
        let color = '#';
        for (let i = 0; i < 3; i++) {
            const value = (hash >> (i * 8)) & 0xFF;
            color += ('00' + value.toString(16)).substr(-2);
        }
        return color;
    };

    // 메시지 아이템에 적용할 스타일을 반환하는 함수
    const getMessageItemStyle = (nickname) => ({
        backgroundColor: stringToColor(nickname), // 사용자 이름을 기반으로 색상 코드 생성
        // 여기에 추가적인 스타일 속성을 넣을 수 있습니다.
    });

    return (
        isLoading ? (
            <Loading />
        ) : (
            <ChatContainer>
                <MessageList>
                    {receivedMessages.map((msg) => (
                        <MessageItem
                            key={msg.id}
                            style={getMessageItemStyle(msg.senderNickname)}
                        >
                            {msg.senderNickname}: {msg.message}
                        </MessageItem>
                    ))}
                    {/* 가상의 요소를 추가하여 ref를 부여하고, 이 요소로 스크롤 위치를 조정합니다. */}
                    <div ref={messagesEndRef} />
                </MessageList>
                <InputContainer>
                    <MessageInput
                        type="text"
                        value={message}
                        onChange={handleMessageChange}
                        onKeyPress={handleKeyPress}
                        placeholder="메시지를 입력하세요"
                    />
                    <SendButton onClick={handleSendMessage}>보내기</SendButton>
                </InputContainer>
            </ChatContainer>
        )
    );
};

export default ChatComponent;