package com.murdock.books.net.chapter10;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.IntStream;

/**
 * @author weipeng2k 2023-10-21 21:12:34
 */
public class LocalPortScannerTest {

    @Test
    public void scanLocalPort() {
        IntStream.range(1, 65536)
                .forEach(this::bindPort);
    }

    private void bindPort(int i) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(i);
        } catch (IOException e) {
            System.out.println("There is a server on port:" + i);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception ex) {

                }
            }
        }
    }

    @Test
    public void justWrite() {
        try {
            ServerSocket serverSocket = new ServerSocket(12201);
            Socket accept = serverSocket.accept();
            PrintWriter out = new PrintWriter(accept.getOutputStream());
            out.println("haha");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
