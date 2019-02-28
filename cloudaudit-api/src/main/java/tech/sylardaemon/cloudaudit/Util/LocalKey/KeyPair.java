package tech.sylardaemon.cloudaudit.Util.LocalKey;


public abstract class KeyPair {
    /**
     * 私钥
     */
    protected SecretKey sk;
    /**
     * 公钥
     */
    protected PublicKey pk;

    /**
     * 获取私钥
     * @return SecretKey 私钥
     */
    public SecretKey getSecretKey() {
        return sk;
    }

    /**
     * 获取公钥
     * @return PublicKey 公钥
     */
    public PublicKey getPublicKey() {
        return pk;
    }
}
