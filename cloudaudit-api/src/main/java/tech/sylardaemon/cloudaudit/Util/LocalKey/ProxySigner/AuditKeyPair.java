package tech.sylardaemon.cloudaudit.Util.LocalKey.ProxySigner;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Client.ClientPublicKey;
import tech.sylardaemon.cloudaudit.Util.LocalKey.KeyPair;
import tech.sylardaemon.cloudaudit.Util.LocalKey.Resource.PairingGetter;

public class AuditKeyPair extends KeyPair {

    /**
     * 审计密钥对初始化，需要客户端公钥，P、Q、e
     * @param pairing JPBC中的Pairing，A类
     * @param clientPublicKey 客户端公钥
     * @param P 用户发来的P
     * @param Q 用户发来的Q
     * @param e e = h(证书，Q)
     */
    public AuditKeyPair(Pairing pairing, ClientPublicKey clientPublicKey, Element P, Element Q, Element e){
        this.sk = new AuditSecretKey(pairing,P,e);
        this.pk = new AuditPublicKey((AuditSecretKey) this.sk,clientPublicKey,Q,e);
    }
    /**
     * 审计密钥对初始化，需要客户端公钥，P、Q、e
     * @param clientPublicKey 客户端公钥
     * @param P 用户发来的P
     * @param Q 用户发来的Q
     * @param e e = h(证书，Q)
     */
    public AuditKeyPair( ClientPublicKey clientPublicKey, Element P, Element Q, Element e){
       this(PairingGetter.PAIRING,clientPublicKey,P,Q,e);
    }

    /**
     * 审计密钥对初始化，直接读取byte数组初始化
     * @param pairing JPBC中的Pairing，A类
     * @param PP byte[] PP
     * @param z byte[] z
     * @param vv byte[] vv
     * @param w byte[] w
     */
    public AuditKeyPair(Pairing pairing,byte [] PP,byte [] z,byte [] vv,byte [] w){
        this.sk = new AuditSecretKey(pairing,PP,z);
        this.pk = new AuditPublicKey(pairing,vv,w);
    }
    /**
     * 审计密钥对初始化，直接读取byte数组初始化
     * @param PP byte[] PP
     * @param z byte[] z
     * @param vv byte[] vv
     * @param w byte[] w
     */
    public AuditKeyPair(byte [] PP,byte [] z,byte [] vv,byte [] w){
        this(PairingGetter.PAIRING,PP,z,vv,w);
    }
}
