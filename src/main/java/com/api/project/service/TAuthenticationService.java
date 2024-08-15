package com.api.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class TAuthenticationService {

    @Value("${azure.ad.client-id}")
    private String clientId;

    @Value("${azure.ad.client-secret}")
    private String clientSecret;

    @Value("${azure.ad.tenant-id}")
    private String tenantId;

    @Value("${azure.ad.scope}")
    private String scope;

    public String getAccessToken() {
        String url = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&scope=" + scope;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to obtain access token");
        }
    }

}
