package com.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                        // 정적 자원과 로그인, 회원가입 페이지는 인증 없이 접근 허용
                        .requestMatchers("/", "/register", "/resources/**", "/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능한 URL
                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // 로그인 페이지 설정
                        .loginPage("/login")
                        .defaultSuccessUrl("/board/list", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        // 로그아웃 성공 시 리다이렉트할 페이지 설정
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.getOrBuild();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
