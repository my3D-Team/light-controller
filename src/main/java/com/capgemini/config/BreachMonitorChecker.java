package com.capgemini.config;

import lombok.Data;

import java.util.List;

/**
 * A checker.
 */
@Data
public class BreachMonitorChecker {

    /**
     * URI to check
     */
    private String uri;

    /**
     * Lights.
     */
    private List<LightBreach> lights;
}
