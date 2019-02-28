package tech.sylardaemon.cloudaudit.proxy.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.sylardaemon.cloudaudit.Util.ByteOperator;
import tech.sylardaemon.cloudaudit.Util.GeneralHash;
import tech.sylardaemon.cloudaudit.api.FileUpload;
import tech.sylardaemon.cloudaudit.proxy.Entity.FileData;
import tech.sylardaemon.cloudaudit.proxy.Mapper.FileBlockMapper;
import tech.sylardaemon.cloudaudit.proxy.Mapper.FileMapper;
import tech.sylardaemon.cloudaudit.proxy.Mapper.UserFileMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class FileService {
    /**
     * 创建临时的读取缓冲区大小，4K
     */
    private static final int TEMP_READ_BUFFER_SIZE = 4096;
    /**
     * 默认一块文件的大小
     */
    private static final long DEFAULT_FILE_BLOCK_SIZE = FileUpload.DEFAULT_FILE_BLOCK_SIZE;

    /**
     * 存储将发送文件的文件夹
     */
    private static Path localFileStorage = Paths.get("D:\\TEMP_FILE\\ProxySigner\\");

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserFileMapper userFileMapper;

    @Autowired
    private FileBlockMapper fileBlockMapper;


    /**
     * 服务端准备接收上传文件
     * @param filename 文件名
     * @param filetype 文件类型
     * @param filemd5 文件MD5值
     * @param totalpieces 文件分块后共多少块
     * @return 成功与否
     */
    public boolean readyToUpload(String filename,String filetype,String filemd5,int totalpieces){
        try {
            Path fileRoute = this.preparedEnvironment(filemd5,filename,filetype,totalpieces);
            if (fileMapper.selectFileIdByMD5(filemd5) == 0){
                //代表数据库中没有
                this.fileMapper.readyToUploadOneFile(filename,filetype,filemd5,new Date(),fileRoute.toString(),1,totalpieces);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 准备上传环境
     * 将在根目录下新建该文件MD5值的文件夹，文件夹中则有fileinfo文件其中指定文件的基本信息，然后就是各个文件的分块，以及分块的MD5值
     * @param filemd5 文件MD5值
     * @param filename 文件名
     * @param filetype 文件类型
     * @param totalpiece 文件总共多少块
     * @return 存文件块的文件夹路径
     * @throws IOException
     */
    private Path preparedEnvironment(String filemd5,String filename,String filetype,int totalpiece) throws IOException {
        //分块文件目录
        Path result = localFileStorage.resolve(filemd5);
        if (!Files.exists(result)){
            //建立该文件目录
            Files.createDirectories(result);
        }
        //fileinfo文件路径
        Path inforoute = localFileStorage.resolve(filemd5).resolve("fileinfo");
        //写入文件的类型值
        if (!Files.exists(inforoute))
        {
            Files.createFile(inforoute);
        }
        FileWriter writer = new FileWriter(inforoute.toFile(),false);
        writer.write(filemd5 + "\r\n");
        writer.write(filename + "\r\n");
        writer.write(filetype + "\r\n");
        writer.write(totalpiece + "\r\n");
        writer.flush();
        writer.close();

        Path block;
        Path blockMD5;
        //各个块创建好
        for(int i = 0 ; i < totalpiece ; i++){
            //块文件
            block = result.resolve(String.valueOf(i));
            if (!Files.exists(block)){
                Files.createFile(block);
            }
            //块文件的MD5值
            blockMD5 = result.resolve( i + ".md5");
            if (!Files.exists(blockMD5)){
                Files.createFile(blockMD5);
            }
        }
        return result;
    }




    /**
     * 存储文件的一个块
     * @param index 块索引值
     * @param piece_md5 块MD5值
     * @param file_md5 整个文件MD5值
     * @param in 读取块的输入流
     * @throws IOException
     */
    public void saveOneBlock(int index, String piece_md5, String file_md5, InputStream in) throws IOException {
        FileData data = this.fileMapper.selectFileDataByMD5(file_md5);
        System.out.println("接收第" + index + "块");
        Path fileDir = Paths.get(data.getFile_route());
        Path pieceFile = fileDir.resolve(String.valueOf(index));
        if (!Files.exists(pieceFile)){
            Files.createFile(pieceFile);
        }
        {
            //写入MD5
            try {
                Path pieceFileMD5 = fileDir.resolve(String.valueOf(index) + ".md5");
                if (!Files.exists(pieceFileMD5)){
                    Files.createFile(pieceFileMD5);
                }
                FileWriter fileWriter = new FileWriter(pieceFileMD5.toFile(),false);
                fileWriter.write(piece_md5);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件片段MD5写入出错");
            }
        }
        FileOutputStream out = new FileOutputStream(pieceFile.toFile(),false);
        int count = -1;
        long readLength = 0;
        byte [] b = new byte[TEMP_READ_BUFFER_SIZE];
        while ((count = in.read(b)) != -1){
            out.write(b,0,count);
            out.flush();
            readLength += count;
            if (readLength >= FileUpload.DEFAULT_FILE_BLOCK_SIZE){
                break;
            }
        }
        out.close();
        in.close();
        this.fileBlockMapper.addFilesOneBlock(data.getFile_id(),index);
    }

    /**
     * 获取该文件还未上传块的索引值
     * @param filemd5 文件MD5值
     * @return 类似位图
     */
    public boolean [] getUnUploadPieceIndex(String filemd5){
        FileData data = this.fileMapper.selectFileDataByMD5(filemd5);
        int [] indexs = this.fileBlockMapper.selectFileBlockIndexs(data.getFile_id());

        boolean [] result = new boolean[data.getTotal_pieces()];
        for (int i = 0 ; i < data.getTotal_pieces() ; i++){
            result[i] = false;
        }
        if (indexs == null){
            return result;
        }
        for (int index : indexs){
            result[index] = true;
        }
        return result;
    }

    /**
     * 检查文件上传完毕没有
     * @param filemd5 文件MD5值
     * @return 上传完毕则true，未完毕则false
     */
    public boolean checkUploadComplete(String filemd5){
        FileData data = fileMapper.selectFileDataByMD5(filemd5);
        if (data == null){
            return false;
        }
        if (checkFileAllBlockExist(data.getFile_id(),data.getTotal_pieces())) {
            //都存在，则上传成功
            if (data.getStatus() == 1){
                uploadComplete(filemd5);
            }
            return true;
        }
        return false;
    }
    /**
     * 检查文件是否存在
     * @param filemd5 文件MD5值
     * @return 完全存在则是0，缺块1，没有-1
     */
    public int checkFileExistByDB(String filemd5){
        FileData data = fileMapper.selectFileDataByMD5(filemd5);
        if (data == null){
            //数据库中不存在
            return -1;
        }
        //数据库中存在
        int status = data.getStatus();
        if (status == 1){
            //代表还未上传完成
            return 1;
        }else{
            //代表数据库中存在
            //现在来检查一下，各个块是否存在
            if (checkFileAllBlockExist(data.getFile_id(),data.getTotal_pieces())){
                //完全存在
                return 0;
            }else {
                //缺块
                return 1;
            }
        }
    }

    /**
     * 简单检查各个块是否存在
     * @param file_id 文件ID
     * @param totalPieces 文件一共多少块
     * @return 存在则true，不存在false
     */
    public boolean checkFileAllBlockExist(int file_id,int totalPieces){
        int dbTotalPieces = fileBlockMapper.selectCountOfBlocks(file_id);
        return totalPieces == dbTotalPieces;
    }


    /**
     * 完全检查：太过于耗时，不推荐使用
     * 从文件系统中检查文件是否正确存在
     * @param filemd5 文件MD5值
     * @return 正确存在true，否则false
     */
    public boolean checkFileExistByFS(String filemd5){
        String strFileDir = fileMapper.selectFileRouteByMD5(filemd5);
        if (strFileDir == null){
            return false;
        }
        Path fileDir = Paths.get(strFileDir);
        //初始化计算MD5工具
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("MD5算法找不到？。。");
            return false;
        }
        //获取文件info中的基本信息
        FileData data = getBasicFileDataByFileInfo(fileDir.resolve("fileinfo"));
        if (data == null){
            return false;
        }
        int numOfPieces = data.getStatus();
        //开始计算分块文件的HASH值
        Path tempBlockPath;
        try{
            for (int i = 0 ; i < numOfPieces ; i++){
                //块文件路径
                tempBlockPath = fileDir.resolve(String.valueOf(i));
                if (!Files.exists(tempBlockPath)){
                    //部分块文件不存在则代表文件不存在
                    return false;
                }
                InputStream in = Files.newInputStream(tempBlockPath);
                int count = -1;
                byte[] b = new byte[TEMP_READ_BUFFER_SIZE];
                while((count = in.read(b)) != -1){
                    md.update(b,0,count);
                }
                in.close();
            }
            //计算完毕，开始比较
            byte[] nowWeHave = md.digest();
            byte[] og = ByteOperator.stringToByte(filemd5);
            return ByteOperator.compareTwoBytes(nowWeHave,og);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取出错");
            return false;
        }
    }

    /**
     * 检查文件的一个块文件MD5值
     * @param filemd5 整个文件MD5值
     * @param index 块文件Index值
     * @return 正确则true，不正确false
     */
    public boolean checkFilesOneBlock(String filemd5,int index){
        Path blockPath = localFileStorage.resolve(filemd5).resolve(String.valueOf(index));
        Path blockMD5Path = localFileStorage.resolve(filemd5).resolve(index + ".md5");

        //如果文件都不存在则block不存在
        if (!Files.exists(blockPath)){
            return false;
        }
        if (!Files.exists(blockMD5Path)){
            return false;
        }

        try(
                InputStream blockIn = Files.newInputStream(blockPath);
                BufferedReader blockMD5In = Files.newBufferedReader(blockMD5Path)
        ) {
            GeneralHash tool = new GeneralHash(GeneralHash.HashMode.MD5);
            byte[] og = tool.Hash(blockIn);
            String strNowWeHave = blockMD5In.readLine();
            if (strNowWeHave == null){
                return false;
            }
            byte[] nowWeHave = ByteOperator.stringToByte(strNowWeHave);

            return ByteOperator.compareTwoBytes(og,nowWeHave);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("检查第" + index + "个块出错！");
            return false;
        }
    }

    /**
     * 是一个文件属于一个用户
     * @param user_id 用户id
     * @param file_id 文件id
     * @return 成功true，未成功false
     */
    public boolean makeUserOwnOneFile(int user_id,int file_id){

        if (userFileMapper.checkUserOwnFile(user_id,file_id) > 0){
            //如果已经拥有了
            return true;
        }

        return userFileMapper.makeUserOwnOneFile(user_id, file_id) > 0;
    }

    /**
     * 通过文件MD5值获取文件id
     * @param file_md5 文件MD5值
     * @return 文件ID，0代表没有
     */
    public int getFileIdByMD5(String file_md5){
        return fileMapper.selectFileIdByMD5(file_md5);
    }

    /**
     * 文件上传完成
     * @param filemd5 文件MD5值
     */
    public void uploadComplete(String filemd5){
        fileMapper.uploadCompeleteByMD5(filemd5,new Date());
    }

    /**
     * 从fileinfo文件中获取文件基本信息
     * @param fileInfoPath fileinfo文件路径
     * @return 文件基本信息，null则读取失败
     */
    public FileData getBasicFileDataByFileInfo(Path fileInfoPath){
        try(BufferedReader reader =  Files.newBufferedReader(fileInfoPath)) {
            String filemd5 = reader.readLine();
            String filename = reader.readLine();
            String filetype = reader.readLine();
            int totalPieces = Integer.parseInt(reader.readLine());

            FileData result = new FileData();
            result.setFile_md5(filemd5);
            result.setFile_name(filename);
            result.setFile_type(filetype);
            result.setStatus(totalPieces);

            return result;
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取该文件大小分切后将会生成多少块
     * @param filesize 整个文件大小
     * @return 分切后一共多少块
     */
    public static int getNumOfPartsAfterSlice(long filesize){
        return (int) Math.ceil(filesize / (double)DEFAULT_FILE_BLOCK_SIZE);
    }
}
