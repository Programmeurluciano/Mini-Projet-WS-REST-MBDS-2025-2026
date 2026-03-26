package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.dto.CategoryMapper;
import com.miniprojet.wsrest.dto.CategoryRequestDTO;
import com.miniprojet.wsrest.dto.CategoryResponseDTO;
import com.miniprojet.wsrest.model.Category;
import com.miniprojet.wsrest.repository.CategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryMapper::toResponse);
    }

    public Optional<CategoryResponseDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toResponse);
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = CategoryMapper.toEntity(dto);
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(dto.getName());
                    return CategoryMapper.toResponse(categoryRepository.save(category));
                })
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}