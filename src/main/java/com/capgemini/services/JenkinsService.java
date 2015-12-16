package com.capgemini.services;

import com.capgemini.config.JenkinsChecker;
import com.capgemini.config.Light;
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

/**
 * Jenkins service.
 */
@Service
public class JenkinsService {

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
     * ArtNet service.
     */
    @Autowired
    private ArtNetService artNetService;

    /**
     * The Jenkins API URL.
     */
    private final static String API_URL = "/api/json/";

    /**
     * Declare logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(JenkinsService.class);

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
        if(configurationService.get().getJenkinsCheckers() != null) {
            for(final JenkinsChecker jenkinsChecker : configurationService.get().getJenkinsCheckers()) {

                final StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append(jenkinsChecker.getUri()).append("job/").append(jenkinsChecker.getJob()).append(API_URL);

                final RestTemplate restTemplate = new RestTemplate();
                final HttpEntity entity = new HttpEntity(this.buildAuthorization(jenkinsChecker.getUserId(), jenkinsChecker.getToken()));

                try {
                    final HttpEntity<String> responseBuild = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, entity, String.class);
                    final ObjectMapper mapper = new ObjectMapper();
                    final JenkinsJob jenkinsJob = mapper.readValue(responseBuild.getBody(), JenkinsJob.class);

                    // Get last build.
                    if(jenkinsJob.getBuilds() != null && !jenkinsJob.getBuilds().isEmpty()) {
                        final StringBuilder urlLastBuild = new StringBuilder();
                        urlLastBuild.append(jenkinsJob.getBuilds().get(0).getUrl()).append(API_URL);
                        final HttpEntity<String> responseFinalBuild = restTemplate.exchange(urlLastBuild.toString(), HttpMethod.GET, entity, String.class);
                        final JenkinsBuild jenkinsBuildWithDetails = mapper.readValue(responseFinalBuild.getBody(), JenkinsBuild.class);
                        this.updateBuild(jenkinsChecker, jenkinsBuildWithDetails);
                    }
                } catch (final Exception e) {
                    logger.error(String.format("Unable to perform job control %s %s", jenkinsChecker.getUri(), jenkinsChecker.getJob()), e);
                }
            }
        }
    }

    /**
     * Update the jinekins build.
     * @param jenkinsChecker is the jenkins checker.
     * @param jenkinsBuild is the jenkins build.
     */
    private void updateBuild(final JenkinsChecker jenkinsChecker, final JenkinsBuild jenkinsBuild) {
        for(final Light light : jenkinsChecker.getLights()) {
            LightState lightState = null;

            if(jenkinsBuild.getResult() != null && !jenkinsBuild.getBuilding()) {
                //logger.info(String.format("The jenkins checker %s is in the %s state (#%s)", jenkinsChecker.getJob(), jenkinsBuild.getResult(), jenkinsBuild.getNumber()));
                if(jenkinsBuild.getResult().equals(Constants.JENKINS_SUCCESS)) {
                    lightState = new LightState(light.getAddress(), light.getLightSuccessColor(), Enum.valueOf(LightEffect.class, light.getLightSuccessEffect()));
                } else if(jenkinsBuild.getResult().equals(Constants.JENKINS_ABORTED)) {
                    lightState = new LightState(light.getAddress(), light.getLightAbortedColor(), Enum.valueOf(LightEffect.class, light.getLightAbortedEffect()));
                } else if(jenkinsBuild.getResult().equals(Constants.JENKINS_FAILURE)) {
                    lightState = new LightState(light.getAddress(), light.getLightFailureColor(), Enum.valueOf(LightEffect.class, light.getLightFailureEffect()));
                } else if(jenkinsBuild.getResult().equals(Constants.JENKINS_NOT_BUILT)) {
                    lightState = new LightState(light.getAddress(), light.getLightNotBuildColor(), Enum.valueOf(LightEffect.class, light.getLightNotBuildEffect()));
                } else if(jenkinsBuild.getResult().equals(Constants.JENKINS_UNSTABLE)) {
                    lightState = new LightState(light.getAddress(), light.getLightUnstableColor(), Enum.valueOf(LightEffect.class, light.getLightUnstableEffect()));
                }

                // Try to update light-controller state.
                try {
                    if(lightState != null) {
                        lightService.setState(lightState);
                    }
                } catch (final LightStateException e) {
                    logger.error("Unable to update the light-controller color", e);
                } catch (final ArtNetServiceException e) {
                    logger.error("Unable to update the light-controller color", e);
                }
            }
        }
    }

    /**
     * Generate headers for authorization.
     * @param user is the user
     * @param token is the token
     * @return the headers
     */
    private MultiValueMap<String, String> buildAuthorization(final String user, final String token) {
        final StringBuilder sbAuth = new StringBuilder();
        sbAuth.append(user).append(":").append(token);

        final StringBuilder sb = new StringBuilder();
        sb.append("Basic ").append(new String(Base64.encodeBase64(sbAuth.toString().getBytes())));

        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", sb.toString());

        return headers;
    }


}
