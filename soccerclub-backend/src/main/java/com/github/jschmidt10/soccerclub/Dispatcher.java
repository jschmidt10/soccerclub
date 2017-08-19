package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the landing point for AWS lambda proxy invocation. Dispatches to the appropriate handler based
 * on the path and method.
 */
public class Dispatcher implements RequestStreamHandler {

    private final LambdaProxyResponseFactory rf = new LambdaProxyResponseFactory();
    private final List<LambdaHandler> handlers = new LinkedList<>();

    public Dispatcher() {
        this(System.getenv("TABLE"), System.getenv("NOTIFICATION_QUEUE"));
    }

    public Dispatcher(String table, String notificationQueue) {
        Preconditions.checkNotNull(table, "Must define a DynamoDB table to use.");
        Preconditions.checkNotNull(notificationQueue, "Must define a notification queue to use.");

        handlers.add(new NotificationHandler(rf, new DynamoNotificationStream(table, notificationQueue)));
        handlers.add(new StatusHandler(rf));
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            LambdaProxyRequest request = LambdaProxyRequest.parse(inputStream);

            context.getLogger().log("httpMethod = " + request.getHttpMethod());
            context.getLogger().log("path = " + request.getPath());

            handle(request, outputStream);
        } catch (Exception e) {
            rf.writeResponse(Http.BAD_REQUEST, "We could not parse your request. Be sure to go through the Lambda Proxy endpoint!", outputStream);
        }
    }

    /*
     * Dispatches the request to the correct handler.
     */
    private void handle(LambdaProxyRequest request, OutputStream outputStream) {
        handlers
                .stream()
                .filter(h -> h.handlesPath(request.getPath()))
                .findFirst()
                .orElseGet(() -> new NotFoundHandler(rf))
                .handle(request, outputStream);
    }
}
