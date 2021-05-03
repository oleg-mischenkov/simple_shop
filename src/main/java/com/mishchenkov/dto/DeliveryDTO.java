package com.mishchenkov.dto;

import com.mishchenkov.entity.Delivery;

import java.io.Serializable;
import java.util.Objects;

public class DeliveryDTO implements Serializable {

    private static final long serialVersionUID = 7354695234571469047L;

    private final int id;
    private final Delivery delivery;

    public DeliveryDTO(int id, Delivery delivery) {
        this.id = id;
        this.delivery = delivery;
    }

    public int getId() {
        return id;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryDTO that = (DeliveryDTO) o;
        return id == that.id && Objects.equals(delivery, that.delivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, delivery);
    }

    @Override
    public String toString() {
        return "DeliveryDTO{" +
                "id=" + id +
                ", delivery=" + delivery +
                '}';
    }
}
