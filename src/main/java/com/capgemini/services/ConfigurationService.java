package com.capgemini.services;

import com.capgemini.config.Monitoring;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Configuration service.
 */
@Service
public class ConfigurationService {

    /**
     * Config file destination.
     */
    @Value("${config}")
    private String config;

    /**
     * Monitoring.
     */
    private Monitoring monitoring;

    /**
     * Init.
     * @throws IOException if we can't read config file.
     */
    @PostConstruct
    public void init() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final String content = new String(Files.readAllBytes(Paths.get(config)));
        monitoring = mapper.readValue(content, Monitoring.class);
    }

    /**
     * Get the configuration monitoring file.
     * @return the monitoring configuration file
     */
    public Monitoring get() {
        return monitoring;
    }

}
