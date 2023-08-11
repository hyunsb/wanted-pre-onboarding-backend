package com.hyunsb.wanted.board;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class ListDTO {

        private Long id;
        private String title;
        private String content;
        private Long userId;

        public static ListDTO from(Board board) {
            return ListDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .userId(board.getUser().getId())
                    .build();
        }
    }
}
