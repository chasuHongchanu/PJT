package com.example.trend.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentItem {
    private int itemId;
    private String itemName;
    private String category;
    private String itemContent;
}
