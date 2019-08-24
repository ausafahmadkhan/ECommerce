package com.example.Shopping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App
{
    public static void main(String args[])
    {
        Logger logger = LogManager.getLogger(App.class);
        logger.info("App started");
        SpringApplication.run(App.class, args);
    }
}
