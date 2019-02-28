package tech.sylardaemon.cloudaudit.Util.LocalKey.Client;


import it.unisa.dia.gas.jpbc.Pairing;
import tech.sylardaemon.cloudaudit.Util.LocalKey.KeyPair;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Resource.PairingGetter;

public class ClientKeyPair extends KeyPair {

    /**
     * 客户端密钥对初始化
     * @param pairing JPBC中的Pairing，A类
     */
    public ClientKeyPair(Pairing pairing){
        sk = new ClientSecretKey(pairing);
        pk = new ClientPublicKey(pairing,sk);
    }

    /**
     * 客户端密钥对初始化
     */
    public ClientKeyPair(){
        this(PairingGetter.PAIRING);
    }

    /**
     * 客户端密钥对初始化
     * @param pairing JPBC中的Pairing，A类
     * @param sec_key 密钥byte
     * @param u 公钥u
     * @param g 公钥g
     * @param v 公钥v
     */
    public ClientKeyPair(Pairing pairing,byte [] sec_key,byte [] u,byte [] g,byte [] v){
        sk = new ClientSecretKey(pairing,sec_key);
        pk = new ClientPublicKey(pairing,u,g,v);
    }
    /**
     * 客户端密钥对初始化
     * @param sec_key 密钥byte
     * @param u 公钥u
     * @param g 公钥g
     * @param v 公钥v
     */
    public ClientKeyPair(byte [] sec_key,byte [] u,byte [] g,byte [] v){
        this(PairingGetter.PAIRING,sec_key,u,g,v);
    }

}
