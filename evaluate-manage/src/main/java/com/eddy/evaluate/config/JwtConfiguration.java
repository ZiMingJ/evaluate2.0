package com.eddy.evaluate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "jwt")
@Data
@Component
public class JwtConfiguration {

    private String secret;
    private Long expire;
    private String head;
}
