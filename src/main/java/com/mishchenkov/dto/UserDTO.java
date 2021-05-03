package com.mishchenkov.dto;

import com.mishchenkov.entity.User;

import java.io.Serializable;
import java.util.Objects;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = -6979452917944759966L;

    private long key;
    private User user;
    private boolean sendEmail;

    public long getKey() {
        return key;
    }

    public UserDTO setKey(long key) {
        this.key = key;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserDTO setUser(User user) {
        this.user = user;
        return this;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public UserDTO setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return key == userDTO.key && sendEmail == userDTO.sendEmail && user.equals(userDTO.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, user, sendEmail);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "key=" + key +
                ", user=" + user +
                ", sendEmail=" + sendEmail +
                '}';
    }
}
