package com.atguigu.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile) {

        String imgUrl = "http://192.168.118.128";

        String s = PmsUploadUtil.class.getResource("/tracker.conf").getPath();

        try {
            ClientGlobal.init(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageClient storageClient = new StorageClient(trackerServer,null);
        try {

            byte[] bytes = multipartFile.getBytes();

            String originalFilename = multipartFile.getOriginalFilename();

            int lastIndexOf = originalFilename.lastIndexOf(".");

            String substring = originalFilename.substring(lastIndexOf + 1);

            String[] uploadFile = storageClient.upload_file(bytes, substring, null);

            for(String uploadInfo:uploadFile){

                imgUrl += "/" + uploadInfo ;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imgUrl;
    }
}
