package com.murdock.books.net.chapter6;

import org.junit.Test;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ARPTest {

    private static final MacAddress SRC_MAC_ADDR = MacAddress.getByName("f0:18:98:1f:91:e4");

    private static MacAddress resolvedAddr;

    @Test
    public void arpRequest() throws Exception {
        // 本机IP
        String strSrcIpAddress = "192.168.31.139";
        // 目标IP，带查询MAC的IP
        String strDstIpAddress = "192.168.31.58";

        InetAddress addr = InetAddress.getByName(strSrcIpAddress);
        // 根据IP获取到对应的Pcap网络接口，可以理解获取到了en0网卡
        PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);

        // 监听网卡的流入数据，每个包监听的长度为65536 bytes
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        // 向网卡发送数据的入口，使用它来发送ARP请求报文
        PcapHandle sendHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        // 构建监听网卡流量运行任务的线程池
        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            // 设置监听流量的规则：监听ARP包，IP地址以及目标MAC地址是本机的包
            handle.setFilter(
                    "arp and src host "
                            + strDstIpAddress
                            + " and dst host "
                            + strSrcIpAddress
                            + " and ether dst "
                            + Pcaps.toBpfString(SRC_MAC_ADDR),
                    BpfProgram.BpfCompileMode.OPTIMIZE);
            // 对于ARP协议的包进行处理，且仅处理ARP响应报文，记录并打印
            Task t = new Task(handle, packet -> {
                if (packet.contains(ArpPacket.class)) {
                    ArpPacket arp = packet.get(ArpPacket.class);
                    if (arp.getHeader().getOperation().equals(ArpOperation.REPLY)) {
                        ARPTest.resolvedAddr = arp.getHeader().getSrcHardwareAddr();
                        System.err.println(packet);
                    }
                }
            });
            pool.execute(t);

            // 构建ARP报文，可以看到ARP被设计用在多种链路层上，而目标MAC地址设置为全0
            ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
            arpBuilder
                    .hardwareType(ArpHardwareType.ETHERNET)
                    .protocolType(EtherType.IPV4)
                    .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES)
                    .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                    .operation(ArpOperation.REQUEST)
                    .srcHardwareAddr(SRC_MAC_ADDR)
                    .srcProtocolAddr(InetAddress.getByName(strSrcIpAddress))
                    .dstHardwareAddr(MacAddress.getByAddress(new byte[]{0, 0, 0, 0, 0, 0}))
                    .dstProtocolAddr(InetAddress.getByName(strDstIpAddress));
            // 将ARP报文放置到以太网的分组中，目标MAC地址为：ff:ff:ff:ff:ff:ff
            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
                    .srcAddr(SRC_MAC_ADDR)
                    .type(EtherType.ARP)
                    .payloadBuilder(arpBuilder)
                    .paddingAtBuild(true);

            Packet p = etherBuilder.build();
            System.out.println(p);
            // 发送分组
            sendHandle.sendPacket(p);

            TimeUnit.SECONDS.sleep(2);
        } finally {
            if (handle.isOpen()) {
                handle.close();
            }
            if (sendHandle.isOpen()) {
                sendHandle.close();
            }
            if (!pool.isShutdown()) {
                pool.shutdown();
            }

            System.out.println(strDstIpAddress + " was resolved to " + resolvedAddr);
        }
    }

    private record Task(PcapHandle handle, PacketListener listener) implements Runnable {

        @Override
        public void run() {
            try {
                handle.loop(1, listener);
            } catch (Exception ex) {
                // Ignore.
            }
        }
    }
}
