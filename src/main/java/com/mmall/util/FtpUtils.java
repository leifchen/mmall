package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

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
@Slf4j
public class FtpUtils {

    private static String ftpIp = PropertiesUtils.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtils.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtils.getProperty("ftp.password");

    private String ip;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    private FtpUtils(String ip, String user, String pwd) {
        this.ip = ip;
        this.user = user;
        this.pwd = pwd;
    }

    public static void uploadFile(List<File> fileList) throws IOException {
        FtpUtils ftpUtils = new FtpUtils(ftpIp, ftpUser, ftpPassword);
        log.info("开始连接ftp服务器");
        String remotePath = "img";
        ftpUtils.uploadFile(remotePath, fileList);
        log.info("开始连接ftp服务器,结束上传,上传结果:{}");
    }

    /**
     * 上传文件到FTP服务器的指定位置
     * @param remotePath 远程服务器指定位置
     * @param fileList   文件列表
     * @return
     * @throws IOException
     */
    private void uploadFile(String remotePath, List<File> fileList) throws IOException {
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
                log.error("上传文件异常", e);
            } finally {
                ftpClient.disconnect();
            }
        }
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
            log.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }
}
