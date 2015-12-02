package com.capgemini.config;

import lombok.Data;

import java.util.List;

/**
 * A checker.
 */
@Data
public class JenkinsChecker {

    /**
     * URI to check
     */
    private String uri;

    /**
     * Job.
     */
    private String job;

    /**
     * Jenkins login.
     */
    private String userId;

    /**
     * Jenkins API key.
     */
    private String token;

    /**
     * Light assigned to this job.
     */
    private List<Light> lights;
}
