package com.murdock.books.net.chapter6;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Consumer;

import static java.lang.System.out;

/**
 * <pre>
 * 一个物理或者虚拟的接口，也可以是绑定于统一个物理硬件的虚拟接口，可以用软件实现一个接口，比如：127.0.0.1的回环地址
 * </pre>
 *
 * @author weipeng2k 2023-10-03 10:29:43
 */
public class NetworkInterfaceTest {

    /**
     * <pre>
     *  en0: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
     * 	options=6460<TSO4,TSO6,CHANNEL_IO,PARTIAL_CSUM,ZEROINVERT_CSUM>
     * 	ether f0:18:98:1f:91:e4
     * 	inet6 fe80::c87:928b:b44b:cdb5%en0 prefixlen 64 secured scopeid 0x6
     * 	inet 192.168.31.134 netmask 0xffffff00 broadcast 192.168.31.255
     * 	nd6 options=201<PERFORMNUD,DAD>
     * 	media: autoselect
     * 	status: active
     * </pre>
     *
     * @throws Exception ex
     */
    @Test
    public void getByNameString() throws Exception {
        NetworkInterface en0 = NetworkInterface.getByName("en0");
        if (en0 != null) {
            displayInterfaceInformation(en0);
        }
    }

    @Test
    public void getByNameStringLocalhost() throws Exception {
        NetworkInterface lo0 = NetworkInterface.getByName("lo0");
        if (lo0 != null) {
            displayInterfaceInformation(lo0);
        }
    }

    /**
     * <pre>
     * 获取全部的NetworkInterface，每个interface上分配的IP，做详情输出
     * </pre>
     *
     * @throws Exception ex
     */
    @Test
    public void getAllByName() throws Exception {
        Consumer<NetworkInterface> consumer = NetworkInterfaceTest::displayInterfaceInformation;
        Consumer<NetworkInterface> andThen = consumer.andThen(NetworkInterfaceTest::println);
        NetworkInterface.networkInterfaces()
                .forEach(andThen);
    }

    static void println(NetworkInterface netint) {
        out.println("=======================");
    }

    /**
     * <pre>
     * 您可以使用isUP()方法发现网络interface是否“启动”(即正在运行)。下列方法指示网络interface类型：
     *
     * isLoopback()指示网络interface是否为回送interface。
     *
     * isPointToPoint()表示该interface是否为点对点interface。
     *
     * isVirtual()指示interface是否为虚拟interface。
     *
     * supportsMulticast()方法指示网络interface是否支持多播。 getHardwareAddress()方法返回网络interface的物理硬件地址(通常称为 MAC 地址)(如果可用)。 getMTU()方法返回最大传输单元(MTU)，它是最大的数据包大小。
     * </pre>
     *
     * @param netint ni
     */
    static void displayInterfaceInformation(NetworkInterface netint) {
        try {
            out.printf("Display name: %s\n", netint.getDisplayName());
            out.printf("Name: %s\n", netint.getName());
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                out.printf("InetAddress: %s\n", inetAddress);
            }

            out.printf("Up? %s\n", netint.isUp());
            out.printf("Loopback? %s\n", netint.isLoopback());
            out.printf("PointToPoint? %s\n", netint.isPointToPoint());
            out.printf("Supports multicast? %s\n", netint.supportsMulticast());
            out.printf("Virtual? %s\n", netint.isVirtual());
            out.printf("Hardware address: %s\n", bytesToHexMac(netint.getHardwareAddress()));
            out.printf("MTU: %s\n", netint.getMTU());
            out.print("\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static String bytesToHexMac(byte[] bytes) {
        if (bytes != null) {
            StringBuilder buf = new StringBuilder(bytes.length * 2);
            for (int i = 0; i < bytes.length; i++) {
                buf.append(String.format("%02x", bytes[i] & 0xff));
                if (i != bytes.length - 1) {
                    buf.append(":");
                }
            }
            return buf.toString();
        } else {
            return null;
        }
    }
}
