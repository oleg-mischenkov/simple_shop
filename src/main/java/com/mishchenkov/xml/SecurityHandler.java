package com.mishchenkov.xml;

import com.mishchenkov.entity.Entities;
import com.mishchenkov.entity.Role;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SecurityHandler extends DefaultHandler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String TAG_CONSTRAINT = "constraint";
    private static final String TAG_URL_PATTERN = "url-pattern";
    private static final String TAG_ROLE = "role";

    private final List<SecurityConstraint> constraintList = new ArrayList<>();
    private final StringBuilder currentTag = new StringBuilder();

    private final Map<String,Consumer<String>> startTagMap;
    private final Map<String,Consumer<String>> endTagMap;

    private SecurityConstraint constraint;
    private String tempTagValue;

    public SecurityHandler() {
        logger.trace("INIT");
        startTagMap = new TreeMap<>();
        endTagMap = new TreeMap<>();

        initStartTagMap();
        initEndTagMap();
    }

    private void initEndTagMap() {
        endTagMap.put(TAG_CONSTRAINT, endConstraintTag);
        endTagMap.put(TAG_URL_PATTERN, endUrlPatternTag);
        endTagMap.put(TAG_ROLE, endRoleTag);
    }

    private void initStartTagMap() {
        startTagMap.put(TAG_CONSTRAINT, startConstraintTag.andThen(setCurrentTag));
        startTagMap.put(TAG_URL_PATTERN, setCurrentTag);
        startTagMap.put(TAG_ROLE, setCurrentTag);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        logger.trace(localName);
        Optional.ofNullable(startTagMap.get(localName)).orElseGet(() -> logger::trace).accept(localName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        logger.trace(localName);
        Optional.ofNullable(endTagMap.get(localName)).orElseGet(() -> logger::trace).accept(tempTagValue);
        tempTagValue = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        logger.trace(new String(ch, start, length));
        setTagValue.accept(new String(ch, start, length));
    }

    private final Consumer<String> setCurrentTag = this::refreshTag;
    private final Consumer<String> startConstraintTag = tagName -> constraint = new SecurityConstraint();

    private final Consumer<String> endConstraintTag = s -> constraintList.add(constraint);
    private final Consumer<String> endUrlPatternTag = tagValue -> constraint.setPath(tagValue);
    private final Consumer<String> endRoleTag = tagValue -> constraint.setRoles(new Role(tagValue));

    private final Consumer<String> setTagValue = value -> tempTagValue = value;

    public List<SecurityConstraint> getConstraintList() {
        List<SecurityConstraint> result = constraintList.stream()
                .map(Entities::copy).collect(Collectors.toList());

        constraintList.clear();
        clearTag();
        refreshTag(null);

        return result;
    }

    private void clearTag() {
        IntStream.range(currentTag.length(), 0).forEach(currentTag::deleteCharAt);
    }

    private void refreshTag(String tagName) {
        clearTag();
        currentTag.append(tagName);
    }

}