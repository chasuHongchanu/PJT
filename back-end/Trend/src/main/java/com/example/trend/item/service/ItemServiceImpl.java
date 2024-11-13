package com.example.trend.item.service;

import com.example.trend.exception.CustomException;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.repository.ItemRepository;
import com.example.trend.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public int regist(ItemRegistRequestDto itemRegistDto) throws  CustomException{
        int result = itemRepository.regist(itemRegistDto);
        // itemId를 반환받아 이미지는 storage의 item/user/itemId/ 경로에 저장
        String itemId = itemRegistDto.getItemId();
        String userId = itemRegistDto.getUserId();
        List<MultipartFile> files = itemRegistDto.getItemImages();
        
        // storage에 이미지 저장
        fileUtil.saveFileIntoStorage(itemId, userId, "item", files);

        // TODO: 시작일이 종료일보다 늦을 때 커스텀 예외 발생

        return result;
    }
}
