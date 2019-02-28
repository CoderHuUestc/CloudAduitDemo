package tech.sylardaemon.cloudaudit.api;

import tech.sylardaemon.cloudaudit.Util.ResponseMessage;

import java.io.InputStream;

public interface FileUpload {

    long DEFAULT_FILE_BLOCK_SIZE = 33554432L;

    /**
     * 文件上传准备
     * @param user_token 用户凭证
     * @param file_name 文件名称
     * @param file_type 文件类型
     * @param file_md5 文件MD5值
     * @param file_size 文件大小，单位为字节
     * @return 回应消息：格式为Code和消息
     */
    ResponseMessage readyToUpload(String user_token, String file_name, String file_type, byte[] file_md5, long file_size);

    /**
     * 上传文件的一块
     * @param user_token 用户凭证
     * @param file_md5 文件MD5值
     * @param piece_md5 上传这一块的MD5值
     * @param file_index 上传文件块的索引值
     * @param in 文件块输入流
     * @return 回应消息：格式为Code和消息
     */
    ResponseMessage uploadBlock(String user_token, byte[] file_md5, byte[] piece_md5, int file_index, InputStream in);

    /**
     * 获取该文件还未上传块的索引
     * @param user_token 用户凭证
     * @param file_md5 文件MD5值
     * @return 回应消息：格式为Code、消息、和boolean[]，其中boolean数组中false指该块未上传
     */
    ResponseMessage getUnuploadPieceIndex(String user_token, byte[] file_md5);
}
