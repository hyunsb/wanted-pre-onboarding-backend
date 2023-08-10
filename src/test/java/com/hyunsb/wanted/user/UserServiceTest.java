package com.hyunsb.wanted.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("사용자 회원가입 서비스 단위 테스트")
    class Signup {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            UserRequest.SignupDTO signupDTO =
                    UserRequest.SignupDTO.builder()
                            .email("user@example.com")
                            .password("12345678")
                            .build();

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .then(invocation -> invocation.getArgument(0));

            Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                    .then(invocation -> {
                        String rawPassword = invocation.getArgument(0);
                        return "encoded_" + rawPassword;
                    });

            // When
            // Then
            userService.signup(signupDTO);
        }
    }
}