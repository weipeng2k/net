package com.murdock.books.net.chapter6;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
     * 会使用java.net.InetAddress.NameService来完成域名解析
     * </pre>
     *
     * @throws Exception ex
     */
    @Test
    public void getByNameStringInternet() throws Exception {
        InetAddress address = InetAddress.getByName("www.taobao.com");
        System.out.println(address);
    }
}
