package com.dobudobu.resto.Controller;

import com.dobudobu.resto.Dto.CategoryDto;
import com.dobudobu.resto.Entity.Category;
import com.dobudobu.resto.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createNewCategory(@RequestBody CategoryDto category){
        categoryService.saveNewCategory(category);
        return ResponseEntity.created(URI.create("/category")).build();
    }

}
