package com.launchersoft.cnabparser.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "com.launchersoft")
@Getter
@Setter
public class CustomProperties {

    /**
     * The directory where the files will be stored.
     */
    private String fileUploadDir = "src/main/resources/files";

}