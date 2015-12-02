package com.capgemini.dto.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Jenkins build.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JenkinsBuild {

    /**
     * The number of build.
     */
    private Integer number;

    /**
     * The url of build.
     */
    private String url;

    /**
     * The build result.
     */
    private String result;

    /**
     * The full name of the job.
     */
    private String fullDisplayName;

    /**
     * Building in progress ?.
     */
    private Boolean building;

    /**
     * Build on.
     */
    private String builtOn;

    /**
     * Duration.
     */
    private Integer duration;
}
