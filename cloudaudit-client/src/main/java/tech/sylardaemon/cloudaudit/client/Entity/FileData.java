package tech.sylardaemon.cloudaudit.client.Entity;

public class FileData {
    private String filename;
    private String filetype;
    private byte[] filemd5;
    private long filesize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public byte[] getFilemd5() {
        return filemd5;
    }

    public void setFilemd5(byte[] filemd5) {
        this.filemd5 = filemd5;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }
}
