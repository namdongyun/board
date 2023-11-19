package com.example.board.service;

import com.example.board.dto.UserDTO;
import com.example.board.entity.User;
import com.example.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRole("USER");

        userRepository.save(user);
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<User> byUsername = userRepository.findByUsername(userDTO.getUsername());
        if (byUsername.isPresent()) {
            // 조회 결과 있음
            User user = byUsername.get();
            if (user.getPassword().equals(userDTO.getPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환
                return UserDTO.toUserDTO(user);
            } else {
                return null;
            }
        } else {
            // 조회 결과 없음
            return null;
        } 
    }

//    public boolean authenticate(String username, String password) {
//        Optional<User> user = userRepository.findByUsername(username);
//
//        if (!user.isPresent()) {
//            return false;
//        }
//
//        // 비밀번호 검증
//        return passwordEncoder.matches(password, user.get().getPassword());
//    }
}
