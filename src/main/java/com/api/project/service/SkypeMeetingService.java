package com.api.project.service;

import com.api.project.model.OAuthSkypeResponse;
import com.api.project.model.SkypeMeetingRequest;
import com.api.project.model.SkypeMeetingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SkypeMeetingService {
    @Value("${skype.ucwa.base-url}")
    private String ucwaBaseUrl;

    @Value("${skype.oauth2.token-url}")
    private String tokenUrl;

    @Value("${skype.client-id}")
    private String clientId;

    @Value("${skype.client-secret}")
    private String clientSecret;

    @Value("${skype.resource}")
    private String resource;

    @Value("${skype.tenant-id}")
    private String tenantId;
    private String getAccessToken() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String resolvedTokenUrl = tokenUrl.replace("{tenant}", tenantId);
        HttpPost httpPost = new HttpPost(resolvedTokenUrl);

        String authBody = "client_id=" + clientId + "&client_secret=" + clientSecret + "&resource=" + resource + "&grant_type=client_credentials";
        StringEntity entity = new StringEntity(authBody);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        CloseableHttpResponse response = client.execute(httpPost);
        String json = EntityUtils.toString(response.getEntity());
        client.close();

        ObjectMapper mapper = new ObjectMapper();
        OAuthSkypeResponse authResponse = mapper.readValue(json, OAuthSkypeResponse.class);
        return authResponse.getAccessToken();
    }

    public SkypeMeetingResponse scheduleSkypeMeeting(SkypeMeetingRequest meetingRequest) throws IOException {
        String accessToken = getAccessToken();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ucwaBaseUrl + "/communications/messaging");

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(meetingRequest);
        StringEntity entity = new StringEntity(jsonBody);
        httpPost.setEntity(entity);
        httpPost.setHeader("Authorization", "Bearer " + accessToken);
        httpPost.setHeader("Content-Type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        client.close();

        return mapper.readValue(jsonResponse, SkypeMeetingResponse.class);
    }

}
