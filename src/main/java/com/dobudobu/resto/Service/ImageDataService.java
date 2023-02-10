package com.dobudobu.resto.Service;

import com.dobudobu.resto.Entity.ImageData;
import com.dobudobu.resto.Repository.ImageDataRepository;
import com.dobudobu.resto.Util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageDataService {

    @Autowired
    private ImageDataRepository imageDataRepository;

    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .menuImage(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData!=null){
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadImage(String fileName){
        Optional<ImageData> dbImageData = imageDataRepository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getMenuImage());

        return images;
    }
}
