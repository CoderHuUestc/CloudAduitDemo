package tech.sylardaemon.cloudaudit.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 不能使用静态方法来实现HASH值的计算，在多线程并发执行的时候，
 * 对于单一的MessageDigest对象会将输入的数据搞混.
 * 如果加锁，获取多个不相关文件的HASH值将会因为单一的MessageDigest而互相等待，降低执行效率，
 * 所以将该类设置为普通对象，当线程需要使用时，new出该工具，并且需要将该类封闭在单一线程中，
 * 如果多个线程获取了同一个该对象，同时进行HASH计算时将会出错!
 */
public class GeneralHash {
    /**
     * 可以使用的Hash算法
     */
    public enum HashMode {
        MD5,
        SHA256,
        SHA384,
        SHA512,
    }

    /**
     * 默认计算块大小，4K
     */
    private static final int DEFAULT_BLOCK_SIZE = 1024 * 4;
    /**
     * 工具
     */
    private MessageDigest tool;
    /**
     * 选择的HASH算法
     */
    private final HashMode mode;

    /**
     * 根据选择的HASH算法初始化工具
     * @param mode
     */
    public GeneralHash(HashMode mode){
        this.mode = mode;
        try {
            switch (this.mode) {
                case MD5:
                    tool = MessageDigest.getInstance("MD5");
                    break;
                case SHA256:
                    tool = MessageDigest.getInstance("SHA-256");
                    break;
                case SHA384:
                    tool = MessageDigest.getInstance("SHA-384");
                    break;
                case SHA512:
                    tool = MessageDigest.getInstance("SHA-512");
                    break;
                default:
                    //默认HASH算法为SHA-256
                    tool = MessageDigest.getInstance("SHA-256");
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对byte数组进行HASH计算
     * @param message 需要被计算的消息
     * @return 计算出来的HASH值
     */
    public byte[] Hash( byte[] message) {

        tool.update(message);

        return tool.digest();
    }

    /**
     * 通过输入流进行HASH计算
     * 计算完HASH值需要自己关闭输入流
     * @param in in 输入流
     * @return 计算出来的HASH值
     */
    public byte[] Hash( InputStream in) {
        try{
            int count = -1;
            byte[] b = new byte[DEFAULT_BLOCK_SIZE];
            while ((count = in.read(b)) != -1) {
                tool.update(b, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(GeneralHash.class + "：文件输入流读取出错");
            return null;
        }
        return tool.digest();
    }

    /**
     * 通过文件路径对文件进行HASH计算
     * @param fileroute 文件路径
     * @return 计算出来的HASH值
     */
    public byte[] Hash(Path fileroute){
        byte [] result = null;
        try(InputStream in = Files.newInputStream(fileroute)) {
            result = this.Hash(in);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(GeneralHash.class + "：文件输入流读取出错");
            return null;
        }
        return result;
    }

    /**
     * 对文件的一小块进行HASH计算
     * 需要自己关闭输入流
     * @param randomAccessFile 文件随机读取器，调用之前需要设置好位置
     * @param readSize 读取大小
     * @return 文件块HASH值
     */
    public byte[] Hash(RandomAccessFile randomAccessFile,long readSize){
        try{
            int count = -1;
            long total = 0;
            byte[] b = new byte[DEFAULT_BLOCK_SIZE];
            while ((count = randomAccessFile.read(b)) != -1) {
                tool.update(b, 0, count);
                total += count;
                if (total >= readSize){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(GeneralHash.class + "：文件输入流读取出错");
            return null;
        }
        return tool.digest();
    }

    /**
     * 获取初始化HASH工具的算法
     * @return 算法
     */
    public HashMode getMode(){
        return this.mode;
    }

}

