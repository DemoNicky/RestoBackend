package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.CategoryDto;
import com.dobudobu.resto.Entity.Category;
import com.dobudobu.resto.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveNewCategory(CategoryDto category) {
        Category category1 = Category.builder()
                .categoryName(category.getName())
                .build();
        return categoryRepository.save(category1);
    }

    public Category findCategoryById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("category not found"));
        return category;
    }
}
