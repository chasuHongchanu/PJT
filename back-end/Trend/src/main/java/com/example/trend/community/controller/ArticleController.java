package com.example.trend.community.controller;

import com.example.trend.community.dto.ArticleRegistRequestDto;
import com.example.trend.community.dto.ArticleListResponseDto;
import com.example.trend.community.dto.ArticleResponseDto;
import com.example.trend.community.service.ArticleService;
import com.example.trend.config.SkipJwt;
import com.example.trend.util.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community")
@Slf4j
@Tag(name = "Community API", description = "API for article operations")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/article")
    @Operation(summary = "게시글 등록", description = "게시글 등록 기능")
    public ResponseEntity<?> regist(@Valid @ModelAttribute ArticleRegistRequestDto requestDto, @RequestAttribute("userId") String userId) {
        // 유저 id 입력
        requestDto.setWriterId(userId);

        // 게시글 등록
        articleService.registArticle(requestDto);
        return ResponseEntity.ok("Regist Article Successful");
    }

    @PutMapping("/article")
    @Operation(summary = "게시글 수정", description = "게시글 수정 기능")
    public ResponseEntity<?> update(@Valid @ModelAttribute ArticleRegistRequestDto requestDto, @RequestAttribute("userId") String userId) {
        // 게시글 수정
        articleService.updateArticle(requestDto, userId);
        return ResponseEntity.ok("Update Article Successful");
    }

    @DeleteMapping("/article")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 기능")
    public ResponseEntity<?> delete(@RequestParam int articleId, @RequestAttribute("userId") String userId) {
        // 게시글 삭제
        articleService.deleteArticle(articleId, userId);
        return ResponseEntity.ok("Delete Article Successful");
    }

    @SkipJwt
    @GetMapping("/article/list")
    @Operation(summary = "전체 게시물 조회", description = "전체 게시물 목록을 조회")
    public ResponseEntity<?> list(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Pagination<ArticleListResponseDto> articleListResponseDtos = articleService.getAllList(page, size);
        return ResponseEntity.ok(articleListResponseDtos);
    }

    @SkipJwt
    @GetMapping("/article/{articleId}")
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 조회")
    public ResponseEntity<?> detail(@PathVariable int articleId) {
        ArticleResponseDto articleResponseDto = articleService.getArticle(articleId);
        return ResponseEntity.ok(articleResponseDto);
    }



    // 게시물 검색 조회 필요
}
