package com.github.jschmidt10.soccerclub;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NotificationSerDeTest {

    private NotificationSerDe serde = new NotificationSerDe();

    @Test
    public void shouldBeSameNotificationAfterRoundTrip() {
        Notification orig = new Notification(1L, "Message", true);
        Notification copy = serde.fromBytes(serde.toBytes(orig));

        assertThat(copy, is(orig));
    }
}
