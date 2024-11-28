package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${AWS_ACCESS_KEY}")
    private String accessKey;

    @Value("${AWS_SECRET_KEY}")
    private String secretKey;

    private S3Client createS3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        S3Client s3Client = createS3Client();

        // Determine the file type and directory
        String fileType = ProjectUtils.determineFileType(file);
        String directory = fileType.equals("images") ? "images" :
                fileType.equals("pdfs") ? "pdfs" :
                        fileType.equals("videos") ? "videos" :
                                fileType.equals("documents") ? "documents" : "others";
        // Construct the S3 key (path in bucket)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String s3Key = directory + "/" + fileName;

        // Upload the file to S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Key)
                        //  .acl("public-read") // Grant public read access
                        .build(),
                RequestBody.fromBytes(file.getBytes())

        );

        // Return the S3 key
        String awsUrl = ProjectUtils.generatePublicUrl(bucketName, region,s3Key);
        return awsUrl;
    }


}

