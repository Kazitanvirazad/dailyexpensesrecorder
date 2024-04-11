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
@Table(name = "category", schema = "EXPENSE_RECORDER")
public class Category {
    @Id
    @Column(name = "categoryid", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryId;

    @Column(name = "categoryname", columnDefinition = "VARCHAR(50)", nullable = false)
    private String categoryName;

    @Column(name = "description", columnDefinition = "VARCHAR(100)")
    private String description;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "BIGSERIAL")
    private User user;
}
