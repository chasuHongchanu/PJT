package com.example.trend.community.service;

import com.example.trend.community.dto.ArticleRegistRequestDto;
import com.example.trend.community.dto.ArticleListResponseDto;
import com.example.trend.community.dto.ArticleResponseDto;
import com.example.trend.community.dto.ArticleSearchRequestDto;
import com.example.trend.community.mapper.ArticleMapper;
import com.example.trend.course.mapper.CourseLikeMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.util.FileUtil;
import com.example.trend.util.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;
    private final FileUtil fileUtil;
    private final CourseLikeMapper articleLikeMapper;


    @Override
    public void registArticle(ArticleRegistRequestDto requestDto) {
        try {
            articleMapper.insertArticle(requestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_ARTICLE, e);
        }

        // 이미지 저장
        saveArticleImages(requestDto.getImageList(), requestDto.getArticleId());
    }

    @Override
    public void updateArticle(ArticleRegistRequestDto requestDto, String userId) {
        int articleId = requestDto.getArticleId();

        // 게시글 수정
        try {
            articleMapper.updateArticle(requestDto, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_ARTICLE, e);
        }

        // 이미지 수정
        // 기존 이미지 파일 삭제
        articleMapper.deleteArticleImage(articleId);
        fileUtil.deleteFiles("articles", articleId);

        // 수정할 이미지 저장
        saveArticleImages(requestDto.getImageList(), requestDto.getArticleId());
    }

    private void saveArticleImages(List<MultipartFile> imageList, int articleId) {
        // 이미지 저장
        if (!(imageList.size() == 1 && imageList.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("articles", articleId, imageList);


            // 파일 이름을 추출하여 itemImageNames 리스트에 추가
            List<String> articleImageNames = imageList.stream()
                    .map(file -> "articles/" + articleId + "/" + file.getOriginalFilename())
                    .toList();

            // db에 이미지 경로 및 이름 저장
            String thumbnail = articleImageNames.get(0);
            articleMapper.insertThumbnail(thumbnail, articleId);
            // db에 게시글 이미지 이름 정보 insert
            for (String articleImageName : articleImageNames) {
                articleMapper.insertArticleImage(articleId, articleImageName);
            }
        }
    }

    @Override
    public void deleteArticle(int articleId, String userId) {
        try {
            int result = articleMapper.deleteArticle(articleId, userId);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_DELETE_ARTICLE);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_ARTICLE, e);
        }
    }

    @Override
    public Pagination<ArticleListResponseDto> getAllList(int page, int size) {
        try {
            int offset = (page - 1) * size;
            // 게시물 목록 가져오기
            List<ArticleListResponseDto> articles = articleMapper.selectAllArticles(size, offset);
            // 전체 개수 파악
            int totalItems = articleMapper.countAllArticles(); // 총 데이터 수
            // 페이징 객체 반환
            return new Pagination<>(articles, totalItems, page, size);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_ALL_ARTICLE, e);
        }
    }

    @Override
    public Pagination<ArticleListResponseDto> searchArticles(int page, int size, ArticleSearchRequestDto articleSearchRequestDto) {
        try {
            int offset = (page - 1) * size;
            // 코스 목록 가져오기
            List<ArticleListResponseDto> articleList = articleMapper.searchArticles(articleSearchRequestDto, size, offset);
            log.info(String.valueOf(articleList.size()));
            // 전체 개수 파악
            int totalItems = articleMapper.countSearchArticles(articleSearchRequestDto); // 총 데이터 수
            // 페이징 객체 반환
            return new Pagination<>(articleList, totalItems, page, size);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public ArticleResponseDto getArticle(int articleId) {
        ArticleResponseDto articleResponseDto;

        // 게시물 정보 가져오기
        try {
            articleResponseDto = articleMapper.selectArticleByArticleId(articleId);
            articleMapper.updateViewCountByArticleId(articleId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NOT_FOUND_ARTICLE_INFO);
        }

        // 이미지 정보 가져오기
        try {
            articleResponseDto.setArticleImages(articleMapper.selectArticleImages(articleId));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_ARTICLE_IMAGES);
        }
        return articleResponseDto;
    }
}
