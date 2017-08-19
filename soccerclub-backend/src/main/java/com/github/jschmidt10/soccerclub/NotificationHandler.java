package com.github.jschmidt10.soccerclub;

import java.io.OutputStream;
import java.util.List;

/**
 * Handles requests for fetching and storing notifications.
 */
public class NotificationHandler implements LambdaHandler {
    private final LambdaProxyResponseFactory rf;
    private final NotificationStream notificationStream;

    public NotificationHandler(LambdaProxyResponseFactory rf, NotificationStream notificationStream) {
        this.rf = rf;
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
    public void handle(LambdaProxyRequest request, OutputStream outputStream) {
        long sinceTimestamp;

        try {
            sinceTimestamp = Long.parseLong(request.getPath().substring("notification/".length()));
        } catch (Exception e) {
            rf.writeResponse(Http.BAD_REQUEST, "You forgot to give us a timestamp with your notification request.", outputStream);
            return;
        }

        List<Notification> notifications = notificationStream.get(sinceTimestamp);

        rf.writeResponse(Http.OK, notifications, outputStream);
    }
}
