package com.murdock.books.net.chapter11;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.stream.IntStream;

/**
 * @author weipeng2k 2024-01-31 15:30:26
 */
public class JedisTest {

    static Jedis jedis;

    @BeforeClass
    public static void init() {
        jedis = new Jedis("sh-crs-hqm4fgcr.sql.tencentcdb.com", 23877);
        jedis.auth("SYWjypw982426!");
    }

    /**
     * Jedis cost:410630ms.
     */
    @Test
    public void writeAndRead() {
        long start = System.currentTimeMillis();
        IntStream.range(0, 10_000)
                .forEach(i -> {
                    jedis.set("redis" + i, String.valueOf(i));
                    jedis.get("redis" + i);
                });
        System.out.println("Jedis cost:" + (System.currentTimeMillis() - start) + "ms.");
    }

    @AfterClass
    public static void stop() {
        jedis.close();
    }
}
