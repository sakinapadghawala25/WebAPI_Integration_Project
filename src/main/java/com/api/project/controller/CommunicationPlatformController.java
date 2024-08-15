package com.api.project.controller;

import com.api.project.service.CommunicationService;
import com.api.project.service.ICommunicationPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommunicationPlatformController {

    private final CommunicationService communicationService;
    private static final Logger logger = LoggerFactory.getLogger(CommunicationPlatformController.class);


    public CommunicationPlatformController(CommunicationService communicationService) {
        this.communicationService=communicationService;
    }


    public void registerPlatform(String name, ICommunicationPlatform platform) {
        communicationService.registerPlatform(name, platform);
        logger.info("Platform registered: {}", name);
    }
    @PostMapping("/message")
    public void sendMessage(String platformName, String channelId, String message) {
        try {
            communicationService.sendMessage(platformName, channelId, message);
            logger.info("Message sent to {} on {}: {}", channelId, platformName, message);
        } catch (Exception e) {
            logger.error("Failed to send message on {}: {}", platformName, e.getMessage());
        }
    }

    @PostMapping("/meeting")
    public String startMeeting(String platformName) {
        try {
            String meetingUrl = communicationService.startMeeting(platformName);
            logger.info("Meeting started on {}: {}", platformName, meetingUrl);
        } catch (Exception e) {
            logger.error("Failed to start meeting on {}: {}", platformName, e.getMessage());
        }
        return platformName;
    }
    @GetMapping("/platforms")
    public void listPlatforms() {
        communicationService.getPlatforms().forEach((name, platform) -> {
            logger.info("Registered platform: {}", name);
        });
    }

}
