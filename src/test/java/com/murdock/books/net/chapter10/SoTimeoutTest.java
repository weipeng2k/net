package com.murdock.books.net.chapter10;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author weipeng2k 2023-10-24 13:48:33
 */
public class SoTimeoutTest {

    @Test(expected = Exception.class)
    public void timeout() throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setSoTimeout(2000);
        InetSocketAddress localhost = new InetSocketAddress("localhost", 8082);
        serverSocket.bind(localhost);

        Socket accept = serverSocket.accept();
        accept.getInputStream().read();
        Thread.sleep(5000);
    }
}
