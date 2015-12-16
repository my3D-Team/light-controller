package com.capgemini.services;

import com.capgemini.dto.LightState;
import com.capgemini.exceptions.ArtNetServiceException;
import com.capgemini.utils.LightEffect;
import fr.azelart.artnetstack.constants.Constants;
import fr.azelart.artnetstack.server.ArtNetServer;
import fr.azelart.artnetstack.utils.ArtNetPacketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * The ArtNet service server container.
 */
@Service
public class ArtNetService {

    /**
     * Configuration service.
     */
    @Autowired
    private ConfigurationService configurationService;

    /**
     * Declare logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ArtNetService.class);

    /**
     * Declare ArtNet server.
     */
    private ArtNetServer artNetServer;

    /**
     * Map light-controller state.
     */
    private Map<Short, LightState> mapLightState;

    /**
     * Clock for effect.
     */
    private Boolean clock;

    /**
     * Current DMX frame.
     */
    private int[] dmx;

    /**
     * Get the local address.
     * @return an InetAddress
     * @throws SocketException if the can't get Inet data
     */
    public static InetAddress getLocalAddress() throws SocketException {
        final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while(ifaces.hasMoreElements()) {
            final NetworkInterface iface = ifaces.nextElement();
            final Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while( addresses.hasMoreElements()) {
                final InetAddress addr = addresses.nextElement();
                if(addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                    return addr;
                }
            }
        }
        return null;
    }

    /**
     * Init the server.
     * @throws ArtNetServiceException if we have a problem.
     */
    @PostConstruct
    public void postConstruct() throws ArtNetServiceException {
        try {
            //artNetServer = new ArtNetServer(InetAddress.getLocalHost(), Constants.SERVER_PORT);
            final InetAddress inetAddress = getLocalAddress();

            logger.info(String.format("Try create an Art-Net server on %s", inetAddress.getHostAddress()));
            artNetServer = new ArtNetServer(getLocalAddress(), Constants.SERVER_PORT);
            logger.info("OK the Art-Net server is launched");


        } catch (final UnknownHostException e) {
            logger.error("Unable to start server", e);
            throw new ArtNetServiceException(e);
        } catch (final SocketException e) {
            logger.error("Unable to start server", e);
            throw new ArtNetServiceException(e);
        }

        clock = Boolean.FALSE;
        dmx = new int[512];

        mapLightState = new HashMap<>();
        mapLightState.put((short) 1, new LightState((short) 1, "0000FF", LightEffect.FULL));
        mapLightState.put((short) 5, new LightState((short) 5, "0000FF", LightEffect.FULL));
        mapLightState.put((short) 9, new LightState((short) 9, "0000FF", LightEffect.FULL));
        mapLightState.put((short) 13, new LightState((short) 13, "0000FF", LightEffect.FULL));
        mapLightState.put((short) 17, new LightState((short) 17, "0000FF", LightEffect.FULL));
    }

    public void sendLightState(final LightState lightState) {
        mapLightState.put(lightState.getAddress(), lightState);
    }

    /**
     * Set error state.
     */
    public void setErrorState(final Boolean errorState) {
        for(final LightState lightState : mapLightState.values()) {
            if(errorState) {
                logger.warn("Oups, we are entering in error state, see error please !");
                lightState.setColor(configurationService.get().getLightNetworkErrorColor());
                lightState.setEffect(Enum.valueOf(LightEffect.class, configurationService.get().getLightNetworkErrorEffect()));
            } else {
                lightState.setColor("000000");
                lightState.setEffect(LightEffect.FULL);
            }
            this.sendLightState(lightState);
        }
    }

    /**
     * Send a light-controller state.
     * @param address is the light-controller address.
     * @param hexa is the light-controller color.
     * @throws ArtNetServiceException if we have a problem I/O network.
     */
    public void sendLightState(Short address, final String hexa) throws ArtNetServiceException {
        final Integer startAddress = address - 1;
        dmx[startAddress] = 1;
        dmx[startAddress + 1] = Integer.parseInt(hexa.substring(0, 2), 16); // RED
        dmx[startAddress + 2] = Integer.parseInt(hexa.substring(2, 4), 16); // GREEN
        dmx[startAddress + 3] = Integer.parseInt(hexa.substring(4, 6), 16); // BLUE
        //logger.info(String.format("Address : %s (%s,%s,%s,%s)", startAddress, dmx[startAddress], dmx[startAddress + 1], dmx[startAddress + 2], dmx[startAddress + 3]));
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
    @Scheduled(fixedDelay = 500)
    public void playEffect() throws ArtNetServiceException {
        for(final LightState lightState : mapLightState.values()) {
            if(lightState.getEffect().name().equals(LightEffect.BLINK.name())) {
                if(clock) {
                    this.sendLightState(lightState.getAddress(), "000000");
                } else {
                    this.sendLightState(lightState.getAddress(), lightState.getColor());
                }
            } else {
                this.sendLightState(lightState.getAddress(), lightState.getColor());
            }
        }

        clock = !clock;
    }

}
