package com.mishchenkov.xml;

import com.mishchenkov.entity.Role;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class SecurityConstraint implements Serializable {

    private static final long serialVersionUID = 211262303749535449L;

    private String path;
    private final Set<Role> roles;

    public SecurityConstraint() {
        roles = new LinkedHashSet<>();
    }

    public String getPath() {
        return path;
    }

    public SecurityConstraint setPath(String path) {
        this.path = path;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public SecurityConstraint setRoles(Role role) {
        roles.add(role);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityConstraint security = (SecurityConstraint) o;
        return Objects.equals(path, security.path) && Objects.equals(roles, security.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, roles);
    }

    @Override
    public String toString() {
        return "Security{" +
                "path='" + path + '\'' +
                ", roles=" + roles +
                '}';
    }
}
