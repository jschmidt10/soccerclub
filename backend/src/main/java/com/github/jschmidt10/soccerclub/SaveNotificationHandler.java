package com.github.jschmidt10.soccerclub;

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
        return path.equals("notification");
    }

    // Accepted HTTP requests
    //
    // POST  /notification
    @Override
    public LambdaProxyResponse handle(LambdaProxyRequest request) {
        Notification notification = null;

        try {
            notification = ObjectMapperHolder.getInstance().readValue(request.getBody(), Notification.class);
        } catch (Exception e) {
            return new LambdaProxyResponse(Http.BAD_REQUEST, "The POST body was not a valid notification.");
        }

        try {
            notificationStream.push(notification);
            return new LambdaProxyResponse(Http.OK, notification);
        } catch (Exception e) {
            return new LambdaProxyResponse(Http.INTERNAL_ERROR, "An error occurred while saving your notification. Please try again.");
        }
    }
}