package com.murdock.books.net.chapter12;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * <pre>
 * 客户端连接上来，然后返回UUID数据
 * </pre>
 *
 * @author weipeng2k 2023-10-29 17:56:29
 */
public class UUIDServer {

    /**
     * hostname
     */
    private final String host;
    /**
     * listen port
     */
    private final int port;
    /**
     * server channel
     */
    private ServerSocketChannel serverSocketChannel;
    /**
     * selector
     */
    private Selector selector;

    public UUIDServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * <pre>
     * 启动服务端，将会监听指定的host和端口，对连接的socket发送uuid数据
     *
     * server准备阶段：
     * 构建server channel：这一步是构建了channel
     * binding到socket：channel连接到socket上，可以监听数据
     * 设置
     * 构建selector
     * 将channel注册到selector上，并关心accept，对于建立连接的事件进行关心
     * </pre>
     */
    public void start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress socketAddress = new InetSocketAddress(host, port);
            serverSocketChannel.bind(socketAddress);
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            begin();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <pre>
     * 开始
     * runtime运行时阶段：
     *       while (true) {
     *           selector.select: 选择得到selection-key
     *           循环处理selection-key：只有一个线程
     *           处理连接：如果是连接的，拿到连接
     *           处理写出：如果是可写的，准备好buf，写出数据
     *           }
     * </pre>
     */
    private void begin() throws Exception {
        while (true) {
            selector.select(this::handleSelectionKey);
        }
    }

    private void handleSelectionKey(SelectionKey selectionKey) {
        try {
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                // client
//                SocketChannel socketChannel = channel.accept();
//                socketChannel.configureBlocking(false);
//                ByteBuffer byteBuffer = ByteBuffer.allocate(128);
//                socketChannel.register(selector, SelectionKey.OP_WRITE, byteBuffer);
            } else if (selectionKey.isWritable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                if (byteBuffer.hasRemaining()) {
                    try {
                        byteBuffer.rewind();
                        byteBuffer.put(UUID.randomUUID().toString().getBytes());
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                    } catch (Exception ex) {
                        selectionKey.cancel();
                        socketChannel.close();
                    }
                } else {
                    byteBuffer.rewind();
                    byteBuffer.put(UUID.randomUUID().toString().getBytes());
                    byteBuffer.flip();
                    try {
                        socketChannel.write(byteBuffer);

                    } catch (Exception ex) {
                        selectionKey.cancel();
                        socketChannel.close();
                    }
                }

                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public void stop() {
        try {
            selector.close();
            serverSocketChannel.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
