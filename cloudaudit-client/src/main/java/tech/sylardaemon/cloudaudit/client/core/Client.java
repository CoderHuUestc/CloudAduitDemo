package tech.sylardaemon.cloudaudit.client.core;

import it.unisa.dia.gas.jpbc.Element;
import tech.sylardaemon.cloudaudit.Util.GeneralHash;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Client.ClientKeyPair;
import tech.sylardaemon.cloudaudit.Util.LocalKey.PublicKey;
import tech.sylardaemon.cloudaudit.Util.LocalKey.SecretKey;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Clien 客户端
 */
public class Client {
    /**
     * 用户公私对
     */
    private ClientKeyPair keyPair;
    /**
     * 存储公私钥的文件夹:keystore
     */
    private final Path keyStoreRoute;
    /**
     * 存储私钥的文件路径
     */
    private final Path secKeyRoute;
    /**
     * 存储公钥的文件路径
     */
    private final Path pubKeyRoute;
    /**
     * HASH值计算工具
     */
    private final GeneralHash hashTool;
    /**
     * 存储将发送文件的文件夹
     */
    private Path localFileStorage;

    /**
     * 客户端初始化，同时初始公私钥对
     */
    public Client(GeneralHash.HashMode mode, Path localFileStorage){
        //初始化计算HASH工具
        hashTool = new GeneralHash(mode);
        //初始化密钥相关路径
        keyStoreRoute   = Paths.get("./keystore");
        secKeyRoute     = Paths.get("./keystore/sec_key");
        pubKeyRoute     = Paths.get("./keystore/pub_key");
        //初始化文件存储路径
        this.localFileStorage = localFileStorage;

        initialKeyPair();
    }

    public Client(GeneralHash.HashMode mode){
        this(mode,Paths.get("D:\\TEMP_FILE\\Client\\"));
    }


    public Client(){
        this(GeneralHash.HashMode.MD5);
    }

    /**
     * 初始化客户端公私钥对，并进行本地文件存储
     */
    private void initialKeyPair(){
        System.out.println("开始初始化密钥...");

        //首先尝试从本地读取公私钥
        if (!readKeyFromFile()){
            System.out.println("读取出错...");
            System.out.println("开始重新生成...");
            //未存储公私钥，则重新生成
            keyPair = new ClientKeyPair();
            //并且写入本地
            writeKeyToFile();
        }
        System.out.println("私钥存储路径:" + this.secKeyRoute.toAbsolutePath());
        System.out.println("公钥存储路径:" + this.pubKeyRoute.toAbsolutePath());
    }

    /**
     * 公私钥写入文件，数据持久化存储
     * @return 成功与否
     */
    private boolean writeKeyToFile(){
        System.out.println("开始将密钥存储到本地...");
        try{
            if (!Files.exists(keyStoreRoute)){
                Files.createDirectory(keyStoreRoute);
            }
            if (!Files.exists(pubKeyRoute)){
                Files.createFile(pubKeyRoute);
            }
            if (!Files.exists(secKeyRoute)){
                Files.createFile(secKeyRoute);
            }
            FileOutputStream s_out = new FileOutputStream(secKeyRoute.toFile(),false);
            FileOutputStream p_out = new FileOutputStream(pubKeyRoute.toFile(),false);

            s_out.write(this.keyPair.getSecretKey().get().toBytes());

            Element [] publicKey = this.keyPair.getPublicKey().get();

            for (int i = 0 ; i < publicKey.length; i++){
                p_out.write(publicKey[i].toBytes());
            }

            s_out.close();
            p_out.close();
            System.out.println("密钥存储成功...");
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("密钥存储失败...");
            return false;
        }
        return true;
    }

    /**
     * 从文件读取公私钥
     * @return 读取成功则返回true，失败返回false
     */
    private boolean readKeyFromFile(){
        System.out.println("开始从本地读取密钥...");

        try{
            FileInputStream s_in = null;
            FileInputStream p_in = null;
            if (isKeyStoreExist()){
                s_in = new FileInputStream(secKeyRoute.toFile());
                p_in = new FileInputStream(pubKeyRoute.toFile());
            }
            if (s_in == null){
                return false;
            }
            byte [] sec_key = new byte[SecretKey.LENGTH],
                    pub_u = new byte [PublicKey.LENGTH],
                    pub_g = new byte [PublicKey.LENGTH],
                    pub_v = new byte [PublicKey.LENGTH];

            s_in.read(sec_key,
                    0,
                    SecretKey.LENGTH);

            p_in.read(pub_u,
                    0,
                    PublicKey.LENGTH);
            p_in.read(pub_g,
                    0,
                    PublicKey.LENGTH);
            p_in.read(pub_v,
                    0,
                    PublicKey.LENGTH);

            this.keyPair = new ClientKeyPair(sec_key,pub_u,pub_g,pub_v);

            s_in.close();
            p_in.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("密钥读取失败...");
            return false;
        }
        System.out.println("密钥读取成功...");
        return true;
    }

    /**
     * 判断是否能够在本地找到公私钥
     * @return 找到与否
     */
    private boolean isKeyStoreExist(){
        return Files.exists(keyStoreRoute) && Files.exists(secKeyRoute) && Files.exists(pubKeyRoute);
    }

    public Path getLocalFileStorage() {
        return localFileStorage;
    }

    public void setLocalFileStorage(Path localFileStorage) {
        this.localFileStorage = localFileStorage;
    }

}
