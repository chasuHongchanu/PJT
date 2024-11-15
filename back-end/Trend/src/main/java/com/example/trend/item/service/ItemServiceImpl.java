package com.example.trend.item.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.repository.ItemRepository;
import com.example.trend.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final FileUtil fileUtil;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, FileUtil fileUtil) {
        this.itemRepository = itemRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public int regist(ItemRegistRequestDto itemRegistDto) {
        int result = itemRepository.regist(itemRegistDto);
        // itemId를 반환받아 이미지는 storage의 item/user/itemId/ 경로에 저장
        String itemId = itemRegistDto.getItemId();
        String userId = itemRegistDto.getUserId();
        List<MultipartFile> files = itemRegistDto.getItemImages();

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
        if(!(files.size() == 1 && files.get(0).isEmpty())) {
            fileUtil.saveFileIntoStorage(itemId, userId, "item", files);
        }

        // TODO: 입력받은 지역을 토대로 지역 JSON을 이용해 위/경도 추출

        return result;
    }
}
