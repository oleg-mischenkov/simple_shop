package com.mishchenkov.dto;

import java.io.Serializable;
import java.util.Objects;

public class OrderStatusDTO implements Serializable {

    private static final long serialVersionUID = 1686131114386208285L;

    private String description;
    private long statusId;
    private long orderId;

    public String getDescription() {
        return description;
    }

    public OrderStatusDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public long getStatusId() {
        return statusId;
    }

    public OrderStatusDTO setStatusId(long statusId) {
        this.statusId = statusId;
        return this;
    }

    public long getOrderId() {
        return orderId;
    }

    public OrderStatusDTO setOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusDTO that = (OrderStatusDTO) o;
        return statusId == that.statusId
                && orderId == that.orderId
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, statusId, orderId);
    }

    @Override
    public String toString() {
        return "OrderStatusDTO{" +
                "description='" + description + '\'' +
                ", statusId=" + statusId +
                ", orderId=" + orderId +
                '}';
    }
}
