package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * This is the landing point for AWS lambda proxy invocation. Dispatches to the appropriate handler based
 * on the path and method.
 */
public class Dispatcher implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final NotFoundHandler NOT_FOUND_HANDLER = new NotFoundHandler();
    private static final APIGatewayProxyResponseEvent BAD_REQUEST = ResponseFactory.newResponse(Http.BAD_REQUEST, "We could not parse your request. Be sure to go through the Lambda Proxy endpoint!");

    private final List<LambdaHandler> handlers;

    public Dispatcher(List<LambdaHandler> handlers) {
        Preconditions.checkNotNull(handlers, "The 'handlers' list cannot be null.");
        this.handlers = handlers;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        context.getLogger().log("Received request for path: " + request.getPath());

        LambdaHandler handler = handlers
                .stream()
                .filter(h -> h.handlesPath(request.getPath()))
                .findFirst()
                .orElse(NOT_FOUND_HANDLER);

        context.getLogger().log("Dispatching to handler: " + handler);

        return handler.handle(request);
    }
}
