package com.vemmio.interview;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

public class TunnelPoolImpl implements TunnelPool {
    private static final Logger LOGGER = getLogger(TunnelPoolImpl.class);
    private final TunnelFactory factory;
    private final ScheduledExecutorService executorService;

    public TunnelPoolImpl(TunnelFactory factory, ScheduledExecutorService executorService) {
        this.factory = factory;
        this.executorService = executorService;
    }

    @Override
    public Future<Tunnel> get(TunnelId id) throws TunnelPoolShutdownException {
        return null;
    }

    @Override
    public Collection<Future<Tunnel>> getAll() {
        return Collections.emptyList();
    }

    @Override
    public void close(TunnelId id) {
    }

    @Override
    public void shutdown() {
    }
}
