package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handles a specific Lambda request.
 */
public interface LambdaHandler {

    /**
     * Identifies if this path can be handled by this handler.
     *
     * @param path
     * @return true if the handler accepts this path, false, otherwise.
     */
    boolean handlesPath(String path);

    /**
     * Handles the request.
     *
     * @param request
     * @return response
     */
    APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request);
}
