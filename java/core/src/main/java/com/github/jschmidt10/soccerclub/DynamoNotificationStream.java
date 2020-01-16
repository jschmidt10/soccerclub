package com.github.jschmidt10.soccerclub;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

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

    private final String table;
    private final AmazonDynamoDB dynamo;
    private final NotificationMapper mapper;

    private final Set<Subscription> subscriptions = new HashSet<>();

    public DynamoNotificationStream(String table, String queue) {
        Objects.requireNonNull(table, "Must pass in a table");
        Objects.requireNonNull(queue, "Must pass in a queue");

        this.dynamo = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        this.table = table;
        this.mapper = new NotificationMapper(table, new SimpleAttr(queue));
    }

    @Override
    public void push(Notification notification) {
        dynamo.putItem(table, mapper.toAttributes(notification));
        subscriptions.forEach(s -> s.notify(notification));
    }

    @Override
    public List<Notification> get(long since) {
        QueryResult result = dynamo.query(mapper.queryByTimeRange(since, System.currentTimeMillis()));

        return Optional.ofNullable(result.getItems())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(mapper::fromAttributes)
                .collect(Collectors.toList());
    }

    @Override
    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    @Override
    public boolean removeSubscription(Subscription subscription) {
        return subscriptions.remove(subscription);
    }

    /**
     * Deletes a notification
     * @param notification The notification to delete
     */
    public void deleteNotification(Notification notification) {
        dynamo.deleteItem(table, mapper.toKeyAttributes(notification));
    }

    @Override
    public void close() throws Exception {
        dynamo.shutdown();
    }
}