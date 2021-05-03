package com.mishchenkov.tag;

import com.mishchenkov.entity.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class LoginTag extends TagSupport {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder stringBuilder = new StringBuilder();

        if (user == null) {
            stringBuilder
                    .append("<div class=\"login-form\">")
                    .append("<form id=\"form-login\" action=\"login\" method=\"post\">")
                    .append("<input class=\"f-email\" type=\"email\" placeholder=\"Email Address\" name=\"email\"/>")
                    .append("<input class=\"f-psw\" type=\"password\" placeholder=\"Password\" name=\"password\"/>")
                    .append("<button type=\"submit\" class=\"btn btn-secondary active\" disabled>Login</button>")
                    .append("</form>")
                    .append("<div>")
                    .append("<a href=\"registration\" class=\"btn btn-default btn-a\" role=\"button\">Registration</a>")
                    .append("</div>")
                    .append("</div>");
        } else {
            stringBuilder
                    .append("<div>")
                    .append("<span><li class=\"fa fa-user\"></li>").append(user.getName()).append("</span>")
                    .append("</div>")
                    .append("<div>")
                    .append("<a href=\"logout\" class=\"btn btn-default btn-a\" role=\"button\">logout</a>")
                    .append("</div>");
        }

        try {
            pageContext.getOut().write(stringBuilder.toString());
        } catch (IOException e) {
            throw new JspException("Can't build a CaptchaTag.", e);
        }

        return SKIP_BODY;
    }
}
