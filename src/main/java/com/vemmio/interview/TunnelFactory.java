package com.vemmio.interview;

public interface TunnelFactory {
    Tunnel create(TunnelId id) throws TunnelNotStartedException;
}