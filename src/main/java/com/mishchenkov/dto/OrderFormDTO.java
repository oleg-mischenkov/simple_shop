package com.mishchenkov.dto;

import java.io.Serializable;
import java.util.Objects;

public class OrderFormDTO implements Serializable {

    private static final long serialVersionUID = 5792157918010467799L;

    private String delivery;
    private String payment;
    private String card;

    public String getDelivery() {
        return delivery;
    }

    public OrderFormDTO setDelivery(String delivery) {
        this.delivery = delivery;
        return this;
    }

    public String getPayment() {
        return payment;
    }

    public OrderFormDTO setPayment(String payment) {
        this.payment = payment;
        return this;
    }

    public String getCard() {
        return card;
    }

    public OrderFormDTO setCard(String card) {
        this.card = card;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderFormDTO that = (OrderFormDTO) o;
        return Objects.equals(delivery, that.delivery)
                && Objects.equals(payment, that.payment)
                && Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delivery, payment, card);
    }

    @Override
    public String toString() {
        return "OrderFormDTO{" +
                "delivery='" + delivery + '\'' +
                ", payment='" + payment + '\'' +
                ", card='" + card + '\'' +
                '}';
    }
}
