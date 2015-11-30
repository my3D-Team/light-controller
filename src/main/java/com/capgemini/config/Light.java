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
     * Light OK color.
     */
    private String lightOkColor;

    /**
     * Light OK effect.
     */
    private String lightOkEffect;

    /**
     * Light KO color.
     */
    private String lightKoColor;

    /**
     * Light KO effect.
     */
    private String lightKoEffect;
}
