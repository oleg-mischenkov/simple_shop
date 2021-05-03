package com.mishchenkov.entity;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class ServletTransportContainer {

    private final ServletRequest request;
    private final ServletResponse servletResponse;

    public ServletTransportContainer(ServletRequest request, ServletResponse servletResponse) {
        this.request = request;
        this.servletResponse = servletResponse;
    }

    public ServletRequest getRequest() {
        return request;
    }

    public ServletResponse getServletResponse() {
        return servletResponse;
    }

    public HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) request;
    }

    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) servletResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServletTransportContainer that = (ServletTransportContainer) o;
        return Objects.equals(request, that.request) && Objects.equals(servletResponse, that.servletResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request, servletResponse);
    }

    @Override
    public String toString() {
        return "ServletTransportContainer{" +
                "request=" + request +
                ", servletResponse=" + servletResponse +
                '}';
    }
}
