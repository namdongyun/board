package com.example.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration // 스프링 설정 클래스임을 나타냅니다.
@EnableWebSocketMessageBroker // 웹소켓 메시지 브로커를 활성화하는 설정을 포함하고 있음을 나타냅니다.
// 즉, STOMP 메시징을 처리하기 위한 기본적인 설정을 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // 메시지 브로커를 위한 메서드를 오버라이드하여 웹소켓 연결과 메시지 처리 방식을 구성할 수 있도록 합니다.


    @Override
    // 메시지 브로커의 옵션을 설정합니다.
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 간단한 메모리 기반 메시지 브로커를 활성화하고, "/topic"으로 시작하는 목적지의 메시지들이 이 브로커를 통해 구독자에게 전달되도록 설정합니다.
        registry.enableSimpleBroker("/topic");

        // 클라이언트가 서버로 메시지를 보낼 때 사용할 경로의 접두사를 설정합니다.
        // 예를 들어, 클라이언트가 "/app/chat.sendMessage"로 메시지를 보내면,
        // 애플리케이션 내의 @MessageMapping 어노테이션이 달린 메서드가 해당 메시지를 처리하게 됩니다.
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    // 클라이언트가 웹소켓 서버에 연결할 때 사용할 엔드포인트를 등록합니다.
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws" 경로로 웹소켓 연결을 위한 엔드포인트를 생성합니다. 클라이언트는 이 경로를 통해 웹소켓 서버에 연결할 수 있습니다.
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // 전체 페이지로부터의 요청 허용
                .withSockJS();
        // SockJS는 웹소켓을 지원하지 않는 브라우저에서도 웹소켓과 유사한 기능을 사용할 수 있도록 폴백 옵션을 제공합니다.
        // 즉, 웹소켓이 불가능할 경우 다른 방식으로 통신할 수 있게 해줍니다.


    }

}
