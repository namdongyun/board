package com.example.board.socket;

import com.example.board.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // StompHeaderAccessor를 사용하여 STOMP 메시지 헤더에 접근합니다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 메시지의 STOMP 명령이 CONNECT인 경우를 체크합니다. CONNECT 명령은 클라이언트가 처음으로 웹소켓 연결을 시도할 때 사용됩니다.
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // STOMP 헤더에서 "Authorization" 헤더 값을 추출하여 authToken 변수에 저장합니다.
            String authToken = accessor.getFirstNativeHeader("Authorization");

            // 추출한 토큰이 null이 아니고, jwtUtil의 validateToken 메서드를 통해 유효한 토큰인지 확인합니다.
            if (authToken != null && jwtUtil.validateToken(authToken)) {
                // 토큰에서 "Bearer " 부분을 제거하고, 실제 토큰 값만을 추출
                authToken = authToken.split(" ")[1].trim();

                // 토큰이 유효하면, 해당 토큰으로부터 Authentication 객체를 생성합니다.
                Authentication auth = jwtUtil.getAuthentication(authToken);
                // 생성된 Authentication 객체를 SecurityContextHolder에 설정하여, 현재 보안 컨텍스트에 인증 정보를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(auth);
                // STOMP 접근자(accessor)에 사용자 정보를 설정하여 연결된 사용자가 누구인지 식별할 수 있게 합니다.
                accessor.setUser(auth);
            } else {
                // 토큰이 유효하지 않다면, AccessDeniedException 예외를 발생시켜 접근을 거부합니다.
                throw new AccessDeniedException("Invalid JWT Token");
            }
        }
        return message;
    }
}
