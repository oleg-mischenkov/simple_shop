package com.mishchenkov.xml;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.List;

public class ConstraintParser implements XMLParser<List<SecurityConstraint>> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public List<SecurityConstraint> parse(String xmlPath) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            SecurityHandler handler = new SecurityHandler();
            reader.setContentHandler(handler);
            reader.parse(xmlPath);

            return handler.getConstraintList();
        } catch (IOException | SAXException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }
}
