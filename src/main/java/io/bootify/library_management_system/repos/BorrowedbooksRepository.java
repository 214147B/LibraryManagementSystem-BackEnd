package io.bootify.library_management_system.repos;

import io.bootify.library_management_system.domain.Borrowedbooks;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BorrowedbooksRepository extends JpaRepository<Borrowedbooks, Long> {

    boolean existsByBorrowbookId(Long id);

}
