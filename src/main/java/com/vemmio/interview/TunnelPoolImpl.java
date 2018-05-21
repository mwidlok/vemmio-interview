package com.vemmio.interview;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

public class TunnelPoolImpl implements TunnelPool {
    private static final Logger LOGGER = getLogger(TunnelPoolImpl.class);
    private final TunnelFactory factory;
    private final ScheduledExecutorService executorService;
    private final Map<TunnelId, Future<Tunnel>> tunnels = new ConcurrentHashMap<>();

    public TunnelPoolImpl(TunnelFactory factory, ScheduledExecutorService executorService) {
        this.factory = factory;
        this.executorService = executorService;
    }

    @Override
    public Future<Tunnel> get(TunnelId id) throws TunnelPoolShutdownException {
        LOGGER.debug("Getting tunnel with id {}", id);
        if (executorService.isShutdown()) {
            throw new TunnelPoolShutdownException();
        }

        Future<Tunnel> existingTunnelTask = tunnels.get(id);
        if (existingTunnelTask == null) {
            return getTunnelTask(id);
        }

        if (!existingTunnelTask.isDone()) {
            return existingTunnelTask;
        }

        try {
            existingTunnelTask.get();
        } catch (Exception e) {
            LOGGER.debug("Error when getting Tunnel with id {}", id);
            remove(id);
            return getTunnelTask(id);
        }

        return existingTunnelTask;
    }

    @Override
    public Collection<Future<Tunnel>> getAll() {
        return tunnels.values();
    }

    @Override
    public void close(TunnelId id) {
        try {
            Tunnel tunnel = tunnels.get(id).get();
            tunnel.close();
        } catch (Exception e) {
            LOGGER.debug("Error during closing tunnel with id {}", id);
            remove(id);
        }
    }

    @Override
    public void shutdown() {
        LOGGER.debug("Starting shutdown operation");
        tunnels.forEach((k, v) -> close(k));
        executorService.shutdown();
        LOGGER.debug("Shutdown operation is completed");
    }

    private Future<Tunnel> getTunnelTask(TunnelId id) {
        Future<Tunnel> tunnelTask = executorService.submit(createTunnelTask(id));
        tunnels.put(id, tunnelTask);
        return tunnelTask;
    }

    private Callable<Tunnel> createTunnelTask(TunnelId id) {
        return () -> factory.create(id);
    }

    private void remove(TunnelId id) {
        if (tunnels.containsKey(id)) {
            tunnels.remove(id);
        }
    }
}
