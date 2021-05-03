package com.mishchenkov.tag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProductPaginationTag extends TagSupport {

    private final transient Logger logger = Logger.getLogger(ProductPaginationTag.class);

    private String url;
    private String[] page;
    private int position;
    private String prefix;

    public void setPosition(String position) {
        this.position = Integer.parseInt(position);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPage(String[] page) {
        this.page = page;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public int doStartTag() {
        StringBuilder stringBuilder = new StringBuilder();
        prepareUrls();

        stringBuilder
                .append("<nav aria-label='Page navigation'>")
                .append("<ul class='pagination pagination-sm'>")
                .append(addArrow(true))
                .append(generateHtmlItems())
                .append(addArrow(false))
                .append("</ul>")
                .append("</nav>");

        try {
            pageContext.getOut().write(stringBuilder.toString());
        } catch (IOException e) {
            logger.warn(e);
            throw new IllegalStateException("Can't build a ProductPaginationTag.", e);
        }

        return SKIP_BODY;
    }

    private String addArrow(boolean direction) {
        StringBuilder stringBuilder = new StringBuilder();
        if (page.length > 1) {
            if (direction && position != 0) {
                stringBuilder
                        .append("<li>")
                        .append(String.format("<a href='%s' aria-label='Previous'>", page[0]))
                        .append("<span aria-hidden='true'>&laquo;</span>")
                        .append("</a>")
                        .append("</li>");
            } else if (!direction && position != page.length - 1){
                stringBuilder
                        .append("<li>")
                        .append(String.format("<a href='%s' aria-label='Next'>", page[page.length - 1]))
                        .append("<span aria-hidden='true'>&raquo;</span>")
                        .append("</a>")
                        .append("</li>");
            }
        }
        return stringBuilder.toString();
    }

    private String generateHtmlItems() {
        return IntStream.range(0, page.length)
                .mapToObj(i -> String.format("<li><a href='%s' style='%s'>%d</a></li>", page[i], getStyle(i), (i + 1) ))
                .reduce(String::concat).orElse("*");
    }

    private String getStyle(int index) {
        if (position == index) {
            return "background-color: orange; color: white;";
        }
        return "";
    }

    private void prepareUrls() {
        url = Arrays.stream(url.split("&"))
                .filter(s -> !s.contains("count=") && !s.contains("position="))
                .collect(Collectors.joining("&"));

        Stream<String> urlStream = Arrays.stream(page);

        if (url.length() == 0) {
            page = urlStream
                    .map(s -> prefix.concat("?").concat(s.substring(1)))
                    .toArray(String[]::new);
        } else {
            page = urlStream
                    .map(s -> prefix.concat("?").concat(url).concat(s))
                    .toArray(String[]::new);
        }
    }
}
