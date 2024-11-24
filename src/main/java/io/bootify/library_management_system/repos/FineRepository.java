package io.bootify.library_management_system.repos;

import io.bootify.library_management_system.domain.Fine;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FineRepository extends JpaRepository<Fine, Long> {
}
