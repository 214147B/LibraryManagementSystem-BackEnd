package io.bootify.library_management_system.rest;

import io.bootify.library_management_system.model.BorrowedbooksDTO;
import io.bootify.library_management_system.service.BorrowedbooksService;
import io.bootify.library_management_system.util.ReferencedException;
import io.bootify.library_management_system.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/borrowedbookss", produces = MediaType.APPLICATION_JSON_VALUE)
public class BorrowedbooksResource {

    private final BorrowedbooksService borrowedbooksService;

    public BorrowedbooksResource(final BorrowedbooksService borrowedbooksService) {
        this.borrowedbooksService = borrowedbooksService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowedbooksDTO>> getAllBorrowedbookss() {
        return ResponseEntity.ok(borrowedbooksService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowedbooksDTO> getBorrowedbooks(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(borrowedbooksService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBorrowedbooks(
            @RequestBody @Valid final BorrowedbooksDTO borrowedbooksDTO) {
        final Long createdId = borrowedbooksService.create(borrowedbooksDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBorrowedbooks(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BorrowedbooksDTO borrowedbooksDTO) {
        borrowedbooksService.update(id, borrowedbooksDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBorrowedbooks(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = borrowedbooksService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        borrowedbooksService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
