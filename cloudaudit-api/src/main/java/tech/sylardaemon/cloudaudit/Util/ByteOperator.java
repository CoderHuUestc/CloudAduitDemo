package tech.sylardaemon.cloudaudit.Util;

import java.util.Base64;

public class ByteOperator {
    /**
     * 将byte数组转换为字符串
     * @param data byte数组
     * @return 字符串
     */
    public static String byteToString(byte [] data){
        return Base64.getEncoder().encodeToString(data);
    }
    /**
     * 将字符串转换位byte数组
     * @param data 字符串
     * @return byte数组
     */
    public static byte [] stringToByte(String data){
        return Base64.getDecoder().decode(data);
    }
    /**
     * 拼接两个byte数组，x | y
     * @param x byte数组一
     * @param y byte数组二
     * @return 返回新的拼接byte数组
     */
    public static byte [] GlueTwoBytes(byte [] x , byte [] y){
        byte [] result = new byte [x.length + y.length];
        int i = 0;
        for (; i < x.length ;i++){
            result[i] = x[i];
        }
        for (int j = 0;j < y.length ; i++,j++){
            result[i] = y[j];
        }
        return result;
    }

    /**
     * 比较两个byte数组是否一致
     * @param x byte数组一
     * @param y byte数组二
     * @return 一致为true，不一致为false
     */
    public static boolean compareTwoBytes(byte [] x,byte [] y){
        if (x.length != y.length ){
            return false;
        }
        for (int i = 0 ; i < x.length ; i++){
            if (x[i] != y[i]){
                return false;
            }
        }
        return true;
    }
}
