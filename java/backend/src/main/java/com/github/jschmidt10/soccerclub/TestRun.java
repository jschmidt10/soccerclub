package com.github.jschmidt10.soccerclub;

import java.util.concurrent.TimeUnit;

public class TestRun {
    public static void main(String[] args) throws Exception {
        DynamoNotificationStream notificationStream = new DynamoNotificationStream("soccerclub", "notifications_PROD");

        Notification notification = new Notification(System.currentTimeMillis(), "It's raining. Sorry guys.", false);
        notificationStream.push(notification);

        TimeUnit.SECONDS.sleep(2L);
        notification = new Notification(System.currentTimeMillis(), "Weathers great! See ya there!", true);
        notificationStream.push(notification);

        notificationStream.close();
    }
}
