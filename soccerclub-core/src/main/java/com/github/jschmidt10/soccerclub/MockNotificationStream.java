package com.github.jschmidt10.soccerclub;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Stores notification in memory only. Just for testing.
 */
public class MockNotificationStream implements NotificationStream {

    private SortedSet<Notification> notifications = new TreeSet<>();
    private List<Subscription> subscriptions = new LinkedList<>();

    @Override
    public void push(Notification notification) {
        subscriptions.forEach(s -> s.notify(notification));
        notifications.add(notification);
    }

    @Override
    public List<Notification> get(long since) {
        return notifications.stream().filter(n -> n.getTimestamp() >= since).collect(Collectors.toList());
    }

    @Override
    public void addSubscrition(Subscription subscription) {
        subscriptions.add(subscription);
    }

    @Override
    public boolean removeSubscrition(Subscription subscription) {
        return subscriptions.remove(subscription);
    }
}
