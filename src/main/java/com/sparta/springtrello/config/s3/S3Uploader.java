package com.sparta.springtrello.config.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Uploader {

    private final AmazonS3Client s3Client;
    @Value("${s3.bucket}")
    private String bucket;

    public S3Uploader(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            // S3에 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
        } catch (Exception e) {
            throw new IllegalArgumentException("파일 업로드 실패", e);
        }

        // S3에 저장된 파일 URL 반환
        return s3Client.getUrl(bucket, fileName).toString();
    }
}
