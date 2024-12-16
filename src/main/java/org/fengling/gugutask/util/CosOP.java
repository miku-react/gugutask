package org.fengling.gugutask.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class CosOP {

    private static String bucketName;
    private static String baseUrl;
    private static COSClient cosClient;

    public CosOP(COSClient cosClient) {
        CosOP.cosClient = cosClient;
    }

    public static String uploadFile(MultipartFile file, String folder) throws IOException {
        // 生成唯一文件名
        String key = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 将 MultipartFile 转为临时 File
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, tempFile);
            PutObjectResult result = cosClient.putObject(putObjectRequest);

            // 返回文件的访问 URL
            return baseUrl + "/" + key;
        } finally {
            // 删除临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Value("${tencent.cos.bucket-name}")
    public void setBucketName(String bucketName) {
        CosOP.bucketName = bucketName;
    }

    @Value("${tencent.cos.base-url}")
    public void setBaseUrl(String baseUrl) {
        CosOP.baseUrl = baseUrl;
    }
}