package com.murdock.books.net.chapter1;

import com.murdock.books.net.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <pre>
 * 单线程旧IO服务端
 * </pre>
 *
 * @author weipeng2k 2017年03月12日 上午11:18:49
 */
public class SimpleServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("accept socket @ " + socket);
            try (
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream()
            ) {
                int data;
                while ((data = in.read()) != -1) {
                    data = Util.transmogrify(data);
                    out.write(data);
                }
                System.out.println("read -1");
            }

        }
    }
}
