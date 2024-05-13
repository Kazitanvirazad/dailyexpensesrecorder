package net.expenses.recorder.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "entry_year", schema = "EXPENSE_RECORDER")
public class EntryYear {
    @Id
    @Column(name = "entryyearid", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID entryYearId;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "BIGSERIAL")
    private User user;

    @Column(name = "year", columnDefinition = "VARCHAR(4)", nullable = false)
    private String year;

    @Column(name = "year_itemcount", columnDefinition = "INTEGER")
    private Integer yearItemCount;

    @Column(name = "year_entrycount", columnDefinition = "INTEGER")
    private Integer yearEntryCount;
}
