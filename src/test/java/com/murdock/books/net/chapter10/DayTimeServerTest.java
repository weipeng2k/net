package com.murdock.books.net.chapter10;

import org.junit.Test;

/**
 * @author weipeng2k 2023-10-24 10:34:56
 */
public class DayTimeServerTest {

    @Test
    public void server() {
        DayTimeServer dayTimeServer = new DayTimeServer(8081);
        dayTimeServer.start();
        System.out.println(dayTimeServer.getInetAddr());

        dayTimeServer.close();
    }

    @Test
    public void out() {
        //
        DayTimeServer dayTimeServer = new DayTimeServer("192.168.31.133", 8081);
        dayTimeServer.start();
        System.out.println(dayTimeServer.getInetAddr());
        dayTimeServer.close();
    }
}
