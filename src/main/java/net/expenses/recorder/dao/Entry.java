package net.expenses.recorder.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import net.expenses.recorder.dao.enums.MonthName;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "entry", schema = "EXPENSE_RECORDER")
public class Entry {
    @Id
    @Column(name = "entryid", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID entryId;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "BIGSERIAL")
    private User user;

    @Column(name = "creationtime", columnDefinition = "TIMESTAMP")
    private Timestamp creationTime;

    @Column(name = "monthname", columnDefinition = "EXPENSE_RECORDER.monthname")
    @Enumerated(value = EnumType.STRING)
    private MonthName monthName;

    @Column(name = "entrymonth", columnDefinition = "DATE", nullable = false)
    private Date entryMonth;

    @Column(name = "entryname", columnDefinition = "VARCHAR(100)", nullable = false)
    private String entryName;

    @Column(name = "amount", columnDefinition = "FLOAT8")
    private Double amount;

    @Column(name = "description", columnDefinition = "VARCHAR(100)")
    private String description;

    @Column(name = "lastmodified", columnDefinition = "TIMESTAMP")
    private Timestamp lastModified;

    @Column(name = "itemcount", columnDefinition = "INTEGER")
    private Integer itemCount;
}
