package com.github.jschmidt10.soccerclub;

import java.util.List;

/**
 * Handles requests for fetching and storing notifications.
 */
public class NotificationHandler implements LambdaHandler {
    private final NotificationStream notificationStream;

    public NotificationHandler(NotificationStream notificationStream) {
        this.notificationStream = notificationStream;
    }

    @Override
    public boolean handlesPath(String path) {
        return path.startsWith("notification");
    }

    // Accepted HTTP requests
    //
    // GET /notification/{sinceTimestamp}
    @Override
    public LambdaProxyResponse handle(LambdaProxyRequest request) {
        long sinceTimestamp;
        try {
            sinceTimestamp = Long.parseLong(request.getPath().substring("notification/".length()));
        } catch (Exception e) {
            return new LambdaProxyResponse(Http.BAD_REQUEST, "You forgot to give us a timestamp with your notification request.");
        }

        List<Notification> notifications;
        try {
            notifications = notificationStream.get(sinceTimestamp);
        } catch (Exception e) {
            return new LambdaProxyResponse(Http.INTERNAL_ERROR, "An error occurred while fetching your notifications. Please try again.");
        }

        return new LambdaProxyResponse(Http.OK, notifications);
    }
}
