package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Immutable list of products
 */
public final class ProductList implements Serializable {

    private static final long serialVersionUID = -8392944638140127591L;

    private final DataContainer<Product,Integer>[] storage;

    /**
     * Constructor creates empty ProductList
     */
    public ProductList() {
        this.storage = new DataContainer[0];
    }

    /**
     * Constructor creates an Object witch contains an array of the products.
     * Note: List<DataContainer<Product,Integer>> - Product is product, Integer is product count
     *
     * @param products - list of the product`s container.
     */
    public ProductList(List<DataContainer<Product,Integer>> products) {
        int prodSize = products.size();
        this.storage = new DataContainer[prodSize];
        IntStream.range(0, prodSize)
                .forEach(i -> storage[i] = Entities.copy(products.get(i)));
    }

    /**
     * It adds product with count and returns new ProductList instance.
     * Note: this method makes deep data copy
     *
     * @param product - any product
     * @param count - count of products
     * @return - new instance of the ProductList
     */
    public ProductList addProduct(Product product, Integer count) {
        List<DataContainer<Product,Integer>> list = getAll();
        list.add(new DataContainer<>(product, count));
        return new ProductList(list);
    }

    /**
     * Method returns deep storage copy
     *
     * @return - list all elements
     */
    public List<DataContainer<Product,Integer>> getAll() {
        return new ArrayList<>(
                Arrays.asList(Entities.copy(storage))
        );
    }

    /**
     * Method returns entity count
     *
     * @return - list size
     */
    public int size() {
        return storage.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductList that = (ProductList) o;
        return Arrays.equals(storage, that.storage);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(storage);
    }

    @Override
    public String toString() {
        return "ProductList{" +
                "storage=" + Arrays.toString(storage) +
                '}';
    }
}
