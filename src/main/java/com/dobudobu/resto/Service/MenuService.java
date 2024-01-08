package com.dobudobu.resto.Service;

import com.dobudobu.resto.Entity.Category;
import com.dobudobu.resto.Entity.ImageData;
import com.dobudobu.resto.Entity.Menu;
import com.dobudobu.resto.Repository.CategoryRepository;
import com.dobudobu.resto.Repository.ImageDataRepository;
import com.dobudobu.resto.Repository.MenuRepository;
import com.dobudobu.resto.Util.ImageUtils;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageDataRepository imageDataRepository;

    public void createNewMenu(String name, String desc,
                              Double price, String idcate, MultipartFile image) throws IOException {

        ImageData imageData = getImageData(image);

        Category category = getCategory(idcate);

        Menu menu = Menu.builder().
                name(name).
                desc(desc).
                price(price).
                category(category).
                image(imageData).
                status(Boolean.FALSE).
                build();

        menuRepository.save(menu);

    }

    private ImageData getImageData(MultipartFile image) throws IOException {
        ImageData imageData = ImageData.builder()
                .name(image.getOriginalFilename())
                .type(image.getContentType())
                .menuImage(ImageUtils.compressImage(image.getBytes())).build();
        if (imageData == null){
            throw new FileUploadException("file tidak di temukan");
        }
        return imageData;
    }

    private Category getCategory(String idcate) {
        Long cateid = Long.parseLong(idcate);
        Category category = categoryRepository.findById(cateid).orElseThrow(
                () -> new RuntimeException("id tidak di temukan"));
        return category;
    }

    public List<Menu> findAllMenu() {
        List<Menu> menus = menuRepository.findAll();
        List<Menu> menus1 = menus.stream().map((p) ->{
            Menu menu = new Menu();

            ImageData imageData = new ImageData();
            Optional<ImageData> imageData1 = imageDataRepository.findByName(p.getImage().getName());
            byte[] images = ImageUtils.decompressImage(imageData1.get().getMenuImage());
                menu.setName(p.getName());
                menu.setDesc(p.getDesc());
                menu.setPrice(p.getPrice());
                menu.setCategory(p.getCategory());

                imageData.setId(p.getImage().getId());
                imageData.setName(p.getImage().getName());
                imageData.setType(p.getImage().getType());
                imageData.setMenuImage(images);

                menu.setImage(imageData);
                menu.setStatus(p.getStatus());

                return menu;
                }).collect(Collectors.toList());
        return menus1;
    }

    private ImageData getImageData(Menu p) {
        Optional<ImageData> imageData = imageDataRepository.findById(p.getImage().getId());
        ImageData imageData2 = new ImageData();
        imageData2.setName(imageData.get().getName());
        imageData2.setType(imageData.get().getType());
        byte[] images = ImageUtils.decompressImage(imageData.get().getMenuImage());
        imageData2.setMenuImage(images);
        return imageData2;
    }


    public void updateMenuData(Long id, String name, String desc, Double price, String idcate,
                               MultipartFile image, Boolean status) throws IOException {

        ImageData imageData = getImageData(image);
        Category category = getCategory1(idcate);

        Menu menu = menuRepository.findById(id).orElseThrow(() -> new NullPointerException("menu tidak di temukan"));
        menu.setName(name);
        menu.setDesc(desc);
        menu.setPrice(price);
        menu.setCategory(category);
        menu.getImage().setType(imageData.getType());
        menu.getImage().setName(imageData.getName());
        menu.getImage().setMenuImage(imageData.getMenuImage());
        menu.setStatus(status);
        menuRepository.save(menu);
    }

    private Category getCategory1(String idcate) {
        Long cate = Long.parseLong(idcate);
        Category category = categoryRepository.findById(cate)
                .orElseThrow(() -> new NullPointerException("category tidak di temukan"));
        return category;
    }
}
