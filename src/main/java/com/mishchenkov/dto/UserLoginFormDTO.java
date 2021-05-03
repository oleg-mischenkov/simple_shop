package com.mishchenkov.dto;

import java.io.Serializable;
import java.util.Objects;

public class UserLoginFormDTO implements Serializable {

    private static final long serialVersionUID = 1480582149684245213L;

    private String mail;
    private transient String password;

    public String getMail() {
        return mail;
    }

    public UserLoginFormDTO setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLoginFormDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginFormDTO that = (UserLoginFormDTO) o;
        return mail.equals(that.mail) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail, password);
    }

    @Override
    public String toString() {
        return "UserLoginFormDTO{" +
                "mail='" + mail + '\'' +
                ", password.length='" + password.length() + '\'' +
                '}';
    }
}
