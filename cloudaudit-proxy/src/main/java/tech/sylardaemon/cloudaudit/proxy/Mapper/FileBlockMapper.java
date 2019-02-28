package tech.sylardaemon.cloudaudit.proxy.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileBlockMapper {

    @Select("SELECT file_block_index FROM file_blocks WHERE file_id = #{file_id};")
    int [] selectFileBlockIndexs(@Param("file_id")int file_id);

    @Select("SELECT COUNT(file_block_index) FROM file_blocks WHERE file_id = #{file_id};")
    int selectCountOfBlocks(@Param("file_id") int file_id);

    @Insert("INSERT INTO file_blocks(file_id,file_block_index) VALUES(#{file_id},#{file_block_index});")
    int addFilesOneBlock(@Param("file_id")int file_id,@Param("file_block_index")int file_block_index);

}
