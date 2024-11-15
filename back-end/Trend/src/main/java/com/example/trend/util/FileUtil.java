package com.example.trend.util;

import com.google.cloud.storage.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class FileUtil {

    private final Bucket bucket;

    @Autowired
    public FileUtil(Bucket bucket) {
        this.bucket = bucket;
    }

    public void saveFileIntoStorage(String userId, int id, String controller, List<MultipartFile> files) {
        String blob = userId + "/" +
                    controller + "/" +
                    id + "/";

        for(MultipartFile file: files) {
            try {
                // 파일 버켓에 저장
                String filePath = blob + file.getOriginalFilename();

                // 이미 존재하면 파일 삭제
                if(bucket.get(filePath) != null) {
                    bucket.get(filePath).delete();
                }

                bucket.create(filePath, file.getBytes(), file.getContentType());

            } catch (IOException e) {
                // custom exception 만들어야 할듯
                e.printStackTrace();
            }
        }
    }
}
