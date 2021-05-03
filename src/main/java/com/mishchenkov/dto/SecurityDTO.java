package com.mishchenkov.dto;

import com.mishchenkov.xml.SecurityConstraint;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class SecurityDTO {

    private SecurityConstraint constraint;
    private ServletTransportDTO servletTransport;

    public SecurityDTO() {
        servletTransport = new ServletTransportDTO();
    }

    public SecurityDTO setConstraint(SecurityConstraint constraint) {
        this.constraint = constraint;
        return this;
    }

    public SecurityDTO setRequest(ServletRequest request) {
        servletTransport.setRequest(request);
        return this;
    }

    public SecurityDTO setResponse(ServletResponse response) {
        servletTransport.setResponse(response);
        return this;
    }

    public SecurityConstraint getConstraint() {
        return constraint;
    }

    public HttpServletRequest getHttpRequest() {
        return servletTransport.getHttpRequest();
    }

    public HttpServletResponse getHttpResponse() {
        return servletTransport.getHttpResponse();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityDTO that = (SecurityDTO) o;
        return Objects.equals(constraint, that.constraint) && Objects.equals(servletTransport, that.servletTransport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint, servletTransport);
    }

    @Override
    public String toString() {
        return "SecurityDTO{" +
                "constraint=" + constraint +
                ", servletTransport=" + servletTransport +
                '}';
    }
}
