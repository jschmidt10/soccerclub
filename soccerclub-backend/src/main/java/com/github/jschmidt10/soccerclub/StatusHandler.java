package com.github.jschmidt10.soccerclub;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Gives some high level status on the lambda endpoint.
 */
public class StatusHandler implements LambdaHandler {
    private final LambdaProxyResponseFactory rf;

    private String startedOn = new SimpleDateFormat().format(new Date());

    public StatusHandler(LambdaProxyResponseFactory rf) {
        this.rf = rf;
    }

    @Override
    public boolean handlesPath(String path) {
        return path.startsWith("status");
    }

    // Accepted HTTP requests
    //
    // GET /status
    @Override
    public void handle(LambdaProxyRequest request, OutputStream outputStream) {
        Map<String, Object> body = new TreeMap<>();
        body.put("startedOn", startedOn);

        rf.writeResponse(Http.OK, body, outputStream);
    }
}
