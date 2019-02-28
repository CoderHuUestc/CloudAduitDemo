package tech.sylardaemon.cloudaudit.proxy.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.sylardaemon.cloudaudit.proxy.Entity.User;
import tech.sylardaemon.cloudaudit.proxy.Mapper.UserMapper;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private static HashMap<String,User> userTokens;

    private static ReentrantReadWriteLock readWriteLock;

    private static ReentrantReadWriteLock.ReadLock readLock;

    private static ReentrantReadWriteLock.WriteLock writeLock;

    static {
        userTokens = new HashMap<>();
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
        System.out.println("用户服务加载完成");
    }


    public User getUserByLogin(String username, String password){
        return userMapper.selectUserByLogin(username,password);
    }

    public boolean register(String username,String password){
        if (userMapper.addOneUser(username,password) > 0){
            return true;
        }else{
            return false;
        }
    }

    public String login(User user){
        String token = User.getToken(user);
        user.setValid_time(System.currentTimeMillis() + 7200000L);
        writeLock.lock();
        userTokens.put(token,user);
        writeLock.unlock();
        return token;
    }

    public boolean checkValidation(String token){
        readLock.lock();
        User user = userTokens.get(token);
        if (user == null){
            readLock.unlock();
            return false;
        }
        readLock.unlock();
        long now = System.currentTimeMillis();
        if (now > user.getValid_time()){
            writeLock.lock();
            userTokens.remove(token);
            writeLock.unlock();
            return false;
        }
        return true;
    }

    /**
     * 通过token获取用户ID
     * @param token 用户token
     * @return 用户ID，如果未登录返回0
     */
    public int getUserId(String token){
        int x = 0;
        readLock.lock();
        User user = userTokens.get(token);
        if (user == null){
            readLock.unlock();
            return 0;
        }
        x = user.getUser_id();
        readLock.unlock();
        return x;
    }

}
