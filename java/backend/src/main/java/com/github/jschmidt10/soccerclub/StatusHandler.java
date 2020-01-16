package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

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
        return path.startsWith("/soccerclub/status");
    }

    // Accepted HTTP requests
    //
    // GET /status
    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        Map<String, Object> body = new TreeMap<>();
        body.put("startedOn", startedOn);

        return ResponseFactory.newResponse(Http.OK, body);
    }
}
