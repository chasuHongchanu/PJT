package com.example.trend.community.dto;

public class ArticleSearchRequestDto {
    private String keyword;
    private String startDate;
    private String endDate;
    private OrderBy orderBy;

    public void setKeyword(String keyword) {
        this.keyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword : null;
    }

    public void setStartDate(String startDate) {
        this.startDate = (startDate != null && !startDate.trim().isEmpty()) ? startDate : null;
    }

    public void setEndDate(String endDate) {
        this.endDate = (endDate != null && !endDate.trim().isEmpty()) ? endDate : null;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = (orderBy != null && !orderBy.trim().isEmpty()) ? OrderBy.valueOf(orderBy.toUpperCase()) : null;
    }
}
