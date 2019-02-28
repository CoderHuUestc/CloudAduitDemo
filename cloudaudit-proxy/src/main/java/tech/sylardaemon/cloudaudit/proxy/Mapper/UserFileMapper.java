package tech.sylardaemon.cloudaudit.proxy.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserFileMapper {

    @Insert("INSERT INTO ca_user_file(user_id,file_id) VALUES(#{user_id},#{file_id});")
    int makeUserOwnOneFile(@Param("user_id")int user_id,@Param("file_id") int file_id);

    @Select("SELECT file_id FROM ca_user_file WHERE user_id = #{user_id};")
    int[] selectUsersFile(@Param("user_id")int user_id);

    @Select("SELECT user_id FROM ca_user_file WHERE file_id = #{file_id};")
    int[] selectFilesOwner(@Param("file_id")int file_id);

    @Select("SELECT COUNT(user_id) FROM ca_user_file WHERE user_id = #{user_id} AND file_id = #{file_id};")
    int checkUserOwnFile(@Param("user_id")int user_id,@Param("file_id") int file_id);

}
