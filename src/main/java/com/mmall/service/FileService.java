package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件Service
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
public interface FileService {

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    String upload(MultipartFile file, String path);
}
