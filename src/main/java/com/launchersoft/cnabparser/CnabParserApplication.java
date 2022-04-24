package com.launchersoft.cnabparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.launchersoft.cnabparser.configuration.CustomProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    CustomProperties.class
})
public class CnabParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CnabParserApplication.class, args);
    }

}
