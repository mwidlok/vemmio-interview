package com.vemmio.interview;

import org.slf4j.Logger;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class App {
    private static final Logger LOGGER = getLogger(App.class);

    public static void main(String[] args) throws TunnelPoolShutdownException, InterruptedException {
        TunnelFactory factory = new TunnelFactoryImpl();
        TunnelPool pool = new TunnelPoolImpl(factory, Executors.newScheduledThreadPool(4));

        openTunnels(pool);
        waitUntilAllTunnelsAreOpened(pool);

        pool.shutdown();
        LOGGER.debug("Application is closed");
    }

    private static void openTunnels(TunnelPool pool) throws TunnelPoolShutdownException {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            pool.get(new TunnelId("foobar", random.nextInt(10)));
        }
    }

    private static void waitUntilAllTunnelsAreOpened(TunnelPool pool) throws InterruptedException {
        while (!pool.getAll().stream().allMatch(Future::isDone)) {
            LOGGER.debug("waiting...");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
