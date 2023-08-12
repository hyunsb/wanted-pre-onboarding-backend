package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.BoardSaveFailureException;
import com.hyunsb.wanted._core.error.exception.ExceededMaximumPageSizeException;
import com.hyunsb.wanted.user.User;
import com.hyunsb.wanted.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class BoardIntegrationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

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

    @Nested
    @DisplayName("게시글 목록 조회 통합 테스트")
    class GetAllList {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            List<Board> mockList = List.of(
                    Board.builder().title("title").content("content").user(User.builder().id(1L).build()).build(),
                    Board.builder().title("title").content("content").user(User.builder().id(1L).build()).build(),
                    Board.builder().title("title").content("content").user(User.builder().id(1L).build()).build(),
                    Board.builder().title("title").content("content").user(User.builder().id(1L).build()).build(),
                    Board.builder().title("title").content("content").user(User.builder().id(1L).build()).build()
            );

            boardRepository.saveAll(mockList);

            Pageable pageable = PageRequest.of(0, 3);

            // When
            Page<BoardResponse.ListDTO> actual = boardService.getAllList(pageable);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(3, actual.getSize()),
                    () -> Assertions.assertEquals(2, actual.getTotalPages()),
                    () -> Assertions.assertEquals(5, actual.getTotalElements())
            );
        }

        @DisplayName("실패 - 페이징 사이즈 범위 초과")
        @Test
        void failure_Test_ExceededPagingSize() {
            // Given
            Pageable pageable = PageRequest.of(0, 200);

            // When
            // Then
            Assertions.assertThrows(ExceededMaximumPageSizeException.class, () ->
                    boardService.getAllList(pageable));
        }
    }
}
