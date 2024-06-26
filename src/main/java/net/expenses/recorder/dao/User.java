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

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user", schema = "EXPENSE_RECORDER")
public class User {
    @Id
    @Column(name = "userid", columnDefinition = "BIGSERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "firstname", columnDefinition = "VARCHAR(50)", nullable = false)
    private String firstName;

    @Column(name = "lastname", columnDefinition = "VARCHAR(50)")
    private String lastName;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String phone;

    @Column(name = "hashedpassword", columnDefinition = "VARCHAR(150)", nullable = false)
    private String hashedPassword;

    @Column(name = "isloggedout", columnDefinition = "BOOL")
    private Boolean loggedOut;

    @Column(name = "datecreated", columnDefinition = "TIMESTAMP")
    private Timestamp dateCreated;

    @ManyToOne
    @JoinColumn(name = "avatarid", columnDefinition = "SERIAL")
    private Avatar avatar;

    @Column(name = "user_bio", columnDefinition = "varchar(100)")
    private String bio;

    @Column(name = "entrycount", columnDefinition = "INTEGER")
    private Integer entryCount;

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof User)
                && this.getUserId().equals(((User) obj).getUserId())
                && this.getEmail().equals(((User) obj).getEmail()));
    }
}
