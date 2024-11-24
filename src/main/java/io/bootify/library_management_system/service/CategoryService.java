package io.bootify.library_management_system.service;

import io.bootify.library_management_system.domain.Book;
import io.bootify.library_management_system.domain.Category;
import io.bootify.library_management_system.model.CategoryDTO;
import io.bootify.library_management_system.repos.BookRepository;
import io.bootify.library_management_system.repos.CategoryRepository;
import io.bootify.library_management_system.util.NotFoundException;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryService(final CategoryRepository categoryRepository,
                           final BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setCategory(category.getCategory().stream()
                .map(Book::getId)
                .toList());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());

        // Handle null category list
        List<Long> categoryIds = categoryDTO.getCategory() != null ? categoryDTO.getCategory() : List.of();

        // Find all books by their IDs
        List<Book> books = bookRepository.findAllById(categoryIds);

        // Verify all books were found
        if (books.size() != categoryIds.size()) {
            throw new NotFoundException("One or more books not found");
        }

        // Set the books to the category
        category.setCategory(new HashSet<>(books));

        return category;
    }
}