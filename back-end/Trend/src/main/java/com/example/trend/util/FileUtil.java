package com.example.trend.util;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {

    private final Bucket bucket;

    @Autowired
    public FileUtil(Bucket bucket) {
        this.bucket = bucket;
    }

    /**
     * 여러 개의 파일을 저장하는 경우
     *
     * @param controller
     * @param id         <= 저장할 컨트롤러 항목의 id
     * @param files
     */
    public void saveFilesIntoStorage(String controller, int id, List<MultipartFile> files) {
        String blob = controller + "/" +
                id + "/";

        for (MultipartFile file : files) {
            try {
                // 파일 버켓에 저장
                String filePath = blob + file.getOriginalFilename();

                // 이미 존재하면 파일 삭제
                if (bucket.get(filePath) != null) {
                    bucket.get(filePath).delete();
                }

                bucket.create(filePath, file.getBytes(), file.getContentType());

            } catch (IOException e) {
                // custom exception 만들어야 할듯
                e.printStackTrace();
            }
        }
    }

    /**
     * 하나의 파일을 저장하는 경우
     * @param controller
     * @param id <= 저장할 컨트롤러 항목의 id
     * @param file
     */
    public void saveFileIntoStorage(String controller, int id, MultipartFile file) {
        saveFileIntoStorage(controller, String.valueOf(id), file);
    }

    public void saveFileIntoStorage(String controller, String id, MultipartFile file) {
        String blob = controller + "/" +
                id + "/";

        try {
            // 파일 버켓에 저장
            String filePath = blob + file.getOriginalFilename();

            // 이미 존재하면 파일 삭제
            if (bucket.get(filePath) != null) {
                bucket.get(filePath).delete();
            }

            bucket.create(filePath, file.getBytes(), file.getContentType());

        } catch (IOException e) {
            // custom exception 만들어야 할듯
            e.printStackTrace();
        }
    }

    public void deleteFiles(int itemId) {
        for (Blob blob : bucket.list(Storage.BlobListOption.prefix("items/" + itemId + "/")).iterateAll()) {
            blob.delete();
        }
    }
}
