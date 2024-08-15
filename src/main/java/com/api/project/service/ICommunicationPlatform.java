package com.api.project.service;

import java.net.URISyntaxException;

public interface ICommunicationPlatform {

    public String startMeeting();
    public String sendMessage(String channelId, String message) throws URISyntaxException;

}
