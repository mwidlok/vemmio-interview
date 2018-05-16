package com.vemmio.interview;

import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class TunnelImpl implements Tunnel {
    private static final Logger LOGGER = getLogger(TunnelImpl.class);
    private final TunnelId id;

    public TunnelImpl(TunnelId id) throws TunnelNotStartedException {
        this.id = id;
        startTunnel(id);
    }

    private void startTunnel(TunnelId id) throws TunnelNotStartedException {
        LOGGER.debug("Starting tunnel {}", id);
        try {
            TimeUnit.SECONDS.sleep(id.getIndex());
            if (id.getIndex() % 2 == 0) {
                throw new TunnelNotStartedException();
            }
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    @Override
    public void close() {
        LOGGER.debug("Closing tunnel {}", id);
    }
}
