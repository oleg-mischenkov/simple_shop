package com.mishchenkov.dto;

import com.mishchenkov.entity.Status;

import java.io.Serializable;
import java.util.Objects;

public class StatusDTO implements Serializable {

    private static final long serialVersionUID = -2651033425391352811L;

    private final int id;
    private final Status status;

    public StatusDTO(int id, Status status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusDTO that = (StatusDTO) o;
        return id == that.id && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "OrderStatusDTO{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
