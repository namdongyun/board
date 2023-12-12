import React, {useContext, useRef, useState} from "react";
import {AuthContext} from "../login/AuthContext";

export default function ChatRoomList() {
    const {token} = useContext(AuthContext); // 현재 로그인 한 사용자의 auth(인증 상태)를 가져옵니다.

    const [chatRooms, setChatRooms] = useState([]); // 채팅방 리스트 상태
    const [selectedRoom, setSelectedRoom] = useState(null); // 선택된 채팅방 상태
    const stompClient = useRef(null);

    // 채팅방 생성 함수
    const createChatRoom = (roomName) => {
        if (stompClient.current) {
            stompClient.current.publish({
                destination: '/app/chat.create',
                body: JSON.stringify({ name: roomName }),
                headers: { 'Authorization': `Bearer ${token}` },
            });
        }
    };

    // 채팅방 리스트를 가져오는 함수 (예시)
    const fetchChatRooms = () => {
        // 서버에 채팅방 리스트를 요청하는 로직을 구현합니다.
        // 예를 들어, 서버에 HTTP GET 요청을 보내고 응답을 받아올 수 있습니다.
        // 이 예시에서는 임시로 하드코딩된 데이터를 사용합니다.
        setChatRooms([{ id: 'room1', name: '채팅방1' }, { id: 'room2', name: '채팅방2' }]);
    };

    useEffect(() => {
        fetchChatRooms(); // 컴포넌트가 마운트될 때 채팅방 리스트를 가져옵니다.

        // 여기에 웹소켓 연결 및 STOMP 클라이언트 설정 코드를 추가합니다.
        // ...

        // 채팅방 생성에 대한 응답을 처리하는 부분
        stompClient.current.subscribe('/user/queue/chat.create', (message) => {
            const newRoom = JSON.parse(message.body);
            setChatRooms(prevRooms => [...prevRooms, newRoom]);

            // 생성된 채팅방으로 입장할 수 있도록 상태를 업데이트합니다.
            setSelectedRoom(newRoom);
        });

        // ...

    }, [token]);

    return (
        <div>
            {/* 채팅방 생성 UI */}
            <input
                type="text"
                placeholder="채팅방 이름"
                onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                        createChatRoom(e.target.value);
                        e.target.value = ''; // 입력 필드 초기화
                    }
                }}
            />

            {/* 채팅방 리스트 UI */}
            <ul>
                {chatRooms.map(room => (
                    <li key={room.id} onClick={() => setSelectedRoom(room)}>
                        {room.name}
                    </li>
                ))}
            </ul>

            {/* 선택된 채팅방이 있을 경우 채팅방 컴포넌트를 렌더링합니다. */}
            {/*{selectedRoom && <ChatRoom room={selectedRoom} />}*/}
        </div>
    );
}