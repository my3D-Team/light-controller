package com.capgemini.services;

import com.capgemini.dto.LightState;
import com.capgemini.exceptions.ArtNetServiceException;
import com.capgemini.exceptions.LightStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the light service, this is a business service.
 * @author Corentin Azelart
 */
@Service
public class LightService {

    /**
     * ArtNet server.
     */
    @Autowired
    private ArtNetService artNetService;

    /**
     * Set a light state, this is the main method to set the light state.
     * @param lightState is the light state
     * @throws LightStateException if we have a problem
     * @return a light state with DMX packet send.
     */
    public LightState setState(final LightState lightState) throws LightStateException, ArtNetServiceException {

        // Check light state DTO.
        this.checkLightState(lightState);

        artNetService.sendLightState(lightState);

        return lightState;
    }

    private void checkLightState(final LightState lightState) throws LightStateException {
        if(lightState.getAddress() <= 0 || lightState.getAddress() > 512) {
            throw new LightStateException("The light address is invalid (must be between 1 and 512)");
        }
        if(lightState.getColor().length() != 6) {
            throw new LightStateException("The color is wrong, must be on 6 chars (without #)");
        }
    }
}
