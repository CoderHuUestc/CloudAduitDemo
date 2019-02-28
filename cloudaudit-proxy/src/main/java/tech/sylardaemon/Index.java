package tech.sylardaemon;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Index {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"classpath:spring/cloudaudit-proxy.xml",
                        "classpath:spring/spring-mybatis-config.xml"});
        context.start();
        System.out.println("start !");
        System.in.read();
    }
}
