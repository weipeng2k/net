package com.murdock.books.net.chapter6;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author weipeng2k 2023-11-11 21:23:06
 */
public class ClientTest {

    @Test
    public void p() {
        System.out.println((byte) 255);
    }

    @Test
    public void http() throws Exception {
        InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 80);
        BufferedReader bufferedReader;
        try (Socket socket = new Socket()) {
            // imply bind & connect
            socket.connect(socketAddress, 3000);

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("GET / HTTP/1.0\r\n");
            out.println("HOST: www.baidu.com\r\n");
            out.println("Accept-Encoding:gzip,deflate\r\n");
            out.println("Accept: */*\r\n");
            out.println("\r\n");
            out.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
