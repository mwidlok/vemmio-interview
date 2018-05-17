package com.vemmio.interview;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private final Map<TunnelId, Tunnel> tunnels = new ConcurrentHashMap<>();
    private final Collection<Future<Tunnel>> tasks = Collections.synchronizedList(new ArrayList<>());

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

        Callable<Tunnel> createTunnelTask = createTunnel(id);
        return getTunnelTask(createTunnelTask);
    }

    @Override
    public Collection<Future<Tunnel>> getAll() {
        return tasks;
    }

    @Override
    public void close(TunnelId id) {
        Tunnel tunnel = tunnels.get(id);
        tunnel.close();
    }

    @Override
    public void shutdown() {
        LOGGER.debug("Starting shutdown operation");
        tunnels.forEach((k, v) -> close(k));
        executorService.shutdown();
        LOGGER.debug("Shutdown operation is completed");
    }

    private Future<Tunnel> getTunnelTask(Callable<Tunnel> createTunnelTask) {
        Future<Tunnel> task = executorService.submit(createTunnelTask);
        tasks.add(task);
        return task;
    }

    private Callable<Tunnel> createTunnel(TunnelId id) {
        return () -> {
            if (tunnels.containsKey(id)) {
                return tunnels.get(id);
            }

            Tunnel newTunnel = factory.create(id);

            // check if in the meantime tunnel was not created
            if (!tunnels.containsKey(id)) {
                tunnels.put(id, newTunnel);
            }

            return tunnels.get(id);
        };
    }
}
