package io.bootify.library_management_system.repos;

import io.bootify.library_management_system.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {
}
