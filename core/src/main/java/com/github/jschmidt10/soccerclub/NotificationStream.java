package com.github.jschmidt10.soccerclub;

import java.util.List;

/**
 * A stream of notifications.
 */
public interface NotificationStream {

    /**
     * Adds a new notification
     *
     * @param notification
     */
    void push(Notification notification);

    /**
     * Gets all the notifications back until the given timestamp.
     *
     * @param since The earliest timestamp to fetch
     * @return notifications
     */
    List<Notification> get(long since);

    /**
     * Adds a new subscriber to the notification stream.
     *
     * @param subscription
     */
    void addSubscription(Subscription subscription);

    /**
     * Removes a subscription from the notification stream.
     *
     * @param subscription
     * @return
     */
    boolean removeSubscription(Subscription subscription);
}
