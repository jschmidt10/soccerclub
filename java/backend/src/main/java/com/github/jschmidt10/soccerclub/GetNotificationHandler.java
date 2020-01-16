package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.List;

/**
 * Handles requests for fetching notifications.
 */
public class GetNotificationHandler implements LambdaHandler {
    private final NotificationStream notificationStream;

    public GetNotificationHandler(NotificationStream notificationStream) {
        this.notificationStream = notificationStream;
    }

    @Override
    public boolean handlesPath(String path) {
        return path.startsWith("/soccerclub/notification/");
    }

    // Accepted HTTP requests
    //
    // GET  /notification/{sinceTimestamp}
    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        long sinceTimestamp;
        try {
            sinceTimestamp = Long.parseLong(request.getPath().substring("/soccerclub/notification/".length()));
        } catch (Exception e) {
            return ResponseFactory.newResponse(Http.BAD_REQUEST, "You forgot to give us a timestamp with your notification request.");
        }

        List<Notification> notifications;
        try {
            notifications = notificationStream.get(sinceTimestamp);
        } catch (Exception e) {
            return ResponseFactory.newResponse(Http.INTERNAL_ERROR, "An error occurred while fetching your notifications. Please try again.");
        }

        return ResponseFactory.newResponse(Http.OK, notifications);
    }
}
