package com.capgemini.services;

import com.capgemini.config.BreachMonitorChecker;
import com.capgemini.config.JenkinsChecker;
import com.capgemini.config.Light;
import com.capgemini.config.LightBreach;
import com.capgemini.dto.LightState;
import com.capgemini.dto.jenkins.JenkinsBuild;
import com.capgemini.dto.jenkins.JenkinsJob;
import com.capgemini.exceptions.ArtNetServiceException;
import com.capgemini.exceptions.LightStateException;
import com.capgemini.utils.Constants;
import com.capgemini.utils.LightEffect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Breach monitor service.
 */
@Service
public class BreachMonitorService {

    /**
     * Configuration service.
     */
    @Autowired
    private ConfigurationService configurationService;

    /**
     * Light service.
     */
    @Autowired
    private LightService lightService;

    /**
     * Declare logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BreachMonitorService.class);

    /**
     * Init.
     */
    @PostConstruct
    private void init() {
        //this.check();
    }

    @Scheduled(fixedDelay = 5000)
    private void scheduled() {
        this.check();
    }

    /**
     * Check all jenkins jobs.
     */
    private void check() {
        if(configurationService.get().getBreachMonitorCheckers() != null) {
            for(final BreachMonitorChecker breachMonitorChecker : configurationService.get().getBreachMonitorCheckers()) {
                final RestTemplate restTemplate = new RestTemplate();
                try {
                    final HttpEntity<String> responseBuild = restTemplate.exchange(breachMonitorChecker.getUri(), HttpMethod.GET, null, String.class);
                    this.updateLight(breachMonitorChecker.getLights(), (responseBuild.getBody() != null && responseBuild.getBody().contains("tmv level1")));
                } catch (final Exception e) {
                    logger.error(String.format("Unable to perform breach monitor control %s", breachMonitorChecker.getUri()), e);
                }
            }
        }
    }

    /**
     * Update the light
     * @param lightBreachs are light breachs
     * @param breached if is breached
     * @throws LightStateException if we have a problem
     * @throws ArtNetServiceException if we have a problem
     */
    private void updateLight(final List<LightBreach> lightBreachs, final Boolean breached) throws LightStateException, ArtNetServiceException {
        for(final LightBreach lightBreach : lightBreachs) {
            if(breached) {
                lightService.setState(new LightState(lightBreach.getAddress(), lightBreach.getLightColorSlaBreached(), LightEffect.FULL));
            } else {
                lightService.setState(new LightState(lightBreach.getAddress(), lightBreach.getLightColorSlaWithin(), LightEffect.FULL));
            }
        }
    }


}
