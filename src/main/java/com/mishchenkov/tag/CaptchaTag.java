package com.mishchenkov.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class CaptchaTag extends TagSupport {

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<img src=\"captcha.jpg?hidden=" ).append(value).append("\" style=\"margin-bottom: 10px\"/>")
                .append("<input class=\"f-captcha\" type=\"text\" placeholder=\"enter image code\" style=\"min-width: 150px\" name=\"captcha\"/>")
                .append("<input type=\"hidden\" name=\"hidden\" value=\"").append(value).append("\">");

        try {
            pageContext.getOut().write(stringBuilder.toString());
        } catch (IOException e) {
            throw new JspException("Can't build a CaptchaTag.", e);
        }

        return SKIP_BODY;
    }
}
