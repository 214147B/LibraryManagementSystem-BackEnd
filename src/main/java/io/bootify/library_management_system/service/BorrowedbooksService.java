package io.bootify.library_management_system.service;

import io.bootify.library_management_system.domain.Book;
import io.bootify.library_management_system.domain.Borrowedbooks;
import io.bootify.library_management_system.domain.Fine;
import io.bootify.library_management_system.domain.User;
import io.bootify.library_management_system.model.BorrowedbooksDTO;
import io.bootify.library_management_system.repos.BookRepository;
import io.bootify.library_management_system.repos.BorrowedbooksRepository;
import io.bootify.library_management_system.repos.FineRepository;
import io.bootify.library_management_system.repos.UserRepository;
import io.bootify.library_management_system.util.NotFoundException;
import io.bootify.library_management_system.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BorrowedbooksService {

    private final BorrowedbooksRepository borrowedbooksRepository;
    private final BookRepository bookRepository;
    private final FineRepository fineRepository;
    private final UserRepository userRepository;

    public BorrowedbooksService(final BorrowedbooksRepository borrowedbooksRepository,
            final BookRepository bookRepository, final FineRepository fineRepository,
            final UserRepository userRepository) {
        this.borrowedbooksRepository = borrowedbooksRepository;
        this.bookRepository = bookRepository;
        this.fineRepository = fineRepository;
        this.userRepository = userRepository;
    }

    public List<BorrowedbooksDTO> findAll() {
        final List<Borrowedbooks> borrowedbookses = borrowedbooksRepository.findAll(Sort.by("id"));
        return borrowedbookses.stream()
                .map(borrowedbooks -> mapToDTO(borrowedbooks, new BorrowedbooksDTO()))
                .toList();
    }

    public BorrowedbooksDTO get(final Long id) {
        return borrowedbooksRepository.findById(id)
                .map(borrowedbooks -> mapToDTO(borrowedbooks, new BorrowedbooksDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BorrowedbooksDTO borrowedbooksDTO) {
        final Borrowedbooks borrowedbooks = new Borrowedbooks();
        mapToEntity(borrowedbooksDTO, borrowedbooks);
        return borrowedbooksRepository.save(borrowedbooks).getId();
    }

    public void update(final Long id, final BorrowedbooksDTO borrowedbooksDTO) {
        final Borrowedbooks borrowedbooks = borrowedbooksRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(borrowedbooksDTO, borrowedbooks);
        borrowedbooksRepository.save(borrowedbooks);
    }

    public void delete(final Long id) {
        borrowedbooksRepository.deleteById(id);
    }

    private BorrowedbooksDTO mapToDTO(final Borrowedbooks borrowedbooks,
            final BorrowedbooksDTO borrowedbooksDTO) {
        borrowedbooksDTO.setId(borrowedbooks.getId());
        borrowedbooksDTO.setUserid(borrowedbooks.getUserid());
        borrowedbooksDTO.setBookId(borrowedbooks.getBookId());
        borrowedbooksDTO.setBorrowDate(borrowedbooks.getBorrowDate());
        borrowedbooksDTO.setReturnDate(borrowedbooks.getReturnDate());
        borrowedbooksDTO.setStatus(borrowedbooks.getStatus());
        borrowedbooksDTO.setBorrowbooks(borrowedbooks.getBorrowbooks() == null ? null : borrowedbooks.getBorrowbooks().getId());
        borrowedbooksDTO.setBorrowbook(borrowedbooks.getBorrowbook() == null ? null : borrowedbooks.getBorrowbook().getId());
        return borrowedbooksDTO;
    }

    private Borrowedbooks mapToEntity(final BorrowedbooksDTO borrowedbooksDTO,
            final Borrowedbooks borrowedbooks) {
        borrowedbooks.setUserid(borrowedbooksDTO.getUserid());
        borrowedbooks.setBookId(borrowedbooksDTO.getBookId());
        borrowedbooks.setBorrowDate(borrowedbooksDTO.getBorrowDate());
        borrowedbooks.setReturnDate(borrowedbooksDTO.getReturnDate());
        borrowedbooks.setStatus(borrowedbooksDTO.getStatus());
        final Book borrowbooks = borrowedbooksDTO.getBorrowbooks() == null ? null : bookRepository.findById(borrowedbooksDTO.getBorrowbooks())
                .orElseThrow(() -> new NotFoundException("borrowbooks not found"));
        borrowedbooks.setBorrowbooks(borrowbooks);
        final Fine borrowbook = borrowedbooksDTO.getBorrowbook() == null ? null : fineRepository.findById(borrowedbooksDTO.getBorrowbook())
                .orElseThrow(() -> new NotFoundException("borrowbook not found"));
        borrowedbooks.setBorrowbook(borrowbook);
        return borrowedbooks;
    }

    public boolean borrowbookExists(final Long id) {
        return borrowedbooksRepository.existsByBorrowbookId(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Borrowedbooks borrowedbooks = borrowedbooksRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User userUser = userRepository.findFirstByUser(borrowedbooks);
        if (userUser != null) {
            referencedWarning.setKey("borrowedbooks.user.user.referenced");
            referencedWarning.addParam(userUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
