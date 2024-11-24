package io.bootify.library_management_system.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Borrowedbookses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Borrowedbooks {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private Long userid;

    @Column
    private Long bookId;

    @Column
    private LocalDate borrowDate;

    @Column
    private LocalDate returnDate;

    @Column
    private String status;

    @OneToMany(mappedBy = "user")
    private Set<User> borrowedbooks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowbooks_id")
    private Book borrowbooks;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowbook_id", unique = true)
    private Fine borrowbook;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
