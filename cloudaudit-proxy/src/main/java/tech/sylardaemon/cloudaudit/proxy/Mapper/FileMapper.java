package tech.sylardaemon.cloudaudit.proxy.Mapper;

import org.apache.ibatis.annotations.*;
import tech.sylardaemon.cloudaudit.proxy.Entity.FileData;

import java.util.Date;

@Mapper
public interface FileMapper {

    /**
     * 通过文件MD5值获得文件ID
     * @param file_md5 文件MD5值
     * @return 文件ID值
     */
    @Select("SELECT IFNULL(MAX(file_id),0) FROM ca_file WHERE file_md5 = #{file_md5};")
    int selectFileIdByMD5(@Param("file_md5") String file_md5);

    /**
     * 通过文件ID获得文件MD5值
     * @param file_id 文件ID
     * @return 文件MD5值
     */
    @Select("SELECT file_md5 FROM ca_file WHERE file_id = #{file_id};")
    String selectFileMD5ById(@Param("file_id") int file_id);

    @Results({
            @Result(property = "file_id",column = "file_id"),
            @Result(property = "file_name",column = "file_name"),
            @Result(property = "file_type",column = "file_type"),
            @Result(property = "file_md5",column = "file_md5"),
            @Result(property = "upload_time",column = "upload_time"),
            @Result(property = "file_route",column = "file_route"),
            @Result(property = "status",column = "status"),
            @Result(property = "total_pieces",column = "total_pieces"),
    })
    @Select("SELECT file_id,file_name,file_type,file_md5,upload_time,file_route,status,total_pieces FROM ca_file WHERE file_md5 = #{file_md5};")
    FileData selectFileDataByMD5(@Param("file_md5") String file_md5);
    @Results({
            @Result(property = "file_id",column = "file_id"),
            @Result(property = "file_name",column = "file_name"),
            @Result(property = "file_type",column = "file_type"),
            @Result(property = "file_md5",column = "file_md5"),
            @Result(property = "upload_time",column = "upload_time"),
            @Result(property = "file_route",column = "file_route"),
            @Result(property = "status",column = "status"),
            @Result(property = "total_pieces",column = "total_pieces"),
    })
    @Select("SELECT file_id,file_name,file_type,file_md5,upload_time,file_route,status,total_pieces FROM ca_file WHERE file_id = #{file_id};")
    FileData selectFileDataById(@Param("file_id") int file_id);

    @Select("SELECT file_route FROM ca_file WHERE file_md5 = #{file_md5};")
    String selectFileRouteByMD5(@Param("file_md5") String file_md5);
    @Select("SELECT file_route FROM ca_file WHERE file_md5 = #{file_id};")
    String selectFileRouteById(@Param("file_id") String file_id);

    @Select("SELECT IFNULL(MAX(status),1) FROM ca_file WHERE file_md5 = #{file_md5};")
    int selectStatusByMD5(@Param("file_md5") String file_md5);
    @Select("SELECT IFNULL(MAX(status),1) FROM ca_file WHERE file_id = #{file_id};")
    int selectStatusById(@Param("file_id") int file_id);

    @Select("SELECT IFNULL(MAX(total_pieces),0) FROM ca_file WHERE file_md5 = #{file_md5};")
    int selectTotalPiecesByMD5(@Param("file_md5") String file_md5);
    @Select("SELECT IFNULL(MAX(total_pieces),0) FROM ca_file WHERE file_id = #{file_id};")
    int selectTotalPiecesById(@Param("file_id") int file_id);


    @Insert("INSERT INTO ca_file(file_name,file_type,file_md5,upload_time,file_route,status,total_pieces) VALUES( #{file_name} , #{file_type} , #{file_md5} , #{upload_time} , #{file_route} , #{status} , #{total_pieces});")
    int readyToUploadOneFile(@Param("file_name") String file_name, @Param("file_type") String file_type, @Param("file_md5") String file_md5, @Param("upload_time") Date upload_time, @Param("file_route") String file_route, @Param("status") int status,@Param("total_pieces") int total_pieces);

    @Update("UPDATE ca_file SET upload_time = #{upload_time} WHERE file_md5 = #{file_md5};")
    int updateUploadTime(@Param("upload_time") Date upload_time,@Param("file_md5")String file_md5);

    @Update("UPDATE ca_file SET status = 0,upload_time = #{upload_time} WHERE file_md5 = #{file_md5};")
    int uploadCompeleteByMD5(@Param("file_md5") String file_md5,@Param("upload_time") Date upload_time);
    @Update("UPDATE ca_file SET status = 0,upload_time = #{upload_time} WHERE file_id = #{file_id};")
    int uploadCompeleteById(@Param("file_id") int file_id,@Param("upload_time") Date upload_time);

    @Update("UPDATE ca_file SET status = 1 , upload_time = #{upload_time} WHERE file_id = #{file_id};")
    int afterCheckFileNeedToReUploadById(@Param("file_id") int file_id,@Param("upload_time") Date upload_time);
    @Update("UPDATE ca_file SET status = 1 , upload_time = #{upload_time} WHERE file_id = #{file_md5};")
    int afterCheckFileNeedToReUploadByMD5(@Param("file_md5") String file_md5,@Param("upload_time") Date upload_time);
}
