package com.reggie.entity;

import java.util.List;

public class PageResult<T> {
    private Integer total;
    private List<T> records;

    public PageResult(Integer total, List<T> list) {
        this.total = total;
        this.records = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
