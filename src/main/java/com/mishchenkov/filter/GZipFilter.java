package com.mishchenkov.filter;

import com.mishchenkov.servlet.WriterCacheHttpResponseWrapper;
import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

public class GZipFilter extends AbstractHttpFilter {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
       if (isGzipAble(request)) {
           response.setCharacterEncoding("UTF-16");
           WriterCacheHttpResponseWrapper wrapper = new WriterCacheHttpResponseWrapper(response);
           chain.doFilter(request, wrapper);

           if (wrapper.isCallPrintWriter() && isTextContent(wrapper.getHttpResponse())) {
               logger.trace("support PRINT");
               response.addHeader("Content-Encoding", "gzip");

               try(OutputStreamWriter streamWriter = new OutputStreamWriter(new GZIPOutputStream(response.getOutputStream()))) {
                   streamWriter.write(wrapper.toString());
               }
           }

       } else {
           logger.trace("no support");
           chain.doFilter(request, response);
       }
    }

    private boolean isTextContent(HttpServletResponse response) {
        return response.getContentType().contains(AppConstant.MIME_TEXT);
    }

    private boolean isGzipAble(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AppConstant.REQ_HEADER_COMPRESS)).orElse("").contains("gzip");
    }
}
