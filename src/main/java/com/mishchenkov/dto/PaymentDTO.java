package com.mishchenkov.dto;

import com.mishchenkov.entity.Payment;

import java.io.Serializable;
import java.util.Objects;

public class PaymentDTO implements Serializable {

    private static final long serialVersionUID = 1001298300841074430L;

    private final int id;
    private final Payment payment;

    public PaymentDTO(int id, Payment payment) {
        this.id = id;
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDTO that = (PaymentDTO) o;
        return id == that.id && Objects.equals(payment, that.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payment);
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "id=" + id +
                ", payment=" + payment +
                '}';
    }
}
