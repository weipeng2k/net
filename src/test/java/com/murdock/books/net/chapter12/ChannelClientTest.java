package com.murdock.books.net.chapter12;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * <pre>
 * 使用Channel客户端访问
 * </pre>
 *
 * @author weipeng2k 2023-10-28 21:32:52
 */
public class ChannelClientTest {

    @Test
    public void blockingMode() throws IOException {
        // 准备channel，连接
        SocketChannel open = SocketChannel.open();
        // IP
        InetAddress address = InetAddress.getByName("www.china-pub.com");
        // Port，也就是TCP
        SocketAddress socketAddress = new InetSocketAddress(address, 80);
        // 连接socket
        open.connect(socketAddress);

        open.write(ByteBuffer.wrap("GET / HTTP/1.1\r\n".getBytes()));
        open.write(ByteBuffer.wrap("Host: www.china-pub.com\r\n".getBytes()));
        open.write(ByteBuffer.wrap("Accept: */*\r\n".getBytes()));
        open.write(ByteBuffer.wrap("\r\n".getBytes()));

        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);

        ByteBuffer byteBuffer = ByteBuffer.allocate(128);

        while (open.read(byteBuffer) != -1) {
            byteBuffer.flip();
            writableByteChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        open.close();
    }

    @Test
    public void unblockingMode() throws Exception {
        // 准备channel，连接
        SocketChannel open = SocketChannel.open();

        // IP
        InetAddress address = InetAddress.getByName("www.china-pub.com");
        // Port，也就是TCP
        SocketAddress socketAddress = new InetSocketAddress(address, 80);
        // 连接socket
        open.connect(socketAddress);
        // 配置blocking mode
        open.configureBlocking(false);
        open.write(ByteBuffer.wrap("GET / HTTP/1.1\r\n".getBytes()));
        open.write(ByteBuffer.wrap("Host: www.china-pub.com\r\n".getBytes()));
        open.write(ByteBuffer.wrap("Accept: */*\r\n".getBytes()));
        open.write(ByteBuffer.wrap("\r\n".getBytes()));

        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);

        ByteBuffer byteBuffer = ByteBuffer.allocate(128);

        while (true) {
            int read = open.read((byteBuffer));
            if (read > 0) {
                byteBuffer.flip();
                writableByteChannel.write(byteBuffer);
                byteBuffer.clear();
            } else if (read == -1) {
                break;
            }
        }

        open.close();
    }
}
