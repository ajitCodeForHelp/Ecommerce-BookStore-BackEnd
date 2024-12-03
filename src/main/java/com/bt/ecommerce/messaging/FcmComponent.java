package com.bt.ecommerce.messaging;

import com.bt.ecommerce.utils.TextUtils;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class FcmComponent {

    public void sendNotificationToUser(String deviceType, String deviceToken,
                                       FcmNotificationBean.Notification notification, FcmNotificationBean.Data data) {
        new Thread(() -> {
            if (!TextUtils.isEmpty(deviceToken)) {
                pushFCMNotification(deviceType, deviceToken, data);
            }
        }).start();
    }

    private String getAccessToken() throws IOException {
        InputStream in = new ClassPathResource("service-account.json").getInputStream();
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(in);
        credentials = credentials.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        AccessToken accessToken = credentials.refreshAccessToken();
        return accessToken.getTokenValue();
    }

    private boolean pushFCMNotification(String deviceType, String deviceToken, FcmNotificationBean.Data data1) {
        try {
            String FMCurl = "https://fcm.googleapis.com/v1/projects/e-commerce-523b9/messages:send";
            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            conn.setRequestProperty("Content-Type", "application/json");

            Map<String, String> data = new HashMap<>();
            data.put("title", data1.getTitle());
            data.put("message", data1.getMessage());
            if (data1.getType() != null) {
                data.put("type", data1.getType());
            }
            if (data1.getTimeStamp() != null) {
                data.put("timeStamp", data1.getTimeStamp());
            }
            if (data1.getImageUrl() != null) {
                data.put("imageUrl", data1.getImageUrl());
            }
            if (data1.getStatus() != null) {
                data.put("status", data1.getStatus());
            }
            if (data1.getOrderId() != null) {
                data.put("orderId", data1.getOrderId());
            }

            Message message = null;
            if (deviceType != null) {
                message = Message.builder()
                        .putAllData(data)
                        .setToken(deviceToken).build();

            }

            FcmNotificationBean fcmNotificationBean = new FcmNotificationBean();
            fcmNotificationBean.setMessage(message);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(new Gson().toJson(fcmNotificationBean)
            );
            wr.flush();
            conn.getInputStream();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}