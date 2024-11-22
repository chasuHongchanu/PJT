package com.example.trend.item.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.*;
import com.example.trend.item.mapper.ItemMapper;
import com.example.trend.util.FileUtil;
import com.example.trend.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemMapper itemMapper;
    private final FileUtil fileUtil;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, FileUtil fileUtil) {
        this.itemMapper = itemMapper;
        this.fileUtil = fileUtil;
    }

    @Override
    public Pagination<ItemRetrieveResponseDto> searchPagedItems(ItemSearchCriteria itemSearchCriteria, int page, int size) {
        System.out.println(page + " " + size);
        int offset = (page - 1) * size;
        List<ItemRetrieveResponseDto> items = itemMapper.searchItems(itemSearchCriteria, offset, size);
        int totalItems = itemMapper.countItems(itemSearchCriteria);

        return new Pagination<>(items, totalItems, page, size);
    }

    @Transactional
    @Override
    public int regist(ItemRequestDto itemRegistDto) {
        // db에 물품 정보 insert
        int result = itemMapper.insertItem(itemRegistDto);

        // itemId를 반환받아 이미지는 storage의 items/itemId/ 경로에 저장
        int itemId = itemRegistDto.getItemId();
        String userId = itemRegistDto.getUserId();

        // 파일 이름을 추출하여 itemImageNames 리스트에 추가
        List<String> itemImageNames = itemRegistDto.getItemImages().stream()
                .map(file -> "items/" + itemId + "/" + file.getOriginalFilename())
                .toList();

        // 시작일이 종료일보다 늦은 경우
        String availableRentalStartDate = itemRegistDto.getAvailableRentalStartDate();
        String availableRentalEndDate = itemRegistDto.getAvailableRentalEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(availableRentalStartDate, formatter);
        LocalDate endDate = LocalDate.parse(availableRentalEndDate, formatter);

        if(startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.INVALID_RENTAL_PERIOD);
        }

        // 아무런 예외도 발생하지 않은 경우 이미지 저장
        // 이미지가 정상적으로 1개 이상 들어온 경우 storage에 이미지 저장
        List<MultipartFile> files = itemRegistDto.getItemImages();
        if(!(files.size() == 1 && files.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("items", itemId, files);
            // 썸네일 저장 (첫 번째로 들어온 사진을 썸네일로 지정)
            itemMapper.insertThumbnail(itemImageNames.get(0), itemId);
            // db에 물품 이미지 이름 정보 insert
            for(String itemImageName: itemImageNames) {
                itemMapper.insertItemImageName(itemId, itemImageName);
            }
        }

        // 물품 등록한 유저 활동점수 증가
        itemMapper.updateUserActivityScore(userId);

        return result;
    }

    @Transactional
    @Override
    public int update(ItemRequestDto itemUpdateDto) {
        // db에 물품 정보 update
        int result = itemMapper.updateItem(itemUpdateDto);
        int itemId = itemUpdateDto.getItemId();

        // 시작일이 종료일보다 늦은 경우
        String availableRentalStartDate = itemUpdateDto.getAvailableRentalStartDate();
        String availableRentalEndDate = itemUpdateDto.getAvailableRentalEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(availableRentalStartDate, formatter);
        LocalDate endDate = LocalDate.parse(availableRentalEndDate, formatter);

        if(startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.INVALID_RENTAL_PERIOD);
        }

        // 파일 이름을 추출하여 itemImageNames 리스트에 추가
        List<String> itemImageNames = itemUpdateDto.getItemImages().stream()
                .map(file -> "items/" + itemId + "/" + file.getOriginalFilename())
                .toList();

        // updateDto로 넘어온 사진과, 현재 저장된 사진들 중 다른 것이 있다면 이전에 저장된 사진은 삭제해야 함
        // DB에서 삭제 (다 날리고 다시 새로 삽입)
        itemMapper.deleteItemImage(itemId);

        // Storage에서 삭제 (다 날리고 다시 새로 삽입)
        fileUtil.deleteFiles("items", String.valueOf(itemId));

        // 아무런 예외도 발생하지 않은 경우 이미지 저장
        // 이미지가 정상적으로 1개 이상 들어온 경우 storage에 이미지 업데이트
        List<MultipartFile> files = itemUpdateDto.getItemImages();
        
        if(!(files.size() == 1 && files.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("items", itemId, files);
            itemMapper.insertThumbnail(itemImageNames.get(0), itemId);

            // db에 물품 이미지 이름 정보 insert
            for(String itemImageName: itemImageNames) {
                itemMapper.insertItemImageName(itemId, itemImageName);
            }
        }

        return result;
    }

    @Transactional
    @Override
    public void delete(int itemId) {
        // DB에서 삭제
        int result = itemMapper.deleteByItemId(itemId);

        // 제대로 삭제되지 않은 경우 예외 처리
        if(result != 1 ) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_ITEM);
        }

        // DB에서 아무 이상 없을 경우 item_image table, Storage에서 이미지 삭제 (hard delete)
        // fileUtil.deleteFiles(itemId);
    }

    @Override
    public List<ItemSimpleInfo> getSimilarItems(int itemId) {
        // 유사 물품 정보 추출
        return itemMapper.selectSimilarItems(itemId);
    }

    @Override
    public List<ItemSimpleInfo> getPeripheralItems(int itemId) {
        return itemMapper.selectPeripheralItems(itemId);
    }

    @Override
    public ItemDetailResponseDto detail(int itemId, String userId) {
        ItemDetailResponseDto itemDetailResponseDto = itemMapper.selectDetailByItemId(itemId);

        // itemId가 존재하지 않을 때 (없는 상품에 접근할 때)
        if(itemDetailResponseDto == null) {
            throw new CustomException(ErrorCode.NO_SUCH_ITEM);
        }

        // item image 이름들을 추출해 저장
        String lessorId = itemDetailResponseDto.getLessorId();
        List<String> itemNameImages = itemMapper.selectItemNameImagesByItemId(itemId, lessorId);
        itemDetailResponseDto.setItemImageNames(itemNameImages);

        // 로그인 한 유저가 해당 물품을 wishlist에 담아놨는가에 대한 정보 저장
        boolean isWishList = false;
        // 로그인 한 유저인 경우 / 안 한 유저는 무조건 false
        if(userId != null) {
            isWishList = itemMapper.isWishListItem(itemId, userId) == 1;
        }
        itemDetailResponseDto.setWishList(isWishList);

        // 게시물 조회수 증가
        itemMapper.updateViewCount(itemId);

        return itemDetailResponseDto;
    }

    @Override
    public ItemLessorInfoDto getLessorInfo(String lessorId) {
        ItemLessorInfoDto itemLessorInfoDto = itemMapper.selectItemLessorInfoByLessorId(lessorId);

        // lessorId가 존재하지 않을 때 (없는 판매자 정보에 접근할 때)
        if(itemLessorInfoDto == null) {
            throw new CustomException(ErrorCode.NO_SUCH_LESSOR);
        }

        return itemLessorInfoDto;
    }

    @Override
    public Pagination<ItemRetrieveResponseDto> getPagedLessorItems(String lessorId, int page, int size) {
        int offset = (page - 1) * size;
        List<ItemRetrieveResponseDto> lessorItems = itemMapper.selectLessorItemsByLessorId(lessorId, offset, size);
        int totalItems = itemMapper.countLessorItems(lessorId);

        // lessorId가 존재하지 않을 때 (없는 판매자 정보에 접근할 때)
        if(lessorItems == null) {
            throw new CustomException(ErrorCode.NO_SUCH_LESSOR);
        }

        return new Pagination<>(lessorItems, totalItems, page, size);
    }

    @Override
    public Pagination<TradeReviewDto> getPagedLessorReviews(String lessorId, int page, int size) {
        int offset = (page - 1) * size;
        List<TradeReviewDto> lessorReviews = itemMapper.selectTradeReviewsByLessorId(lessorId, offset, size);
        int totalReviews = itemMapper.countLessorReviews(lessorId);

        return new Pagination<>(lessorReviews, totalReviews, page, size);
    }

    @Override
    public Pagination<ArticleSimpleInfo> getPagedLessorArticles(String lessorId, int page, int size) {
        int offset = (page - 1) * size;
        List<ArticleSimpleInfo> articles = itemMapper.selectArticlesByLessorId(lessorId, offset, size);
        int totalArticles = itemMapper.countLessorArticles(lessorId);

        // 게시글을 한 개 이상 작성한 경우
        if(!articles.isEmpty()) {
            // articleId를 매핑했으니, 그 값을 토대로 article image를 저장
            for (ArticleSimpleInfo articleSimpleInfo : articles) {
                int articleId = articleSimpleInfo.getArticleId();
                List<String> articleImages = itemMapper.selectArticleImagesByArticleId(articleId);
                articleSimpleInfo.setArticleImages(articleImages);
            }
        }

        return new Pagination<>(articles, totalArticles, page, size);
    }

    @Transactional
    @Override
    public int like(String userId, String itemId) {
        return itemMapper.insertWishList(userId, itemId);
    }

    @Transactional
    @Override
    public int likeCancel(String userId, String itemId) {
        return itemMapper.deleteWishList(userId, itemId);
    }
}
