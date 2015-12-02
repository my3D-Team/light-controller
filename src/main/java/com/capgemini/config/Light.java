package com.capgemini.config;

import lombok.Data;

/**
 * Light.
 */
@Data
public class Light {

    /**
     * Light address.
     */
    private Short address;

    /**
     * Light color.
     */
    private String lightSuccessColor;

    /**
     * Light effect.
     */
    private String lightSuccessEffect;

    /**
     * Light color.
     */
    private String lightUnstableColor;

    /**
     * Light effect.
     */
    private String lightUnstableEffect;

    /**
     * Light color.
     */
    private String lightFailureColor;

    /**
     * Light effect.
     */
    private String lightFailureEffect;

    /**
     * Light color.
     */
    private String lightNotBuildColor;

    /**
     * Light effect.
     */
    private String lightNotBuildEffect;

    /**
     * Light color.
     */
    private String lightAbortedColor;

    /**
     * Light effect.
     */
    private String lightAbortedEffect;


}
