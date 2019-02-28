package tech.sylardaemon.cloudaudit.proxy.Server;


import org.springframework.beans.factory.annotation.Autowired;
import tech.sylardaemon.cloudaudit.Util.ResponseMessage;
import tech.sylardaemon.cloudaudit.api.UserManager;
import tech.sylardaemon.cloudaudit.proxy.Entity.User;
import tech.sylardaemon.cloudaudit.proxy.Service.UserService;

public class UserManagerImpl implements UserManager {

    @Autowired
    private UserService userService;

    @Override
    public ResponseMessage login(String username, String password) {
        User user = userService.getUserByLogin(username,password);
        if (user != null){
            System.out.println("用户 " + username + " 登录成功");
            String token = userService.login(user);
            return new ResponseMessage(ResponseMessage.SUCCESS,"登录成功",token);
        }else {
            return new ResponseMessage(ResponseMessage.ERROR,"登录失败");
        }
    }

    @Override
    public ResponseMessage register(String username, String password) {
        if (userService.register(username,password)){

            return login(username,password);

        }else{
            return new ResponseMessage(ResponseMessage.ERROR,"登录失败");
        }
    }
}
