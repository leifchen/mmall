package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 配置文件工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
public class FtpUtils {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    private static String ftpIp = PropertiesUtils.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtils.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtils.getProperty("ftp.password");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FtpUtils(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtils ftpUtils = new FtpUtils(ftpIp, 21, ftpUser, ftpPassword);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtils.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}");
        return result;
    }

    /**
     * 上传文件到FTP服务器的指定位置
     * @param remotePath 远程服务器指定位置
     * @param fileList   文件列表
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        // 连接FTP服务器
        if (connectServer(ip, user, pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    FileInputStream fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;
            } finally {
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * 连接FTP服务
     * @param ip   FTP的IP
     * @param user FTP的用户
     * @param pwd  FTP的密码
     * @return
     */
    private boolean connectServer(String ip, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
