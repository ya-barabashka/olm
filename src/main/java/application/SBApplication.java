package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = {"application.rest.controller"})
public class SBApplication {
    public static void main(String[] args) {
        SpringApplication.run(SBApplication.class, args);
    }
}
