package com.bt.ecommerce.primary.service;

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
        String fileType = determineFileType(file);
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
        String awsUrl = generatePublicUrl(s3Key);
        return awsUrl;
    }

    private String determineFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            // Check for image files
            if (contentType.startsWith("image/")) {
                return "images";
            }
            // Check for PDF files
            else if (contentType.equals("application/pdf")) {
                return "pdfs";
            }
            // Check for video files
            else if (contentType.startsWith("video/")) {
                return "videos";
            }
            // Check for document files (Word, Excel, PowerPoint, Text)
            else if (contentType.equals("application/msword") ||
                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || // Word .docx
                    contentType.equals("application/vnd.ms-excel") ||
                    contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || // Excel .xlsx
                    contentType.equals("application/vnd.ms-powerpoint") ||
                    contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation") || // PowerPoint .pptx
                    contentType.equals("text/plain")) { // Text documents .txt
                return "documents";
            }
        }
        // Default case: return "others" for unknown file types
        return "others";
    }

    private String generatePublicUrl(String s3Key) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3Key;
    }
}

