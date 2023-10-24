package com.murdock.books.net.chapter10;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Optional;

/**
 * @author weipeng2k 2023-10-24 10:35:24
 */
public class DayTimeServer {

    /**
     * current server port
     */
    private final int port;
    private final String host;
    /**
     * status
     */
    private Status status = Status.OFF;
    /**
     * server
     */
    private ServerSocket serverSocket;

    public DayTimeServer(int port) {
        this(null, port);
    }

    public DayTimeServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        if (cas(Status.OFF, Status.ON)) {
            if (serverSocket == null) {
                try {
                    serverSocket = new ServerSocket();
                    SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
                    serverSocket.bind(socketAddress);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                Socket accept = serverSocket.accept();
                PrintWriter writer = new PrintWriter(accept.getOutputStream(), true);
                writer.println(new Date());
                accept.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close() {
        if (cas(Status.ON, Status.OFF)) {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public InetAddress getInetAddr() {
        return Optional.ofNullable(serverSocket)
                .map(ServerSocket::getInetAddress)
                .orElse(null);
    }

    boolean cas(Status current, Status target) {
        boolean changed = false;

        if (status == current) {
            status = target;
            changed = true;
        }
        return changed;
    }

    enum Status {
        ON,
        OFF
    }


}
