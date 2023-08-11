package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.BoardSaveFailureException;
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
}