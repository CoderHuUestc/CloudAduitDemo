package tech.sylardaemon.cloudaudit.Util.LocalKey.Client;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import tech.sylardaemon.cloudaudit.Util.LocalKey.PublicKey;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Resource.PairingGetter;
import tech.sylardaemon.cloudaudit.Util.LocalKey.SecretKey;

/**
 * 该类的状态值完全封装在类中，想要获取相关值只能得到副本
 */
public class ClientPublicKey implements PublicKey {
    /**
     * 公钥u，G1群的随机值
     */
    private Element u;
    /**
     * 公钥g，G2群的随机值
     */
    private Element g;
    /**
     * 公钥v，值为g^x
     */
    private Element v;

    /**
     * 基于私钥x，生成公钥
     * @param pairing JPBC中的Pairing，为A类
     * @param secureKey 通过私钥生成相对的公钥
     */
    public ClientPublicKey(Pairing pairing, SecretKey secureKey){
        this.u = pairing.getG1().newRandomElement();
        this.g = pairing.getG2().newRandomElement();
        this.v = g.duplicate().powZn(secureKey.get());
    }
    /**
     * 基于私钥x，生成公钥
     * @param secureKey 通过私钥生成相对的公钥
     */
    public ClientPublicKey(SecretKey secureKey){
        this(PairingGetter.PAIRING,secureKey);
    }

    /**
     * 已被存储的直接重新生成
     * @param pairing JPBC中的Pairing，为A类
     * @param u 公钥u，为G1群随机值
     * @param g 公钥g，为G2群随机值
     * @param v 公钥v，g^x
     */
    public ClientPublicKey(Pairing pairing, byte[] u, byte[] g, byte[] v){
        this.u = pairing.getG1().newElementFromBytes(u);
        this.g = pairing.getG2().newElementFromBytes(g);
        this.v = pairing.getG2().newElementFromBytes(v);
    }
    /**
     * 已被存储的直接重新生成
     * @param u 公钥u，为G1群随机值
     * @param g 公钥g，为G2群随机值
     * @param v 公钥v，g^x
     */
    public ClientPublicKey(byte[] u, byte[] g, byte[] v){
        this(PairingGetter.PAIRING,u,g,v);
    }


    /**
     * 获取公钥，同样的返回拷贝，计算时原公钥则不会被改变
     * @return u、g、v的Element数组
     */
    @Override
    public Element[] get() {
        return new Element[]{ this.u.duplicate() , this.g.duplicate() , this.v.duplicate() };
    }
}
