package com.leyou.upload.service;

import com.leyou.upload.controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    private static final List<String> content_types = Arrays.asList("image/gif","image/jpeg");
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String  origionalname = multipartFile.getOriginalFilename();
        //verify type
        String contentType = multipartFile.getContentType();
        if(!content_types.contains(contentType)){
            logger.warn("file upload failed "+origionalname);
            return null;
        }

        //verify content
        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
        if (bufferedImage == null) {
            logger.warn("file content failed");
            return null;
        }
        //save
        multipartFile.transferTo(new File("C:\\Users\\Agape\\IdeaProjects\\leyou\\images\\"+origionalname));
        //retuen url path
        return "http://image.leyou.com/"+origionalname;


    }
}




