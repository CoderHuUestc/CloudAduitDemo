package tech.sylardaemon.cloudaudit.proxy.Entity;

import java.util.Date;

public class FileData {

    private int file_id;
    private String file_name;
    private String file_type;
    private String file_md5;
    private Date upload_time;
    private String file_route;
    private int status;
    private int total_pieces;

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }

    public Date getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Date upload_time) {
        this.upload_time = upload_time;
    }

    public String getFile_route() {
        return file_route;
    }

    public void setFile_route(String file_route) {
        this.file_route = file_route;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_pieces() {
        return total_pieces;
    }

    public void setTotal_pieces(int total_pieces) {
        this.total_pieces = total_pieces;
    }
}
