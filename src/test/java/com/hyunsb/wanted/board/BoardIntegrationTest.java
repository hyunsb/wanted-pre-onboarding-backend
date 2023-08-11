package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.BoardSaveFailureException;
import com.hyunsb.wanted.user.User;
import com.hyunsb.wanted.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardIntegrationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        userRepository.save(user);
    }

    @Nested
    @DisplayName("게시글 생성 통합 테스트")
    class Save {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given

            Long userId = 1L;
            BoardRequest.SaveDTO saveDTO =
                    BoardRequest.SaveDTO.builder()
                            .title("testTitle")
                            .content("testContent")
                            .build();

            // When
            // Then
            boardService.save(saveDTO, userId);
        }

        @DisplayName("실패 - 유효하지 않은 userId")
        @Test
        void failure_Test_InvalidUserId() {
            // Given
            Long userId = 2L;
            BoardRequest.SaveDTO saveDTO =
                    BoardRequest.SaveDTO.builder()
                            .title("testTitle")
                            .content("testContent")
                            .build();
            // When
            // Then
            Assertions.assertThrows(BoardSaveFailureException.class,
                    () -> boardService.save(saveDTO, userId));
        }
    }
}
