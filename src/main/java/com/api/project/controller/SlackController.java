package com.api.project.controller;

import com.api.project.service.SlackAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
public class SlackController {

    private final SlackAdapter slackAdapter;

    @Autowired
    private SlackController(SlackAdapter slackAdapter){
        this.slackAdapter=slackAdapter;
    }

    @PostMapping("/message")
    public String sendMessage(@RequestParam String channelId, @RequestParam String message){

        return slackAdapter.sendMessage(channelId,message);
    }
}
