package com.murdock.books.net.chapter10;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author weipeng2k 2023-10-24 14:50:34
 */
public class ClientTester {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8083);
        while (true) {
            Socket accept = serverSocket.accept();
            Thread input = new Thread(new Input(accept.getInputStream()));
            input.start();
            Thread output = new Thread(new Output(accept.getOutputStream()));
            output.start();

            input.join();
            output.join();
        }
    }

    /**
     * input stream -> standard output
     */
    static class Input implements Runnable {

        InputStream inputStream;

        public Input(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int i = inputStream.read();
                    if (i == -1) {
                        break;
                    }
                    System.out.write(i);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * standard in -> output stream
     */
    static class Output implements Runnable {

        private PrintWriter writer;

        public Output(OutputStream outputStream) {
            writer = new PrintWriter(outputStream, true);
        }

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    if (line.equals(".")) {
                        break;
                    }
//                    System.err.println(line);
                    writer.println(line);
                    writer.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
