package com.hyunsb.wanted.board;

import com.hyunsb.wanted._core.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        boardService.save(saveDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
