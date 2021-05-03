package com.mishchenkov.dto;

import com.mishchenkov.entity.Product;

import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 618352872721865489L;

    private long key;
    private Product product;

    public long getKey() {
        return key;
    }

    public ProductDTO setKey(long key) {
        this.key = key;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ProductDTO setProduct(Product product) {
        this.product = product;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return key == that.key && product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, product);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "key=" + key +
                ", product=" + product +
                '}';
    }
}
