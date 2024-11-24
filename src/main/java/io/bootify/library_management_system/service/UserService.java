package io.bootify.library_management_system.service;

import io.bootify.library_management_system.domain.Borrowedbooks;
import io.bootify.library_management_system.domain.User;
import io.bootify.library_management_system.model.UserDTO;
import io.bootify.library_management_system.repos.BorrowedbooksRepository;
import io.bootify.library_management_system.repos.UserRepository;
import io.bootify.library_management_system.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BorrowedbooksRepository borrowedbooksRepository;

    public UserService(final UserRepository userRepository,
                       final BorrowedbooksRepository borrowedbooksRepository) {
        this.userRepository = userRepository;
        this.borrowedbooksRepository = borrowedbooksRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setUser(user.getUser() == null ? null : user.getUser().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        // Handle the Borrowedbooks relationship
        if (userDTO.getUser() != null) {
            Borrowedbooks borrowedBooks = borrowedbooksRepository.findById(userDTO.getUser())
                    .orElseThrow(() -> new NotFoundException("Borrowedbooks not found with id: " + userDTO.getUser()));
            user.setUser(borrowedBooks);
        } else {
            user.setUser(null);
        }

        return user;
    }
}