package com.example.board.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                        // 정적 자원과 로그인, 회원가입 페이지는 인증 없이 접근 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능한 URL
                        .requestMatchers("/", "/api/register", "/static/**", "api/login", "api/board/list",
                                "api/board/view/**", "api", "/ws/**", "/error/**").permitAll()
                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // 로그인 api 주소 설정
                        .loginProcessingUrl("/api/login")

                        // 로그인에 성공했을 때 실행될 AuthenticationSuccessHandler를 정의합니다.
                        .successHandler(new AuthenticationSuccessHandler() {

                            // 로그인 성공 시 실행될 메소드를 오버라이드합니다.
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                // 사용자 정보에 접근하여 필요한 정보를 가져옵니다.
                                Object principal = authentication.getPrincipal();
                                String username = "";
                                String role = "";

                                // principal이 UserDetails 인스턴스라면, 사용자 이름과 권한 정보를 가져옵니다.
                                if(principal instanceof UserDetails userDetails){
                                    username = userDetails.getUsername();
                                    role = userDetails.getAuthorities().toString();
                                }
                                // 응답의 Content-Type을 JSON으로 설정
                                response.setContentType("application/json;charset=UTF-8");

                                // HTTP 응답 상태 코드를 200(성공)으로 설정
                                response.setStatus(HttpServletResponse.SC_OK);

                                // JSON 문자열을 만들어 응답의 본문에 쓰기
                                response.getWriter()
                                        .write("{\"username\": \"" + username + "\", \"role\": \"" + role + "\"}");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        // 로그아웃 api 주소 설정
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        }))
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                        .permitAll()
                );
        return http.getOrBuild();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
