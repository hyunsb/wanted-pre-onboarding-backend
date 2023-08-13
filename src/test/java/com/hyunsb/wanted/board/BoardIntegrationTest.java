package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.BoardNotFoundException;
import com.hyunsb.wanted._core.error.exception.BoardSaveFailureException;
import com.hyunsb.wanted._core.error.exception.ExceededMaximumPageSizeException;
import com.hyunsb.wanted.user.User;
import com.hyunsb.wanted.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
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
        User persistenceUser = userRepository.save(user);

        List<Board> mockList = List.of(
                Board.builder().title("title").content("content").user(persistenceUser).build(),
                Board.builder().title("title").content("content").user(persistenceUser).build(),
                Board.builder().title("title").content("content").user(persistenceUser).build(),
                Board.builder().title("title").content("content").user(persistenceUser).build(),
                Board.builder().title("title").content("content").user(persistenceUser).build()
        );
        boardRepository.saveAll(mockList);
    }

    @Transactional
    @Nested
    @DisplayName("게시글 생성 통합 테스트")
    class Save {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            List<User> allUsers = userRepository.findAll();
            Long userId = allUsers.get(0).getId();

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

    @Nested
    @DisplayName("특정 게시글 목록 조회 통합 테스트")
    class getBoardBy {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Long boardId = 1L;

            // When
            BoardResponse.DetailDTO actual = boardService.getBoardBy(boardId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(1L, actual.getId()),
                    () -> Assertions.assertEquals("title", actual.getTitle()),
                    () -> Assertions.assertEquals("content", actual.getContent())
            );
        }

        @DisplayName("실패 - 유효하지 않은 게시글 아이디")
        @Test
        void failure_Test_InvalidBoardId() {
            // Given
            boardRepository.deleteAll();
            Long boardId = 1L;

            // When
            // Then
            Assertions.assertThrows(BoardNotFoundException.class, () ->
                    boardService.getBoardBy(boardId));
        }
    }
}
