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
@Table(name = "entry_month", schema = "EXPENSE_RECORDER")
public class EntryMonth {
    @Id
    @Column(name = "entrymonthid", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID entryMonthId;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "BIGSERIAL")
    private User user;

    @Column(name = "month", columnDefinition = "VARCHAR(10)", nullable = false)
    private String month;

    @Column(name = "year", columnDefinition = "VARCHAR(4)", nullable = false)
    private String year;

    @Column(name = "month_itemcount", columnDefinition = "INTEGER")
    private Integer monthItemCount;

    @Column(name = "month_entrycount", columnDefinition = "INTEGER")
    private Integer monthEntryCount;
}
