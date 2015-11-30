package com.capgemini.services;

import com.capgemini.dto.LightState;
import com.capgemini.exceptions.ArtNetServiceException;
import com.capgemini.utils.LightEffect;
import fr.azelart.artnetstack.constants.Constants;
import fr.azelart.artnetstack.server.ArtNetServer;
import fr.azelart.artnetstack.utils.ArtNetPacketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * The ArtNet service server container.
 */
@Service
public class ArtNetService {

    /**
     * Declare logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ArtNetService.class);

    /**
     * Declare ArtNet server.
     */
    private ArtNetServer artNetServer;

    /**
     * Map light state.
     */
    private Map<Short, LightState> mapLightState;

    /**
     * Clock for effect.
     */
    private Integer clock;

    /**
     * Current DMX frame.
     */
    private int[] dmx;

    /**
     * Init the server.
     * @throws ArtNetServiceException if we have a problem.
     */
    @PostConstruct
    public void postConstruct() throws ArtNetServiceException {
        try {
            artNetServer = new ArtNetServer(InetAddress.getLocalHost(), Constants.SERVER_PORT);
        } catch (final UnknownHostException e) {
            logger.error("Unable to start server", e);
            throw new ArtNetServiceException(e);
        } catch (final SocketException e) {
            logger.error("Unable to start server", e);
            throw new ArtNetServiceException(e);
        }

        clock = 0;

        dmx = new int[512];

        mapLightState = new HashMap<>();
        mapLightState.put((short) 1, new LightState((short) 1, "000000", LightEffect.FULL));
        mapLightState.put((short) 5, new LightState((short) 5, "000000", LightEffect.FULL));
        mapLightState.put((short) 9, new LightState((short) 9, "000000", LightEffect.FULL));
        mapLightState.put((short) 13, new LightState((short) 13, "000000", LightEffect.FULL));
        mapLightState.put((short) 17, new LightState((short) 17, "000000", LightEffect.FULL));
    }

    public void sendLightState(final LightState lightState) {
        mapLightState.put(lightState.getAddress(), lightState);
    }

    /**
     * Send a light state.
     * @param address is the light address.
     * @param hexa is the light color.
     * @throws ArtNetServiceException if we have a problem I/O network.
     */
    public void sendLightState(final Short address, final String hexa) throws ArtNetServiceException {
        final Integer startAddress = address - 1;
        dmx[startAddress] = 1;
        dmx[startAddress + 1] = Integer.parseInt(hexa.substring(0, 2), 16); // RED
        dmx[startAddress + 2] = Integer.parseInt(hexa.substring(2, 4), 16); // GREEN
        dmx[startAddress + 3] = Integer.parseInt(hexa.substring(4, 6), 16); // BLUE
        this.sendPacket(dmx);
    }

    /**
     * Send packet.
     * @param dmx is the dmx packet.
     * @throws ArtNetServiceException if we have a I/O problem.
     */
    private void sendPacket(final int[] dmx) throws ArtNetServiceException {
        // Try to send packet...
        try {
            artNetServer.sendPacket(ArtNetPacketEncoder.encodeArtDmxPacket("0", "0", dmx));
        } catch (final IOException e) {
            logger.error("Unable to send packet over network", e);
            throw new ArtNetServiceException(e);
        }
    }

    /**
     * Play effect.
     * @throws ArtNetServiceException if we have a problem.
     */
    @Scheduled(fixedDelay = 100)
    public void playEffect() throws ArtNetServiceException {

        for(final LightState lightState : mapLightState.values()) {
            if(lightState.getEffect().name().equals(LightEffect.BLINK.name())) {
                if(clock >= 500) {
                    this.sendLightState(lightState.getAddress(), "000000");
                } else {
                    this.sendLightState(lightState.getAddress(), lightState.getColor());
                }
            }
            if(lightState.getEffect().name().equals(LightEffect.STROB.name())) {
                if((clock / 2) % 100 == 0) {
                    this.sendLightState(lightState.getAddress(), "000000");
                } else {
                    this.sendLightState(lightState.getAddress(), lightState.getColor());
                }
            }
            if(lightState.getEffect().name().equals(LightEffect.FULL.name())) {
                this.sendLightState(lightState.getAddress(), lightState.getColor());
            }
        }

        // Progress clock
        clock = clock + 100;
        if(clock == 1100) {
            clock = 0;
        }
    }

}
