package com.capgemini.config;

import lombok.Data;

import java.util.List;

/**
 * Monitoring.
 */
@Data
public class Monitoring {

    /**
     * Jenkins checkers.
     */
    private List<HttpChecker> httpCheckers;
}
