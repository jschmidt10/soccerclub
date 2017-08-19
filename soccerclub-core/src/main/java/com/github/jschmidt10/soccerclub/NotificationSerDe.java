package com.github.jschmidt10.soccerclub;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Handles serializing and deserializing of Notifications for storage in DynamoDB.
 */
public class NotificationSerDe {

    /**
     * Serializes a Notification to bytes.
     *
     * @param notification
     * @return byte buffer
     */
    public ByteBuffer toBytes(Notification notification) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream output = new DataOutputStream(baos)) {

            output.writeLong(notification.getTimestamp());
            output.writeUTF(notification.getMessage());
            output.writeBoolean(notification.isSoccerOn());

        } catch (IOException e) {
            throw new RuntimeException("Error occurred during Notification serialization", e);
        }
        return ByteBuffer.wrap(baos.toByteArray());
    }

    /**
     * Deserializes the given bytes into a new Notification.
     *
     * @param bytes
     * @return new notification
     */
    public Notification fromBytes(ByteBuffer bytes) {
        long timestamp;
        String message;
        boolean isSoccerOn;

        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.array()))) {

            timestamp = input.readLong();
            message = input.readUTF();
            isSoccerOn = input.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred during Notificationd deserialization", e);
        }

        return new Notification(timestamp, message, isSoccerOn);
    }
}
