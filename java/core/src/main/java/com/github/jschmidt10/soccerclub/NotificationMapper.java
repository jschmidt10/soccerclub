package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Maps between notifications and Attribute/Values
 */
public class NotificationMapper {

    public final static String ATTR_QUEUE = "queue";
    public final static String ATTR_TIMESTAMP = "timestamp";
    public final static String ATTR_IS_SOCCER_ON = "isSoccerOn";
    public final static String ATTR_MESSAGE = "message";

    private final static String ALIAS_TIMESTAMP = "#TS";
    private final static String TIME_RANGE_QUERY = NotificationMapper.ATTR_QUEUE + " = :queue AND " + ALIAS_TIMESTAMP + " BETWEEN :start AND :end";
    private final static Map<String, String> QUERY_ALIASES;

    static {
        Map<String, String> names = new TreeMap<>();
        names.put(ALIAS_TIMESTAMP, ATTR_TIMESTAMP);
        QUERY_ALIASES = Collections.unmodifiableMap(names);
    }

    private final String table;
    private final AttributeValue queue;

    public NotificationMapper(String table, AttributeValue queue) {
        this.table = table;
        this.queue = queue;
    }

    public Notification fromAttributes(Map<String, AttributeValue> attributes) {
        long timestamp = getLong(attributes.get(ATTR_TIMESTAMP));
        String message = getString(attributes.get(ATTR_MESSAGE));
        boolean isSoccerOn = getBoolean(attributes.get(ATTR_IS_SOCCER_ON));

        return new Notification(timestamp, message, isSoccerOn);
    }

    private long getLong(AttributeValue value) {
        return Long.valueOf(value.getN());
    }

    private boolean getBoolean(AttributeValue value) {
        return value.getBOOL();
    }

    private String getString(AttributeValue value) {
        return value.getS();
    }

    public Map<String, AttributeValue> toKeyAttributes(Notification notification) {
        Map<String, AttributeValue> attributes = new TreeMap<>();

        attributes.put(ATTR_QUEUE, queue);
        attributes.put(ATTR_TIMESTAMP, new SimpleAttr(notification.getTimestamp()));

        return attributes;
    }

    public Map<String, AttributeValue> toAttributes(Notification notification) {
        Map<String, AttributeValue> attributes = toKeyAttributes(notification);

        attributes.put(ATTR_IS_SOCCER_ON, new SimpleAttr(notification.isSoccerOn()));
        attributes.put(ATTR_MESSAGE, new SimpleAttr(notification.getMessage()));

        return attributes;
    }

    public QueryRequest queryByTimeRange(long from, long to) {
        Map<String, AttributeValue> attributeValues = new TreeMap<>();
        attributeValues.put(":queue", queue);
        attributeValues.put(":start", new SimpleAttr(from));
        attributeValues.put(":end", new SimpleAttr(to));

        QueryRequest request = new QueryRequest();

        request.setTableName(table);
        request.setKeyConditionExpression(TIME_RANGE_QUERY);
        request.setExpressionAttributeNames(QUERY_ALIASES);
        request.setExpressionAttributeValues(attributeValues);

        return request;
    }
}
