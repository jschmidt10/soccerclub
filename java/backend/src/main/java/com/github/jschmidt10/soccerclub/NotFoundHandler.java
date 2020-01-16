package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for an unknown path.
 */
public class NotFoundHandler implements LambdaHandler {

    @Override
    public boolean handlesPath(String path) {
        return true;
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        return ResponseFactory.newResponse(Http.NOT_FOUND, "Page not found");
    }
}
