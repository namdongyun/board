package com.example.board.dto;

import com.example.board.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN
    private Timestamp create_date;
    // 기본 생성자
    public UserDTO() {

    }
    // 모든 필드를 매개변수로 받는 생성자
    public UserDTO(Long id, String username, String password, String email, String role, Timestamp create_date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.create_date = create_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getcreate_date() {
        return create_date;
    }

    public void setcreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setcreate_date(user.getCreate_date());

        return userDTO;
    }
}
