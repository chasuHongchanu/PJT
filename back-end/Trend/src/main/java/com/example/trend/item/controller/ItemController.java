package com.example.trend.item.controller;

import com.example.trend.config.SkipJwt;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.*;
import com.example.trend.item.service.ItemService;
import com.example.trend.user.jwt.JwtUtil;
import com.example.trend.util.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;

    private final double DEFAULT_LATITUDE = 37.5074;
    private final double DEFAULT_LONGITUDE = 126.7218;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @SkipJwt
    @GetMapping("/rent/list")
    public ResponseEntity<?> list(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        ItemSearchCriteria itemSearchCriteria = new ItemSearchCriteria();
        // 필터링하지 않았을 시 기본 위/경도 지정 후 검색
        itemSearchCriteria.setLatitude(DEFAULT_LATITUDE);
        itemSearchCriteria.setLongitude(DEFAULT_LONGITUDE);
        Pagination<ItemRetrieveResponseDto> pagedItemList = itemService.searchPagedItems(itemSearchCriteria, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(pagedItemList);
    }

    @PostMapping("/rent")
    public ResponseEntity<?> regist(@Valid @ModelAttribute("itemRegistDto") ItemRequestDto itemRegistDto, @RequestAttribute("userId") String userId) {
        itemRegistDto.setUserId(userId);
        // 가격이 비어있는 경우
        if(itemRegistDto.getItemPrice() == 0) {
            throw new CustomException(ErrorCode.MISSING_ITEM_PRICE);
        }

        // 정상적인 데이터 DB에 등록
        int result = itemService.regist(itemRegistDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/rent")
    public ResponseEntity<?> update(@Valid @ModelAttribute("itemUpdateDto") ItemRequestDto itemUpdateDto,
                                    @RequestAttribute("userId") String userId)
        {
            itemUpdateDto.setUserId(userId);
            // 가격이 비어있는 경우
            if(itemUpdateDto.getItemPrice() == 0) {
                throw new CustomException(ErrorCode.MISSING_ITEM_PRICE);
        }

        // 정상적인 데이터 DB에 등록
        int result = itemService.update(itemUpdateDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 수정되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
    
    @DeleteMapping("/rent")
    public ResponseEntity<?> delete(@RequestParam("itemId") int itemId) {
        itemService.delete(itemId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 삭제되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @SkipJwt
    @GetMapping("/rent")
    public ResponseEntity<?> detail(@RequestParam("itemId") int itemId, HttpServletRequest request) {
        String userId = null;
        if(request.getAttribute("userId") != null) {
            userId = request.getAttribute("userId").toString();
        }

        ItemDetailResponseDto itemDetailResponseDto = itemService.detail(itemId, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(itemDetailResponseDto);
    }

    @SkipJwt
    @GetMapping("/rent/similar")
    public ResponseEntity<?> detailSimilarItems(@RequestParam("itemId") int itemId) {
        List<ItemSimpleInfo> similarItems = itemService.getSimilarItems(itemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(similarItems);
    }

    @SkipJwt
    @GetMapping("/rent/peripheral")
    public ResponseEntity<?> detailPeripheralItems(@RequestParam("itemId") int itemId) {
        List<ItemSimpleInfo> peripheralItems = itemService.getPeripheralItems(itemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(peripheralItems);
    }

    @SkipJwt
    @GetMapping("/rent/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    @ModelAttribute ItemSearchCriteria itemSearchCriteria) {
        // 최대 가격이 최소 가격보다 작으면 예외 발생
        Integer minPrice = itemSearchCriteria.getMinPrice();
        Integer maxPrice = itemSearchCriteria.getMaxPrice();
        if(minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new CustomException(ErrorCode.INVALID_PRICE_RANGE);
        }

        Pagination<ItemRetrieveResponseDto> pagedItemList = itemService.searchPagedItems(itemSearchCriteria, page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pagedItemList);
    }

    @GetMapping("/rent/lessor/{lessorId}")
    public ResponseEntity<?> lessor(@PathVariable String lessorId) {
        ItemLessorInfoDto itemLessorInfoDto = itemService.getLessorInfo(lessorId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemLessorInfoDto);
    }

    @GetMapping("/rent/lessor/{lessorId}/items")
    public ResponseEntity<?> lessorItems(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "3") int size,
                                         @PathVariable String lessorId) {
        Pagination<ItemRetrieveResponseDto> pagedLessorItems = itemService.getPagedLessorItems(lessorId, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(pagedLessorItems);
    }

    @GetMapping("/rent/lessor/{lessorId}/reviews")
    public ResponseEntity<?> lessorReviews(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @PathVariable String lessorId) {

        Pagination<TradeReviewDto> pagedLessorReviews = itemService.getPagedLessorReviews(lessorId, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(pagedLessorReviews);
    }

    @GetMapping("/rent/lessor/{lessorId}/articles")
    public ResponseEntity<?> lessorArticles(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "3") int size,
                                            @PathVariable String lessorId) {

        Pagination<ArticleSimpleInfo> pagedLessorArticles = itemService.getPagedLessorArticles(lessorId, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(pagedLessorArticles);
    }

    @PostMapping("/rent/{itemId}/like")
    public ResponseEntity<?> itemLike(@PathVariable String itemId,
                                      @RequestAttribute("userId") String userId) {
        int result = itemService.like(userId, itemId);

        Map<String, Object> response = new HashMap<>();
        if(result == 1) {
            response.put("message", "성공적으로 수정되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }

        else {
            response.put("message", "좋아요에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @DeleteMapping("/rent/{itemId}/likeCancel")
    public ResponseEntity<?> itemLikeCancel(@PathVariable String itemId,
                                      @RequestAttribute("userId") String userId) {
        int result = itemService.likeCancel(userId, itemId);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 수정되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }

        else {
            response.put("message", "수정에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
