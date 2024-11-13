package com.example.trend.item.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ItemRegistRequestDto {
    private String itemName;
    private List<MultipartFile> itemImages;
    private int itemPrice;
    private String itemCategory;
    private String itemAddress;
    private String itemContent;
    private String availableRentalStartDate;
    private String availableRentalEndDate;

    public ItemRegistRequestDto(String itemName, List<MultipartFile> itemImages, int itemPrice, String itemCategory, String itemAddress, String itemContent, String availableRentalStartDate, String availableRentalEndDate) {
        this.itemName = itemName;
        this.itemImages = itemImages;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemAddress = itemAddress;
        this.itemContent = itemContent;
        this.availableRentalStartDate = availableRentalStartDate;
        this.availableRentalEndDate = availableRentalEndDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<MultipartFile> getItemImages() {
        return itemImages;
    }

    public void setItemImages(List<MultipartFile> itemImages) {
        this.itemImages = itemImages;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getAvailableRentalStartDate() {
        return availableRentalStartDate;
    }

    public void setAvailableRentalStartDate(String availableRentalStartDate) {
        this.availableRentalStartDate = availableRentalStartDate;
    }

    public String getAvailableRentalEndDate() {
        return availableRentalEndDate;
    }

    public void setAvailableRentalEndDate(String availableRentalEndDate) {
        this.availableRentalEndDate = availableRentalEndDate;
    }
}
