package com.example.board.security;

import com.example.board.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {
    private final Account account;

    public PrincipalDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        //사용자가 'admin' 역할을 가지고 있다면 'ROLE_ADMIN' 권한을 부여합니다.
        if (userHasAdminRole()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else {
            // 사용자의 권한을 설정합니다. 'ROLE_USER' 권한을 부여합니다.
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }


        return authorities;
    }

    private boolean userHasAdminRole() {

        return "ADMIN".equals(account.getRole());
    }

    // get Account 메서드
    public Account getAccount() {
        return account;
    }

    // get Password 메서드
    @Override
    public String getPassword() {
        return account.getPassword();
    }

    // get Username 메서드
    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
