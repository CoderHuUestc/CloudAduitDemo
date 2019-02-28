package tech.sylardaemon.cloudaudit.proxy.Mapper;

import org.apache.ibatis.annotations.*;
import tech.sylardaemon.cloudaudit.proxy.Entity.User;

@Mapper
public interface UserMapper {

    @Results({
            @Result(property = "user_id",column = "user_id"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
    })
    @Select("SELECT user_id,username,password FROM ca_user WHERE username = #{username} AND password = #{password};")
    User selectUserByLogin(@Param("username") String username, @Param("password") String password);


    @Insert("INSERT INTO ca_user(username,password) VALUES(#{username},#{password});")
    int addOneUser(@Param("username") String username, @Param("password") String password);
}
