package com.github.jschmidt10.soccerclub;

import java.io.OutputStream;

/**
 * Handler for an unknown path.
 */
public class NotFoundHandler implements LambdaHandler {

    private final LambdaProxyResponseFactory rf;

    public NotFoundHandler(LambdaProxyResponseFactory rf) {
        this.rf = rf;
    }

    @Override
    public boolean handlesPath(String path) {
        return true;
    }

    @Override
    public void handle(LambdaProxyRequest request, OutputStream outputStream) {
        rf.writeResponse(Http.NOT_FOUND, "Page not found", outputStream);
    }
}
