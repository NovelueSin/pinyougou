package com.pinyougou.manager.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传的控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-09-28<p>
 */
@RestController
public class UploadController {

    /** 定义文件服务器的访问url a.properties fileServerUrl=xxx */
    @Value("${fileServerUrl}")
    private String fileServerUrl;

    /** 文件上传 */
    @PostMapping("/upload")
    public Map<String,Object> upload(@RequestParam("file")MultipartFile multipartFile){
        Map<String, Object> data = new HashMap<>();
        data.put("status", 500);
        try{
            System.out.println("fileServerUrl: " + fileServerUrl);

            // 获取原文件名
            String originalFilename = multipartFile.getOriginalFilename();

            /** ######### 上传文件到FastDFS ######### */
            // 获取配置文件，得到它的绝对路径
            String confFilename = this.getClass().getResource("/fastdfs_client.conf").getPath();
            // 初始化客户端全局对象
            ClientGlobal.init(confFilename);
            // 创建存储客户端对象
            StorageClient storageClient = new StorageClient();
            // 上传文件到FastDFS
            // 第一个参数：上传文件的字节数组
            String[] arr = storageClient.upload_file(multipartFile.getBytes(),
                    FilenameUtils.getExtension(originalFilename),null);

            // http://192.168.12.131/ group1 / remote_name
            StringBuilder sb = new StringBuilder(fileServerUrl);
            for (String str : arr){
                sb.append("/" + str);
            }
            System.out.println("url: " + sb.toString());
            data.put("status", 200);
            data.put("url", sb.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
}
