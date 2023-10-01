package com.murdock.books.net.util;

/**
 * @author weipeng2k 2017年03月12日 上午11:24:04
 */
public class Util {
    public static int transmogrify(int data) {
        return Character.isLetter(data) ? data ^ ' ' : data;
    }
}
