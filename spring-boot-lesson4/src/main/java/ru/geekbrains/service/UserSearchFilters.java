package ru.geekbrains.service;

import java.math.BigDecimal;

public class UserSearchFilters {
    private String usernameFilter;
    private Integer minAgeFilter;
    private Integer maxAgeFilter;
    private Integer page;
    private Integer size;
    private String sortField;
    private String sortDirection;

    public String getUsernameFilter() {
        return usernameFilter;
    }

    public void setUsernameFilter(String usernameFilter) {
        this.usernameFilter = usernameFilter;
    }

    public Integer getMinAgeFilter() {
        return minAgeFilter;
    }

    public void setMinAgeFilter(Integer minAgeFilter) {
        this.minAgeFilter = minAgeFilter;
    }

    public Integer getMaxAgeFilter() {
        return maxAgeFilter;
    }

    public void setMaxAgeFilter(Integer maxAgeFilter) {
        this.maxAgeFilter = maxAgeFilter;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
