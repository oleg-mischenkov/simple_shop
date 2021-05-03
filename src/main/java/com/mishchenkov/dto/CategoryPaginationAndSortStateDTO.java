package com.mishchenkov.dto;

import java.io.Serializable;
import java.util.Objects;

public class CategoryPaginationAndSortStateDTO implements Serializable {

    private static final long serialVersionUID = -8152916612783592900L;

    private String count;
    private String position;
    private String sortBy;

    public String getCount() {
        return count;
    }

    public CategoryPaginationAndSortStateDTO setCount(String count) {
        this.count = count;
        return this;
    }

    public String getPosition() {
        return position;
    }

    public CategoryPaginationAndSortStateDTO setPosition(String position) {
        this.position = position;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public CategoryPaginationAndSortStateDTO setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryPaginationAndSortStateDTO that = (CategoryPaginationAndSortStateDTO) o;
        return count.equals(that.count) && position.equals(that.position) && sortBy.equals(that.sortBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, position, sortBy);
    }

    @Override
    public String toString() {
        return "CategoryPaginationAndSortStateDTO{" +
                "count='" + count + '\'' +
                ", position='" + position + '\'' +
                ", sortBy='" + sortBy + '\'' +
                '}';
    }
}
