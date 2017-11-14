package com.github.jschmidt10.soccerclub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Gives some high level status on the lambda endpoint.
 */
public class StatusHandler implements LambdaHandler {
    private String startedOn = new SimpleDateFormat().format(new Date());

    @Override
    public boolean handlesPath(String path) {
        return path.startsWith("status");
    }

    // Accepted HTTP requests
    //
    // GET /status
    @Override
    public LambdaProxyResponse handle(LambdaProxyRequest request) {
        Map<String, Object> body = new TreeMap<>();
        body.put("startedOn", startedOn);

        return new LambdaProxyResponse(Http.OK, body);
    }
}
