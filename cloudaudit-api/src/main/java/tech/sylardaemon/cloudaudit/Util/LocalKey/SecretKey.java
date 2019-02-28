package tech.sylardaemon.cloudaudit.Util.LocalKey;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 私钥接口，只需要获取密钥
 */
public interface SecretKey {
    /**
     * 私钥字节长度
     */
    int LENGTH = 20;

    /**
     * 获取私钥
     * @return 得到私钥的副本
     */
    Element get();
}
