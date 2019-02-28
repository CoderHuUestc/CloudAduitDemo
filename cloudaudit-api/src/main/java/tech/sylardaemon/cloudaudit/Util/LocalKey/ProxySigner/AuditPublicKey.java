package tech.sylardaemon.cloudaudit.Util.LocalKey.ProxySigner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Client.ClientPublicKey;
import tech.sylardaemon.cloudaudit.Util.LocalKey.PublicKey;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Resource.PairingGetter;

public class AuditPublicKey implements PublicKey {
    /**
     * 审计公钥vv，同样G2群内，vv = v^e * g^z*e * Q
     */
    private Element vv;
    /**
     * 审计公钥w，G1群内，w = u^PP
     */
    private Element w;

    /**
     * 审计公钥根据用户公钥、审计私钥、用户发来的Q和计算e = h(证书，Q)生成
     * @param secretKey 审计私钥
     * @param clientPublicKey  用户公钥
     * @param Q 用户发来的Q
     * @param e e = h(证书，Q)
     */
    AuditPublicKey(AuditSecretKey secretKey, ClientPublicKey clientPublicKey, Element Q, Element e){
        //获取客户端公钥u、g、v
        Element [] ugv = clientPublicKey.get();
        Element v_e = ugv[2].powZn(e);
        Element g_ze = ugv[1].powZn( secretKey.getZ().mul(e) );
        this.vv = v_e.mul(g_ze);
        this.vv.mul(Q);
        this.w = ugv[0].powZn(secretKey.get());
    }


    /**
     * 审计公钥读取初始化
     * @param pairing JPBC中的Pairing，为A类
     * @param vv 读取的vv byte数组
     * @param w 读取的w byte数组
     */
    AuditPublicKey(Pairing pairing,byte [] vv,byte [] w){
        this.vv = pairing.getG2().newElementFromBytes(vv);
        this.w = pairing.getG1().newElementFromBytes(w);
    }


    /**
     * 审计公钥读取初始化
     * @param vv 读取的vv byte数组
     * @param w 读取的w byte数组
     */
    AuditPublicKey(byte [] vv,byte [] w){
        this(PairingGetter.PAIRING,vv,w);
    }
    /**
     * 获取公钥，同样的返回拷贝，计算时原公钥则不会被改变
     * @return vv、w的Element数组
     */
    @Override
    public Element[] get() {
        return new Element [] { vv.duplicate() , w.duplicate() };
    }
}
