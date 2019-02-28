package tech.sylardaemon;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import tech.sylardaemon.cloudaudit.client.Helper.FileUploadHelper;
import tech.sylardaemon.cloudaudit.client.Helper.UserManagerHelper;
import tech.sylardaemon.cloudaudit.client.core.Client;

import java.nio.file.Path;

public class Index {
    //文件列表
    public static String [] fileList = {
            "(中文)零基础深度学习.pdf",
            "[程序员的思维修炼：开发认知潜能的九堂课（中文版）].(Andy.Hunt).崔康.扫描版.pdf",
            "[教程] 深度学习 (Bengio & Goodfellow).pdf",
            "《Hadoop核心指南》第三版.pdf",
            "《Java核心技术 卷II 高级特性(原书第9版)》（完整中文版）.pdf",
            "《Maven权威指南》(中文).pdf",
            "《鸟哥的Linux私房菜》.pdf",
            "flipped.txt",
            "神经网络设计.pdf",
            "实践论.pdf",
            "凸优化——影印版.pdf",
            "图灵程序设计丛书 算法 第4版.pdf",
            "微信公众平台企业应用开发实战.pdf",
            "学习vi和vim编辑器_第7版.pdf",
            "D:\\TEMP_FILE\\Client\\Marvels.The.Punisher.S01E01.3AM.1080p.NF.WEB-DL.DD5.1.x264-NTb.mkv"
    };

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring/cloudaudit-client.xml"});
        context.start();
        Client client = new Client();
        Path workPlace = client.getLocalFileStorage();

        UserManagerHelper userManagerHelper = new UserManagerHelper(context);
        FileUploadHelper fileUploadHelper = new FileUploadHelper(context);

        String token = userManagerHelper.loginToGetToken(args[0],args[1]);

        fileUploadHelper.setToken(token);

        for (String filename : fileList){
            fileUploadHelper.uploadOneFile(workPlace.resolve(filename));
        }


    }
}
