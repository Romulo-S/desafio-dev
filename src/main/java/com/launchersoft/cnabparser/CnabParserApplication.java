package com.launchersoft.cnabparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.launchersoft.cnabparser.configuration.CustomProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "CNAB File API", version = "2.0", description = "CNAB Information"))
@EnableConfigurationProperties({
    CustomProperties.class
})
public class CnabParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CnabParserApplication.class, args);
    }

}
