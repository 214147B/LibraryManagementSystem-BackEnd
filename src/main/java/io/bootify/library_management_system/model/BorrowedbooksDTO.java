package io.bootify.library_management_system.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BorrowedbooksDTO {

    private Long id;

    private Long userid;

    private Long bookId;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    @Size(max = 255)
    private String status;

    private Long borrowbooks;

    @BorrowedbooksBorrowbookUnique
    private Long borrowbook;

}
