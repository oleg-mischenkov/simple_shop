package com.mishchenkov.servlet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class WriterCacheHttpResponseWrapper extends HttpServletResponseWrapper {

    private boolean isCallPrintWriter;

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final PrintWriter printWriter;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response the {@link javax.servlet.http.HttpServletResponse} to be wrapped.
     * @throws IllegalArgumentException if the response is null
     */
    public WriterCacheHttpResponseWrapper(HttpServletResponse response) {
        super(response);
        byteArrayOutputStream = new ByteArrayOutputStream();
        printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_16));
    }

    public boolean isCallPrintWriter() {
        return isCallPrintWriter;
    }

    @Override
    public PrintWriter getWriter() {
        isCallPrintWriter = true;
        return printWriter;
    }

    public HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) super.getResponse();
    }

    public byte[] getWroteContent() {
        printWriter.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String toString() {
        printWriter.flush();
        return byteArrayOutputStream.toString();
    }
}
