package com.github.jschmidt10.soccerclub;

import com.google.common.base.Preconditions;

import java.util.LinkedList;
import java.util.List;

/**
 * The Dispatcher configured for the soccerclub application.
 */
public class SoccerclubApp extends Dispatcher {

    public SoccerclubApp() {
        this(System.getenv("TABLE"), System.getenv("NOTIFICATION_QUEUE"));
    }

    public SoccerclubApp(String table, String notificationQueue) {
        super(createHandlers(table, notificationQueue));
    }

    private static List<LambdaHandler> createHandlers(String table, String notificationQueue) {
        Preconditions.checkNotNull(table, "Must define a DynamoDB table to use.");
        Preconditions.checkNotNull(notificationQueue, "Must define a notification queue to use.");

        List<LambdaHandler> handlers = new LinkedList<>();
        handlers.add(new NotificationHandler(new DynamoNotificationStream(table, notificationQueue)));
        handlers.add(new StatusHandler());

        return handlers;
    }
}
