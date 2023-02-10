package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Entity.Menu;
import com.dobudobu.resto.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

//    @GetMapping
//    private ResponseEntity<String> coba(){
//        return ResponseEntity.ok("bisa coeg");
//    }

    @PostMapping
    public ResponseEntity<?> insertNewMenu(@RequestParam("name")String name,
                                           @RequestParam("desc")String desc,
                                           @RequestParam("price")Double price,
                                           @RequestParam("categoryid")String idcate,
                                           @RequestParam("image")MultipartFile image) throws IOException {

        menuService.createNewMenu(name, desc, price, idcate, image);
        return ResponseEntity.created(URI.create("/menu")).build();
    }

    @GetMapping
    public List<Menu> getAllMenu(){
        return menuService.findAllMenu();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMenu(@PathVariable("id")Long id,
                                           @RequestParam("name")String name,
                                           @RequestParam("desc")String desc,
                                           @RequestParam("price")Double price,
                                           @RequestParam("categoryid")String idcate,
                                           @RequestParam("image")MultipartFile image,
                                           @RequestParam("status")Boolean status) throws IOException {
        menuService.updateMenuData(id, name, desc, price, idcate, image, status);
        return ResponseEntity.created(URI.create("/menu")).build();
    }

}
