package com.vemmio.interview;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

public class TunnelPoolTest {
    private final TunnelFactory factory = mock(TunnelFactory.class);
    private final Tunnel tunnel1 = mock(Tunnel.class);
    private final Tunnel tunnel2 = mock(Tunnel.class);
    private final TunnelPool pool = new TunnelPoolImpl(factory, Executors.newSingleThreadScheduledExecutor());

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void beforeEach() throws TunnelNotStartedException {
        when(factory.create(any())).thenReturn(tunnel1, tunnel2);
    }

    @Test
    public void shouldOpenNewTunnel() throws Exception {
        TunnelId id = new TunnelId("foobar", 1);

        pool.get(id).get();

        verify(factory, only()).create(id);
    }

    @Test
    public void shouldUseAlreadyOpenedTunnel() throws Exception {
        TunnelId id1 = new TunnelId("foobar", 1);
        TunnelId id2 = new TunnelId("foobar", 1);

        pool.get(id1).get();
        pool.get(id2).get();

        verify(factory, only()).create(id1);
    }

    @Test
    public void shouldRemoveTunnelFromPoolWhenExceptionWasThrownDuringOpening() throws Exception {
        TunnelId id = new TunnelId("foobar", 1);
        when(factory.create(any()))
                .thenThrow(TunnelNotStartedException.class)
                .thenReturn(tunnel1);

        try {
            pool.get(id).get();
        } catch (ExecutionException e) {
            // tunnel should be removed from pool
        }
        pool.get(id).get();

        verify(factory, times(2)).create(id);
    }

    @Test
    public void shouldCloseTunnel() throws Exception {
        TunnelId id = new TunnelId("foobar", 1);

        Tunnel tunnel = pool.get(id).get();
        pool.close(id);

        verify(tunnel, times(1)).close();
    }

    @Test
    public void shouldCloseAllTunnelsOnShutdown() throws Exception {
        TunnelId id1 = new TunnelId("foobar", 1);
        TunnelId id2 = new TunnelId("foobar", 2);

        Tunnel tunnel1 = pool.get(id1).get();
        Tunnel tunnel2 = pool.get(id2).get();
        pool.shutdown();

        verify(tunnel1, only()).close();
        verify(tunnel2, only()).close();
    }

    @Test
    public void shouldNotOpenNewTunnelsOnShutdown() throws Exception {
        TunnelId id1 = new TunnelId("foobar", 1);
        TunnelId id2 = new TunnelId("foobar", 2);

        pool.get(id1).get();
        pool.shutdown();

        exception.expect(TunnelPoolShutdownException.class);
        pool.get(id2).get();
    }
}