package com.api.project.model;

import com.api.project.service.ICommunicationPlatform;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;


public class SlackPlatform implements ICommunicationPlatform {
    private final String token;
    private final String apiUrl = "https://slack.com/api/";

    public SlackPlatform(String token) {
        this.token = token;
    }

    @Override
    public String sendMessage(String channelId, String message) {
        String url = apiUrl + "chat.postMessage";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + token);
            post.setHeader("Content-type", "application/json");

            String json = new ObjectMapper().writeValueAsString(new Message(channelId, message));
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println(response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    @Override
    public String startMeeting() {
        throw new UnsupportedOperationException("Starting a meeting is not supported in Slack.");
    }

    private static class Message {
        public String channel;
        public String text;
        public Message(String channel, String text) {
            this.channel = channel;
            this.text = text;
        }
    }


}
