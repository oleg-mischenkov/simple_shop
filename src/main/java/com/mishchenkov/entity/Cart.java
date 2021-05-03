package com.mishchenkov.entity;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Simple class witch contains list of products
 */
public class Cart implements Serializable {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final long serialVersionUID = 1824357477058012008L;

    private final Map<Product,Integer> storage;

    public Cart() {
        logger.trace("create new Cart");
        storage = new LinkedHashMap<>();
    }

    /**
     * It adds product to the Cart. If it exists that product then entity count will be sum.
     *
     * @param product - some product
     */
    public void add(Product product) {
        int prodCount = Optional.ofNullable(storage.get(product)).orElse(0);
        storage.put(product, ++prodCount);
    }

    /**
     * It removes product from a cart.
     *
     * @param product - some product
     * @return - true: if cart had this product and false: if did`t
     */
    public boolean remove(Product product) {
        return Optional.ofNullable(storage.remove(product)).isPresent();
    }

    /**
     * It removes just one product. If product count equals 1 then method will remove this item.
     *
     * @param product - some product
     * @return - true: when product count was changed
     */
    public boolean removeOne(Product product) {
        int prodCount = Optional.ofNullable(storage.get(product)).orElse(0);

        if (prodCount != 1) {
            storage.put(product, --prodCount);
            return true;
        }
        return remove(product);
    }

    /**
     * It returns all products which have had in the cart.
     *
     * @return - List of the DataContainers. Each container consists of a product and a count of this product.
     */
    public List<DataContainer<Product,Integer>> getAll() {
        return storage.entrySet().stream()
                .map(entry -> new DataContainer<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * It removes all entities of storage
     *
     * @return - boolean as an success marker
     */
    public boolean clear() {
        logger.trace("clear all");
        storage.clear();
        return storage.size() == 0;
    }

    /**
     * This method shows product count
     *
     * @param product - some product
     * @return - - product count or 0 if that product doesn't exist
     */
    public int showValue(Product product) {
        return Optional.ofNullable(storage.get(product)).orElse(0);
    }

    /**
     * Simple method which returns sum of products.
     *
     * @return - int as sum of products
     */
    public int size() {
        return storage.values().stream()
                .reduce((integer, integer2) -> integer + integer2)
                .orElse(0);
    }

    /**
     * Method returns price in cents. For example: if total price equals $1.5 then it will return 150
     *
     * @return - price in cents
     */
    public int getTotalPrice() {
        return storage.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().getMoneyInCents() * entry.getValue())
                .reduce((i1, i2) -> i1 + i2).orElse(0);
    }

    /**
     * Calculates products price witch consists cents after point.
     *
     * @return - price as float
     */
    public float getTotalPriceAsFloat() {
        return ((float) getTotalPrice()) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return storage.equals(cart.storage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storage);
    }

    @Override
    public String toString() {
        return "Cart{"
                .concat(storage.toString())
                .concat("}");
    }
}
