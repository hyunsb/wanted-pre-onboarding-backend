package com.hyunsb.wanted.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunsb.wanted._core.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        })
class BoardControllerTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("게시글 생성 컨트롤러 단위 테스트")
    class Save {

        @DisplayName("성공")
        @Test
        @WithMockUser
        void success_Test() throws Exception {
            // Given
            String uri = "/board";
            String title = "테스트용 게시글 제목";
            String content = "테스트용 게시글 내용";

            BoardRequest.SaveDTO saveDTO =
                    BoardRequest.SaveDTO.builder()
                            .title(title)
                            .content(content)
                            .build();

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(objectMapper.writeValueAsString(saveDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }
}
