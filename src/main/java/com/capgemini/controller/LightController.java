package com.capgemini.controller;

import com.capgemini.dto.LightState;
import com.capgemini.exceptions.ArtNetServiceException;
import com.capgemini.exceptions.LightStateException;
import com.capgemini.services.LightService;
import com.capgemini.utils.LightEffect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Light controller.
 */
@RestController
@RequestMapping("/api/light")
public class LightController {

    /**
     * Inject light-controller service.
     */
    @Autowired
    private LightService lightService;



    /**
     * Perform a light-controller change state.
     * @param address is the address
     * @param color is the color
     * @return a light-controller state
     * @throws LightStateException if we have an invalid request on light-controller state
     * @throws ArtNetServiceException if we can't make I/O call
     */
    @RequestMapping(method = RequestMethod.GET, value = "set/{address}/{color}")
    public LightState get(@PathVariable("address") final Short address, @PathVariable("color") final String color) throws LightStateException, ArtNetServiceException {
        final LightState lightState = new LightState(address, color, LightEffect.FULL);
        return lightService.setState(lightState);
    }

    /**
     * Perform a light-controller change state.
     * @param address is the address
     * @param color is the color
     * @param effect is the effect
     * @return a light-controller state
     * @throws LightStateException if we have an invalid request on light-controller state
     * @throws ArtNetServiceException if we can't make I/O call
     */
    @RequestMapping(method = RequestMethod.GET, value = "set/{address}/{color}/{effect}")
    public LightState get(@PathVariable("address") final Short address, @PathVariable("color") final String color, @PathVariable("effect") final String effect) throws LightStateException, ArtNetServiceException {
        final LightState lightState = new LightState(address, color, Enum.valueOf(LightEffect.class, effect));
        return lightService.setState(lightState);
    }
}
