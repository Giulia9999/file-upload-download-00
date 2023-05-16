package com.example.fileuploadanddownload;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    public String upload(MultipartFile multipartFile) throws IOException {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String fileName = UUID.randomUUID().toString();
        String completeFileName = fileName + "." + extension;
        File finalFolder = new File(fileRepositoryFolder);
        if(!finalFolder.exists()) throw new IOException("File does not exist");
        if(!finalFolder.isDirectory()) throw new IOException("Directory does not exist");
        File finalDestination = new File(fileRepositoryFolder + "\\" + completeFileName);
        if(finalDestination.exists()) throw new IOException("file already exists");
        multipartFile.transferTo(finalDestination);
        return completeFileName;
    }

    public byte[] download(String fileName, HttpServletResponse response) throws IOException{
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension){
            case "jpg", "jpeg" -> response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            case "gif" -> response.setContentType(MediaType.IMAGE_GIF_VALUE);
            case "png" -> response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        File fileFromRepo = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepo.exists()) throw new IOException("File not found");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepo));
    }
}
