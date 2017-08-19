package com.github.jschmidt10.soccerclub;

/**
 * A subscription to the soccerclub notifications.
 */
public interface Subscription {

    /**
     * Sends a notification to the subscriber.
     *
     * @param notification
     */
    void notify(Notification notification);
}
