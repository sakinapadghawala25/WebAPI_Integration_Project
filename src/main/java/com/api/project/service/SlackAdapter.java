package com.api.project.service;

import com.api.project.model.SlackClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SlackAdapter {

   private final SlackClient slackClient;

    public SlackAdapter(){
        this.slackClient= new SlackClient();
    }

    public String sendMessage(String channelId, String message){
        try{
            return slackClient.sendMessage(channelId, message);
        } catch(IOException e){
            throw new RuntimeException("Failed to send message", e);
        }
    }

}
