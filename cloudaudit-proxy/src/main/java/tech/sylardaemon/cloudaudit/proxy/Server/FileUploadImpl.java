package tech.sylardaemon.cloudaudit.proxy.Server;

import org.springframework.beans.factory.annotation.Autowired;
import tech.sylardaemon.cloudaudit.Util.ByteOperator;
import tech.sylardaemon.cloudaudit.Util.ResponseMessage;
import tech.sylardaemon.cloudaudit.api.FileUpload;
import tech.sylardaemon.cloudaudit.proxy.Service.FileService;
import tech.sylardaemon.cloudaudit.proxy.Service.UserService;

import java.io.IOException;
import java.io.InputStream;

public class FileUploadImpl implements FileUpload {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Override
    public ResponseMessage readyToUpload(String user_token, String file_name, String file_type, byte[] file_md5, long file_size) {
        if (!this.userService.checkValidation(user_token)){
            //token失效
            return new ResponseMessage(ResponseMessage.TOKEN_ERROR,"Token失效，请重新登录");
        }

        String strFileMD5 = ByteOperator.byteToString(file_md5);
        int total_pieces = FileService.getNumOfPartsAfterSlice(file_size);


        switch ( this.fileService.checkFileExistByDB(strFileMD5) ){
            case 0:
                //完全存在
                fileService.makeUserOwnOneFile(this.userService.getUserId(user_token),this.fileService.getFileIdByMD5(strFileMD5));
                return new ResponseMessage(ResponseMessage.SUCCESS,"文件上传完毕");
            case -1:
                //没有
                //则数据库中添加记录
            case 1:
                //缺块
                //但数据库中有，则不用添加记录,但需要准备环境
                fileService.readyToUpload(file_name , file_type , strFileMD5 , total_pieces);
        }
        return new ResponseMessage(ResponseMessage.NEED_TO_REUPLOAD,"文件需要重传");
    }

    @Override
    public ResponseMessage uploadBlock(String user_token, byte[] file_md5, byte[] piece_md5, int file_index, InputStream in) {
        if (!userService.checkValidation(user_token)){
            //token失效
            return new ResponseMessage(ResponseMessage.TOKEN_ERROR,"Token失效，请重新登录");
        }

        try{
            fileService.saveOneBlock(file_index ,ByteOperator.byteToString(piece_md5),ByteOperator.byteToString(file_md5), in);
            return new ResponseMessage(ResponseMessage.SUCCESS,"上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("上传失败");
            return new ResponseMessage(ResponseMessage.BLOCK_TO_REUPLOAD,"上传块失败");
        }

    }

    @Override
    public ResponseMessage getUnuploadPieceIndex(String user_token, byte[] file_md5) {
        if (!userService.checkValidation(user_token)){
            //token失效
            return new ResponseMessage(ResponseMessage.TOKEN_ERROR,"Token失效，请重新登录");
        }

        String strFileMD5 = ByteOperator.byteToString(file_md5);

        if (fileService.checkUploadComplete(strFileMD5)){
            fileService.makeUserOwnOneFile(this.userService.getUserId(user_token),this.fileService.getFileIdByMD5(strFileMD5));
            return new ResponseMessage(ResponseMessage.SUCCESS,"上传完成");
        }
        boolean [] unUploadIndex = fileService.getUnUploadPieceIndex(strFileMD5);
        return new ResponseMessage(ResponseMessage.SUCCESS,"请求未上传的索引值",unUploadIndex);
    }

}
