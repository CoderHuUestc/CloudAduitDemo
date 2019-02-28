package tech.sylardaemon.cloudaudit.Util.LocalKey;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 公钥接口，只需要获取密钥
 */
public interface PublicKey {
    /**
     * 公钥每个分量字节长度
     */
    int LENGTH = 128;

    /**
     * 获取公钥组
     * @return 如果是客户端：返回[u,g,v]，如果是代理签名方[vv,w]
     */
    Element[] get();
}
