package org.acme.service;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.domain.Category;
import org.acme.dto.CategoryDTO;
import org.acme.repository.CategoryRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategoriesEntities() {
        return categoryRepository.listAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category createCategory(String name, String description, String iconUrl) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIconUrl(iconUrl);
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    public Category updateCategory(Category category) {
        return categoryRepository.getEntityManager().merge(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id);
        if (category != null) {
            categoryRepository.delete(category);
        }
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.description = category.getDescription();
        dto.iconUrl = category.getIconUrl();
        return dto;
    }
}
