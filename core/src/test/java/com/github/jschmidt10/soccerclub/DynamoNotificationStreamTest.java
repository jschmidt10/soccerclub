package com.github.jschmidt10.soccerclub;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DynamoNotificationStreamTest {

    private static final String TABLE = "soccerclub";
    private static final String TEST_TOPIC = "notifications_TEST";

    private DynamoNotificationStream stream = new DynamoNotificationStream(TABLE, TEST_TOPIC);

    @Test
    public void shouldSendMessagesToDynamo() {
        long now = System.currentTimeMillis();
        String msg = "The time is " + now;
        Notification sent = new Notification(now, msg, true);

        stream.push(sent);

        Optional<Notification> found = stream
                .get(now)
                .stream()
                .filter(n -> n.equals(sent))
                .findFirst();

        assertThat(found.isPresent(), is(true));
    }
}
