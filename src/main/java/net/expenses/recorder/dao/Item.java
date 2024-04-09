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
@Table(name = "item", schema = "EXPENSE_RECORDER")
public class Item {
    @Id
    @Column(name = "itemno", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemNo;

    @Column(name = "itemname", columnDefinition = "VARCHAR(50)")
    private String itemName;

    @Column(name = "price", columnDefinition = "FLOAT8")
    private Double price;

    @Column(name = "count", columnDefinition = "INTEGER")
    private Integer count;

    @Column(name = "totalamount", columnDefinition = "FLOAT8")
    private Double totalAmount;

    @ManyToOne
    @Column(name = "entryno", columnDefinition = "UUID")
    private Entry entry;

    @Column(name = "description", columnDefinition = "VARCHAR(100")
    private String description;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "BIGSERIAL")
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoryid", columnDefinition = "UUID")
    private Category category;
}
