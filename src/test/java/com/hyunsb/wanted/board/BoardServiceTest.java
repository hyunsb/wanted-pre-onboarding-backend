package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.BoardNotFoundException;
import com.hyunsb.wanted._core.error.exception.BoardSaveFailureException;
import com.hyunsb.wanted._core.error.exception.ExceededMaximumPageSizeException;
import com.hyunsb.wanted.user.User;
import com.hyunsb.wanted.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardService boardService;

    @Nested
    @DisplayName("게시글 생성 서비스 단위 테스트")
    class Save {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(User.builder().id(1L).build()));

            Mockito.when(boardRepository.save(ArgumentMatchers.any(Board.class)))
                    .then(invocation -> invocation.getArgument(0));

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
            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            Long userId = 1L;
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
    @DisplayName("게시글 목록 조회 서비스 단위 테스트")
    class getAllList {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given

            List<Board> mockList = List.of(
                    Board.builder().id(1L).title("title").content("content").user(User.builder().id(1L).build()).build(),
                    Board.builder().id(2L).title("title").content("content").user(User.builder().id(2L).build()).build(),
                    Board.builder().id(3L).title("title").content("content").user(User.builder().id(3L).build()).build(),
                    Board.builder().id(4L).title("title").content("content").user(User.builder().id(4L).build()).build(),
                    Board.builder().id(5L).title("title").content("content").user(User.builder().id(5L).build()).build()
            );

            Pageable pageable = PageRequest.of(0, 3);
            Page<Board> mockPage = new PageImpl<>(mockList, pageable, 2);

            Mockito.when(boardRepository.findAll(pageable))
                    .thenReturn(mockPage);

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
            Pageable pageable = PageRequest.of(0, 101);

            // When
            // Then
            Assertions.assertThrows(ExceededMaximumPageSizeException.class, () -> boardService.getAllList(pageable));
        }
    }

    @Nested
    @DisplayName("특정 게시글 목록 조회 서비스 단위 테스트")
    class getBoardBy {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Long boardId = 1L;
            Board board = Board.builder()
                    .id(1L)
                    .title("title")
                    .content("content")
                    .user(new User())
                    .build();

            Mockito.when(boardRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(board));

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
            Long boardId = 1L;
            Mockito.when(boardRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            // When
            // Then
            Assertions.assertThrows(BoardNotFoundException.class, () -> boardService.getBoardBy(boardId));
        }
    }
}