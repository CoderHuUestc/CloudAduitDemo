package tech.sylardaemon.cloudaudit.api;

import tech.sylardaemon.cloudaudit.Util.ResponseMessage;

public interface UserManager {
    /**
     * 用户登录，返回登录凭证，再接下来上传文件都需要此凭证
     * @param username 用户名
     * @param password 用户密码
     * @return 登录凭证
     */
    public ResponseMessage login(String username, String password);

    /**
     * 用户登录，返回登录凭证，再接下来上传文件都需要此凭证
     * @param username 用户名
     * @param password 用户密码
     * @return 登录凭证
     */
    public ResponseMessage register(String username, String password);
}
