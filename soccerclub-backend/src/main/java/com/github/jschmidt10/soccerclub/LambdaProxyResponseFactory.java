package com.github.jschmidt10.soccerclub;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Creates a new JSON object that is compatible as an AWS lambda proxy parse.
 */
public class LambdaProxyResponseFactory {

    private final ObjectMapper mapper = ObjectMapperHolder.getInstance();

    /**
     * Writes an empty HTTP response with the given status.
     *
     * @param status
     * @param outputStream
     */
    public void writeResponse(int status, OutputStream outputStream) {
        writeResponse(status, "", outputStream);
    }

    /**
     * Writes the given json as the HTTP response.
     *
     * @param status
     * @param body
     * @param outputStream
     */
    public void writeResponse(int status, Object body, OutputStream outputStream) {
        try {
            mapper.writeValue(outputStream, create(status, body));
        } catch (IOException e) {
            writeResponse(Http.INTERNAL_ERROR, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream, null);
        }
    }

    private ObjectNode create(int status) {
        ObjectNode node = mapper.createObjectNode();
        node.put("statusCode", status);
        return node;
    }

    private ObjectNode create(int status, Object body) throws JsonProcessingException {
        ObjectNode node = create(status);
        node.put("body", mapper.writeValueAsString(body));
        return node;
    }
}
