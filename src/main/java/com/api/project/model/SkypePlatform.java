package com.api.project.model;

import com.api.project.service.ICommunicationPlatform;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SkypePlatform implements ICommunicationPlatform {

    private final String token;
    private final String apiUrl = "https://api.skype.com/";

    public SkypePlatform(String token) {
        this.token = token;
    }

    @Override
    public String sendMessage(String channelId, String message) {
        String url = apiUrl + "v3/conversations/" + channelId + "/activities";
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + token);
            post.setHeader("Content-type", "application/json");

            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("topic", "New message");
            messageMap.put("activityType", "message");
            messageMap.put("activityTitle", "Message from Adapter");
            messageMap.put("text", message);
            messageMap.put("recipient", channelId);
           /* messageMap.put("type", "message");
            messageMap.put("text", message);*/

            String json = new ObjectMapper().writeValueAsString(messageMap);
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                //System.out.println(response.getStatusLine().getStatusCode());
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                //System.out.println(responseBody);
                if (statusCode == 200) {
                    System.out.println("Message sent successfully");
                } else {
                    System.out.println("Failed to send message: " + responseBody);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public String startMeeting() {
       // String url = apiUrl + "v3/conversations";
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            URIBuilder builder = new URIBuilder(apiUrl + "me/onlineMeetings");
            URI url = builder.build();

            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + token);
            post.setHeader("Content-type", "application/json");

            Map<String, Object> meetingMap = new HashMap<>();
            meetingMap.put("startDateTime", "2023-12-31T23:59:59Z"); // Example date, adjust accordingly
            meetingMap.put("endDateTime", "2024-01-01T01:59:59Z"); // Example date, adjust accordingly
            meetingMap.put("subject", "Example Meeting");
            /*meetingMap.put("type", "conversation");
            meetingMap.put("subject", "Instant Meeting");*/

            String json = new ObjectMapper().writeValueAsString(meetingMap);
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 201) {
                    Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
                    String meetingUrl = (String) responseMap.get("threadId");
                    return "Skype meeting started: " + meetingUrl;
                } else {
                    return "Failed to start Skype meeting" + responseBody;
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "Error starting Skype meeting";
        }
    }
}