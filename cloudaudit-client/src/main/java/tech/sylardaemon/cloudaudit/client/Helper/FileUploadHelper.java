package tech.sylardaemon.cloudaudit.client.Helper;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.context.ApplicationContext;
import tech.sylardaemon.cloudaudit.Util.GeneralHash;
import tech.sylardaemon.cloudaudit.Util.ResponseMessage;
import tech.sylardaemon.cloudaudit.api.FileUpload;
import tech.sylardaemon.cloudaudit.client.Entity.FileData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUploadHelper {

    private static final int DEFAULT_TEMP_BLOCK_SIZE = 2048;

    @Reference
    private FileUpload uploadTool;

    private GeneralHash tool;

    private String token;

    private boolean [] readyToIndexs;

    public FileUploadHelper(FileUpload fileUpload){
        this.uploadTool = fileUpload;
        this.tool = new GeneralHash(GeneralHash.HashMode.MD5);
    }

    public FileUploadHelper(ApplicationContext context){
        this((FileUpload) context.getBean("fileUpload"));
    }

    public void uploadOneFile(Path filePath){
        if (token == null){
            return;
        }
        FileData fileData = getFileBaseInfo(filePath);
        ResponseMessage responseMessage;

        //准备上传
        responseMessage = uploadTool.readyToUpload(token,fileData.getFilename(),fileData.getFiletype(),fileData.getFilemd5(),fileData.getFilesize());

        if (responseMessage.getCode() == ResponseMessage.SUCCESS){
            //上传成功，妙传
            System.out.println("文件"+ fileData.getFilename() + responseMessage.getMessage());
        }else if (responseMessage.getCode() == ResponseMessage.NEED_TO_REUPLOAD){
            int tryToYUploadPieceIndex = 0;

            while((tryToYUploadPieceIndex = getWhichPieceOfFile(fileData.getFilemd5())) > 0){
                for (int i = 0 ; i < this.readyToIndexs.length ;i++){
                    try {
                        if (!readyToIndexs[i]){
                            tryToUploadOneBlock(i,fileData.getFilemd5(),filePath);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(fileData.getFilename() + "上传失败");
                    }
                }
            }
            if (tryToYUploadPieceIndex == -1){
                System.out.println("获取失败");
            }else {
                System.out.println("上传完成");
            }
        }
    }


    private FileData getFileBaseInfo(Path fileroute){
        String filename = fileroute.getFileName().toString();
        int indexOfDot = filename.lastIndexOf(".");
        String filetype = filename.substring(indexOfDot + 1);
        filename = filename.substring(0,indexOfDot);
        byte[] filemd5 = tool.Hash(fileroute);
        long filesize = 0;
        try {
            filesize = Files.size(fileroute);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件大小读取出错");
        }

        FileData result = new FileData();
        result.setFilename(filename);
        result.setFiletype(filetype);
        result.setFilemd5(filemd5);
        result.setFilesize(filesize);

        return result;
    }

    /**
     * 尝试上传一块文件
     * @param index 文件块index值
     * @param filemd5 文件MD5值
     * @param fileroute 文件路径
     * @throws IOException
     */
    private void tryToUploadOneBlock(int index,byte[] filemd5,Path fileroute) throws IOException {
        System.out.println("开始上传" + fileroute.getFileName() + "的第" + index + "个Block");

        //跳转到开始读的地方：
        //比如第一块：0 ~ DEFAULT_FILE_BLOCK_SIZE-1
        //第二块则是：DEFAULT_FILE_BLOCK_SIZE ~ DEFAULT_FILE_BLOCK_SIZE*2 - 1
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileroute.toFile(),"r");
        randomAccessFile.seek(index * FileUpload.DEFAULT_FILE_BLOCK_SIZE);

        byte[] piece_hash = this.tool.Hash(randomAccessFile,FileUpload.DEFAULT_FILE_BLOCK_SIZE);
        randomAccessFile.close();

        ResponseMessage responseMessage;
        Path tempPath = createTempBlockToUpload(index,fileroute);
        if (tempPath == null){
            System.out.println("创建临时文件失败");
            throw new IOException("创建临时文件失败");
        }
        InputStream dataTransfer = Files.newInputStream(tempPath);
        responseMessage = this.uploadTool.uploadBlock(this.token,filemd5,piece_hash,index,dataTransfer);


        if (responseMessage.getCode() == ResponseMessage.SUCCESS){
            System.out.println(responseMessage.getMessage());
            Files.delete(tempPath);
            dataTransfer.close();
            this.readyToIndexs[index] = true;
        }else if (responseMessage.getCode() == ResponseMessage.BLOCK_TO_REUPLOAD){
            int i = -1;
            do {
                ++i;
                //重传3次
                System.out.println("上传失败，重新上传");
                dataTransfer = Files.newInputStream(tempPath);
                responseMessage = this.uploadTool.uploadBlock(this.token,filemd5,piece_hash,index,dataTransfer);
                System.out.println(responseMessage.getMessage());
                if (responseMessage.getCode() == ResponseMessage.SUCCESS){
                    break;
                }
            }while(i < 3);
        }
    }

    /**
     * 获取文件的那一块还没上传
     * @param file_md5 文件的MD5值
     * @return 0代表上传完成，大于0的代表未上传文件块索引值，-1则获取失败
     */
    private int getWhichPieceOfFile(byte [] file_md5){
        ResponseMessage responseMessage = this.uploadTool.getUnuploadPieceIndex(this.token,file_md5);
        if (responseMessage.getCode() == ResponseMessage.SUCCESS){
            System.out.println(responseMessage.getMessage());
            this.readyToIndexs = (boolean []) responseMessage.getData();
            if (this.readyToIndexs == null){
                return 0;
            }
            for (int i = 0 ; i < this.readyToIndexs.length ;i++){
                if (!this.readyToIndexs[i]){
                    return i + 1;
                }
            }
            return 0;
        }else{
            System.out.println(responseMessage.getMessage());
            return -1;
        }
    }

    /**
     * 创建一个临时的块数据文件准备去上传
     * @param index 文件块的索引值
     * @param fileRoute 文件的路径
     * @return 文件块的路径
     */
    private Path createTempBlockToUpload(int index,Path fileRoute){
        //创建临时的块数据文件：源文件名 + .block + index值
        Path result = fileRoute.getParent().resolve(fileRoute.getFileName() + ".block" + index);
        try {
            //跳转到开始读的地方：
            //比如第一块：0 ~ DEFAULT_FILE_BLOCK_SIZE-1
            //第二块则是：DEFAULT_FILE_BLOCK_SIZE ~ DEFAULT_FILE_BLOCK_SIZE*2 - 1
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileRoute.toFile(),"r");
            randomAccessFile.seek(index * FileUpload.DEFAULT_FILE_BLOCK_SIZE);

            //复写临时文件数据块
            if (!Files.exists(result)){
                Files.createFile(result);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(result.toFile(),false);

            int count = -1;
            long total = 0;
            byte[] b = new byte[DEFAULT_TEMP_BLOCK_SIZE];
            while((count = randomAccessFile.read(b)) != -1){
                fileOutputStream.write(b,0,count);
                fileOutputStream.flush();
                total += count;
                if (total >= FileUpload.DEFAULT_FILE_BLOCK_SIZE){
                    //读取到一定块大小则跳出
                    break;
                }
            }

            randomAccessFile.close();
            fileOutputStream.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
