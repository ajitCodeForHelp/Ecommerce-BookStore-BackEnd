package com.bt.ecommerce.messaging;

import com.google.firebase.messaging.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmNotificationBean {

    private String to;
    private Data data;
    private Notification notification;
    private String category = null;
    private Message message;

    @Getter
    @Setter
    public static class Data {
        private String title;
        private String message;
        private String imageUrl;
        private String timeStamp;
        private String type;
        private String orderId;
        private String status;

    }

    @Getter
    @Setter
    public static class Notification {
        public String title;
        public String body;
        public String sound = "notification_sound.caf";

        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }
}

