package com.murdock.books.net.chapter11;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author weipeng2k 2024-01-31 15:41:58
 */
public class LettuceTest {
    static RedisCommands<String, String> syncCommands;

    static RedisClient redisClient;

    @BeforeClass
    public static void init() {
        redisClient = RedisClient.create("redis://SYWjypw982426!@sh-crs-hqm4fgcr.sql.tencentcdb.com:23877/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        syncCommands = connection.sync();
    }

    /**
     * Lettuce cost:349684ms.
     */
    @Test
    public void writeAndRead() {
        long start = System.currentTimeMillis();
        IntStream.range(0, 10_000)
                .forEach(i -> {
                    syncCommands.set("REDIS" + i, String.valueOf(i));
                    syncCommands.get("REDIS" + i);
                });
        System.out.println("Lettuce cost:" + (System.currentTimeMillis() - start) + "ms.");
    }

    @AfterClass
    public static void stop() {
        redisClient.close();
    }
}
