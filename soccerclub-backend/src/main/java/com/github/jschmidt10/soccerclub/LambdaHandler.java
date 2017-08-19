package com.github.jschmidt10.soccerclub;

import java.io.OutputStream;

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
     * @param outputStream
     */
    void handle(LambdaProxyRequest request, OutputStream outputStream);
}
