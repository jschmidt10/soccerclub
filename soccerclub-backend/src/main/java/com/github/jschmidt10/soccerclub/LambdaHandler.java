package com.github.jschmidt10.soccerclub;

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
     */
    LambdaProxyResponse handle(LambdaProxyRequest request);
}
