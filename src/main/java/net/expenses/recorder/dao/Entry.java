package net.expenses.recorder.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.expenses.recorder.dao.enums.Month;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
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
    @Column(name = "entryno", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID entryNo;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "BIGSERIAL")
    private User user;

    @Column(name = "entrytime", columnDefinition = "TIMESTAMP")
    private Timestamp entryTime;

    @Column(name = "monthname", columnDefinition = "EXPENSE_RECORDER.monthname")
    @Enumerated(value = EnumType.STRING)
    private Month monthName;

    @Column(name = "month", columnDefinition = "DATE", nullable = false)
    private Date month;

    @Column(name = "amount", columnDefinition = "FLOAT8")
    private Double amount;

    @Column(name = "description", columnDefinition = "VARCHAR(100")
    private String description;

    @Column(name = "lastmodified", columnDefinition = "TIMESTAMP")
    private Timestamp lastModified;

    @OneToMany(targetEntity = Item.class, fetch = FetchType.LAZY)
    private List<Item> items;
}
