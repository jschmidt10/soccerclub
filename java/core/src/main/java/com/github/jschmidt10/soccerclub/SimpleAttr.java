package com.github.jschmidt10.soccerclub;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * Simplication of Amazon's AttributeValue object.
 */
public class SimpleAttr extends AttributeValue {

    public SimpleAttr(String value) {
        withS(value);
    }

    public SimpleAttr(long value) {
        withN(String.valueOf(value));
    }

    public SimpleAttr(boolean value) {
        withBOOL(value);
    }
}
