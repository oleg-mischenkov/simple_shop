package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {

    private static final long serialVersionUID = -170894963140107426L;

    private String title;
    private String sku;
    private Money price;
    private ProductType productType;
    private Manufacture manufacture;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Manufacture getManufacture() {
        return manufacture;
    }

    public void setManufacture(Manufacture manufacture) {
        this.manufacture = manufacture;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(title, product.title)
                && Objects.equals(sku, product.sku)
                && Objects.equals(price, product.price)
                && Objects.equals(productType, product.productType)
                && Objects.equals(manufacture, product.manufacture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, sku, price, productType, manufacture);
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", productType=" + productType +
                ", manufacture=" + manufacture +
                '}';
    }

    public static Builder getBuilder() {
        return new Product().new Builder();
    }

    public class Builder {

        private Builder(){}

        public Builder setTitle(String title) {
            Product.this.title = title;
            return this;
        }

        public Builder setSku(String sku) {
            Product.this.sku = sku;
            return this;
        }

        public Builder setPrice(Money price) {
            Product.this.price = price;
            return this;
        }

        public Builder setProductType(ProductType productType) {
            Product.this.productType = productType;
            return this;
        }

        public Builder setManufacture(Manufacture manufacture) {
            Product.this.manufacture = manufacture;
            return this;
        }

        public Product build() {
            return Product.this;
        }
    }
}