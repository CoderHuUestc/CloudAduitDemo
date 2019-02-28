package tech.sylardaemon.cloudaudit.Util;

import java.io.Serializable;

/**
 * 返回数据，json数据格式
 * @param <T> data的数据类型
 */
public class ResponseMessage<T> implements Serializable{

    /**
     * code成功值
     */
    public static final int SUCCESS = 0;
    /**
     * code失败值
     */
    public static final int ERROR = 1;

    /**
     * token失效
     */
    public static final int TOKEN_ERROR = 2;

    /**
     * 可以准备上传了
     */
    public static final int READY_TO_UPLOAD = 3;

    /**
     * 需要重新上传
     */
    public static final int NEED_TO_REUPLOAD = 4;

    /**
     * 块文件需要重传
     */
    public static final int BLOCK_TO_REUPLOAD = 5;


    /**
     * 返回数据：code
     */
    private int code;
    /**
     * 返回数据：消息
     */
    private String message;

    /**
     * 返回数据：相关数据
     */
    private T data;


    /**
     * 初始化返回数据
     * @param code 成功状态值：0代表成功，1代表错误
     * @param message 消息
     * @param data 返回的相关数据
     *
     */
    public ResponseMessage(int code, String message, T data){
        this.code       = code;
        this.message    = message;
        this.data       = data;
    }

    /**
     * 初始化返回数据，设置meta，data数据为null。只通知接口调用成功与否，以及相关状态
     * @param code
     * @param message
     */
    public ResponseMessage(int code , String message){
        this(code,message,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
