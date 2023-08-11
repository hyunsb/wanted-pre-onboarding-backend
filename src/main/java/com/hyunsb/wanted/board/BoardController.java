package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.error.exception.UnauthorizedException;
import com.hyunsb.wanted._core.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<Object> save(
            @RequestBody BoardRequest.SaveDTO saveDTO,
            HttpServletRequest request) {
        log.info("POST /board : " + saveDTO);

        Long userId = (Long) request.getAttribute(JwtProvider.REQUEST);
        if (userId == null) throw new UnauthorizedException();

        boardService.save(saveDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/board")
    public ResponseEntity<Object> getAllList(
            @PageableDefault(size = 5) Pageable pageable) {
        log.info("GET /board : " + pageable);

        Page<BoardResponse.ListDTO> allList = boardService.getAllList(pageable);
        return ResponseEntity.ok(allList);
    }
}
