package com.murdock.books.net.chapter6;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author weipeng2k 2023-10-01 11:49:50
 */
public class InetAddressTest {

    @Test(expected = UnknownHostException.class)
    public void getByNameString() throws Exception {
        InetAddress address = InetAddress.getByName("none");
        System.out.println(address);
    }

    /**
     * <pre>
     * taobao.com，表示查询域名服务器地址
     * www.taobao.com，表示查询taobao的域名服务，其中www服务器的地址
     *
     * host为空串，返回本机回环地址
     *
     * 会使用{@link java.net.InetAddress.NameService}来完成域名解析
     * 该接口的扩展分为两种，一种是使用文件的HostsFileNameService，另一种是PlatformNameService
     * NameService提供了根据域名查询InetAddress的能力，以及根据IP查询Host的能力
     * 在构建InetAddress时，会保留hostname和ip
     *
     * 在实现层面java会call系统的socket库，也就是通过jni来调用gethostbyname方法来获取到主机名
     * 返回的InetAddress是多个，默认取第一个
     *
     * </pre>
     *
     * @throws Exception ex
     */
    @Test
    public void getByNameStringInternet() throws Exception {
        InetAddress address = InetAddress.getByName("www.taobao.com");
        System.out.println(address);
    }

    @Test
    public void getAllByNameStringInternet() throws Exception {
        InetAddress[] allByName = InetAddress.getAllByName("www.taobao.com");
        Arrays.stream(allByName)
                .forEach(System.out::println);
    }

    /**
     * 使用ip造型，利用{@link sun.net.util.IPAddressUtil}来解析ip字符串，获得byte[] ip
     *
     * @throws Exception ex
     */
    @Test
    public void getByNameIpInternet() throws Exception {
        InetAddress address = InetAddress.getByName("39.156.66.10");
        System.out.println(address);
        address.getHostName();
        System.out.println(address);

        for (byte addressAddress : address.getAddress()) {
            System.err.println(addressAddress < 0 ? addressAddress + 256 : addressAddress);
        }
    }

    /**
     * isReach，利用了icmp的echo
     *
     * @throws Exception ex
     */
    @Test
    public void isReach() throws Exception {
        InetAddress address = InetAddress.getByName("192.168.31.93");
        boolean reachable = address.isReachable(3000);
        System.out.println(reachable);
    }
}
