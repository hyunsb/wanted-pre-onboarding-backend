package com.hyunsb.wanted.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(UserRequest.SignupDTO signupDTO) {
        User signupUser = signupDTO.toEntityWithEncodedPassword(passwordEncoder);
        userRepository.save(signupUser);
    }
}
