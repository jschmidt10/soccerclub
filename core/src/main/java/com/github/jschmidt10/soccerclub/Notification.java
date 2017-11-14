package com.github.jschmidt10.soccerclub;

import lombok.Data;

/**
 * A soccerclub notification.
 */
@Data
public class Notification implements Comparable<Notification> {
    private final long timestamp;
    private final String message;
    private final boolean isSoccerOn;

    @Override
    public int compareTo(Notification o) {
        return Long.compare(o.timestamp, timestamp);
    }
}
