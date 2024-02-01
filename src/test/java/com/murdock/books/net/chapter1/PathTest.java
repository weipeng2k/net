package com.murdock.books.net.chapter1;

import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

/**
 * @author weipeng2k 2023-12-22 10:31:48
 */
public class PathTest {

    @Test
    public void get() throws Exception {
        // 不推荐了
        // Paths.get("pom.xml");
        // working folder is project directory.
        Path path = Path.of("pom.xml");
        System.out.println(path.toAbsolutePath());

        File file = new File("pom.xml");
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsolutePath());

        file = new File("bin/abc/../pom.xml");
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());

    }

    @Test
    public void relative() {
        Path path = Path.of("/test.pom");
        // /test.pom
        System.out.println(path.toAbsolutePath());
    }

    /**
     * 与File的Canonical类似
     */
    @Test
    public void notation() {
        Path path = Path.of("/home", "admin/app/./trade/../bin/start.sh");
        System.out.println(path.toAbsolutePath());
        Path normalize = path.normalize();
        System.out.println("NORMALIZE:" + normalize.toAbsolutePath());
    }

    @Test
    public void create_by_uri() {
        URI uri = URI.create("file:///home/admin/bin/../start.sh");
        Path path = Path.of(uri);
        System.out.println(path.toAbsolutePath());
        System.out.println(path.normalize().toAbsolutePath());
    }

    /**
     * 类似File#getName
     */
    @Test
    public void get_file() {
        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.getFileName());
        File file = new File("/home/admin/app/abc.jar");
        System.out.println(file.getName());
    }

    @Test
    public void get_root() {
        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.getRoot());
//        File file = new File("/home/admin/app/abc.jar");
//        System.out.println(file.getParent());
    }

    @Test
    public void get_parent() {
        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.getParent());
        File file = new File("/home/admin/app/abc.jar");
        System.out.println(file.getParent());
    }

    @Test
    public void get_number_element() {
        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.getNameCount());
//        File file = new File("/home/admin/app/abc.jar");
//        System.out.println(file.getName());
    }

    @Test
    public void sub() {
        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.subpath(0, 3));
//        File file = new File("/home/admin/app/abc.jar");
//        System.out.println(file.getName());
    }

    /**
     * 小写，膈应
     */
    @Test
    public void to_uri() {
        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.toUri());
    }

    @Test(expected = Exception.class)
    public void to_real() throws Exception {
        Path path1 = Path.of("pom.xml");
        Path realPath = path1.toRealPath();
        System.out.println(realPath.toAbsolutePath());

        Path path = Path.of("/home/admin/app/abc.jar");
        System.out.println(path.getClass());
        path.toRealPath();
    }

}
