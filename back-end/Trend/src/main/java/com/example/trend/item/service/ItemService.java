package com.example.trend.item.service;

import com.example.trend.item.dto.*;
import com.example.trend.util.Pagination;

import java.util.List;

public interface ItemService {
    Pagination<ItemRetrieveResponseDto> searchPagedItems(ItemSearchCriteria itemSearchCriteria, int page, int size);

    int regist(ItemRequestDto itemRegistDto);

    ItemDetailResponseDto detail(int itemId, String userId);

    ItemLessorInfoDto getLessorInfo(String lessorId);

    Pagination<ItemRetrieveResponseDto> getPagedLessorItems(String lessorId, int page, int size);

    int update(ItemRequestDto itemUpdateDto);

    void delete(int itemId);

    List<ItemSimpleInfo> getSimilarItems(int itemId);

    List<ItemSimpleInfo> getPeripheralItems(int itemId);

    Pagination<TradeReviewDto> getPagedLessorReviews(String lessorId, int page, int size);

    Pagination<ArticleSimpleInfo> getPagedLessorArticles(String lessorId, int page, int size);

    int like(String userId, String itemId);

    int likeCancel(String userId, String itemId);

    ItemRequestDto getUpdateViewInfo(int itemId);
}
