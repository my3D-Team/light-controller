package com.capgemini.dto.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Jenkins Job.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JenkinsJob {

    /**
     * Name.
     */
    private String name;

    /**
     * Url.
     */
    private String url;

    /**
     * Last builds;
     */
    private List<JenkinsBuild> builds;
}
