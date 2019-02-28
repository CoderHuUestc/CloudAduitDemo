package tech.sylardaemon.cloudaudit.client.Helper;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.context.ApplicationContext;
import tech.sylardaemon.cloudaudit.Util.ByteOperator;
import tech.sylardaemon.cloudaudit.Util.GeneralHash;
import tech.sylardaemon.cloudaudit.Util.ResponseMessage;
import tech.sylardaemon.cloudaudit.api.UserManager;

public class UserManagerHelper {

    @Reference
    private UserManager userManager;

    private String username;

    private String password;

    private String token;

    private GeneralHash tool;

    public UserManagerHelper(UserManager userManager){
        this.userManager = userManager;
        this.tool = new GeneralHash(GeneralHash.HashMode.MD5);
    }

    public UserManagerHelper(ApplicationContext context){
        this((UserManager) context.getBean("userManager"));
    }

    /**
     * 登录得到token
     * @param username 用户名
     * @param password 密码
     * @return 用户token
     */
    public String loginToGetToken(String username,String password){
        this.username = username;
        this.password = ByteOperator.byteToString(this.tool.Hash(password.getBytes()));

        this.relogin();
        return this.token;
    }

    /**
     * 尝试重新登录
     */
    public void relogin(){
        int tryTimes = 0;
        do {

            if (this.login()){
                return;
            }

            ++tryTimes;
        }while (tryTimes < 3);
        System.exit(0);
    }

    /**
     * 登录动作
     * @return 登陆成功true，失败false
     */
    private boolean login(){
        boolean isSuccess = false;
        //登录获取返回消息
        ResponseMessage responseMessage = this.userManager.login(this.username,this.password);
        if (responseMessage.getCode() == ResponseMessage.SUCCESS){
            //成功，token得到
            this.token = (String) responseMessage.getData();
            isSuccess = true;
        }
        //打印消息
        System.out.println(responseMessage.getMessage());
        return isSuccess;
    }

    public String getToken() {
        return token;
    }
}
