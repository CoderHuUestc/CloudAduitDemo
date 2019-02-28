package tech.sylardaemon.cloudaudit.Util.LocalKey.Client;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Resource.PairingGetter;
import tech.sylardaemon.cloudaudit.Util.LocalKey.SecretKey;

/**
 * 该类的状态值完全封装在类中，想要获取相关值只能得到副本
 */
public class ClientSecretKey implements SecretKey {
    /**
     * Zr域的一个数
     */
    private Element x;

    /**
     * 生成随机私钥
     * @param pairing JPBC中的Pairing，为A类
     */
    public ClientSecretKey(Pairing pairing){
        x = pairing.getZr().newRandomElement();
    }

    /**
     * 生成随机私钥
     */
    public ClientSecretKey(){
        this(PairingGetter.PAIRING);
    }
    /**
     * 已被存储的直接重新生成
     * @param pairing JPBC中的Pairing，为A类
     * @param sec_key 私钥的byte数组
     */
    public ClientSecretKey(Pairing pairing , byte [] sec_key){
        x = pairing.getZr().newElementFromBytes(sec_key);
    }
    /**
     * 已被存储的直接重新生成
     * @param sec_key 私钥的byte数组
     */
    public ClientSecretKey(byte [] sec_key){
        this(PairingGetter.PAIRING,sec_key);
    }

    /**
     * 只能够得到私钥的拷贝，这样原私钥在计算的时候就不会被改变
     * @return 私钥的数值
     */
    @Override
    public Element get() {
        return x.duplicate();
    }
}
