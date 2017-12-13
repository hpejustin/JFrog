package com.jfrog.cloud;

import be.ordina.msdashboard.EnableMicroservicesDashboardServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMicroservicesDashboardServer
public class Application {
    public static void main( String[] args ) {
        SpringApplication.run(Application.class, args);
    }
}
