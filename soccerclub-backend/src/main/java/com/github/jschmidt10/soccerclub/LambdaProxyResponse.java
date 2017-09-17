package com.github.jschmidt10.soccerclub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Creates a new JSON object that is compatible as an AWS lambda proxy parse.
 */
@AllArgsConstructor
public class LambdaProxyResponse {

    private final ObjectMapper mapper = ObjectMapperHolder.getInstance();

    private final int statusCode;
    private final Object body;

    /**
     * Writes this response to the given output stream as JSON.
     *
     * @param outputStream
     * @throws IOException If the JSON response could not be written to the output stream
     */
    public void writeJson(OutputStream outputStream) throws IOException {
        mapper.writeValue(outputStream, this);
    }
}
