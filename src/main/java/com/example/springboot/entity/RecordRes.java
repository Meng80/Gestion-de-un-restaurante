package com.example.springboot.entity;

import lombok.Data;

@Data
public class RecordRes extends Record{
    private String username;
    private String productName;
    private String categoryName;
}
