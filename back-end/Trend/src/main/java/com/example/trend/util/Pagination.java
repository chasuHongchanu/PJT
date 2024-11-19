package com.example.trend.util;

import java.util.List;

public class Pagination<T> {
    private List<T> data;
    private int totalItems;
    private int currentPage;
    private int totalPages;

    public Pagination(List<T> data, int totalItems, int currentPage, int pageSize) {
        this.data = data;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }

    // Getter
    public List<T> getData() {
        return data;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
/*
<<<컨트롤러 예시>>>
@GetMapping("/api/entities")
public ResponseEntity<ApiResponse<Pagination<MyEntity>>> getEntities(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
    Pagination<MyEntity> pagedData = myService.getPagedList(page, size);
    return ResponseEntity.ok(new ApiResponse<>("Success", pagedData));
}

<<<서비스 예시>>>
@Service
public class MyService {
    private final MyMapper myMapper;

    public MyService(MyMapper myMapper) {
        this.myMapper = myMapper;
    }

    public Pagination<MyEntity> getPagedList(int page, int size) {
        int offset = (page - 1) * size;
        List<MyEntity> data = myMapper.findAllWithPagination(offset, size);
        int totalItems = myMapper.countAll(); // 총 데이터 수
        return new Pagination<>(data, totalItems, page, size);
    }
}

<<<매퍼 예시>>>
@Mapper
public interface MyMapper {
    @Select("SELECT * FROM my_table LIMIT #{size} OFFSET #{offset}")
    List<MyEntity> findAllWithPagination(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM my_table")
    int countAll();
}
 */
