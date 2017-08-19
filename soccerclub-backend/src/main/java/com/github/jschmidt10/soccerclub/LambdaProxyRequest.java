package com.github.jschmidt10.soccerclub;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper for a raw JSON object that knows how to extract Lambda proxy request information.
 */
@Data
@AllArgsConstructor
public class LambdaProxyRequest {

    /**
     * Factory method for creating a new LambdaProxyRequest from the request input stream.
     *
     * @param inputStream
     * @return request
     * @throws IOException if an error occurs while reading the request
     */
    public static LambdaProxyRequest parse(InputStream inputStream) throws IOException {
        try {
            JsonNode node = ObjectMapperHolder.getInstance().readValue(inputStream, JsonNode.class);

            String path = node.get("pathParameters").get("proxy").asText();
            String httpMethod = node.get("httpMethod").asText();

            return new LambdaProxyRequest(path, httpMethod);

        } finally {
            IOUtils.closeQuietly(inputStream, null);
        }

    }

    private final String path;
    private final String httpMethod;
}
