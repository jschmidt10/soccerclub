package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handles requests for creating new notifications.
 */
public class SaveNotificationHandler implements LambdaHandler {
    private final NotificationStream notificationStream;

    public SaveNotificationHandler(NotificationStream notificationStream) {
        this.notificationStream = notificationStream;
    }

    @Override
    public boolean handlesPath(String path) {
        return path.equals("/soccerclub/notification");
    }

    // Accepted HTTP requests
    //
    // POST  /notification
    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        Notification notification = null;

        try {
            notification = ObjectMapperHolder.getInstance().readValue(request.getBody(), Notification.class);
        } catch (Exception e) {
            return ResponseFactory.newResponse(Http.BAD_REQUEST, "The POST body was not a valid notification.");
        }

        try {
            notificationStream.push(notification);
            return ResponseFactory.newResponse(Http.OK, notification);
        } catch (Exception e) {
            return ResponseFactory.newResponse(Http.INTERNAL_ERROR, "An error occurred while saving your notification. Please try again.");
        }
    }
}