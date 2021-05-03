package com.mishchenkov.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductFilterFormDTO implements Serializable {

    private static final long serialVersionUID = 8498454993868430667L;

    private String minPrice;
    private String maxPrice;
    private String productTitle;
    private String manufacture;
    private String productType;

    public String getMinPrice() {
        return minPrice;
    }

    public ProductFilterFormDTO setMinPrice(String minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public ProductFilterFormDTO setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public ProductFilterFormDTO setProductTitle(String productTitle) {
        this.productTitle = productTitle;
        return this;
    }

    public String getManufacture() {
        return manufacture;
    }

    public ProductFilterFormDTO setManufacture(String manufacture) {
        this.manufacture = manufacture;
        return this;
    }

    public String getProductType() {
        return productType;
    }

    public ProductFilterFormDTO setProductType(String productType) {
        this.productType = productType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductFilterFormDTO that = (ProductFilterFormDTO) o;
        return minPrice.equals(that.minPrice) && maxPrice.equals(that.maxPrice) && productTitle.equals(that.productTitle) && manufacture.equals(that.manufacture) && productType.equals(that.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minPrice, maxPrice, productTitle, manufacture, productType);
    }

    @Override
    public String toString() {
        return "ProductFilterFormDTO{" +
                "minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
