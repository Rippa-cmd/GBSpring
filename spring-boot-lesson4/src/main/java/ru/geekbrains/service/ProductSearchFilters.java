package ru.geekbrains.service;

import java.math.BigDecimal;

public class ProductSearchFilters {
    private String productNameFilter;
    private BigDecimal minCostFilter;
    private BigDecimal maxCostFilter;
    private Integer page;
    private Integer size;
    private String sortField;
    private String sortDirection;

    public String getProductNameFilter() {
        return productNameFilter;
    }

    public void setProductNameFilter(String productNameFilter) {
        this.productNameFilter = productNameFilter;
    }

    public BigDecimal getMinCostFilter() {
        return minCostFilter;
    }

    public void setMinCostFilter(BigDecimal minCostFilter) {
        this.minCostFilter = minCostFilter;
    }

    public BigDecimal getMaxCostFilter() {
        return maxCostFilter;
    }

    public void setMaxCostFilter(BigDecimal maxCostFilter) {
        this.maxCostFilter = maxCostFilter;
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
