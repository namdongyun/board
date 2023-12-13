package com.example.board.utils;

import com.example.board.entity.Account;
import com.example.board.entity.RefreshToken;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.RefreshTokenRepository;
import com.example.board.security.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final PrincipalDetailsService principalDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    // JWT 생성 메소드
    public String createJwt(String username) {

        Claims claims = Jwts.claims();
        // 생성된 claims 객체에 put 메서드를 사용하여 사용자 이름을 저장합니다
        claims.setSubject(username);

        // 만료시간 : 1Hour
        long expiredMs = 1000L * 60 * 60;

        return Jwts.builder() //  JWT 빌더를 초기화합니다.
                .setClaims(claims) // 위에서 생성한 claims를 JWT에 포함시킵니다.
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰이 발급된 시간을 현재 시간으로 설정합니다.
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰의 만료 시간을 설정합니다.
                .signWith(SignatureAlgorithm.HS256, secretKey) // 주어진 비밀 키와 HS256 알고리즘으로 JWT에 서명합니다.
                .compact(); // 위에서 설정한 내용들로 JWT를 문자열로 압축하고 반환합니다.
    }

    // "Authorization" 헤더를 추출하는 메소드
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // 토큰이 "Bearer "로 시작하는지 확인합니다. 만약 시작하지 않는다면 false를 반환
            if (!token.substring(0, "Bearer ".length()).equalsIgnoreCase("Bearer ")) {
                return false;
            } else {
                // 토큰에서 "Bearer " 부분을 제거하고, 실제 토큰 값만을 추출
                token = token.split(" ")[1].trim();
            }
            // 제공된 토큰을 파싱하고, 서명을 검증하여 토큰에 담긴 클레임(claims)
            // secretKey는 토큰의 서명을 검증할 때 사용되는 비밀 키
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

            // 토큰의 만료 시간이 현재 시간보다 이전인지 확인합니다.
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Spring Security 인증과정에서 권한확인을 위한 기능
    // 주어진 JWT 토큰을 이용하여 Authentication 객체를 생성하는 기능
    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자의 계정 이름을 추출한 후, 해당 사용자의 UserDetails를 로드합니다.
        // UserDetails는 사용자의 정보(예: 사용자 이름, 패스워드, 권한)를 담고 있는 객체입니다.
        UserDetails userDetails = principalDetailsService.loadUserByUsername(this.getUsername(token));

        // UserDetails와 사용자의 권한 정보를 이용하여 UsernamePasswordAuthenticationToken 객체를 생성합니다.
        // 여기서 두 번째 인자인 ""는 패스워드 필드를 비워두는데, 이미 토큰을 통해 인증을 거쳤기 때문에 패스워드는 필요하지 않습니다.
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // token에 담겨있는 사용자의 username 추출
    public String getUsername(String token) {
        // 주어진 secretKey를 사용해 토큰의 서명을 검증하고, 토큰의 클레임을 파싱한 다음, 토큰에 담긴 subject 필드를 반환
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();

        // .getSubject()는 jwt의 페이로드(Payload)부분의 'sub' claims에 해당하는 값을 추출합니다.
    }

    // Refresh Token 생성 메소드
    @Transactional
    public String createRefreshToken(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 만료시간 : 7Days
        long expiredMs = 1000L * 60 * 60 * 24 * 7;
        Instant expiryDate = Instant.now().plusMillis(expiredMs);

        String rfToken = Jwts.builder()
                .setSubject(account.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(account);
        refreshToken.setRfToken(rfToken);
        refreshToken.setExpiryDate(expiryDate);

        refreshTokenRepository.save(refreshToken);

        return rfToken;
    }

}
