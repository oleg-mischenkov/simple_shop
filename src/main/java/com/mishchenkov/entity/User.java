package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = -3939682121996976523L;

    private String name;
    private String secondName;
    private String email;
    private Role role;
    private transient String password;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(secondName, user.secondName) && Objects.equals(email, user.email) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, secondName, email, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public int compareTo(User o) {
        if (o != null) {
            return email.compareTo(o.email);
        }
        return -1;
    }

    public static Builder getBuilder() {
        return new User().new Builder();
    }

    public class Builder {

        private Builder() {}

        public Builder setName(String name) {
            User.this.name = name;
            return this;
        }

        public Builder setSecondName(String secondName) {
            User.this.secondName = secondName;
            return this;
        }

        public Builder setEmail(String email) {
            User.this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            User.this.password = password;
            return this;
        }

        public Builder setRole(Role role) {
            User.this.role = role;
            return this;
        }

        public User build() {
            return User.this;
        }
    }
}
