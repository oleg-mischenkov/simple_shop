package com.mishchenkov.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

public class CartTest {

    @Test
    public void shouldAddTwoEqualsProduct_addTest() {
        //given
        Cart cart = new Cart();
        Product product1 = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(100)).build();
        Product product2 = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(100)).build();

        //when
        cart.add(product1);
        cart.add(product2);

        //then
        int expected = 2;
        int result = cart.getAll().get(0).getValue();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldRemoveProduct_removeTest() {
        //given
        Cart cart = new Cart();
        Product product = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(100)).build();
        cart.add(product);
        cart.add(Product.getBuilder().setTitle("Prod 2").setPrice(new Money(100)).build());
        cart.add(Product.getBuilder().setTitle("Prod 2").setPrice(new Money(100)).build());

        int prodCount = cart.size();

        //when
        cart.remove(Product.getBuilder().setTitle("Prod 2").setPrice(new Money(100)).build());

        //then
        int expected = 1;
        int result = cart.size();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldAddTreElementsWhenTwoOfThemIsEquals_getAllTest() {
        //given
        Cart cart = new Cart();
        Product product = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(100)).build();
        cart.add(product);
        cart.add(Product.getBuilder().setTitle("Prod 2").setPrice(new Money(100)).build());
        cart.add(Product.getBuilder().setTitle("Prod 2").setPrice(new Money(100)).build());

        //when
        List<DataContainer<Product, Integer>> cartAll = cart.getAll();

        //then
        int expected = 2;
        int result = cartAll.size();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void sizeTest() {
        //given
        int iteration = 10;
        Cart cart = new Cart();
        Product product = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(100)).build();

        //when
        IntStream.range(0, iteration).forEach(i -> cart.add(product));

        //then
        int result = cart.size();
        Assert.assertEquals(iteration, result);
    }

    @Test
    public void getTotalPrice() {
        //given
        int iteration = 10;
        int price = 134;
        Cart cart = new Cart();
        Product product = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(price)).build();
        IntStream.range(0, iteration).forEach(i -> cart.add(product));

        //when
        int result = cart.getTotalPrice();

        //then
        int expected = iteration * price;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void getTotalPriceAsFloat() {
        //given
        int iteration = 10;
        int price = 134;
        Cart cart = new Cart();
        Product product = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(price)).build();
        IntStream.range(0, iteration).forEach(i -> cart.add(product));

        //when
        float result = cart.getTotalPriceAsFloat();

        //then
        float expected = (iteration * price) / 100;
        Assert.assertEquals(expected, result, .4);
    }
}