package com.example.board.service;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.dto.ChatRoomDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatMessage;
import com.example.board.entity.ChatRoom;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import com.example.board.repository.ChatRoomRepository;
import com.example.board.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AccountRepository accountRepository;


    // 채팅방 리스트 불러오기
    public List<ChatRoomDTO> findAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        // Entity를 DTO로 변환합니다.
        return chatRooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 채팅방 생성
    public ChatRoomDTO createChatRoom(ChatRoomDTO chatRoomDTO) {
        // 현재 jwt 토큰으로 인증된 정보에서 account 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { throw new IllegalStateException("인증 정보가 없습니다."); }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account currentAccount = principalDetails.getAccount();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomName(chatRoomDTO.getChatRoomName());
        chatRoom.setHost(currentAccount);

        chatRoomRepository.save(chatRoom);

        return convertToDTO(chatRoom);
    }

    // 채팅방 삭제
    public void deleteChatRoom(Long roomId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { throw new IllegalStateException("인증 정보가 없습니다."); }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account currentAccount = principalDetails.getAccount();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        // 현재 계정이 채팅방의 호스트인지 확인, 관리자는 삭제 가능
        if (chatRoom.getHost().equals(currentAccount) || currentAccount.getRole().equals("ADMIN")) {
            chatMessageRepository.deleteByChatRoomId(roomId);
            chatRoomRepository.delete(chatRoom);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "채팅방의 호스트만이 채팅방을 삭제할 수 있습니다.");
        }
    }

    // 채팅방 입장
    @Transactional
    public ChatMessageDTO joinRoom(Long roomId, Principal principal) {
        // 현재 ChatRoom 객체 추출
        ChatRoom currentChatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // principal 으로 현재 사용자 Account 객체 추출
        Account currentAccount = accountRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // currentAccount가 currentChatRoom의 members에 포함되어 있는지 확인
        boolean isMember = currentChatRoom.getMembers().contains(currentAccount);
        if (!isMember) {    // 현재 사용자가 해당 채팅방의 맴버가 아닐 경우
            currentChatRoom.getMembers().add(currentAccount); // 채팅방에 사용자 추가
            chatRoomRepository.save(currentChatRoom); // 채팅방 정보 업데이트

            ChatMessage joinMessage = new ChatMessage();
            joinMessage.setMessage(currentAccount.getNickname() + "님이 입장하셨습니다.");
            joinMessage.setChatRoom(currentChatRoom);
            joinMessage.setSender(currentAccount);

            ChatMessage newMs = chatMessageRepository.save(joinMessage);

            ChatMessageDTO joinMessageDTO = new ChatMessageDTO();
            joinMessageDTO.setMessage(newMs.getMessage());
            joinMessageDTO.setChatRoomId(newMs.getChatRoom().getId());

            return joinMessageDTO;
        }
        else {  // 현재 사용자가 이미 해당 채팅방의 맴버일 경우
            ChatMessage rejoinMessage = new ChatMessage();
            rejoinMessage.setMessage(currentAccount.getNickname() + "님이 재입장하셨습니다.");
            rejoinMessage.setChatRoom(currentChatRoom);
            rejoinMessage.setSender(currentAccount);

            ChatMessage reMs = chatMessageRepository.save(rejoinMessage);

            ChatMessageDTO rejoinMessageDTO = new ChatMessageDTO();
            rejoinMessageDTO.setMessage(reMs.getMessage());
            rejoinMessageDTO.setChatRoomId(reMs.getChatRoom().getId());

            return rejoinMessageDTO;
        }
    }


    // ChatMessage -> ChatMessageDTO 로 변환 메소드
    private ChatRoomDTO convertToDTO(ChatRoom chatRoom) {

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();

        chatRoomDTO.setId(chatRoom.getId());                        // 채팅방 id
        chatRoomDTO.setChatRoomName(chatRoom.getChatRoomName());    // 채팅방 이름
        chatRoomDTO.setCreatedAt(chatRoom.getCreatedAt());          // 채팅방 생성 시간
        chatRoomDTO.setHostId(chatRoom.getHost().getId());          // 방장 id

        return chatRoomDTO;
    }
}
