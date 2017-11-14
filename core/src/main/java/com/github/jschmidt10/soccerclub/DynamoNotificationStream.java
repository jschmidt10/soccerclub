package com.github.jschmidt10.soccerclub;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A NotificationStream backed by DynamoDB.
 * <p>
 * Data Model:
 * <pre>
 *     HashKey = queue
 *     SortKey = timestamp
 * </pre>
 */
public class DynamoNotificationStream implements NotificationStream {

    private final static Map<String, String> ATTRIBUTE_NAMES;

    static {
        Map<String, String> names = new TreeMap<>();
        names.put("#TS", "timestamp");

        ATTRIBUTE_NAMES = Collections.unmodifiableMap(names);
    }

    private final NotificationSerDe serde = new NotificationSerDe();

    private final String table;
    private final AttributeValue queue;
    private final AmazonDynamoDB dynamo;

    private final Set<Subscription> subscriptions = new HashSet<>();

    public DynamoNotificationStream(String table, String queue) {
        Preconditions.checkNotNull(table, "Must pass in a table");
        Preconditions.checkNotNull(queue, "Must pass in a queue");

        this.dynamo = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        this.table = table;
        this.queue = new AttributeValue().withS(queue);
    }

    @Override
    public void push(Notification notification) {
        dynamo.putItem(putRequest(notification));
        subscriptions.forEach((s) -> s.notify(notification));
    }

    private PutItemRequest putRequest(Notification notification) {
        PutItemRequest request = new PutItemRequest();
        request.setTableName(table);

        request.addItemEntry("queue", queue);
        request.addItemEntry("timestamp", new AttributeValue().withN(String.valueOf(notification.getTimestamp())));
        request.addItemEntry("payload", new AttributeValue().withB(serde.toBytes(notification)));

        return request;
    }

    @Override
    public List<Notification> get(long since) {
        QueryResult result = dynamo.query(queryRequest(since, System.currentTimeMillis()));

        return Optional.ofNullable(result.getItems())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(i -> serde.fromBytes(i.get("payload").getB()))
                .collect(Collectors.toList());
    }

    private QueryRequest queryRequest(long from, long to) {
        // TODO: reverse sort timestamps and cap how many messages we pull

        Map<String, AttributeValue> attributeValues = new TreeMap<>();
        attributeValues.put(":queue", queue);
        attributeValues.put(":start", new AttributeValue().withN(String.valueOf(from)));
        attributeValues.put(":end", new AttributeValue().withN(String.valueOf(to)));

        QueryRequest request = new QueryRequest();

        request.setTableName(table);
        request.setKeyConditionExpression("queue = :queue AND #TS BETWEEN :start AND :end");
        request.setExpressionAttributeNames(ATTRIBUTE_NAMES);
        request.setExpressionAttributeValues(attributeValues);

        return request;
    }

    @Override
    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    @Override
    public boolean removeSubscription(Subscription subscription) {
        return subscriptions.remove(subscription);
    }
}