package com.github.jschmidt10.soccerclub;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A subscription that saves notifications in a list. Just for testing.
 */
public class MockSubscription implements Subscription {

    private SortedSet<Notification> notifications = new TreeSet<>();

    @Override
    public void notify(Notification notification) {
        notifications.add(notification);
    }

    public SortedSet<Notification> getNotifications() {
        return notifications;
    }
}
