package tech.sylardaemon.cloudaudit.Util.LocalKey.ProxySigner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Resource.PairingGetter;
import tech.sylardaemon.cloudaudit.Util.LocalKey.SecretKey;

public class AuditSecretKey implements SecretKey {
    /**
     * 用户的审计私钥，PP = P + z*e
     */
    private Element PP;
    /**
     * Zr域中的随机元素z
     */
    private Element z;

    /**
     * 生成用户审计私钥
     * @param pairing JPBC中的Pairing，为A类
     * @param P 用户发来的元素P，P = h(证书,Q)*x + y
     * @param e 元素e，e = h(证书，Q)
     */
    AuditSecretKey(Pairing pairing,Element P,Element e){
        this.z = pairing.getZr().newRandomElement();
        Element ze = this.z.duplicate();
        ze.mul(e);
        this.PP = P.duplicate().add(ze);
    }
    /**
     * 生成用户审计私钥
     * @param P 用户发来的元素P，P = h(证书,Q)*x + y
     * @param e 元素e，e = h(证书，Q)
     */
    AuditSecretKey(Element P,Element e){
        this(PairingGetter.PAIRING,P,e);
    }

    /**
     * 读取出的用户审计私钥
     * @param pairing JPBC中的Pairing，为A类
     * @param PP 读取的PP byte数组
     * @param z 读取的z byte数组
     */
    AuditSecretKey(Pairing pairing,byte [] PP,byte [] z){
        this.z = pairing.getZr().newElementFromBytes(z);
        this.PP = pairing.getZr().newElementFromBytes(PP);
    }
    /**
     * 读取出的用户审计私钥
     * @param PP 读取的PP byte数组
     * @param z 读取的z byte数组
     */
    AuditSecretKey(byte [] PP,byte [] z){
        this(PairingGetter.PAIRING,PP,z);
    }

    /**
     * 获取审计私钥PP
     * @return 审计私钥PP
     */
    @Override
    public Element get() {
        return this.PP.duplicate();
    }

    public Element getZ(){
        return this.z.duplicate();
    }
}
