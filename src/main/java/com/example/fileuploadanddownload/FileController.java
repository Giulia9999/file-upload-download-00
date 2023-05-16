package com.example.fileuploadanddownload;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(fileService.upload(multipartFile));
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(fileService.download(fileName, response));
    }
}
