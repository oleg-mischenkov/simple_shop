package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {

    private static final long serialVersionUID = -6022324112409372391L;

    private Status status;
    private String statusDescription;
    private Date orderDate;
    private User user;
    private ProductList productList;
    private Payment payment;
    private Delivery delivery;

    public Status getStatus() {
        return status;
    }

    public Order setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Order setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
        return this;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public ProductList getProductList() {
        return productList;
    }

    public Order setProductList(ProductList productList) {
        this.productList = productList;
        return this;
    }

    public Payment getPayment() {
        return payment;
    }

    public Order setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Order setDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(status, order.status)
                && Objects.equals(statusDescription, order.statusDescription)
                && Objects.equals(orderDate, order.orderDate)
                && Objects.equals(user, order.user)
                && Objects.equals(productList, order.productList)
                && Objects.equals(payment, order.payment)
                && Objects.equals(delivery, order.delivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status,
                statusDescription,
                orderDate,
                user,
                productList,
                payment,
                delivery);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderStatus=" + status +
                ", statusDescription='" + statusDescription + '\'' +
                ", orderDate=" + orderDate +
                ", user=" + user +
                ", productList=" + productList +
                ", payment=" + payment +
                ", delivery=" + delivery +
                '}';
    }
}
