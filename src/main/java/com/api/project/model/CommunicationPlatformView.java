package com.api.project.model;


import com.api.project.controller.CommunicationPlatformController;

public class CommunicationPlatformView {

    private final CommunicationPlatformController controller;

    public CommunicationPlatformView(CommunicationPlatformController controller) {
        this.controller = controller;
    }

    public void initialize() {
        // Register platforms
        controller.registerPlatform("Slack", new SlackPlatform("your-slack-token"));
        controller.registerPlatform("Skype", new SkypePlatform("your-skype-token"));
        controller.registerPlatform("Teams", new TeamsPlatform("your-teams-token"));
        controller.registerPlatform("Zoom", new ZoomPlatform("your-zoom-token"));
        controller.registerPlatform("BigBlueButton", new BigBlueButtonPlatform("https://demo.bigbluebutton.org/", "your-secret"));

        //list platform
        controller.listPlatforms();

        // Example usage
        controller.sendMessage("Slack", "#general", "Hello, Slack!");
        controller.startMeeting("Zoom");
        controller.startMeeting("BigBlueButton");
    }
}