package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.utils.ProjectUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileManagerService {

    @Autowired
    protected Cloudinary cloudinary;

    public String uploadCloudinaryFile(MultipartFile file) throws IOException {
        String folderName = ProjectUtils.getFolderName(file.getContentType());
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName));
        return uploadResult.get("url").toString();

    }


}
