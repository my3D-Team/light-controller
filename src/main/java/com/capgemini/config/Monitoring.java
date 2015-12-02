package com.capgemini.config;

import lombok.Data;

import java.util.List;

/**
 * Monitoring.
 */
@Data
public class Monitoring {

    /**
     * Light network color if we have a network error.
     */
    private String lightNetworkErrorColor;

    /**
     * Light network effect if we have a network error.
     */
    private String lightNetworkErrorEffect;

    /**
     * Jenkins checkers.
     */
    private List<JenkinsChecker> jenkinsCheckers;

    /**
     * Breach monitor checkers.
     */
    private List<BreachMonitorChecker> breachMonitorCheckers;
}
