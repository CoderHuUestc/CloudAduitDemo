package tech.sylardaemon.cloudaudit.proxy.Entity;

import tech.sylardaemon.cloudaudit.Util.ByteOperator;
import tech.sylardaemon.cloudaudit.Util.GeneralHash;

public class User {

    private int user_id;

    private String username;

    private String password;

    private long valid_time;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getValid_time() {
        return valid_time;
    }

    public void setValid_time(long valid_time) {
        this.valid_time = valid_time;
    }

    public static String getToken(User user){
        StringBuffer temp = new StringBuffer(user.getUser_id());
        temp.append(user.getUsername());
        temp.append(user.getPassword());

        GeneralHash hashTool = new GeneralHash(GeneralHash.HashMode.MD5);
        return ByteOperator.byteToString(hashTool.Hash(temp.toString().getBytes()));
    }


}
