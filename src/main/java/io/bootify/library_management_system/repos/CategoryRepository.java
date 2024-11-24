package io.bootify.library_management_system.repos;

import io.bootify.library_management_system.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
