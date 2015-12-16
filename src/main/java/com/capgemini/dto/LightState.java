package com.capgemini.dto;

import com.capgemini.utils.LightEffect;
import lombok.Data;

/**
 * A light-controller state.
 */
@Data
public class LightState {

    /**
     * The light-controller address, a light-controller between 1 and 512.
     * Only on one universe.
     */
    private short address;

    /**
     * Hexa color.
     */
    private String color;

    /**
     * Effect string.
     */
    private LightEffect effect;

    /**
     * Constructor.
     * @param address is the default address.
     * @param color is the light-controller color.
     * @param lightEffect is the light-controller effect
     */
    public LightState(final Short address, final String color, final LightEffect lightEffect) {
        this.address = address;
        this.color = color;
        this.effect = lightEffect;
    }

    /**
     * Constructor.
     * @param address is the default address.
     * @param lightEffect is the light-controller effect
     */
    public LightState(final Short address, final LightEffect lightEffect) {
        this.address = address;
        this.color = "000000";
        this.effect = lightEffect;
    }

    public LightState(final Short address) {
        this.address = address;
        this.color = "000000";
        this.effect = LightEffect.FULL;
    }

    private LightState() {}

}
