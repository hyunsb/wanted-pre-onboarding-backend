package com.hyunsb.wanted.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Nested
    @DisplayName("사용자 회원가입 서비스 통합 테스트")
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

            // When
            // Then
            userService.signup(signupDTO);
        }
    }
}
