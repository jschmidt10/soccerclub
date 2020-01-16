package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * Factory for creating APIGatewayProxyResponseEvents.
 */
public final class ResponseFactory {

    private static final Map<String, String> CORS_AND_JSON_HEADERS = new TreeMap<>();

    static {
        CORS_AND_JSON_HEADERS.put("Content-type", "application/json");

        CORS_AND_JSON_HEADERS.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key");
        CORS_AND_JSON_HEADERS.put("Access-Control-Allow-Methods", "*");
        CORS_AND_JSON_HEADERS.put("Access-Control-Allow-Origin", "*");
    }

    private ResponseFactory() {
    }

    public static APIGatewayProxyResponseEvent newResponse(int statusCode, Object body) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(statusCode);
        response.setBody(stringify(body));
        response.setHeaders(CORS_AND_JSON_HEADERS);
        return response;
    }

    private static String stringify(Object body) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectMapperHolder.getInstance().writeValue(baos, body);
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error converting " + body + " into a JSON string.");
        }
    }
}
