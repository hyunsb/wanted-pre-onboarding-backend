package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.BoardNotFoundException;
import com.hyunsb.wanted._core.error.exception.BoardSaveFailureException;
import com.hyunsb.wanted._core.error.ErrorMessage;
import com.hyunsb.wanted._core.error.exception.ExceededMaximumPageSizeException;
import com.hyunsb.wanted.user.User;
import com.hyunsb.wanted.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(BoardRequest.SaveDTO saveDTO, Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new BoardSaveFailureException(ErrorMessage.INVALID_USER));

        Board board = getBoardValueOf(saveDTO, userId);
        boardRepository.save(board);
    }

    private Board getBoardValueOf(BoardRequest.SaveDTO saveDTO, Long userId) {
        User user = User.builder().id(userId).build();
        return saveDTO.toEntityWith(user);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponse.ListDTO> getAllList(Pageable pageable) {
        if (pageable.getPageSize() > 100)
            throw new ExceededMaximumPageSizeException();

        return boardRepository.findAll(pageable)
                .map(BoardResponse.ListDTO::from);
    }

    @Transactional(readOnly = true)
    public BoardResponse.DetailDTO getBoardBy(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        return BoardResponse.DetailDTO.from(board);
    }
}
