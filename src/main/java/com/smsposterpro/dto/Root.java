package com.smsposterpro.dto;

import lombok.Data;

import java.util.List;

@Data
public class Root <T>{
    private int total;

    private int totalNotFiltered;

    private List<T> rows;
}
