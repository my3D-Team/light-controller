package com.capgemini.services;

import com.capgemini.config.Monitoring;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * Monitoring.
     */
    private Monitoring monitoring;
/*
    @PostConstruct
    public void init() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final String content = new String(Files.readAllBytes(Paths.get("configuration.json")));
        monitoring = mapper.readValue(content, Monitoring.class);
    }*/

    /**
     * Get the configuration monitoring file.
     * @return the monitoring configuration file
     */
    public Monitoring get() {
        return monitoring;
    }

}
