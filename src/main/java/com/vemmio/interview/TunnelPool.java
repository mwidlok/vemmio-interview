package com.vemmio.interview;

import java.util.Collection;
import java.util.concurrent.Future;

public interface TunnelPool {
    Future<Tunnel> get(TunnelId id) throws TunnelPoolShutdownException;

    Collection<Future<Tunnel>> getAll();

    void close(TunnelId id);

    void shutdown();
}
