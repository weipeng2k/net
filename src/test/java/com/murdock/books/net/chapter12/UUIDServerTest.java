package com.murdock.books.net.chapter12;

import org.junit.Test;

/**
 * @author weipeng2k 2023-10-29 20:05:34
 */
public class UUIDServerTest {

    @Test
    public void startServer() throws Exception {
        UUIDServer uuidServer = new UUIDServer("localhost", 8090);
        uuidServer.start();
        Thread.sleep(10_000);
    }

}