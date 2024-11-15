package com.example.trend.item.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.mapper.ItemMapper;
import com.example.trend.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemMapper itemMapper;
    private final FileUtil fileUtil;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, FileUtil fileUtil) {
        this.itemMapper = itemMapper;
        this.fileUtil = fileUtil;
    }

    @Transactional
    @Override
    public int regist(ItemRegistRequestDto itemRegistDto) {
        // 파일 이름을 추출하여 itemImageNames 리스트에 추가
        List<String> itemImageNames = itemRegistDto.getItemImages().stream()
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());
        itemRegistDto.setItemImageNames(itemImageNames);

        // db에 물품 정보 insert
        int result = itemMapper.insertItem(itemRegistDto);
        // itemId를 반환받아 이미지는 storage의 userId/item/itemId/ 경로에 저장
        int itemId = itemRegistDto.getItemId();
        System.out.println("###########" + itemId);
        String userId = itemRegistDto.getUserId();

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
            fileUtil.saveFileIntoStorage(userId, itemId,"item", files);
        }

        // db에 물품 이미지 이름 정보 insert
        for(String itemImageName: itemImageNames) {
            itemMapper.insertItemImageName(itemId, itemImageName);
        }

        // 물품 등록한 유저 활동점수 증가
        itemMapper.updateUserActivityScore(userId);

        // TODO: 입력받은 지역을 토대로 지역 JSON을 이용해 위/경도 추출

        return result;
    }
}
