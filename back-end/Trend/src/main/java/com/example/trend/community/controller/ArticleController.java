package com.example.trend.community.controller;

import com.example.trend.community.dto.ArticleListResponseDto;
import com.example.trend.community.dto.ArticleRegistRequestDto;
import com.example.trend.community.dto.ArticleResponseDto;
import com.example.trend.community.dto.comment.ArticleCommentRequestDto;
import com.example.trend.community.service.ArticleCommentService;
import com.example.trend.community.service.ArticleLikeService;
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
    private final ArticleLikeService articleLikeService;
    private final ArticleCommentService articleCommentService;

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

    /*
    좋아요: 게시글 및 댓글 좋아요
     */
    @PostMapping("/{articleId}/like")
    @Operation(summary = "커뮤니티 게시물 좋아요", description = "게시물 좋아요 처리 기능")
    public ResponseEntity<?> likeArticle(@PathVariable int articleId, @RequestAttribute("userId") String userId) {
        articleLikeService.likeAticle(articleId, userId);
        return ResponseEntity.ok("Like Article Successful");
    }

    @DeleteMapping("/{articleId}/like")
    @Operation(summary = "커뮤니티 게시물 좋아요 취소", description = "커뮤니티 게시물 좋아요 취소 처리 기능")
    public ResponseEntity<?> unLikeArticle(@PathVariable int articleId, @RequestAttribute("userId") String userId) {
        articleLikeService.unLikeArticle(articleId, userId);
        return ResponseEntity.ok("UnLike Article Successful");
    }

    @GetMapping("/{articleId}/like")
    @Operation(summary = "커뮤니티 게시물 좋아요 확인", description = "좋아요 한 게시물인지 확인하는 기능")
    public ResponseEntity<?> isLikeArticle(@PathVariable int articleId, @RequestAttribute("userId") String userId) {
        boolean result = articleLikeService.isLikeArticle(articleId, userId);
        return ResponseEntity.ok("좋아요 여부: " + result);
    }
    
    /*
    좋아요: 댓글 좋아요
     */
    @PostMapping("/comment/{articleCommentId}/like")
    @Operation(summary = "커뮤니티 댓글 좋아요", description = "커뮤니티 댓글 좋아요 처리 기능")
    public ResponseEntity<?> likeArticleComment(@PathVariable int articleCommentId, @RequestAttribute("userId") String userId) {
        articleLikeService.likeArticleComment(articleCommentId, userId);
        return ResponseEntity.ok("Like ArticleComment Successful");
    }

    @DeleteMapping("/comment/{articleCommentId}/like")
    @Operation(summary = "커뮤니티 게시물 댓글 좋아요 취소", description = "커뮤니티 댓글 좋아요  취소 처리 기능")
    public ResponseEntity<?> unLikeArticleComment(@PathVariable int articleCommentId, @RequestAttribute("userId") String userId) {
        articleLikeService.unLikeArticleComment(articleCommentId, userId);
        return ResponseEntity.ok("UnLike ArticleComment Successful");
    }

    @GetMapping("/comment/{articleCommentId}/like")
    @Operation(summary = "커뮤니티 게시물 댓글 좋아요 확인", description = "좋아요 한 커뮤니티 댓글인지 확인하는 기능")
    public ResponseEntity<?> isLikeArticleComment(@PathVariable int articleCommentId, @RequestAttribute("userId") String userId) {
        boolean result = articleLikeService.isLikeArticleComment(articleCommentId, userId);
        return ResponseEntity.ok("댓글 좋아요 여부: " + result);
    }
    
    /*
    댓글
     */
    @PostMapping("/{articleId}/comment")
    @Operation(summary = "게시글 댓글 작성 기능", description = "게시글 댓글 작성하는 기능")
    public ResponseEntity<?> createComment(
            @PathVariable int articleId,
            @RequestBody ArticleCommentRequestDto articleCommentRequestDto,
            @RequestAttribute("userId") String userId) {
        articleCommentRequestDto.setArticleId(articleId);
        articleCommentRequestDto.setUserId(userId);
        articleCommentService.registComment(articleCommentRequestDto);
        return ResponseEntity.ok("Regist Article Comment Successful");
    }

    @PostMapping("/{articleId}/comment/{parentCommentId}")
    @Operation(summary = "게시글 대댓글 작성 기능", description = "게시글 대댓글 작성하는 기능")
    public ResponseEntity<?> createCommentReply(
            @PathVariable int articleId,
            @PathVariable int parentCommentId,
            @RequestBody ArticleCommentRequestDto articleCommentRequestDto,
            @RequestAttribute("userId") String userId) {
        articleCommentRequestDto.setArticleId(articleId);
        articleCommentRequestDto.setParentsCommentId(parentCommentId);
        articleCommentRequestDto.setUserId(userId);
        articleCommentService.registReply(articleCommentRequestDto);
        return ResponseEntity.ok("Regist Article reply Successful");
    }

    @PutMapping("/{articleId}/comment/{commentId}")
    @Operation(summary = "게시글 댓글 수정 기능", description = "게시글 댓글 수정 기능")
    public ResponseEntity<?> updateCommentReply(
            @PathVariable int articleId,
            @PathVariable int commentId,
            @RequestBody ArticleCommentRequestDto articleCommentRequestDto,
            @RequestAttribute("userId") String userId) {
        articleCommentRequestDto.setArticleId(articleId);
        articleCommentRequestDto.setCommentId(commentId);
        articleCommentRequestDto.setUserId(userId);
        articleCommentService.updateComment(articleCommentRequestDto);
        return ResponseEntity.ok("Update Article Comment Successful");
    }

    @DeleteMapping("/{articleId}/comment/{commentId}")
    @Operation(summary = "게시글 댓글 삭제 기능", description = "게시글 댓글 삭제 기능")
    public ResponseEntity<?> deleteComment(
            @PathVariable int commentId,
            @RequestAttribute("userId") String userId) {
        articleCommentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("Delete Article Comment Successful");
    }
}
