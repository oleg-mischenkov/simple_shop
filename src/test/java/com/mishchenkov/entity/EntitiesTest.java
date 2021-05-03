package com.mishchenkov.entity;

import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void shouldDeepClone_copyTest() {
        //given
        Product product1 = Product.getBuilder().setTitle("Prod 1").setPrice(new Money(100)).build();
        Product product2 = product1;

        //when
        boolean referenceMarker = product1 == product2;
        product2 = Entities.copy(product1);
        boolean equalsMarker = product1 == product2;

        //then
        Assert.assertTrue(referenceMarker);
        Assert.assertFalse(equalsMarker);
    }
}