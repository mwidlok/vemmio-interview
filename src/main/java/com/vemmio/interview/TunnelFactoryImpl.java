package com.vemmio.interview;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class TunnelFactoryImpl implements TunnelFactory {
    private static final Logger LOGGER = getLogger(TunnelFactoryImpl.class);

    @Override
    public Tunnel create(TunnelId id) throws TunnelNotStartedException {
        LOGGER.debug("Creating tunnel {}", id);
        return new TunnelImpl(id);
    }
}
