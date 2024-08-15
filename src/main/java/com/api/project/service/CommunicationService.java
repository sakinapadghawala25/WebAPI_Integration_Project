package com.api.project.service;

import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
@Service
public class CommunicationService {
    private final Map<String, ICommunicationPlatform> platforms = new HashMap<>();

    public void registerPlatform(String name, ICommunicationPlatform platform) {
        platforms.put(name, platform);
    }

    public void sendMessage(String platformName, String channelId, String message) {
        ICommunicationPlatform platform = platforms.get(platformName);
        if (platform != null) {
            try {
                platform.sendMessage(channelId, message);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Platform not registered: " + platformName);
        }
    }

    public String startMeeting(String platformName) {
        ICommunicationPlatform platform = platforms.get(platformName);
        if (platform != null) {
            platform.startMeeting();
        } else {
            throw new IllegalArgumentException("Platform not registered: " + platformName);
        }
        return platformName;
    }
    public Map<String, ICommunicationPlatform> getPlatforms() {
        return platforms;
    }
}
