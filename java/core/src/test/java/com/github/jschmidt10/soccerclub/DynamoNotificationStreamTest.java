package com.github.jschmidt10.soccerclub;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DynamoNotificationStreamTest {

    private static final String TABLE = "soccerclub";
    private static final String TEST_TOPIC = "notifications_TEST";

    private DynamoNotificationStream stream = new DynamoNotificationStream(TABLE, TEST_TOPIC);

    @Test
    public void shouldSendMessagesToDynamo() {
        long now = System.currentTimeMillis();
        long longTimeAgo = now - 100_000;
        long littleWhileAgo = now - 100;

        Notification oldMessage = new Notification(longTimeAgo, "Long time ago", true);
        Notification recentMessage = new Notification(littleWhileAgo, "Little while ago", true);
        Notification currentMessage = new Notification(now, "Right now", true);

        try {
            stream.push(oldMessage);
            stream.push(recentMessage);
            stream.push(currentMessage);

            List<Notification> messages = stream.get(littleWhileAgo);

            assertThat(messages.size(), is(2));
            assertThat(messages.contains(recentMessage), is(true));
            assertThat(messages.contains(currentMessage), is(true));
        }
        finally {
            stream.deleteNotification(oldMessage);
            stream.deleteNotification(recentMessage);
            stream.deleteNotification(currentMessage);
        }
    }
}
