package com.example.trend.util;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

        // 해당 경로가 이미 존재하면 폴더 삭제
        deleteFiles(controller, String.valueOf(id));
        for (MultipartFile file : files) {
            try {
                // 파일 버켓에 저장
                String filePath = blob + file.getOriginalFilename();
                bucket.create(filePath, file.getBytes(), file.getContentType());

            } catch (IOException e) {
                // custom exception 만들어야 할듯
                e.printStackTrace();
                throw new CustomException(ErrorCode.FAIL_TO_SAVE_IMAGE);
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

    public String saveFileIntoStorage(String controller, String id, MultipartFile file) {
        String blob = controller + "/" +
                id + "/";

        try {
            // 파일 이름 설정
            String fileName = file.getOriginalFilename();
            // 파일 버켓에 저장
            String filePath = blob + fileName;

            // 이미 존재하면 파일 삭제
            deleteFiles(controller, id);

            bucket.create(filePath, file.getBytes(), file.getContentType());
            return filePath;
        } catch (IOException e) {
            // custom exception 만들어야 할듯
            e.printStackTrace();
            throw new CustomException(ErrorCode.FAIL_TO_SAVE_IMAGE);
        }
    }

    public void deleteFiles(String controller, String id) {
        // Prefix로 지정된 경로의 Blob 리스트 가져오기
        Iterable<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(controller + "/" + id + "/")).iterateAll();

        // 경로에 파일이 있는지 확인
        boolean hasFiles = blobs.iterator().hasNext();

        if (!hasFiles) {
            System.out.println("No files found in the specified path.");
            return; // 경로에 파일이 없으면 삭제 작업을 종료
        }

        // 파일이 있다면 삭제 진행
        for (Blob blob : blobs) {
            blob.delete();
            System.out.println("Deleted file: " + blob.getName());
        }
    }
}
