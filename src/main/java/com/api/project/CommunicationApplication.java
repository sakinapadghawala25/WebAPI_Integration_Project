package com.api.project;

import com.api.project.controller.CommunicationPlatformController;
import com.api.project.model.CommunicationPlatformView;
import com.api.project.service.CommunicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunicationApplication {
    public static void main(String[] args) {

        CommunicationService manager = new CommunicationService();
        CommunicationPlatformController controller = new CommunicationPlatformController(manager);
        CommunicationPlatformView view = new CommunicationPlatformView(controller);

        view.initialize();

       SpringApplication.run(CommunicationApplication.class, args);
    }
}
