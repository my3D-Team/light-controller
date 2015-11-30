package com.capgemini.config;

import lombok.Data;

import java.util.List;

/**
 * A checker.
 */
@Data
public class HttpChecker {

    /**
     * URI to check
     */
    private String uri;

    /**
     * Light assigned to this job.
     */
    private List<Light> lights;
}
