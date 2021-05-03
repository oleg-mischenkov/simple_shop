package com.mishchenkov.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class LanguageSelectorTag extends TagSupport {

    private String language;
    private String[] langList;

    @Override
    public int doStartTag() throws JspException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<select id=\"lang-select\" class=\"input-sm\" style=\"margin-left:12px;margin-top:10px;\">");

        for(String element: langList) {
            stringBuilder.append(
                    String.format(
                            "<option value='%s' %s>%s</option>",
                            element,
                            language.equals(element) ? "selected" : "",
                            element
                    )
            );
        }

        stringBuilder.append("</select>");

        try {
            pageContext.getOut().write(stringBuilder.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLangList(String[] langList) {
        this.langList = langList;
    }
}
