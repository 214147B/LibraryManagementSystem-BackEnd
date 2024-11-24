package io.bootify.library_management_system.repos;

import io.bootify.library_management_system.domain.Borrowedbooks;
import io.bootify.library_management_system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByUser(Borrowedbooks borrowedbooks);

}
